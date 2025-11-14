package com.ontrexdex.trexbot.commands.music.control;

import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.handlers.GuildMusicManager;
import com.ontrexdex.trexbot.commands.music.handlers.PlayerManager;
import me.duncte123.botcommons.messaging.EmbedUtils;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Arrays;
import java.util.List;

public class EndCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            channel.sendMessage("Bot is currently not in a voice channel!").queue();
            return;
        }

        AudioChannel audioChannel = audioManager.getConnectedChannel();

        if (!audioChannel.getMembers().contains(ctx.getMember())) {
            channel.sendMessage("You have to be in the same voice channel as me to use this command").queue();
            return;
        }

        audioManager.closeAudioConnection();
        channel.sendMessage("Disconnected from your channel").queue();

        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);

        channel.sendMessageEmbeds(EmbedUtils.embedMessage(String.format(
                "🛑 Stopping song and clearing queue."
        )).setColor(0xff3b3b).build()).queue();
    }

    @Override
    public String getName() {
        return "end";
    }

    @Override
    public String getHelp() {
        return "Clears the current queue and leaves the voice channel" + "\n"
                + Config.get("prefix") + getName();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("getout", "butnotthemovie", "itsagoodmovie");
    }
}
