package de.presti.ree6.commands.impl.fun;

import com.google.gson.JsonObject;
import de.presti.ree6.commands.Category;
import de.presti.ree6.commands.CommandEvent;
import de.presti.ree6.commands.interfaces.Command;
import de.presti.ree6.commands.interfaces.ICommand;
import de.presti.ree6.main.Main;
import de.presti.ree6.utils.external.RequestUtility;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A command to get random fun facts.
 */
@Command(name = "funfact", description = "Just some random facts!", category = Category.FUN)
public class FunFact implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        JsonObject js = RequestUtility.request(RequestUtility.Request.builder().url("https://useless-facts.sameerkumar.website/api").build()).getAsJsonObject();

        Main.getInstance().getCommandManager().sendMessage(js.get("data").getAsString(), commandEvent.getChannel(), commandEvent.getInteractionHook());
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[] { "randomfact", "facts" };
    }
}
