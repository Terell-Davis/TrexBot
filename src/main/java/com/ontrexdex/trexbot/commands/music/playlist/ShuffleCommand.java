package com.ontrexdex.trexbot.commands.music.playlist;

import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.handlers.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.handlers.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ShuffleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

        if (ctx.getAuthor().isBot()) return;

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        if (!memberVoiceState.inAudioChannel()){
            channel.sendMessage("Please join a voice channel to use this command.").queue();
            return;
        }
        List<AudioTrack> list = new ArrayList<>();
        musicManager.scheduler.getQueue().drainTo(list);

        Collections.shuffle(list);
        musicManager.scheduler.getQueue().addAll(list);

        List<AudioTrack> tracks = new ArrayList<>(queue);
        AudioPlayer player = musicManager.player;
        AudioTrack track = tracks.get(0);
        AudioTrackInfo info = track.getInfo();


        channel.sendMessageEmbeds(EmbedUtils.embedMessage(String.format(
                "**__Next Up:__** [%s](%s)\n%s %s",
                info.title,
                info.uri,
                player.isPaused() ? "\u23F8" : "🥞 ",
                formatTime(track.getDuration()), " 🥞"
        )).setColor(0xf98100).build()).queue();
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String getHelp() {
        return "Takes all the music and shuffles it around!" + "\n"
                + Config.get("prefix") + getName();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("sh", "shuf","moveitmoveit");
    }

    private String formatTime(long timeInMillis){
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
