package com.ontrexdex.trexbot;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class TrexBot {

    private TrexBot() throws LoginException{
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder().setColor(0xf98100).setFooter("Bot already in use!"));
        System.out.print("Bot is already in use!");
    }

    public static void main(String[] args) throws LoginException {
        JDA bot = JDABuilder.createDefault(Config.get("TOKEN")).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();

        bot.addEventListener(new ReadyListener());
        bot.getPresence().setStatus(OnlineStatus.ONLINE);
        bot.getPresence().setActivity(Activity.playing("LoZ:Totk " +
                Config.get("PREFIX") + "help"));
    }

}
