package com.ontrexdex.trexbot.commands;

import com.ontrexdex.trexbot.Config;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;


public class InspireCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        // Using api from https://inspirobot.me/
        WebUtils.ins.getText("https://inspirobot.me/api?generate=true&oy=vey").async((text) -> {
            final EmbedBuilder embed;
            if (text != null) {
                embed = EmbedUtils.embedImage(text).setColor(0xf98100);
            }else {
                embed = EmbedUtils.embedMessage("⛔ Something went wrong! Please try again.").setColor(0xf98100);
            }
            channel.sendMessageEmbeds(embed.build()).queue();

        });
    }

    @Override
    public String getName() {
        return "inspire";
    }

    @Override
    public String getHelp() {
        return "https://inspirobot.me/ but in a discord command!" + "\n"
                + Config.get("prefix") + getName();
    }
}
