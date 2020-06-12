package me.jd1992.chatcolors.events;

import me.jd1992.chatcolors.ChatColors;
import me.jd1992.chatcolors.util.ColorObject;
import me.jd1992.chatcolors.util.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
/**
 * Listener on sending of chat messages, check if the requirements meets and format the message with the players choosen chatcolor
 *
 * @author Jan Dietze
 * @version 1.0
 */
public class PlayerChatListener implements Listener {
	
	private final ChatColors plugin;
	
	public PlayerChatListener ( ChatColors plugin ) {
		this.plugin = plugin;
	}
	
	@EventHandler ( priority = EventPriority.HIGH )
	public void onChat ( AsyncPlayerChatEvent event ) {
		Player player = event.getPlayer();
		// Check if the player has a chatcolor choosen and if he has the permission to use it
		boolean useAll = player.hasPermission( this.plugin.getConfigHandler().getConfigPermission(Constants.Permission.ADMIN))
		                 || player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.TEAM ) );
		if ( this.plugin.getPlayerColor().get( player ) != null
		     && ( player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.VIP ) )
		          || useAll ) ) {
			// Get the chatcolor object for the player and format the chatmessage
			ColorObject colorObject = this.plugin.getPlayerColor().get(player);
			if ( this.plugin.getColors().get( colorObject.getId() ) != null
			     && ( this.plugin.getColors().get( colorObject.getId() ).isActive()
			          || useAll ) ) {
				event.setMessage( colorObject.getColorCode() + event.getMessage() );
			} else {
				this.plugin.getMongoHandler().setPlayerChosen( player, - 1 );
			}
		}
	}
}
