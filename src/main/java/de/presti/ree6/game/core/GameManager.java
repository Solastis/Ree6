package de.presti.ree6.game.core;

import de.presti.ree6.game.core.base.GameInfo;
import de.presti.ree6.game.core.base.IGame;
import de.presti.ree6.main.Main;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Class to manage Games.
 */
public class GameManager {

    /**
     * A HashMap used to cache the Games.
     * The Key is the Name of the Game.
     */
    private final static HashMap<String, Class<? extends IGame>> gameCache = new HashMap<>();

    /**
     * A HashMap used to cache the GameSessions.
     * The Key is the ID of the Channel.
     */
    private final static HashMap<String, GameSession> gameSessions = new HashMap<>();

    /**
     * Method used to create a new GameSession.
     * @param gameIdentifier The Identifier of the Session.
     * @param gameName The Name of the Game.
     * @param channel The Channel where the Game is played.
     * @param participants The Participants of the Game.
     * @return The created GameSession.
     */
    public static GameSession createGameSession(String gameIdentifier, String gameName, MessageChannelUnion channel, ArrayList<User> participants) {
        GameSession gameSession = new GameSession(gameIdentifier, channel, participants);
        gameSession.setGame(getGame(gameName, gameSession));
        gameSessions.put(gameIdentifier, gameSession);
        return gameSession;
    }

    /**
     * Method used to get a Game by its name.
     * @param gameName The Name of the Game.
     * @param gameSession The GameSession of the Game.
     * @return The Game.
     */
    public static IGame getGame(String gameName, GameSession gameSession) {

        if (gameCache.containsKey(gameName.toLowerCase().trim())) {
            try {
                return gameCache.get(gameName.toLowerCase().trim()).getDeclaredConstructor(GameSession.class).newInstance(gameSession);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e ) {
                Main.getInstance().getLogger().error("Failed to create instance of " + gameName + "!", e);
            }
        }

        Reflections reflections = new Reflections("de.presti.ree6.game.impl");
        Set<Class<? extends IGame>> classes = reflections.getSubTypesOf(IGame.class);

        for (Class<? extends IGame> aClass : classes) {
            if (aClass.isAnnotationPresent(GameInfo.class) && aClass.getAnnotation(GameInfo.class).name().trim().equalsIgnoreCase(gameName)) {
                try {
                    if (!gameCache.containsKey(gameName.toLowerCase().trim())) {
                        gameCache.put(gameName.toLowerCase().trim(), aClass);
                    }
                    return aClass.getDeclaredConstructor(GameSession.class).newInstance(gameSession);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e ) {
                    Main.getInstance().getLogger().error("Failed to create instance of " + aClass.getSimpleName() + "!", e);
                }
            }
        }

        return null;
    }

    /**
     * Method used to get a GameSession by its Identifier.
     * @param gameIdentifier The Identifier of the GameSession.
     * @return The GameSession.
     */
    public static GameSession getGameSession(String gameIdentifier) {
        return gameSessions.get(gameIdentifier);
    }

    /**
     * Method used to get all GameSessions.
     * @param channel The Channel where the GameSessions are played.
     * @return A List of GameSessions.
     */
    public static List<GameSession> getGameSessions(MessageChannelUnion channel) {
        return gameSessions.values().stream().filter(gameSession -> gameSession.getChannel().getId().equals(channel.getId())).toList();
    }

    /**
     * Method used to get all GameSessions.
     * @return A List of GameSessions.
     */
    public static List<GameSession> getGameSessions() {
        return (List<GameSession>) gameSessions.values();
    }

    /**
     * Method used to remove a GameSession.
     * @param session The GameSession.
     */
    public static void removeGameSession(GameSession session) {
        gameSessions.remove(session.getGameIdentifier());
    }
}
