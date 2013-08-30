package gory_moon.stevescarts2.downloader.core.helper;

import gory_moon.stevescarts2.downloader.Main;

import java.io.InputStream;
import java.util.Properties;

public class XMLHelper {

	protected static Properties properties = new Properties();
	protected InputStream repoStream = null;
	
	public XMLHelper(String path){
		setProp(path);
	}

	protected void setProp(String path) {
		try {
			repoStream = Main.instance.getClass().getResourceAsStream(path);
			properties.loadFromXML(repoStream);
			repoStream.close();
		} catch (Exception e) {
		}
	}
	
	public String getXMLProps(String key) {
		return properties.getProperty(key);
	}

	public String[] convertToArray(String c, String xmlProps) {
		return xmlProps.split(c);
	}
	
}
