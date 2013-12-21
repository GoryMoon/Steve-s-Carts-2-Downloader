package gory_moon.stevescarts2.downloader.core.handlers;

import gory_moon.stevescarts2.downloader.core.helper.DebugHelper;
import gory_moon.stevescarts2.downloader.core.helper.OnlineXMLHelper;
import gory_moon.stevescarts2.downloader.core.helper.Version;
import gory_moon.stevescarts2.downloader.core.helper.XMLHelper;


public class VersionHandler {
	
	private XMLHelper xmlRemoteHelper;
	private XMLHelper xmlLocalHelper;
	private final static String XML_REMOTE_VERSION = "https://dl.dropboxusercontent.com/u/65769242/Steves%20Carts%202%20Downloader/SC2DVN.xml";
	private final static String XML_LOCAL_VERSION = "resources/SC2DV.xml";
	
	private Version remoteVersion;
	private Version localVersion;
	
	public VersionHandler(){
		xmlRemoteHelper = new OnlineXMLHelper(XML_REMOTE_VERSION);
		xmlLocalHelper = new XMLHelper(XML_LOCAL_VERSION);
	}

	public static Version calculateVersion(String version) {
		Version v = new Version();
		
		String[] val;
		val = version.split("\\.");
		
		v.major = Integer.parseInt(val[0]);
		if(Integer.parseInt(val[1]) != 0) v.minor = Integer.parseInt(val[1]);
		if(val.length > 2 && Integer.parseInt(val[2]) != 0) v.fix = Integer.parseInt(val[2]);
		if(val.length > 3 && !val[3].equals("dev0")) v.dev = Integer.parseInt(val[3].replace("dev", ""));
		
		return v;
	}
	
	public Version getLocalVersion() {
		if(localVersion == null) {
			xmlLocalHelper.setProp();
			localVersion = calculateVersion(xmlLocalHelper.getXMLProps("localVersion"));
		}
		return localVersion;
	}
	
	public Version getRemoteVersion() {
		if(remoteVersion == null) {
			xmlRemoteHelper.setProp();
			remoteVersion = calculateVersion(xmlRemoteHelper.getXMLProps("remoteVersion"));
		}
		return remoteVersion;
	}
	
	public VersionHandler getVersions() {
		getLocalVersion();
		getRemoteVersion();
		return this;
	}
	
	public boolean needsUpdate() {
		DebugHelper.print(DebugHelper.DEBUG, "Online version: " + remoteVersion + ", Local Version: "+ localVersion);
		return !localVersion.greaterThenEqual(remoteVersion);
	}
	
}
