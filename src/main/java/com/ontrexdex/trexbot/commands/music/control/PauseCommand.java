package com.ontrexdex.trexbot.commands.music.control;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.musicassets.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.musicassets.PlayerManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        TextChannel channel = ctx.getChannel();
        AudioPlayer player = musicManager.player;
        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();

        if (player.getPlayingTrack() == null) {
            EmbedBuilder pause = new EmbedBuilder();
            pause.setColor(0xf51707);
            pause.setDescription("The player isn't playing anything.");
            channel.sendMessage(pause.build()).queue();
            return;
        }

        if (memberVoiceState.inVoiceChannel()) {
            musicManager.player.setPaused(true);
            channel.sendMessage("[Last Surprise Stops]").queue();

            AudioTrackInfo info = player.getPlayingTrack().getInfo();
            channel.sendMessage(EmbedUtils.embedMessage(String.format(
                    "**__Now Paused:__** [%s](%s)\n%s %s - %s %s",
                    info.title,
                    info.uri,
                    player.isPaused() ? "\u23F8" : "🥞 ",
                    formatTime(player.getPlayingTrack().getPosition()),
                    formatTime(player.getPlayingTrack().getDuration()), " 🥞"
            )).setColor(0xf51707).build()).queue();

            channel.sendMessage("`" + Config.get("prefix") + "resume` to resume song").queue();
        }else{
            EmbedBuilder other = new EmbedBuilder();
            other.setColor(0xf51707);
            other.setDescription("Please join a voice channel to use this command!");
            channel.sendMessage(other.build()).queue();
        }
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getHelp() {
        return "Pauses Current Song (Run `" + Config.get("prefix") +"resume` or `" + Config.get("prefix") + "ply` to resume song)";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("stop", "freeze");
    }
}
