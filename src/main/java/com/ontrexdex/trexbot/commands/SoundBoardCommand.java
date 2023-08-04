package com.ontrexdex.trexbot.commands;

import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.music.control.JoinCommand;
import com.ontrexdex.trexbot.commands.music.musicassets.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SoundBoardCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        final TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        PlayerManager manager = PlayerManager.getInstance();
        final List<String> args = ctx.getArgs();
        final String path = Config.get("SOUNDS");

        if (args.isEmpty()) {
            EmbedBuilder fk = new EmbedBuilder();
            fk.setColor(0xf98100);
            fk.setTitle("Specify which sound to play");
            fk.setDescription("Usage: `" + Config.get("prefix") + "sb <sound name>`" + "\n `"
                    + Config.get("prefix") + "sb list` to show list of available sounds");
            channel.sendMessageEmbeds(fk.build()).queue();
        }

        if (!audioManager.isConnected()) {
            JoinCommand join = new JoinCommand();
            join.handle(ctx);
        }

        File sound = new File(path);
        String[] sounds = sound.list();

        if (args.get(0).equals("list")){
            EmbedBuilder list = new EmbedBuilder();
            list.setColor(0xf98100);
            list.setTitle("🥞 **__Sounds__** 🥞");
            int counter = 0;
            for (String soundname : sounds){
                 counter++;
                list.setColor(0xf98100);
                list.appendDescription((counter + ". " + soundname.replace(".mp3", "") + "\n"));
            }

            channel.sendMessageEmbeds(list.build()).queue();
            return;
        }

        String play = path + args.get(0) + ".mp3";

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        assert memberVoiceState != null;
        if(memberVoiceState.inAudioChannel()) {
            manager.loadAndPlay((TextChannel) channel, play);
            manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(105);
        }
    }

    @Override
    public String getName() {
        return "SoundBoard";
    }

    @Override
    public String getHelp() {
        return "Usage:" + Config.get("PREFIX") + "soundboard [soundname] (Use 'list' to see available sounds)";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("sb", "sound");
    }
}
