package de.presti.ree6.audio.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import de.presti.ree6.audio.AudioPlayerSendHandler;
import net.dv8tion.jda.api.entities.Guild;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;

    /**
     * The Guild of the Music-Manager.
     */
    public final Guild guild;

    /**
     * Creates a player and a track scheduler.
     *
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(Guild guild, AudioPlayerManager manager) {
        this.guild = guild;
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}