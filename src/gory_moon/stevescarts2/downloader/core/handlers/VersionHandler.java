package gory_moon.stevescarts2.downloader.core.handlers;

import gory_moon.stevescarts2.downloader.Main;
import gory_moon.stevescarts2.downloader.core.helper.DebugHelper;
import gory_moon.stevescarts2.downloader.core.helper.OnlineXMLHelper;
import gory_moon.stevescarts2.downloader.core.helper.XMLHelper;


public class VersionHandler {
	
	private XMLHelper xmlRemoteHelper;
	private static XMLHelper xmlLocalHelper;
	private final static String XML_REMOTE_VERSION = "https://dl.dropboxusercontent.com/u/65769242/Steves%20Carts%202%20Downloader/SC2DV.xml";
	private final static String XML_LOCAL_VERSION = "resources/SC2DV.xml";
	
	private String remoteVersion;
	private static String localVersion;
	
	public VersionHandler(){
		xmlRemoteHelper = new OnlineXMLHelper(XML_REMOTE_VERSION);
		
		compareVersions();
	}

	private boolean compareVersions(){
		int CV = Integer.parseInt(localVersion.replace(".", ""));
		remoteVersion = xmlRemoteHelper.getXMLProps("remoteVersion");
		int RV = Integer.parseInt(remoteVersion.replace(".", ""));
		
		DebugHelper.print(DebugHelper.DEBUG, "Online version: " + remoteVersion + ", Local Version: "+ Main.getDownloaderVersion());
        if (RV>CV){
        	DebugHelper.print(DebugHelper.INFO, "Needs update!");
        	Main.setNeedsUpdate(true);
        	return false;
        }
        DebugHelper.print(DebugHelper.INFO, "No update needed!");
        Main.setNeedsUpdate(false);
        return true;
	}
	
	public static String getLocalVersion() {
		xmlLocalHelper = new XMLHelper(XML_LOCAL_VERSION);
		localVersion = xmlLocalHelper.getXMLProps("localVersion");
		
		return localVersion;
	}
	
	public String getRemoteVersion() {
		return remoteVersion;
	}
	
}
