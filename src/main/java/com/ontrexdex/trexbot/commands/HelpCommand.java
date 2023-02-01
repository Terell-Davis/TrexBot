package com.ontrexdex.trexbot.commands;

import com.ontrexdex.trexbot.CommandManager;
import com.ontrexdex.trexbot.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;

public class HelpCommand implements ICommand {
    private final CommandManager manager;

    public HelpCommand(CommandManager manager) { this.manager = manager;}

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getMessage().getChannel().asTextChannel();

        if (args.isEmpty()){
            StringBuilder sbuilder = new StringBuilder();
            EmbedBuilder ebuilder = new EmbedBuilder()
                    .setTitle("📄 __**Command List**__ 📄");
            ebuilder.setColor(0xf98100);

            sbuilder.append("__List of commands__\n");
            manager.getCommand().stream().map(ICommand::getName).forEach(
                    (it) -> ebuilder.appendDescription("__").appendDescription(Config.get("PREFIX")).appendDescription(it)
                            .appendDescription("__ - " + manager.getCommand(it).getHelp()).appendDescription("\n")
                            .appendDescription("**Aliases**: " + manager.getCommand(it).getAliases().toString()
                                    .replace("["," ").replace("]",""))
                            .appendDescription("\n"));

            channel.sendMessageEmbeds(ebuilder.build()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null){
            channel.sendMessage("No Command found for " + Config.get("PREFIX") + "" + search).queue();
        }

        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Prints out a list of commands\n" +
                "`" + Config.get("PREFIX") + "help [command]`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("commands", "cmd", "commandlist","h");
    }
}
