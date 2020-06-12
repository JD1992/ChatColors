package me.jd1992.chatcolors.commands;

import me.jd1992.chatcolors.ChatColors;
import me.jd1992.chatcolors.util.Constants;
import me.jd1992.chatcolors.util.SelectionGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 * Command to open the GUI for choosing a chatcolor
 *
 * @author Jan Dietze
 * @version 1.0
 */
public class CommandUser implements CommandExecutor {
	
	private final ChatColors plugin;
	
	public CommandUser ( ChatColors plugin ) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand ( CommandSender sender, Command command, String label, String[] args ) {
		// Check because console shouldn't be able to perform this command
		if ( ! ( sender instanceof Player ) ) {
			this.plugin.getMessageHandler().sendConfigMessage( sender, Constants.Message.Error.NOCONSOLE );
			return true;
		}
		Player player = ( Player ) sender;
		if ( args.length == 0 ) {
			// Check if the player has the permission to display all chatcolors
			boolean displayAll = player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.ADMIN) ) ||
			                     player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.TEAM ) );
			// Open the selection GUI if permitted or send message to buy vip
			if ( player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.VIP ) ) || displayAll ) {
				new SelectionGUI( this.plugin, this.plugin.getConfigHandler().getFormattedConfigValue( Constants.Texts.GuiName.SELECTION ),
						this.plugin.getColors(), displayAll ).open( player, this.plugin.getColors() );
			} else {
				this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Error.BUYVIP );
			}
		} else {
			this.plugin.getMessageHandler().sendPluginMessage( player, "§4/chatfarbe" );
		}
		return true;
	}
}
