package gory_moon.stevescarts2.downloader.core.helper;

import gory_moon.stevescarts2.downloader.Main;

import java.io.InputStream;
import java.util.Properties;

public class XMLHelper {

	protected Properties properties = new Properties();
	protected InputStream repoStream = null;
	protected String path;
    protected Prop prop;
	
	public XMLHelper(String path){
		this.path = path;
	}

	public Prop getProp() {
        if (prop == null) {
            try {
                repoStream = getStream();
                properties.loadFromXML(repoStream);
                repoStream.close();
                prop = new Prop();
            } catch (Exception ignored) {}
        }

		return prop;
	}

    protected InputStream getStream() throws Exception {
        return Main.instance.getClass().getResourceAsStream(path);
    }

    public class Prop {

        public String getXMLProp(String key) {
            return properties == null ? null: properties.getProperty(key);
        }

    }
	
}
