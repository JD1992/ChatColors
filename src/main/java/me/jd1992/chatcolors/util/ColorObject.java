package me.jd1992.chatcolors.util;

import lombok.Getter;
/**
 * An object which represents a chatcolor with all the relevant data for it
 *
 * @author Jan Dietze
 * @version 1.0
 */
public class ColorObject {
	
	private @Getter int id;
	private @Getter int woolId;
	private @Getter String colorCode;
	private @Getter String name;
	private @Getter boolean active;
	
	ColorObject ( int id , int woolId , String colorCode , String name , boolean active ) {
		this.id = id;
		this.woolId = woolId;
		this.colorCode = colorCode;
		this.name = name;
		this.active = active;
	}
	
}
