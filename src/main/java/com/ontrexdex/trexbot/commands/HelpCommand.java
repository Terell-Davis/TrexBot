package com.ontrexdex.trexbot.commands;

import com.ontrexdex.trexbot.CommandManager;
import com.ontrexdex.trexbot.Config;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

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
            EmbedBuilder ebuilder = new EmbedBuilder()
                    .setTitle("📄 __**Command List**__ 📄");
            ebuilder.setColor(0x3341f0);
            manager.getCommand().stream().map(ICommand::getName).forEach(
                    (it) -> ebuilder
                            // Prefix + Command
                            .appendDescription("> **__")
                            .appendDescription(Config.get("PREFIX")).appendDescription(it)
                            .appendDescription("__**: "
                            // Description Text
                            + manager.getCommand(it).getHelp().split("\n")[0])
                            .appendDescription("\n")
                            // Usage Text
                            .appendDescription("> Usage: `" +
                                    manager.getCommand(it).getHelp()
                                    .substring(
                                            manager.getCommand(it).getHelp()
                                                    .indexOf(Config.get("PREFIX")
                                                            + manager.getCommand(it).getName())
                                    )
                                    + "`")
                            .appendDescription("\n")
                            // Aliases Text
                            .appendDescription("> Aliases: " + manager.getCommand(it).getAliases().toString()
                                    .replace("["," *").replace("]","") + "*")
                            .appendDescription("\n \n"));
            channel.sendMessageEmbeds(ebuilder.build()).queue();
            return;
        }

        String search = args.getFirst();
        ICommand command = manager.getCommand(search);

        if (command == null){
            channel.sendMessage("No Command found for " + Config.get("PREFIX") + search).queue();
        }

        assert command != null;
        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "List all Commands." + "\n"
                + Config.get("PREFIX") + getName();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("commands", "cmd", "commandlist","h");
    }
}
