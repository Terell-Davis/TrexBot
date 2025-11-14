package com.ontrexdex.trexbot.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.ontrexdex.trexbot.Config;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import javax.sound.midi.MetaMessage;
import java.util.Arrays;
import java.util.List;

public class QuoteCommand implements ICommand {
    private final WebhookClient client;

    public QuoteCommand() {
        WebhookClientBuilder builder = new WebhookClientBuilder(Config.get("WEBHOOK_URL"));
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Quotes");
            thread.setDaemon(true);
            return thread;
        });
        this.client = builder.build();
    }

    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        final Message message = ctx.getMessage();
        //final Member target = message.getMentions().getMembers().get(0);

        if (message.getReferencedMessage() == null){
            if (args.size() < 2 || message.getMentions().equals("")) {
                EmbedBuilder quote = new EmbedBuilder();
                quote.setColor(0xf98100);
                quote.setTitle("Someone said something wild, now to document it");
                quote.setDescription("Usage: `" + Config.get("prefix") + "quote [member] <message>`");
                channel.sendMessageEmbeds(quote.build()).queue();
                return;
            }
        }

        Member Username; String AvatarUrl; String messageContent;

        if (message.getReferencedMessage() != null){
            Username = message.getReferencedMessage().getMember();
            assert Username != null;
            AvatarUrl = Username.getEffectiveAvatarUrl()
                    .replaceFirst("gif", "png") + "?size=512";
            messageContent = message.getReferencedMessage().getContentDisplay();
        } else {
            Username = message.getMentions().getMembers().getFirst();
            AvatarUrl = Username.getEffectiveAvatarUrl()
                    .replaceFirst("gif", "png") + "?size=512";
            messageContent = String.join(" ", args.subList(1, args.size()));
        }

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(Username.getEffectiveName())
                .setAvatarUrl(AvatarUrl)
                .setContent(messageContent);
        client.send(builder.build());
    }

    @Override
    public String getName() {
        return "quote";
    }

    @Override
    public String getHelp() {
        return "Someone said something wild, now to document it [Add channel Webhook link in the .env]" + "\n"
                + "You can either add text after using it or reply to someones message." + "\n"
                + Config.get("prefix") + getName() + "[@GuildMember] <message>";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("qt");
    }
}
