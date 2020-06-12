package me.jd1992.chatcolors.events;

import me.jd1992.chatcolors.ChatColors;
import me.jd1992.chatcolors.util.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listeners for status changes of the player e.g player leaves the server so the entry in the map for the player isn't needed anymore
 *
 * @author Jan Dietze
 * @version 1.0
 */
public class PlayerStatusListener implements Listener {

    private final ChatColors plugin;

    public PlayerStatusListener (ChatColors plugin) {

        this.plugin = plugin;
    }

    /**
     * Listener to check on join if a player meets the requirements to have a chatcolor and then get the corresponding chatcolor
     *
     * @param event The PlayerJoinEvent which get's called
     */
    @EventHandler
    public void onJoin (PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (player.hasPermission(this.plugin.getConfigHandler().getConfigPermission(Constants.Permission.ADMIN)) ||
            player.hasPermission(this.plugin.getConfigHandler().getConfigPermission(Constants.Permission.TEAM)) ||
            player.hasPermission(this.plugin.getConfigHandler().getConfigPermission(Constants.Permission.VIP))) {
            this.plugin.getMongoHandler().getPlayerChosen(player);
        }
    }

    /**
     * Listener to check and remove the player from the player-chatcolor map after leaving the server
     *
     * @param event The PlayerQuitEvent which get's called
     */
    @EventHandler
    public void onQuit (PlayerQuitEvent event) {

        Player player = event.getPlayer();
        plugin.getPlayerColor().remove(player);
    }
}

