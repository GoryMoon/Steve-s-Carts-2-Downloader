package gory_moon.stevescarts2.downloader.core.helper;

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
		if(type != DEBUG)
			System.out.println(message);
        else if (Main.debug)
            System.out.println(message);
    }

}
