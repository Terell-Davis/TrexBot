package com.ontrexdex.trexbot.commands;

import com.ontrexdex.trexbot.Config;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.List;
import java.util.Random;

public class RepostCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        //String path = Config.get("ASSETS");

        // Get a list of the files in the folder, doing this so you can just drag and drop photos in a folder
        List<String> repost = new java.util.ArrayList<>(List.of());

        File folder = new File(Config.get("ASSETS"));
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    repost.add(listOfFile.getName());
                    System.out.println("File " + listOfFile.getName()
                            .toLowerCase().replaceAll("\\s+",""));
                } else if (listOfFile.isDirectory()) {
                    System.out.println("No Files");
                }
            }
            Random ran = new Random();
            FileUpload file = FileUpload.fromData(new File(Config.get("ASSETS")
                    + repost.get(ran.nextInt(repost.size()))));
            channel.sendFiles(file).queue();
        } else {
            EmbedBuilder other = new EmbedBuilder();
            other.setColor(0xA3BE8C);
            other.setDescription("No Files Found");
            channel.sendMessageEmbeds(other.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "repost";
    }

    @Override
    public String getHelp() {
        return  "Someone beat you to posting that exact same thing" + "\n"
                + Config.get("PREFIX") + getName();
    }
}
