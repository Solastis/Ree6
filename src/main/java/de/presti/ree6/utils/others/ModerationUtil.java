package de.presti.ree6.utils.others;

import de.presti.ree6.main.Main;
import de.presti.ree6.utils.apis.GoogleVisionAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to handle the moderation user behaviour.
 */
public class ModerationUtil {

    /**
     * Constructor should not be called, since it is a utility class that doesn't need an instance.
     *
     * @throws IllegalStateException it is a utility class.
     */
    private ModerationUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get the Blacklisted Words.
     *
     * @param guildId the ID of the Guild.
     * @return an {@link ArrayList} with every Blacklisted word from the Guild.
     */
    public static List<String> getBlacklist(String guildId) {
        return Main.getInstance().getSqlConnector().getSqlWorker().getChatProtectorWords(guildId);
    }

    /**
     * Check if the given Message contains any word that is blacklisted.
     *
     * @param guildId the ID of the Guild.
     * @param message the Message-Content.
     * @return true, if there is a blacklisted for contained.
     */
    public static boolean checkMessage(String guildId, String message) {
        return Arrays.stream(message.toLowerCase().split(" ")).anyMatch(word -> checkBlacklist(guildId, word));
    }

    /**
     * Check if the given word is blacklisted.
     *
     * @param guildId the ID of the Guild.
     * @param word    the word to check.
     * @return true, if there is a blacklisted for contained.
     */
    public static boolean checkBlacklist(String guildId, String word) {
        return getBlacklist(guildId).contains(word);
    }

    /**
     * Check if the given Server should be moderated.
     *
     * @param guildId the ID of the Guild.
     * @return true, if the Server should be moderated.
     */
    public static boolean shouldModerate(String guildId) {
        return Main.getInstance().getSqlConnector().getSqlWorker().isChatProtectorSetup(guildId);
    }

    /**
     * Check if an Image contains any Blacklisted Words.
     *
     * @param fileUrl the URL of the Image.
     * @return true, if the Image contains any Blacklisted Words.
     */
    public static boolean checkImage(String guildId, String fileUrl) {
        return Arrays.stream(GoogleVisionAPI.retrieveTextFromImage(fileUrl)).anyMatch(word -> checkBlacklist(guildId, word));
    }

    /**
     * Blacklist a Word.
     *
     * @param guildId the ID of the Guild.
     * @param word    the Word you want to blacklist.
     */
    public static void blacklist(String guildId, String word) {
        if (!getBlacklist(guildId).contains(word)) {
            Main.getInstance().getSqlConnector().getSqlWorker().addChatProtectorWord(guildId, word);
        }
    }

    /**
     * Blacklist a list of words.
     *
     * @param guildId  the ID of the Guild.
     * @param wordList the List of Words, which should be blacklisted.
     */
    public static void blacklist(String guildId, List<String> wordList) {
        wordList.forEach(word -> blacklist(guildId, word));
    }

    /**
     * Remove a word from the Blacklist.
     *
     * @param guildId the ID of the Guild.
     * @param word    the Word that should be removed from the Blacklist.
     */
    public static void removeBlacklist(String guildId, String word) {
        if (getBlacklist(guildId).contains(word)) {
            Main.getInstance().getSqlConnector().getSqlWorker().removeChatProtectorWord(guildId, word);
        }
    }

    /**
     * Remove a List of words from the Blacklist.
     *
     * @param guildId  the ID of the Guild.
     * @param wordList the List of Words that should be removed from the Blacklist.
     */
    public static void removeBlacklist(String guildId, List<String> wordList) {
        wordList.forEach(word -> removeBlacklist(guildId, word));
    }
}
