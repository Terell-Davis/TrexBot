package com.ontrexdex.trexbot.commands.music.handlers;

import com.ontrexdex.trexbot.Config;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.YoutubeSourceOptions;
import dev.lavalink.youtube.clients.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        YoutubeSourceOptions options = new YoutubeSourceOptions().setAllowSearch(true)
                .setRemoteCipher("https://cipher.kikkia.dev/",null, "TrexBot");
        /* ^ A very very lovely person has a public server for this, bless them. Can replace with a private but already
        annoyed that damn Google keeps breaking stuff.
        * */
        YoutubeAudioSourceManager youtube = new dev.lavalink.youtube.YoutubeAudioSourceManager(options,
                new TvHtml5EmbeddedWithThumbnail());

        playerManager.registerSourceManager(youtube);

        if (Config.get("RFTOKEN") != null || Objects.equals(Config.get("YTSKIPINI"), "true")) {
            youtube.useOauth2(Config.get("RFTOKEN"), true);
        } else {
            youtube.useOauth2(null, false);
        }

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager (Guild guild){
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null){
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setColor(0xf98100).setTitle("🥞**__Adding to queue__**🥞");
                builder.setDescription(track.getInfo().title);
                channel.sendMessageEmbeds(builder.build()).queue();

                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().getFirst();
                }

                EmbedBuilder builder = new EmbedBuilder();
                builder.setColor(0xf98100).setTitle("🥞**__Adding to queue__**🥞");
                builder.setDescription(firstTrack.getInfo().title + " (First track of playlist " + playlist.getName() + ")");
                channel.sendMessageEmbeds(builder.build()).queue();

                play(musicManager, firstTrack);

                for(AudioTrack track : playlist.getTracks()){
                    if (track != playlist.getSelectedTrack()){
                        musicManager.scheduler.addToQueue(track);
                    }
                }
                playlist.getTracks().forEach(musicManager.scheduler::queue);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("No Track/Playlist Found").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });

    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}