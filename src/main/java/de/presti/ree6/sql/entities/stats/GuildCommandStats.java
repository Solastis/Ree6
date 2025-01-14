package de.presti.ree6.sql.entities.stats;

import de.presti.ree6.sql.base.annotations.Property;
import de.presti.ree6.sql.base.annotations.Table;

/**
 * SQL Entity for the Guild-Stats.
 */
@Table(name = "GuildStats")
public class GuildCommandStats extends CommandStats {

    /**
     * The Guild ID.
     */
    @Property(name = "gid")
    private String guildId;

    /**
     * Constructor.
     */
    public GuildCommandStats() {
    }

    /**
     * Constructor.
     *
     * @param command Name of the Command.
     * @param uses    Number of times the Command was used.
     */
    public GuildCommandStats(String guildId, String command, int uses) {
        super(command, uses);
        this.guildId = guildId;
    }

    /**
     * Get the Guild ID.
     * @return the guild ID.
     */
    public String getGuild() {
        return guildId;
    }
}
