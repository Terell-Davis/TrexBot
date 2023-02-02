package com.ontrexdex.trexbot.commands.music.playlist;

import com.google.api.services.youtube.model.Channel;
import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.musicassets.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.musicassets.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MoveTrackCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        AudioPlayer player = musicManager.player;

        if (ctx.getAuthor().isBot()) return;

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        if (!memberVoiceState.inAudioChannel()){
            EmbedBuilder other = new EmbedBuilder();
            other.setColor(0xf98100);
            other.setDescription("Please join a voice channel to use this command!");
            channel.sendMessageEmbeds(other.build()).queue();
            return;
        }

        if(queue.isEmpty()){
            EmbedBuilder other = new EmbedBuilder();
            other.setColor(0xf98100);
            other.setDescription("There is nothing to move!");
            channel.sendMessageEmbeds(other.build()).queue();
            return;
        }

        if ((ctx.getArgs().isEmpty())) {
            EmbedBuilder pause = new EmbedBuilder();
            pause.setColor(0xf98100);
            pause.setTitle("Please enter what you want to move.");
            pause.setDescription("`" + Config.get("PREFIX") + "movetrack [fromIndex] [toIndex]`");
            channel.sendMessageEmbeds(pause.build()).queue();
            return;
        }


        List<AudioTrack> list = new ArrayList<>();
        queue.drainTo(list);

        try{
            int fromIndex = Integer.parseInt(ctx.getArgs().get(0)) - 1;
            int toIndex = Integer.parseInt(ctx.getArgs().get(1)) - 1;

            AudioTrack item = list.remove(fromIndex);
            list.add(toIndex, item);

            queue.addAll(list);

            AudioTrackInfo info = item.getInfo();
            channel.sendMessageEmbeds(EmbedUtils.embedMessage(String.format(
                    "**__Moving to Index:__** "+(toIndex+1)+" \n [%s](%s)\n%s %s - %s %s",
                    info.title,
                    info.uri,
                    player.isPaused() ? "\u23F8" : "🎶 ",
                    formatTime(player.getPlayingTrack().getPosition()),
                    formatTime(player.getPlayingTrack().getDuration()), " 🎶"
            )).setColor(0xf98100).build()).queue();
        }catch (NumberFormatException e){ //This is for you Aaron, I get the feeling you're going to try and break something.
            channel.sendMessage("Invalid input. Please enter only numbers").queue();
            System.out.println("Invalid input. Not a number.");
        } catch (ArrayIndexOutOfBoundsException e){
            channel.sendMessage("You tried to break something didn't you?").queue();
            channel.sendMessage("How about not?").queue();
        }







    }

    @Override
    public String getName() {
        return "movetrack";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return ICommand.super.getAliases();
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
