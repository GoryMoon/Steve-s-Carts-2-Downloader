package gory_moon.stevescarts2.downloader.core.helper;

import gory_moon.stevescarts2.downloader.Main;

import java.io.InputStream;
import java.util.Properties;

public class LocalXMLHelper {

	public static Properties localProperties = new Properties();
	
	public LocalXMLHelper(String path){
		InputStream localRepoStream = null;
		
		try {
			localRepoStream = Main.instance.getClass().getResourceAsStream(path);
			localProperties.loadFromXML(localRepoStream);
			localRepoStream.close();
		} catch (Exception e) {
		}
	}

	public String getXMLProps(String key) {
		return localProperties.getProperty(key);
	}

	public String[] convertToArray(String c, String xmlProps) {
		return xmlProps.split(c);
	}
	
}
