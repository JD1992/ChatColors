package me.jd1992.chatcolors.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import me.jd1992.chatcolors.ChatColors;
import org.bson.Document;
import org.bukkit.entity.Player;

/**
 * Handles all database actions from the plugin to the mongo database. Actions like insert chatcolors, delete chatcolors, insert and update selections for players chosen
 * chatcolors.
 *
 * @author Jan Dietze
 * @version 1.0
 */
public class MongoHandler {
	
	private final ChatColors plugin;
	
	private MongoClient client;
	private MongoCollection < Document > userData;
	private MongoCollection < Document > colors;
	private MongoDatabase database;
	
	public MongoHandler ( ChatColors plugin ) {
		this.plugin = plugin;
		
		String username = this.plugin.getConfigHandler().getConfigString( Constants.Database.USERNAME );
		String password = this.plugin.getConfigHandler().getConfigString( Constants.Database.PASSWORD );
		String host = this.plugin.getConfigHandler().getConfigString( Constants.Database.HOST );
		int port = this.plugin.getConfigHandler().getConfigInt( Constants.Database.PORT );
		
		String credentials = "";
		if ( ! ( username.equals( "" ) ) || ! ( password.equals( "" ) ) ) {
			credentials = String.format( "%s:%s@", username, password );
		}
		
		String connectionString = String.format( "mongodb://%s%s:%d", credentials, host, port );
		MongoClientURI uri = new MongoClientURI( connectionString );
		
		try {
			this.client = this.connect( uri );
			this.database = this.getDatabase( this.plugin.getConfigHandler().getConfigString( Constants.Database.NAME ) );
			this.colors = this.getCollection( Constants.Database.Field.COLOR_COLLECTION );
			this.userData = this.getCollection( Constants.Database.Field.USERDATA_COLLECTION );
			this.getColorList();
		} catch ( Exception e ) {
			this.plugin.getMessageHandler().log( "Es ist ein Fehler bei der Verbindung zur Datenbank aufgetretten." );
			this.plugin.getMessageHandler().log( e );
		}
		
	}
	
	/**
	 * Build up a connection to the database
	 *
	 * @param uri The URI for the connection to the database
	 *
	 * @return The mongoclient object
	 */
	private MongoClient connect ( MongoClientURI uri ) {
		return new MongoClient( uri );
	}
	
	/**
	 * Close the connection to the database
	 */
	public void disconnect () {
		this.client.close();
	}
	
	/**
	 * Get the database object
	 *
	 * @param dbName The name of the database to get
	 *
	 * @return The mongo database object corresponding to the dbName
	 */
	private MongoDatabase getDatabase ( String dbName ) {
		return client.getDatabase( dbName );
	}
	
	/**
	 * Get a collection from the database
	 *
	 * @param collectionName The name of the collection to get
	 *
	 * @return The collection object corresponding to the collectionName
	 */
	private MongoCollection < Document > getCollection ( String collectionName ) {
		return database.getCollection( collectionName );
	}
	
	/**
	 * Insert a new color in the database
	 *
	 * @param player The player which created the color (for status/error messages)
	 * @param args   A String array with all arguments needed for this action (id, woolId, colorCode, name)
	 */
	public void insertColor ( Player player, String[] args ) {
		try {
			int iID = Integer.parseInt( args[ 1 ] );
			// Check if the color already exists
			if ( this.plugin.getColors().containsKey( iID ) ) {
				this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Color.EXISTS );
				return;
			}
			int iWoolID = Integer.parseInt( args[ 2 ] );
			String cColorCode = org.bukkit.ChatColor.translateAlternateColorCodes( '&', args[ 3 ] );
			String cName = org.bukkit.ChatColor.translateAlternateColorCodes( '&', args[ 4 ] );
			cName = cName.replace( "_", " " );
			
			Document document = new Document( Constants.Database.Field.ID, iID )
					.append( Constants.Database.Field.WOOLID, iWoolID )
					.append( Constants.Database.Field.COLORCODE, cColorCode )
					.append( Constants.Database.Field.NAME, cName )
					.append( Constants.Database.Field.ACTIVE, false );
			
			this.colors.insertOne( document );
			this.getColorList();
			this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Color.CREATED );
		} catch ( Exception e ) {
			this.plugin.getMessageHandler().log( "Es ist ein Fehler beim erstellen der Chatfarbe aufgetretten." );
			this.plugin.getMessageHandler().log( e );
			this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Error.PARAMETER );
		}
	}
	
	/**
	 * Delete a chatcolor from the database
	 *
	 * @param player The player which removed the color (for status/error messages)
	 * @param sid    The database id to select the color
	 */
	public void removeColor ( Player player, String sid ) {
		try {
			int id = Integer.parseInt( sid );
			this.colors.deleteOne( new Document( Constants.Database.Field.ID, id ) );
			this.getColorList();
			this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Color.DELETED );
		} catch ( Exception e ) {
			this.plugin.getMessageHandler().log( "Es ist ein Fehler beim löschen einer Chatfarbe aufgetretten." );
			this.plugin.getMessageHandler().log( e );
			this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Error.PARAMETER );
		}
	}
	
	/**
	 * Get the colors from the database and put a created colorobjects for each in the corressponding hashmap
	 */
	private void getColorList () {
		MongoCursor < Document > c = this.colors.find().iterator();
		try {
			this.plugin.getColors().clear();
			while ( c.hasNext() ) {
				Document dbItem = c.next();
				int id = ( int ) dbItem.get( Constants.Database.Field.ID );
				int woolID = ( int ) dbItem.get( Constants.Database.Field.WOOLID );
				String colorCode = ( String ) dbItem.get( Constants.Database.Field.COLORCODE );
				String name = ( String ) dbItem.get( Constants.Database.Field.NAME );
				boolean active = ( Boolean ) dbItem.get( Constants.Database.Field.ACTIVE );
				// Create a ColorObject out of the given values
				ColorObject color = new ColorObject( id, woolID, colorCode, name, active );
				this.plugin.getColors().put( color.getId(), color );
			}
			this.plugin.getMessageHandler().log( "Chatfarben erfolgreich aus Datenbank geladen." );
		} catch ( Exception e ) {
			this.plugin.getMessageHandler().log( "Es ist ein Fehler beim laden der Chatfarben aus der Datenbank aufgetretten." );
			this.plugin.getMessageHandler().log( e );
		}
	}
	
	/**
	 * Check if the player has a chatcolor and put his chosen color in the corresponding hashmap
	 *
	 * @param player The player to set the chosen color
	 */
	public void getPlayerChosen ( Player player ) {
		MongoCursor < Document > c = this.userData.find( new Document( Constants.Database.Field.UUID, player.getUniqueId().toString() ) ).iterator();
		try {
			if ( c.hasNext() ) {
				Document dbItem = c.next();
				int id = ( int ) dbItem.get( Constants.Database.Field.CHOSEN );
				// Check if the player has advanced permissions
				boolean displayAll = player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.ADMIN ) ) ||
				                     player.hasPermission( this.plugin.getConfigHandler().getConfigPermission( Constants.Permission.TEAM ) );
				// Check if the players chosen chatcolor still exists
				if ( this.plugin.getColors().containsKey( id ) ) {
					if ( this.plugin.getColors().get( id ).isActive() || displayAll ) {
						// Put or replace the player in the hashmap with the related chatcolor id
						if ( ! this.plugin.getPlayerColor().containsKey( player ) ) {
							this.plugin.getPlayerColor().put( player, this.plugin.getColors().get( id ) );
						} else {
							this.plugin.getPlayerColor().replace( player, this.plugin.getColors().get( id ) );
						}
					}
				} else {
					// Set the players chosen chatcolor to -1 because the color doesn't exist
					this.userData.updateOne( new Document( Constants.Database.Field.UUID, player.getUniqueId().toString() ),
							new Document( "$set", new Document().append( Constants.Database.Field.CHOSEN, - 1 ) ) );
				}
			} else {
				// Insert the player because there is no entry
				this.userData.insertOne(
						new Document( Constants.Database.Field.UUID, player.getUniqueId().toString() ).append( Constants.Database.Field.CHOSEN, - 1 ) );
			}
		} catch ( Exception e ) {
			this.plugin.getMessageHandler().log( "Es ist ein Fehler beim laden der Spielerchatfarbe aufgetretten." );
			this.plugin.getMessageHandler().log( e );
			this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Error.GENERAL );
		}
	}
	
	/**
	 * Change the status of a color (active/not active)
	 *
	 * @param player The player which changed the color status (for status/error messages)
	 * @param id     The database id to select the color
	 * @param status The status to change to (active = true)
	 */
	public void changeColorStatus ( Player player, String id, boolean status ) {
		try {
			int iID = Integer.parseInt( id );
			MongoCursor < Document > c = this.colors.find( new Document( Constants.Database.Field.ID, iID ) ).iterator();
			if ( ! c.hasNext() ) {
				this.plugin.getMessageHandler().sendPluginMessage( player, "&4Diese chatfarbe existiert nicht. (Datenbank_ID)" );
				return;
			}
			boolean active = ( Boolean ) c.next().get( Constants.Database.Field.ACTIVE );
			if ( active == status ) {
				this.plugin.getMessageHandler().sendPluginMessage( player, "&4Diese chatfarbe ist bereits in dem Status." );
				return;
			}
			
			this.colors.updateOne( new Document( Constants.Database.Field.ID, iID ),
					new Document( "$set", new Document().append( Constants.Database.Field.ACTIVE, status ) ) );
			this.getColorList();
			if ( status ) {
				this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Color.ACTIVATED );
			} else {
				this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Color.DEACTIVATED );
			}
			
		} catch ( Exception e ) {
			this.plugin.getMessageHandler().log( "Es ist ein Fehler beim ändern des Status einer Chatfarbe aufgetretten." );
			this.plugin.getMessageHandler().log( e );
			this.plugin.getMessageHandler().sendPluginMessage( player, "&4Diese chatfarbe existiert nicht. (Datenbank_ID)" );
		}
	}
	
	/**
	 * Sets the new chosen id for a player in the database
	 *
	 * @param player The player to set the chosen value
	 * @param id     The id to set for the player
	 */
	public void setPlayerChosen ( Player player, int id ) {
		MongoCursor < Document > cursor = this.userData.find( new Document( Constants.Database.Field.UUID, player.getUniqueId().toString() ) ).iterator();
		try {
			if ( cursor.hasNext() ) {
				// Check if the user selected the same chatcolor (turn off the chatcolor for the player)
				Document dbItem = cursor.next();
				int choosenId = ( int ) dbItem.get( Constants.Database.Field.CHOSEN );
				if ( choosenId == id ) { id = - 1; }
			}
			this.userData.updateOne( new Document( Constants.Database.Field.UUID, player.getUniqueId().toString() ),
					new Document( "$set", new Document().append( Constants.Database.Field.CHOSEN, id ) ) );
			// If player turned his chatcolor off remove him/her from the list
			if ( id == - 1 ) {
				this.plugin.getPlayerColor().remove( player );
			} else {
				// Check if player already is in the list
				if ( this.plugin.getPlayerColor().containsKey( player ) ) {
					this.plugin.getPlayerColor().replace( player, this.plugin.getColors().get( id ) );
				} else {
					this.plugin.getPlayerColor().put( player, this.plugin.getColors().get( id ) );
				}
			}
			this.getColorList();
			this.plugin.getMessageHandler().sendConfigMessage( player, Constants.Message.Color.CHOSEN );
		} catch ( Exception e ) {
			this.plugin.getMessageHandler().log( "Es ist ein Fehler beim setzen der gewählten Chatfarbe augetretten." );
			this.plugin.getMessageHandler().log( e );
		}
		
	}
	
}
