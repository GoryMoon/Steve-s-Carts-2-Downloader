package gory_moon.stevescarts2.downloader.core.helper;

import java.io.InputStream;
import java.net.URL;

public class OnlineXMLHelper extends XMLHelper {

    public OnlineXMLHelper(String path) {
        super(path);
    }

    @Override
    protected InputStream getStream() throws Exception {
        URL remoteVersionURL = new URL(path);
        return remoteVersionURL.openStream();
    }
}
