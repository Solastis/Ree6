package de.presti.ree6.commands.impl.fun;

import de.presti.ree6.commands.Category;
import de.presti.ree6.commands.CommandEvent;
import de.presti.ree6.commands.interfaces.Command;
import de.presti.ree6.commands.interfaces.ICommand;
import de.presti.ree6.main.Main;
import de.presti.ree6.utils.apis.Neko4JsAPI;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import pw.aru.api.nekos4j.image.Image;
import pw.aru.api.nekos4j.image.ImageProvider;

/**
 * A command to send someone a kiss.
 */
@Command(name = "kiss", description = "Kiss someone", category = Category.FUN)
public class Kiss implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (commandEvent.isSlashCommand()) {
            OptionMapping targetOption = commandEvent.getSlashCommandInteractionEvent().getOption("target");

            if (targetOption != null && targetOption.getAsMember() != null) {
                sendKiss(targetOption.getAsMember(), commandEvent);
            } else {
                Main.getInstance().getCommandManager().sendMessage("No User was given to Kiss!", 5, commandEvent.getChannel(), commandEvent.getInteractionHook());
            }
        } else {
            if (commandEvent.getArguments().length == 1) {
                if (commandEvent.getMessage().getMentions().getMembers().isEmpty()) {
                    Main.getInstance().getCommandManager().sendMessage("No User mentioned!", 5, commandEvent.getChannel(), commandEvent.getInteractionHook());
                    Main.getInstance().getCommandManager().sendMessage("Use " + Main.getInstance().getSqlConnector().getSqlWorker().getSetting(commandEvent.getGuild().getId(), "chatprefix").getStringValue() + "kiss @user", 5, commandEvent.getChannel(), commandEvent.getInteractionHook());
                } else {
                    sendKiss(commandEvent.getMessage().getMentions().getMembers().get(0), commandEvent);
                }
            } else {
                Main.getInstance().getCommandManager().sendMessage("Not enough Arguments!", 5, commandEvent.getChannel(), commandEvent.getInteractionHook());
                Main.getInstance().getCommandManager().sendMessage("Use " + Main.getInstance().getSqlConnector().getSqlWorker().getSetting(commandEvent.getGuild().getId(), "chatprefix").getStringValue() + "kiss @user", 5, commandEvent.getChannel(), commandEvent.getInteractionHook());
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("kiss", "Kiss someone")
                .addOptions(new OptionData(OptionType.USER, "target", "The User that should be kissed!").setRequired(true));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }

    /**
     * Sends a kiss to the given user.
     * @param member The user that should be kissed.
     * @param commandEvent The command event.
     */
    public void sendKiss(Member member, CommandEvent commandEvent) {

        Main.getInstance().getCommandManager().sendMessage(commandEvent.getMember().getAsMention() + " kissed " + member.getAsMention(), commandEvent.getChannel(), null);

        ImageProvider ip = Neko4JsAPI.imageAPI.getImageProvider();

        Image im = null;
        try {
            im = ip.getRandomImage("kiss").execute();
        } catch (Exception ignored) {
        }

        Main.getInstance().getCommandManager().sendMessage((im != null ? im.getUrl() : "https://images.ree6.de/notfound.png"), commandEvent.getChannel(), null);
        if (commandEvent.isSlashCommand()) commandEvent.getInteractionHook().sendMessage("Check below!").queue();
    }
}
