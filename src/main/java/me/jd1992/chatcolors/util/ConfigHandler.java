package me.jd1992.chatcolors.util;

import me.jd1992.chatcolors.ChatColors;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
/**
 * Handler for the plugin config
 *
 * @author Jan Dietze
 * @version 1.0
 */
public class ConfigHandler {
	
	private final FileConfiguration config;
	
	public ConfigHandler ( ChatColors plugin) {
		this.config = plugin.getConfig();
		setDefaultDatabase();
		setDefaultPermissions();
		setDefaultValues();
		setDefaultMessages();
		
		this.config.options().copyDefaults( true );
		plugin.saveConfig();
	}
	
	/**
	 * Config Standard-Werte für Datenbank Verbindungsdaten
	 */
	private void setDefaultDatabase () {
		config.addDefault( Constants.Database.HOST, "localhost" );
		config.addDefault( Constants.Database.PORT, 27017 );
		config.addDefault( Constants.Database.USERNAME, "" );
		config.addDefault( Constants.Database.PASSWORD, "" );
		config.addDefault( Constants.Database.NAME, "chatcolorDB" );
	}
	
	/**
	 * Setting the default values for the permissions in the config
	 */
	private void setDefaultPermissions () {
		this.config.addDefault( Constants.Permission.ADMIN, "chatcolors.admin" );
		this.config.addDefault( Constants.Permission.TEAM, "chatcolors.team" );
		this.config.addDefault( Constants.Permission.VIP, "chatcolors.vip" );
	}
	
	/**
	 * Setting the default values for plugin relevant options and values in the config
	 */
	private void setDefaultValues () {
		this.config.addDefault( Constants.Texts.GuiName.SELECTION, "&5Chatfarben" );
		this.config.addDefault( Constants.Texts.GuiName.BUY, "&5Chatfarben" );
		
		this.config.addDefault( Constants.Texts.GuiItem.ON, "&a&lAn" );
		this.config.addDefault( Constants.Texts.GuiItem.OFF, "&c&lAus" );
		this.config.addDefault( Constants.Texts.GuiItem.DEACTIVATED, "&c&lDeaktiviert" );
		
		this.config.addDefault( Constants.Texts.Prefix.USER, "&6&o&lChatColors&8>" );
		this.config.addDefault( Constants.Texts.Prefix.CONSOLE, "[ChatColors]" );
	}
	
	/**
	 * Setting the default values for messages in the config
	 */
	private void setDefaultMessages () {
		this.config.addDefault( Constants.Message.Error.GENERAL,
				"&4Es ist ein Fehler aufgetretten, versuche es in Kürze erneut. Sollte es wieder nicht funktionieren, melde es dem Team." );
		this.config.addDefault( Constants.Message.Error.NOCONSOLE, "Dieser befehl steht nur Ingame zur Verfügung." );
		this.config.addDefault( Constants.Message.Error.NOPERMISSION, "&4Du hast keine Berechtigung für diese Aktion." );
		this.config.addDefault( Constants.Message.Error.BUYVIP, "&4Kaufe dir VIP um die Chatfarben nutzen zu können." );
		this.config.addDefault( Constants.Message.Error.PARAMETER, "&4Überprüfe deine Eingabe." );
		
		this.config.addDefault( Constants.Message.Reload.START, "&4Das Plugin wird neugeladen." );
		this.config.addDefault( Constants.Message.Reload.END, "&4Das Plugin wurde erfolgreich neugeladen." );
		
		this.config.addDefault( Constants.Message.Color.CREATED, "&4Eine neue Farbe wurde erstellt." );
		this.config.addDefault( Constants.Message.Color.EXISTS, "&4Diese DATABASE_ID ist bereits vergeben." );
		this.config.addDefault( Constants.Message.Color.ACTIVATED, "&4Eine Farbe wurde aktiviert." );
		this.config.addDefault( Constants.Message.Color.DEACTIVATED, "&4Eine Farbe wurde deaktiviert." );
		this.config.addDefault( Constants.Message.Color.DELETED, "&4Eine Farbe wurde entfernt." );
		this.config.addDefault( Constants.Message.Color.CHOSEN, "&4Deine Chatfarbenauswahl wurde erfolgreich aktualisiert." );
	}
	
	/**
	 * Getting a config string with translated colorcodes
	 *
	 * @param node The node from the config to find the string e.g messages.player.noMoney
	 *
	 * @return The config string with translated colorcodes
	 */
	public String getFormattedConfigValue ( String node ) {
		return ChatColor.translateAlternateColorCodes( '&', this.config.getString( node ) );
	}
	
	/**
	 * Getting a config string with translated colorcodes
	 *
	 * @param node The node from the config to find the string e.g messages.player.noMoney
	 *
	 * @return The config string with translated colorcodes
	 */
	public String getConfigString ( String node ) {
		return this.config.getString( node );
	}
	
	/**
	 * Getting a config int
	 *
	 * @param node The node from the config to find the int e.g database.port
	 *
	 * @return The config int
	 */
	public int getConfigInt ( String node ) {
		return this.config.getInt( node );
	}
	
	/**
	 * Getting a permission string out of the config and strip the colorcodes for savety
	 *
	 * @param node The node from the config to find the string e.g permissions.vip
	 *
	 * @return The config string with stipped out colorcodes
	 */
	public String getConfigPermission ( String node ) {
		return ChatColor.stripColor( this.config.getString( node ) );
	}
	
}
