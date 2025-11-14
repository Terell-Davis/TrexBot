package com.ontrexdex.trexbot.commands;

import com.ontrexdex.trexbot.Config;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Arrays;
import java.util.List;

public class FixTwitCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        Message Twitlink = ctx.getMessage().getReferencedMessage();
        String link = Twitlink.getContentRaw();

        String fixed = link.replace("x.com", "fixvx.com");
        channel.sendMessage(fixed).queue();

    }

    @Override
    public String getName() {
        return "fixtwit";
    }

    @Override
    public String getHelp() {
        return "Fixes the Embeds for twitter links" + "\n"
                + "Reply to a message with " + Config.get("prefix") + getName() + "\n"
                + "> -# I'm not calling it X. Stupid ahh name";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("fx", "vx", "fxvx", "xisastupidname", "fixupx");
    }
}
