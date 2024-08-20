package com.ontrexdex.trexbot.commands;

import com.ontrexdex.trexbot.Config;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RepostCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        String path = Config.get("ASSETS");

        List<String> repost = Arrays.asList("repost.gif", "ffxivrepost.gif", "mmeyes.jpg", "suavarepost.gif");
        Random ran = new Random();

        FileUpload file = FileUpload.fromData(new File(path + repost.get(ran.nextInt(repost.size()))));
        channel.sendFiles(file).queue();
    }

    @Override
    public String getName() {
        return "repost";
    }

    @Override
    public String getHelp() {
        return  "Someone beat you to posting that exact same thing"
                + "\n" + "`" + Config.get("PREFIX") + getName() + "`";
    }
}
