package me.jd1992.chatcolors.util;

import me.jd1992.chatcolors.ChatColors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The ingame Chest GUI to select the chatcolor you want to use with items for the chatcolors, items as placeholder for empty spaces and
 * with indicator items for the choosable chatcolors and the choosen chatcolor
 *
 * @author Jan Dietze
 * @version 1.0
 */
public class SelectionGUI {
	
	private final ChatColors plugin;
	
	private Inventory inventory;
	private ItemStack itemDeactivated;
	
	/**
	 * Constructor for the chatcolor selection GUI
	 *
	 * @param plugin         The plugin which is using the handler
	 * @param title          The title of the GUI
	 * @param itemsToDisplay A map with the items that should be displayed
	 * @param displayAll     Boolean if all items should be displayed
	 */
	public SelectionGUI ( ChatColors plugin, String title, Map < Integer, ColorObject > itemsToDisplay, boolean displayAll ) {
		this.plugin = plugin;
		int size = 0;
		for ( Map.Entry < Integer, ColorObject > item : itemsToDisplay.entrySet() ) {
			if ( item.getValue().isActive() || displayAll ) { size++; }
		}
		int inventorySize = 1;
		if ( size > 9 ) {
			inventorySize++;
			if ( size > 18 ) {
				inventorySize++;
			}
		}
		this.inventory = Bukkit.createInventory( null, inventorySize * 9 * 2, title );
		this.itemDeactivated = getItemstack( Material.SULPHUR, 0, this.plugin.getConfigHandler().getFormattedConfigValue( Constants.Texts.GuiItem.DEACTIVATED ) );
	}
	
	/**
	 * Opens the inventory
	 *
	 * @param player         Player for which the inventory will be opened
	 * @param itemsToDisplay A map with the items that should be displayed
	 */
	public void open ( Player player, Map < Integer, ColorObject > itemsToDisplay ) {
		this.fillPlaceholders();
		this.fillItems( player, itemsToDisplay );
		
		player.openInventory( this.inventory );
	}
	
	/**
	 * Fill the inventory with
	 *
	 * @param player         Player for which the inventory will be filled
	 * @param itemsToDisplay A map with the items that should be displayed
	 */
	private void fillItems ( Player player, Map < Integer, ColorObject > itemsToDisplay ) {
		boolean displayAll = player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.ADMIN ) ) ||
		                     player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.TEAM ) );
		int pos = 0;
		int chosen = getChosenId( player );
		
		for ( ColorObject entry : itemsToDisplay.values() ) {
			if ( pos == 9 || pos == 27 ) { pos += 9; }
			if ( pos > 44 ) { return; }
			if ( entry.isActive() || displayAll ) {
				ItemStack item = getItemstack( Material.WOOL, entry.getWoolId(), nameWithID( entry ) );
				if ( displayAll ) {
					item = getDeactivatedItem( entry, item );
				}
				
				this.inventory.setItem( pos, item );
				if ( entry.isActive() || displayAll ) {
					if ( entry.getId() == chosen ) {
						this.inventory.setItem( pos + 9, getItemstack( Material.INK_SACK, 10,
								nameWithID( entry.getId(), this.plugin.getConfigHandler().getFormattedConfigValue( Constants.Texts.GuiItem.ON ) ) ) );
					} else {
						this.inventory.setItem( pos + 9, getItemstack( Material.INK_SACK, 8,
								nameWithID( entry.getId(), this.plugin.getConfigHandler().getFormattedConfigValue( Constants.Texts.GuiItem.OFF ) ) ) );
					}
				} else {
					this.inventory.setItem( pos + 9, this.itemDeactivated );
				}
				pos++;
			}
		}
	}
	
	/**
	 * Returns a item with a the Database id and the text for deactivated items in the lore
	 *
	 * @param entry The entry for which the item is
	 * @param item  The item for the entry
	 *
	 * @return The new item with the new lore
	 */
	private ItemStack getDeactivatedItem ( ColorObject entry, ItemStack item ) {
		List < String > lore = new ArrayList <>();
		ItemMeta itemMeta = item.getItemMeta();
		if ( ! entry.isActive() ) { lore.add( plugin.getConfigHandler().getFormattedConfigValue( Constants.Texts.GuiItem.DEACTIVATED ) ); }
		lore.add( "§4ID: " + entry.getId() );
		itemMeta.setLore( lore );
		item.setItemMeta( itemMeta );
		return item;
	}
	
	/**
	 * Gets the item name with the corresponding id in the name from a colorobject
	 *
	 * @param object Colorobject to get the name for
	 *
	 * @return The name with the id in it
	 */
	private String nameWithID ( ColorObject object ) {
		return this.nameWithID( object.getId(), object.getName() );
	}
	
	/**
	 * Gets the item name with the corresponding id in the name from a colorobject
	 *
	 * @param id   The Id to hide in the name
	 * @param name The name to hide the id in
	 *
	 * @return The name with the id in it
	 */
	private String nameWithID ( int id, String name ) {
		String sid = String.valueOf( id );
		int control = sid.length();
		String idSet = "";
		if ( control > 1 ) {
			String[] ids = sid.split( "" );
			for ( String pid : ids ) {
				idSet += "§" + pid;
			}
		} else {
			idSet = "§" + sid;
		}
		return "§" + control + name + idSet;
	}
	
	/**
	 * Fill the inventory with the placeholder items
	 */
	private void fillPlaceholders () {
		ItemStack placeholder = getItemstack( Material.STAINED_GLASS_PANE, 15, "§r" );
		for ( int i = 0 ; i < inventory.getContents().length ; i++ ) {
			this.inventory.setItem( i, placeholder );
		}
	}
	
	/**
	 * Get a itemstack
	 *
	 * @param material The Material for the itemstack
	 * @param damage   The Damage value for the itemstack
	 * @param name     The Displaname for the itemstack
	 *
	 * @return The itemstack with the specified materiel, damage and name
	 */
	private ItemStack getItemstack ( Material material, int damage, String name ) {
		ItemStack itemStack = new ItemStack( material, 1, ( short ) damage );
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName( name );
		itemStack.setItemMeta( itemMeta );
		
		return itemStack;
	}
	
	/**
	 * Get the id for the chatcolor which the player has selected
	 *
	 * @param player The player for which the id should be get
	 *
	 * @return The chosen id for the player or -1 if none is chosen
	 */
	private int getChosenId ( Player player ) {
		int chosen;
		if ( this.plugin.getPlayerColor().containsKey( player ) ) {
			chosen = this.plugin.getPlayerColor().get( player ).getId();
		} else {
			this.plugin.getMongoHandler().getPlayerChosen( player );
			if ( this.plugin.getPlayerColor().containsKey( player ) ) {
				chosen = this.plugin.getPlayerColor().get( player ).getId();
			} else {
				chosen = - 1;
			}
		}
		return chosen;
	}
}
