package com.ontrexdex.trexbot.commands.music.control;

import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.musicassets.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.musicassets.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;


import java.util.Arrays;
import java.util.List;

public class VolumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        @Deprecated
        final TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        final List<String> args = ctx.getArgs();
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager music = manager.getGuildMusicManager(ctx.getGuild());
        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        AudioPlayer player = music.player;

        if (ctx.getAuthor().isBot()) return;

        if (player.getPlayingTrack() == null) {
            EmbedBuilder empty = new EmbedBuilder();
            empty.setColor(0xf98100);
            empty.setDescription("The Player isn't playing anything");
            channel.sendMessageEmbeds(empty.build()).queue();
            return;
        }

        if (args.isEmpty()) {
            EmbedBuilder usage = new EmbedBuilder();
            usage.setColor(0xff3923);
            usage.setTitle("Specify Volume");
            usage.setDescription("Usage: `" + Config.get("prefix") + "Volume [volume]`");
            channel.sendMessageEmbeds(usage.build()).queue();
            return;
        }

        if(memberVoiceState.inAudioChannel()) {
            try {
                music.player.setVolume(Integer.parseInt(args.get(0)));
                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0xf98100);
                success.setTitle("🔊 Volume set to:" + args.get(0));
                channel.sendMessageEmbeds(success.build()).queue();
            } catch (IllegalArgumentException e) {
                if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xf98100);
                    error.setTitle("⛔ The hell did you put to get this?");
                    error.setDescription("Really what did you do??????");
                    channel.sendMessageEmbeds(error.build()).queue();
                }
            }
        }else{
            EmbedBuilder other = new EmbedBuilder();
            other.setColor(0xf98100);
            other.setDescription("Please join a voice channel to use this command!");
            channel.sendMessageEmbeds(other.build()).queue();
        }
    }
    @Override
    public String getName(){
        return "volume";
    }

    @Override
    public String getHelp(){
        return "Usage:" + Config.get("PREFIX") + "volume [# Percent]";
    }

    @Override
    public List<String> getAliases () {
        return Arrays.asList("vol", "v");
    }
}
