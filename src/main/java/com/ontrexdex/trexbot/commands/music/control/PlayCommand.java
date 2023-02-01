package com.ontrexdex.trexbot.commands.music.control;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.musicassets.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.musicassets.PlayerManager;
import com.ontrexdex.trexbot.commands.music.playlist.NowPlayingCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayCommand implements ICommand {
    private final YouTube youTube;

    public PlayCommand() {
        YouTube temp = null;
        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("Jokerbot - Java Discord bot")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        youTube = temp;
    }

    @Override
    public void handle(CommandContext ctx) {
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        final TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();

        if(musicManager.player.isPaused()){
           musicManager.player.setPaused(false);
            NowPlayingCommand playing = new NowPlayingCommand();
            playing.handle(ctx);
            return;
        }

        if ((ctx.getArgs().isEmpty())) {
            EmbedBuilder pause = new EmbedBuilder();
            pause.setColor(0xf98100);
            pause.setTitle("Please enter what you want to play.");
            pause.setDescription("Usage: `" + Config.get("PREFIX") + "play [url/song name]`");
            channel.sendMessageEmbeds(pause.build()).queue();
            return;
        }

        String input = String.join(" ", ctx.getArgs());

        if (!isUrl(input)) {
            String ytSearched = searchYoutube(input);

            if (ytSearched == null) {
                EmbedBuilder yt = new EmbedBuilder();
                yt.setColor(0xf98100);
                yt.setDescription("Youtube returned no results.");
                channel.sendMessageEmbeds(yt.build()).queue();
                return;
            }
            input = ytSearched;
        }

        if (!audioManager.isConnected()) {
            JoinCommand join = new JoinCommand();
            join.handle(ctx);
        }

        if(memberVoiceState.inAudioChannel()) {
            manager.loadAndPlay(channel, input);
            manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(80);
        }else{
            EmbedBuilder other = new EmbedBuilder();
            other.setColor(0xf98100);
            other.setDescription("Please join a voice channel to use this command!");
            channel.sendMessageEmbeds(other.build()).queue();
        }
    }

    private boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Nullable
    private String searchYoutube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list(Collections.singletonList("id,snippet"))
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType(Collections.singletonList("video"))
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.get("YOUTUBE"))
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getHelp() {
        return "Can play music from Youtube, Soundcloud, & Bandcamp!\n" +
                "`" + Config.get("prefix") + getName() + " <url>`";
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getName() {
        return "play";
    }

    public List<String> getAliases() {
        return Arrays.asList("p", "pl");
    }
}
