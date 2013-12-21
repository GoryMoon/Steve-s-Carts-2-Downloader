package gory_moon.stevescarts2.downloader.core.helper;

import gory_moon.stevescarts2.downloader.Main;

import java.io.InputStream;
import java.util.Properties;

public class XMLHelper {

	protected static Properties properties = new Properties();
	protected InputStream repoStream = null;
	protected String path;
	
	public XMLHelper(String path){
		this.path = path;
	}

	public XMLHelper setProp() {
		try {
			repoStream = Main.instance.getClass().getResourceAsStream(path);
			properties.loadFromXML(repoStream);
			repoStream.close();
		} catch (Exception ignored) {}
		return this;
	}
	
	public String getXMLProps(String key) {
		return properties == null ? null: properties.getProperty(key);
	}

	public String[] convertToArray(String c, String xmlProps) {
		return xmlProps.split(c);
	}
	
}
