package me.jd1992.chatcolors;

import lombok.Getter;
import me.jd1992.chatcolors.commands.CommandAdministration;
import me.jd1992.chatcolors.commands.CommandUser;
import me.jd1992.chatcolors.events.InventoryClickListener;
import me.jd1992.chatcolors.events.PlayerChatListener;
import me.jd1992.chatcolors.events.PlayerStatusListener;
import me.jd1992.chatcolors.util.ColorObject;
import me.jd1992.chatcolors.util.ConfigHandler;
import me.jd1992.chatcolors.util.MessageHandler;
import me.jd1992.chatcolors.util.MongoHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Mainclass of the chatcolor plugin
 *
 * @author Jan Dietze
 * @version 1.0
 */
public final class ChatColors extends JavaPlugin {

	private @Getter HashMap < Player, ColorObject >  playerColor = new HashMap <>();
	private @Getter HashMap < Integer, ColorObject > colors      = new HashMap <>();

	private @Getter MongoHandler mongoHandler;
	private @Getter MessageHandler messageHandler;
	private @Getter ConfigHandler configHandler;

	@Override
	public void onDisable () {
		mongoHandler.disconnect();
	}

	@Override
	public void onEnable () {
		configHandler = new ConfigHandler( this );
		messageHandler = new MessageHandler( this );
		mongoHandler = new MongoHandler( this );

		initCommands();
		initListeners();

	}

	/**
	 * Registering the commands and setting their executors
	 */
	private void initCommands () {
		getCommand( "particles" ).setExecutor( new CommandAdministration(this ));
		getCommand( "particle" ).setExecutor( new CommandUser(this ));
	}

	/**
	 * Registering of the listensers
	 */
	private void initListeners () {
		getServer().getPluginManager().registerEvents( new PlayerStatusListener( this ), this );
		getServer().getPluginManager().registerEvents( new PlayerChatListener( this ), this );
		getServer().getPluginManager().registerEvents( new InventoryClickListener( this ), this );
	}

}
