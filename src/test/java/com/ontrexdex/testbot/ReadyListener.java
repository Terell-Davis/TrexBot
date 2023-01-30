package com.ontrexdex.testbot;

import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class ReadyListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadyListener.class);
    private final CommandManager manager = new CommandManager();

    //To confirm if bot is online.
    @Override
    public void onReady(@Nonnegative ReadyEvent event){
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    //Listens to messages users send
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent message){
        User user = message.getAuthor();

        if(user.isBot() || message.isWebhookMessage()){return;}

        String prefix = "t-";
        String raw = message.getMessage().getContentRaw();

        //Detects prefix and passes it to command manager
        if(raw.startsWith(prefix)){
            manager.handle(message);
        }

        //Shutdowns the bot through discord
        if(raw.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals(Config.get("OWNER_ID"))){
            LOGGER.info("Shutting Down");
            message.getJDA().shutdown();
            BotCommons.shutdown(message.getJDA());
            return;
        }

    }
}
