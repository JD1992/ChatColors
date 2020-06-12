package me.jd1992.chatcolors.events;

import me.jd1992.chatcolors.ChatColors;
import me.jd1992.chatcolors.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Listener for the clicks in the selection inventory
 *
 * @author Jan Dietze
 * @version 1.0
 */
public class InventoryClickListener implements Listener {
	
	private final ChatColors plugin;
	
	/**
	 * Constructor with the plugin
	 *
	 * @param plugin The plugin which is using this listener
	 */
	public InventoryClickListener ( ChatColors plugin ) {
		this.plugin = plugin;
	}
	
	/**
	 * The event which gets triggered after a click in the inventory of a player.
	 * <p>
	 * It checks if it's the right inventory, then if the clicked item ins't a placeholder after this it updates the players chosen
	 * chatcolor to the clicked one
	 *
	 * @param event The event which got fired
	 */
	@EventHandler
	public void onInventoryClick ( InventoryClickEvent event ) {
		Inventory inventory = event.getClickedInventory();
		if ( inventory == null ) {
			event.setCancelled( true );
			return;
		}
		Player player = ( Player ) event.getWhoClicked();
		String inventoryName = player.getOpenInventory().getTopInventory().getTitle();
		
		// Check if the inventory is the right one
		if ( ! inventoryName.equals( this.plugin.getConfigHandler().getFormattedConfigValue(Constants.Texts.GuiName.SELECTION)) ) {
			return;
		}
		event.setCancelled( true );
		
		// Check if player clicked on a placeholder
		ItemStack current = event.getCurrentItem();
		if ( current.getType() == Material.SULPHUR ||
		     current.getType() == Material.STAINED_GLASS_PANE ) { return; }
		
		// Set the new chatcolor to the clicked one
		if ( current.hasItemMeta() ) {
			this.plugin.getMongoHandler().setPlayerChosen( player , getID( current.getItemMeta().getDisplayName() ) );
			player.closeInventory();
			player.playSound( player.getLocation() , Sound.BLOCK_WOOD_BUTTON_CLICK_OFF , 1 , 1 );
		}
		// BUGFIX: Update player inventory with small delay, to remove ghost items
		Bukkit.getScheduler().runTaskLater(this.plugin , player :: updateInventory, 1);
	}
	
	/**
	 * Extract the id from the items displayname
	 *
	 * @param displayName DisplayName of the item
	 *
	 * @return extracted database id
	 */
	private int getID ( String displayName ) {
		String idString;
		int id = - 1;
		try {
			int control = Integer.parseInt( displayName.substring( 1 , 2 ) ) * 2;
			idString = displayName.substring( displayName.length() - control).replace("§" , "");
			id = Integer.parseInt( idString );
		} catch ( Exception ex ) {
			this.plugin.getMessageHandler().log( ex );
			return id;
		}
		return id;
	}
	
}