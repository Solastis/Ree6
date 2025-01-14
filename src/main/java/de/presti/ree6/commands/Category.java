package de.presti.ree6.commands;

/**
 * Different Categories for the commands.
 */
public enum Category {

    /**
     * Category used for informativ Commands.
     */
    INFO("re_icon_info",1019221661070917632L, false, "Used to gather information or provide information."),
    /**
     * Category used for moderativ Commands.
     */
    MOD("re_icon_mod",1019221710932803624L, false, "Moderation Tools that can help you manager Users or prevent rule breaking on the Server."),
    /**
     * Category used for music Commands.
     */
    MUSIC("re_icon_music",1019221781762023575L, false, "Music utilities that allow you to have fun with your friends through the power of music!"),
    /**
     * Category used for fun Commands.
     */
    FUN("re_icon_fun",1019221814670532628L, false, "Fun utilities that give you the power to do dumb stuff because you can!"),
    /**
     * Category used for level Commands.
     */
    LEVEL("re_icon_level",1019221845972635809L, false, "Leveling Tools that allow you to keep track of progress on the Server."),
    /**
     * Category used for community Commands.
     */
    COMMUNITY("re_icon_community",1019221884686057552L, false, "Community Tools that allow you to interact with your community."),
    /**
     * Category used for NSFW Commands.
     */
    NSFW("re_icon_nsfw",1019221923466584166L, false, "NSFW stuff that you can only use in NSFW Channels."),
    /**
     * Category used for admin Commands.
     */
    HIDDEN("re_icon_hidden",1019221957817933865L, false, "Hidden Commands that are only visible to the Owner of the Bot.");

    private final String icon;
    private final long iconId;
    private final boolean iconAnimated;
    private final String description;
    Category(String icon, long iconId, boolean iconAnimated, String description) {
        this.icon = icon;
        this.iconId = iconId;
        this.iconAnimated = iconAnimated;
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public long getIconId() {
        return iconId;
    }

    public boolean isIconAnimated() {
        return iconAnimated;
    }

    public String getDescription() {
        return description;
    }
}
