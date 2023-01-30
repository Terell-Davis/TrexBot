package com.ontrexdex.trexbot.commands.music.playlist;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.ontrexdex.trexbot.Config;
import com.ontrexdex.trexbot.commands.CommandContext;
import com.ontrexdex.trexbot.commands.ICommand;
import com.ontrexdex.trexbot.commands.music.control.JoinCommand;
import com.ontrexdex.trexbot.commands.music.musicassets.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddTrackCommand implements ICommand {
    private final YouTube youTube;
    public AddTrackCommand() {
        YouTube temp = null;


        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("Jokerbot - Java Discord bot")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        youTube = temp;
    }
    @Override
    public void handle(CommandContext ctx) {
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        final TextChannel channel = ctx.getMessage().getChannel().asTextChannel();
        PlayerManager manager = PlayerManager.getInstance();
        final List<String> args = ctx.getArgs();

        if (ctx.getAuthor().isBot()) return;

        if(args.isEmpty()){
            EmbedBuilder usage = new EmbedBuilder();
            usage.setColor(0xf98100);
            usage.setTitle("Specify a track to add it");
            usage.setDescription("Usage: `" + Config.get("prefix") + "add [Track]`");
            ctx.getChannel().sendMessageEmbeds(usage.build()).queue();
            return;
        }

        String input = String.join(" ", ctx.getArgs());

        if (!isUrl(input)) {
            String ytSearched = searchYoutube(input);

            if (ytSearched == null) {
                channel.sendMessage("Youtube returned no results").queue();
                return;
            }

            input = ytSearched;
        }

        if (!audioManager.isConnected()) {
            JoinCommand join = new JoinCommand();
            join.handle(ctx);
        }

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        if(memberVoiceState.inAudioChannel()) {
            manager.loadAndPlay(channel, input);
            manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(80);
        }
    }

    private boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Nullable
    private String searchYoutube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list(Collections.singletonList("id,snippet"))
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType(Collections.singletonList("video"))
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.get("YOUTUBE"))
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName() {
        return "addtrack";
    }

    @Override
    public String getHelp() {
        return "Usage: `" + Config.get("prefix") + "addtrack [Track #]`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("add");
    }
}
