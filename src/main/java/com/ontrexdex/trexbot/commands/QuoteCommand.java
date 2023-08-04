package com.ontrexdex.trexbot.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.ontrexdex.trexbot.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Arrays;
import java.util.List;

public class QuoteCommand implements ICommand {
    private final WebhookClient client;

    public QuoteCommand() {
        WebhookClientBuilder builder = new WebhookClientBuilder(Config.get("webhook_url"));
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
        final Member target = (Member) message.getMentions().getUsers();

        if (args.size() < 2 || message.getMentions().equals("")) {
            EmbedBuilder quote = new EmbedBuilder();
            quote.setColor(0xf98100);
            quote.setTitle("Used to quote a person with name");
            quote.setDescription("Usage: `" + Config.get("prefix") + "quote [member] <message>`");
            channel.sendMessageEmbeds(quote.build()).queue();
            return;
        }

        final String quote = String.join(" ", args.subList(1, args.size()));

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(target.getEffectiveName())
                .setAvatarUrl(target.getUser().getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                .setContent(quote);

        client.send(builder.build());
    }

    @Override
    public String getName() {
        return "quote";
    }

    @Override
    public String getHelp() {
        return "Use this to catch your friends in 4k [Make sure you've added a webhook link in the .env]" + "\n"
                + "`" + Config.get("prefix") + getName() + "[@GuildMember] <message>`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("qt");
    }
}
