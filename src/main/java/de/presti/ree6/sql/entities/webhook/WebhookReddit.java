package de.presti.ree6.sql.entities.webhook;

import de.presti.ree6.sql.base.annotations.Property;
import de.presti.ree6.sql.base.annotations.Table;

/**
 * SQL Entity for the Reddit-Webhooks.
 */
@Table(name = "RedditNotify")
public class WebhookReddit extends Webhook {

    /**
     * Name of the Channel.
     */
    @Property(name = "subreddit")
    private String subreddit;

    /**
     * Constructor.
     */
    public WebhookReddit() {
    }


    /**
     * Constructor.
     *
     * @param guildId   The guild ID.
     * @param subreddit      The name of the Subreddit.
     * @param channelId The channel ID.
     * @param token     The token.
     */
    public WebhookReddit(String guildId, String subreddit, String channelId, String token) {
        super(guildId, channelId, token);
        this.subreddit = subreddit;
    }

    /**
     * Get the name of the Subreddit.
     * @return the subreddit name.
     */
    public String getSubreddit() {
        return subreddit;
    }
}
