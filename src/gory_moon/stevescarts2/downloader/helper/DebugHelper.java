package gory_moon.stevescarts2.downloader.helper;

import gory_moon.stevescarts2.downloader.Main;

public enum DebugHelper{
	INFO, DEBUG, 
	ERROR, WARNING;
	
	public static void print(DebugHelper type, String message){
		switch(type){
		case INFO:
			message = "[INFO] "+message;
			break;
		case DEBUG:
			message = "[DEBUG] "+message;
			break;
		case ERROR:
			message = "[ERROR] "+message;
			break;
		case WARNING:
			message = "[WARNING] "+message;
			break;
		default:
			message = "[INFO] "+message;
		}
		if(!Main.debug && type == DEBUG)
			message = null;
		else
			System.out.println(message);
	}

}
