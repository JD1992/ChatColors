package me.jd1992.chatcolors.commands;

import me.jd1992.chatcolors.ChatColors;
import me.jd1992.chatcolors.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 * Admin command to create, update(with the create command and the use of a DATABASE_ID that already exists), delete, activate, deactivate the chatcolors and reload the plugin
 *
 * @author Jan Dietze
 * @version 1.0
 */
public class CommandAdministration implements CommandExecutor {
	
	private final ChatColors plugin;
	
	public CommandAdministration ( ChatColors plugin ) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand ( CommandSender sender, Command command, String label, String[] args ) {
		// Check because console shouldn't be able to perform this command
		if ( ! ( sender instanceof Player ) ) {
			this.plugin.getMessageHandler().sendConfigMessage( sender, Constants.Message.Error.NOCONSOLE );
			return true;
		}
		// Check because only a player with the admin permission should be able to perform this command
		Player player = ( Player ) sender;
		if ( ! ( player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.ADMIN ) ) ) ) {
			//player.sendMessage( "Unbekannter Befehl." );
			return true;
		}
		
		switch ( args.length ) {
			case 1:
				// A reload of the config and the database
				if ( args[ 0 ].equalsIgnoreCase( "reload" ) ) {
					this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Reload.START );
					this.plugin.reloadConfig();
					this.plugin.saveConfig();
					Bukkit.getPluginManager().disablePlugin( this.plugin );
					Bukkit.getPluginManager().enablePlugin( this.plugin );
					this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Reload.END );
				} else {
					this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Error.PARAMETER );
				}
				break;
			case 2:
				// Remove a chatcolor from the database
				if ( args[ 0 ].equalsIgnoreCase( "remove" ) ) {
					this.plugin.getMongoHandler().removeColor( player, args[ 1 ] );
				} else if ( args[ 0 ].equalsIgnoreCase( "activate" ) ) {
					// Activate a chatcolor so it is choosable for all players (default = deactivated)
					this.plugin.getMongoHandler().changeColorStatus( player, args[ 1 ], true );
				} else if ( args[ 0 ].equalsIgnoreCase( "deactivate" ) ) {
					// Deactivate a chatcolor so it is only choosable with the team or admin permission (default = deactivated)
					this.plugin.getMongoHandler().changeColorStatus( player, args[ 1 ], false );
				} else {
					this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Error.PARAMETER );
				}
				break;
			case 5:
				// Create a new chatcolor (default it is deactivated)
				if ( args[ 0 ].equalsIgnoreCase( "create" ) ) {
					this.plugin.getMongoHandler().insertColor( player, args );
				} else {
					this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Error.PARAMETER );
				}
				break;
			default:
				// With the incorrect number of arguments a info is displayed with the explanation of the possible commands
				this.plugin.getMessageHandler().sendPluginMessage( player, "§4/chatcolors <create/reload/remove/activate/deactivate>" );
				this.plugin.getMessageHandler().sendPluginMessage( player,
						"§4create Datenbank-DATABASE_ID wool-SubID colorCode name (create 1 5 &Zahl &ZahlFarbe_eins) -> &Zahl entspricht Farbcodes" );
				this.plugin.getMessageHandler().sendPluginMessage( player, "§4reload" );
				this.plugin.getMessageHandler().sendPluginMessage( player, "§4remove Datenbank-DATABASE_ID" );
				this.plugin.getMessageHandler().sendPluginMessage( player, "§4activate Datenbank-DATABASE_ID" );
				this.plugin.getMessageHandler().sendPluginMessage( player, "§4deactivate Datenbank-DATABASE_ID" );
		}
		return true;
	}
}
