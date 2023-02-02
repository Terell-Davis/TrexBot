package com.ontrexdex.trexbot;

import com.ontrexdex.trexbot.commands.*;
import com.ontrexdex.trexbot.commands.music.control.*;
import com.ontrexdex.trexbot.commands.music.playlist.DeleteTrackCommand;
import com.ontrexdex.trexbot.commands.music.playlist.MoveTrackCommand;
import com.ontrexdex.trexbot.commands.music.playlist.QueueCommand;
import com.ontrexdex.trexbot.commands.music.playlist.ShuffleCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new HelpCommand(this));

        // Music Focused Commands
        addCommand((new PlayCommand())); addCommand(new PauseCommand()); addCommand(new NowPlayingCommand()); addCommand(new EndCommand());
        addCommand(new SkipCommand()); addCommand(new ResumeCommand()); addCommand(new QueueCommand());
        addCommand(new ShuffleCommand()); addCommand(new DeleteTrackCommand()); // Thank you Brian
        addCommand(new MoveTrackCommand());

        // Helpful Commands
        addCommand(new FixTwitCommand());

        // Fun Commands
        addCommand(new QuoteCommand()); addCommand(new InspireCommand()); addCommand(new RepostCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean exist = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
        if (exist) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommand() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {

        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        return null;
    }


    void handle(MessageReceivedEvent message) {
        String[] split = message.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "")
                .split("\\s+");
        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            message.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(message, args);

            cmd.handle(ctx);
        }
    }
}
