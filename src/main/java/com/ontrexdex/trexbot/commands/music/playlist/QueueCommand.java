package com.ontrexdex.trexbot.commands.music.playlist;

import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.musicassets.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.musicassets.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        // List<String> args = ctx.getArgs();

        if (queue.isEmpty()) {
            channel.sendMessage("There is nothing here but me and my thoughts").queue();
            return;
        }

        // A reminder to myself: Make it where people can see past 20 when they run the command
        // Example being t-q 40 will show tracks 20 - 40. The limit is there so it won't spam the channel

        int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> tracks = new ArrayList<>(queue);
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Current Queue (Total: " + queue.size() + ")");

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = tracks.get(i);
            AudioTrackInfo info = track.getInfo();

            builder.setColor(0xf98100);
            builder.appendDescription(String.format(
                    (i + 1) + ". %s - [%s] 🎵\n",
                    info.title,
                    formatTime(track.getDuration())
            ));
        }
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    private String formatTime(long timeInMillis){
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "Displays the songs currently in queue" + "\n"
                + "`" + Config.get("prefix") + getName() + "`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("q", "que", "list");
    }
}
