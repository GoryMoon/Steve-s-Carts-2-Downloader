package gory_moon.stevescarts2.downloader.core.helper;

import java.net.URL;

public class OnlineXMLHelper extends XMLHelper {

	public OnlineXMLHelper(String path) {
		super(path);
	}
	
	@Override
	public XMLHelper setProp() {
		try {
			URL remoteVersionURL = new URL(path);
			repoStream = remoteVersionURL.openStream();
			properties.loadFromXML(repoStream);
			repoStream.close();
		} catch (Exception ignored) {}
		return this;
	}

}
