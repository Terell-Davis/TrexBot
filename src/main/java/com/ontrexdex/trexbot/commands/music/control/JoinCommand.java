package com.ontrexdex.trexbot.commands.music.control;

import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        if (audioManager.isConnected()) {
            channel.sendMessage("Already connected to a channel").queue();
            return;
        }

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();

        if (!memberVoiceState.inAudioChannel()){
            channel.sendMessage("Please join a voice channel to use this command.").queue();
            return;
        }

        AudioChannel voiceChannel = memberVoiceState.getChannel();
        Member selfMember = ctx.getGuild().getSelfMember();

        if (!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)){
            channel.sendMessageFormat("Insufficient Privileges for %s.", voiceChannel).queue();
            return;
        }

        if (memberVoiceState.inAudioChannel()){
            audioManager.openAudioConnection(voiceChannel);
            channel.sendMessage("Sending Calling Card.").queue();
        }


    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Bot joins voice channel";
    }
}
