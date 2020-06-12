package me.jd1992.chatcolors.util;

/**
 * This class contains every constant value, config nodes and permissions
 */
public final class Constants {
	
	private static final String STATE_WARNING = "Utility class!";
	
	public final class Database {
		
		public static final String HOST = "database.mongo.host";
		public static final String PORT = "database.mongo.port";
		public static final String USERNAME = "database.mongo.username";
		public static final String PASSWORD = "database.mongo.password";
		public static final String NAME = "database.mongo.databaseName";
		
		public final class Field {
			
			private Field () {
				throw new IllegalStateException( STATE_WARNING );
			}
			
			public static final String COLOR_COLLECTION = "colors";
			public static final String USERDATA_COLLECTION = "userData";
			public static final String ID = "id";
			public static final String WOOLID = "woolID";
			public static final String COLORCODE = "colorCode";
			public static final String NAME = "name";
			public static final String ACTIVE = "active";
			public static final String CHOSEN = "chosen";
			public static final String UUID = "uuid";
			
		}
	}
	
	public final class Permission {
		
		private Permission () {
			throw new IllegalStateException( STATE_WARNING);
		}
		
		public static final String ADMIN = "permission.admin";
		public static final String TEAM = "permission.team";
		public static final String VIP = "permission.vip";
		
	}
	
	public final class Texts {
		
		public final class GuiName {
			
			private GuiName () {
				throw new IllegalStateException( STATE_WARNING );
			}
			
			public static final String SELECTION = "gui.name.selectionInventory";
			public static final String BUY = "gui.name.buyInventory";
			
		}
		
		public final class GuiItem {
			
			private GuiItem () {
				throw new IllegalStateException( STATE_WARNING );
			}
			
			public static final String ON = "gui.item.on";
			public static final String OFF = "gui.item.off";
			public static final String DEACTIVATED = "gui.item.deactivated";
			
		}
		
		public final class Prefix {
			
			private Prefix () {
				throw new IllegalStateException( STATE_WARNING );
			}
			
			public static final String USER = "prefix.user";
			public static final String CONSOLE = "prefix.console";
			
		}
	}
	
	public final class Message {
		
		public final class Error {
			
			private Error () {
				throw new IllegalStateException( STATE_WARNING );
			}
			
			public static final String GENERAL = "message.error.general";
			public static final String NOCONSOLE = "message.error.noConsole";
			public static final String NOPERMISSION = "message.error.noPermission";
			public static final String BUYVIP = "message.error.buyVip";
			public static final String PARAMETER = "message.error.wrongParameter";
			
		}
		
		public final class Reload {
			
			private Reload () {
				throw new IllegalStateException( STATE_WARNING );
			}
			
			public static final String START = "message.reload.start";
			public static final String END = "message.reload.end";
			
		}
		
		public final class Color {
			
			private Color () {
				throw new IllegalStateException( STATE_WARNING );
			}
			
			public static final String CREATED = "message.color.created";
			public static final String EXISTS = "message.color.exists";
			public static final String ACTIVATED = "message.color.activated";
			public static final String DEACTIVATED = "message.color.deactivated";
			public static final String DELETED = "message.color.deleted";
			public static final String CHOSEN = "message.color.chosen";
			
		}
		
	}
	
}
