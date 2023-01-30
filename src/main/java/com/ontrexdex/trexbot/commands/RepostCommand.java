package com.ontrexdex.trexbot.commands;

import com.ontrexdex.trexbot.Config;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;

public class RepostCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        String path = Config.get("ASSETS");
        FileUpload file = FileUpload.fromData(new File(path + "repost.png"));


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
