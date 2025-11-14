package com.ontrexdex.trexbot.commands.music.playlist;

import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.handlers.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.handlers.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class DeleteTrackCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        final List<String> args = ctx.getArgs();

        if (ctx.getAuthor().isBot()) return;

        if(args.isEmpty()){
            EmbedBuilder usage = new EmbedBuilder();
            usage.setColor(0xf98100);
            usage.setTitle("Specify track to delete");
            usage.setDescription("Usage: `" + Config.get("prefix") + "deletetrack [Track #]`");
            channel.sendMessageEmbeds(usage.build()).queue();
            return;
        }

        if (queue.isEmpty()) {
            EmbedBuilder empty = new EmbedBuilder();
            empty.setColor(0xf98100);
            empty.setDescription("There is nothing here but me and my thoughts!");
            channel.sendMessageEmbeds(empty.build()).queue();
            return;
        }

        if(memberVoiceState.inAudioChannel()) {
            if (args.get(0).equals("all")) {
                musicManager.scheduler.getQueue().clear();
                EmbedBuilder all = new EmbedBuilder();
                all.setColor(0xf98100);
                all.setTitle("Clearing entire queue!");
                channel.sendMessageEmbeds(all.build()).queue();
            } else {
                List<AudioTrack> tracks = new ArrayList<>(queue);
                AudioTrack track = tracks.get(Integer.parseInt(args.get(0)) - 1);
                AudioTrackInfo info = track.getInfo();

                List<AudioTrack> list = new ArrayList<>();
                musicManager.scheduler.getQueue().drainTo(list);

                list.remove(Integer.parseInt(args.get(0)) - 1);
                musicManager.scheduler.getQueue().addAll(list);

                channel.sendMessageEmbeds(EmbedUtils.embedMessage(String.format(
                        "**__Removed:__** [%s](%s)",
                        info.title,
                        info.uri
                )).setColor(0xf98100).build()).queue();
            }
        }else{
            EmbedBuilder other = new EmbedBuilder();
            other.setColor(0xf98100);
            other.setDescription("Please join a voice channel to use this command!");
            channel.sendMessageEmbeds(other.build()).queue();
        }

    }

    @Override
    public String getName() {
        return "deletetrack";
    }

    @Override
    public String getHelp() {
        return  "Removes the  specified track from the queue." + "\n"
                + Config.get("prefix") + getName() + "{Track #}";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("dt", "clear", "delete");
    }
}
