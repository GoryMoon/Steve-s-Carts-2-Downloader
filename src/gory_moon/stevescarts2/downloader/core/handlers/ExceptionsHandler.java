package gory_moon.stevescarts2.downloader.core.handlers;

import gory_moon.stevescarts2.downloader.core.helper.DebugHelper;
import gory_moon.stevescarts2.downloader.core.helper.OnlineXMLHelper;

public class ExceptionsHandler {
	
    /*
     * These are exeptions that is hard to fix.
     */
    private String[] exeptions = {""};
	private String[] vexeptions = {""};

    private OnlineXMLHelper xmlHelper;
	private final String XML_EXCEPTIONS = "https://dl.dropboxusercontent.com/u/65769242/Steves%20Carts%202%20Downloader/SC2DExceptions.xml";
    
    public ExceptionsHandler(){
    	xmlHelper = (OnlineXMLHelper) new OnlineXMLHelper(XML_EXCEPTIONS).setProp();
    	setExceptions();
    	setVersionExceptions();
    }
    
    private void setExceptions(){
        exeptions = xmlHelper.convertToArray("¾",xmlHelper.getXMLProps("exceptions"));
        for(int i=0;i<=exeptions.length-1;i++){
        	String tab;
        	tab = (exeptions[i].split("½")[0].length() < 11) ? "\t\t" : "\t" ;
        	DebugHelper.print(DebugHelper.DEBUG, "Exeption "+ i +": "+ exeptions[i].split("½")[0] + tab + exeptions[i].split("½")[1]);
        }
    }

    private void setVersionExceptions(){
    	
    	vexeptions = xmlHelper.convertToArray("¾",xmlHelper.getXMLProps("vexceptions"));
    	for(int i = 0; i <= vexeptions.length-1;i++){
    		DebugHelper.print(DebugHelper.DEBUG, "Version Exeption "+ i +": " + vexeptions[i]);
    	}
    }
    
	public String[] getVexeptions() {
		return vexeptions;
	}
    
	public String[] getExeptions() {
		return exeptions;
	}
	
}
