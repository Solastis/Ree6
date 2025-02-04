package de.presti.ree6.commands.impl.fun;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.presti.ree6.commands.Category;
import de.presti.ree6.commands.CommandEvent;
import de.presti.ree6.commands.interfaces.Command;
import de.presti.ree6.commands.interfaces.ICommand;
import de.presti.ree6.main.Main;
import de.presti.ree6.utils.data.Data;
import de.presti.ree6.utils.external.RequestUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A command used to search for animes!
 */
@Command(name = "anime", description = "Search for animes on kitsu.io!", category = Category.FUN)
public class Anime implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        Message message = commandEvent.isSlashCommand() ?
                commandEvent.getInteractionHook().sendMessage("Searching for the Anime...").complete() :
                commandEvent.getChannel().sendMessage("Searching for the Anime...").complete();

        String[] args = commandEvent.getArguments();

        if (commandEvent.isSlashCommand()) {
            OptionMapping searchQueryMapping = commandEvent.getSlashCommandInteractionEvent().getOption("search");
            if (searchQueryMapping != null)
                args = searchQueryMapping.getAsString().split(" ");
        }

        StringBuilder builder = new StringBuilder();

        for (final String string : args)
            builder.append(string).append(' ');

        if (builder.toString().endsWith(" "))
            builder = new StringBuilder(builder.substring(0, builder.length() - 1));

        if (args.length > 0) {
            sendAnime(commandEvent, message, builder.toString());
        } else {
            message.editMessage("Please provide a query!").queue();
        }
    }

    /**
     * Send the anime to the channel.
     * @param commandEvent the CommandEvent.
     * @param message the Message.
     * @param query the query.
     */
    public void sendAnime(CommandEvent commandEvent, Message message, String query) {
        RequestUtility.Request request = RequestUtility.Request.builder()
                .url("https://kitsu.io/api/edge/anime?filter[text]=" + URLEncoder.encode(query, StandardCharsets.UTF_8))
                .build();
        JsonElement jsonElement = RequestUtility.request(request);

        if (jsonElement != null &&
                jsonElement.isJsonObject() &&
                jsonElement.getAsJsonObject().has("data") &&
                jsonElement.getAsJsonObject().get("data").isJsonArray()) {
            JsonArray dataArray = jsonElement.getAsJsonObject().getAsJsonArray("data");

            JsonObject data = !dataArray.isEmpty() && dataArray.get(0).isJsonObject() ?
                    dataArray.get(0).getAsJsonObject() : new JsonObject();

            JsonObject attributes = data.has("attributes") && data.get("attributes").isJsonObject()
                    ? data.getAsJsonObject("attributes") : new JsonObject();

            String url = data.has("links") &&
                    data.get("links").isJsonObject() && data.getAsJsonObject("links").has("self") ?
                    data.getAsJsonObject("links").get("self").getAsString() : null;

            String name = attributes.has("canonicalTitle") ?
                    attributes.get("canonicalTitle").getAsString() : "Error while resolving the Name!";

            String thumbnailUrl = attributes.has("posterImage") &&
                    attributes.get("posterImage").isJsonObject() &&
                    attributes.getAsJsonObject("posterImage").has("large") ?
                    attributes.getAsJsonObject("posterImage").get("large").getAsString() : null;

            String description = attributes.has("synopsis") ?
                    attributes.get("synopsis").getAsString() : "?";

            String status = attributes.has("status") ?
                    attributes.get("status").getAsString() : "?";

            String type = attributes.has("showType") ?
                    attributes.get("showType").getAsString() : "?";

            String genres = attributes.has("genres") &&
                    attributes.get("genres").isJsonArray() ?
                    attributes.getAsJsonArray("genres").toString() : "?";

            String startDate = attributes.has("startDate") ?
                    attributes.get("startDate").getAsString() : "?";

            String endDate = attributes.has("endDate") ?
                    attributes.get("endDate").getAsString() : "?";

            String episodes = attributes.has("episodeCount") ?
                    attributes.get("episodeCount").getAsString() : "?";

            String duration = attributes.has("totalLength") ?
                    attributes.get("totalLength").getAsInt() + " minutes" : "?";

            String rating = attributes.has("averageRating") ?
                    attributes.get("averageRating").getAsString() : "?";

            String rank = attributes.has("ratingRank") ?
                    attributes.get("ratingRank").getAsString() : "?";

            EmbedBuilder em = new EmbedBuilder();

            em.setTitle(name, url);
            em.setThumbnail(thumbnailUrl);
            em.setDescription(description);
            em.addField(":hourglass_flowing_sand: **Status**", status, true);
            em.addField(":dividers: **Type**", type, true);
            em.addField(":arrow_right: **Genres**", genres, false);
            em.addField(":calendar: **Aired**", "from **" + startDate + "** to **" + endDate + "**", false);
            em.addField(":minidisc: **Episodes**", episodes, true);
            em.addField(":stopwatch: **Duration**", duration, true);
            em.addField(":star: **Average Rating**", " **" + rating + "/100**", true);
            em.addField(":trophy: **Rank**", "**TOP " + rank + "**", true);
            em.setFooter(commandEvent.getMember().getUser().getAsTag() + " - " + Data.ADVERTISEMENT, commandEvent.getMember().getUser().getAvatarUrl());

            if (commandEvent.isSlashCommand()) {
                message.editMessage("Anime found!").queue();
                Main.getInstance().getCommandManager().sendMessage(em, commandEvent.getChannel(), null);
            } else {
                message.editMessageEmbeds(em.build()).queue(message1 -> message1.editMessage("Anime found!").queue());
            }
        } else {
            message.editMessage("There was an error while trying to get the Anime!").queue();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("anime", "Search for animes on kitsu.io!")
                .addOption(OptionType.STRING, "search", "The search query to search for.", true);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
