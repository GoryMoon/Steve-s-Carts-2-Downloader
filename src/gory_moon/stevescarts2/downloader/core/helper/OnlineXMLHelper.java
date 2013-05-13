package gory_moon.stevescarts2.downloader.core.helper;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class OnlineXMLHelper {

	public static Properties remoteProperties = new Properties();
	
	public OnlineXMLHelper(String url){
		InputStream remoteRepoStream = null;
		
		try {
			URL remoteVersionURL = new URL(url);
			remoteRepoStream = remoteVersionURL.openStream();
			remoteProperties.loadFromXML(remoteRepoStream);
			remoteRepoStream.close();
		} catch (Exception e) {
		}
	}

	public String getXMLProps(String key) {
		return remoteProperties.getProperty(key);
	}

	public String[] convertToArray(String c, String xmlProps) {
		return xmlProps.split(c);
	}
	
}
