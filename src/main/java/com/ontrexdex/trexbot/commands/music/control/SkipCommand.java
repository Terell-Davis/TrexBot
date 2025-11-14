package com.ontrexdex.trexbot.commands.music.control;

import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.handlers.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.handlers.PlayerManager;
import com.ontrexdex.trexbot.commands.music.handlers.TrackScheduler;
import com.ontrexdex.trexbot.commands.music.playlist.NowPlayingCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Arrays;
import java.util.List;

public class SkipCommand implements ICommand {
        @Override
        public void handle(CommandContext ctx) {
            TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
            PlayerManager playerManager = PlayerManager.getInstance();
            GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
            TrackScheduler scheduler = musicManager.scheduler;
            AudioPlayer player = musicManager.player;
            GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();

            if(memberVoiceState.inAudioChannel()) {
                if (player.getPlayingTrack() == null) {
                    EmbedBuilder empty = new EmbedBuilder();
                    empty.setColor(0xff3b3b);
                    empty.setDescription("The Player isn't playing anything");
                    channel.sendMessageEmbeds(empty.build()).queue();
                    return;
                }

                scheduler.nextTrack();
                channel.sendMessage("Skipping current track").queue();

                NowPlayingCommand playing = new NowPlayingCommand();
                playing.handle(ctx);

            }else{
                EmbedBuilder other = new EmbedBuilder();
                other.setColor(0xA3BE8C);
                other.setDescription("Please join a voice channel to use this command!");
                channel.sendMessageEmbeds(other.build()).queue();
            }
        }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Skips the current song playing" + "\n"
                + Config.get("prefix") + getName();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("s", "sk","next");
    }

}
