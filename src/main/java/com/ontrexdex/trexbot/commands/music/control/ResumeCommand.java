package com.ontrexdex.trexbot.commands.music.control;

import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.musicassets.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.musicassets.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ResumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            EmbedBuilder pause = new EmbedBuilder();
            pause.setColor(0xf98100);
            pause.setDescription("The player isn't playing anything.");
            channel.sendMessageEmbeds(pause.build()).queue();
            return;
        }

        if (memberVoiceState.inAudioChannel()) {
            musicManager.player.setPaused(false);

            AudioTrackInfo info = player.getPlayingTrack().getInfo();
            channel.sendMessageEmbeds(EmbedUtils.embedMessage(String.format(
                    "**__Now Playing:__** [%s](%s)\n%s %s - %s %s",
                    info.title,
                    info.uri,
                    player.isPaused() ? "\u23F8" : "🥞 ",
                    formatTime(player.getPlayingTrack().getPosition()),
                    formatTime(player.getPlayingTrack().getDuration()), " 🥞"
            )).setColor(0xf98100).build()).queue();
        }else{
            EmbedBuilder other = new EmbedBuilder();
            other.setColor(0xf98100);
            other.setDescription("Please join a voice channel to use this command!");
            channel.sendMessageEmbeds(other.build()).queue();
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
        return "resume";
    }

    @Override
    public String getHelp() {
        return "Resumes the currently paused song" + "\n"
                + "`" + Config.get("prefix") + getName() + "`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("start", "up", "unpause", "r");
    }
}
