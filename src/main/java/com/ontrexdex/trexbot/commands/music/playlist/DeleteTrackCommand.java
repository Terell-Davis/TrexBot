package com.ontrexdex.trexbot.commands.music.playlist;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.tyskyworks.kakorot.Config;
import com.tyskyworks.kakorot.commands.CommandContext;
import com.tyskyworks.kakorot.commands.ICommand;
import com.tyskyworks.kakorot.commands.music.musicassets.GuildMusicManager;
import com.tyskyworks.kakorot.commands.music.musicassets.PlayerManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class DeleteTrackCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        final List<String> args = ctx.getArgs();

        if (ctx.getAuthor().isBot()) return;

        if(args.isEmpty()){
            EmbedBuilder usage = new EmbedBuilder();
            usage.setColor(0xf51707);
            usage.setTitle("Specify track to delete");
            usage.setDescription("Usage: `" + Config.get("prefix") + "deletetrack [Track #]`");
            ctx.getChannel().sendMessage(usage.build()).queue();
            return;
        }

        if (queue.isEmpty()) {
            EmbedBuilder empty = new EmbedBuilder();
            empty.setColor(0xf51707);
            empty.setDescription("There is nothing here but me and my thoughts!");
            ctx.getChannel().sendMessage(empty.build()).queue();
            return;
        }

        if(memberVoiceState.inVoiceChannel()) {
            if (args.get(0).equals("all")) {
                musicManager.scheduler.getQueue().clear();
                EmbedBuilder all = new EmbedBuilder();
                all.setColor(0xf51707);
                all.setTitle("Clearing entire queue!");
                ctx.getChannel().sendMessage(all.build()).queue();
            } else {
                List<AudioTrack> tracks = new ArrayList<>(queue);
                AudioTrack track = tracks.get(Integer.parseInt(args.get(0)) - 1);
                AudioTrackInfo info = track.getInfo();

                List<AudioTrack> list = new ArrayList<>();
                musicManager.scheduler.getQueue().drainTo(list);

                list.remove(Integer.parseInt(args.get(0)) - 1);
                musicManager.scheduler.getQueue().addAll(list);

                channel.sendMessage(EmbedUtils.embedMessage(String.format(
                        "**__Removed:__** [%s](%s)",
                        info.title,
                        info.uri
                )).setColor(0xf51707).build()).queue();
            }
        }else{
            EmbedBuilder other = new EmbedBuilder();
            other.setColor(0xf51707);
            other.setDescription("Please join a voice channel to use this command!");
            channel.sendMessage(other.build()).queue();
        }

    }

    @Override
    public String getName() {
        return "DeleteTrack";
    }

    @Override
    public String getHelp() {
        return "Usage: `" + Config.get("prefix") + "deletetrack [Track #]`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("dt", "clear", "delete");
    }
}
