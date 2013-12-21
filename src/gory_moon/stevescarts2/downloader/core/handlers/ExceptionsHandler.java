package gory_moon.stevescarts2.downloader.core.handlers;

import gory_moon.stevescarts2.downloader.core.helper.DebugHelper;
import gory_moon.stevescarts2.downloader.core.helper.OnlineXMLHelper;

public class ExceptionsHandler {
	
    /*
     * These are exeptions that is hard to fix.
     */
    private String[] exeptions = {""};

    private OnlineXMLHelper xmlHelper;

    public ExceptionsHandler(){
        String XML_EXCEPTIONS = "https://dl.dropboxusercontent.com/u/65769242/Steves%20Carts%202%20Downloader/SC2DExceptions.xml";
        xmlHelper = (OnlineXMLHelper) new OnlineXMLHelper(XML_EXCEPTIONS).setProp();
    	setExceptions();
    }
    
    private void setExceptions(){
        exeptions = xmlHelper.convertToArray("¾",xmlHelper.getXMLProps("exceptions"));
        for(int i=0;i<=exeptions.length-1;i++){
            String[] ex = exeptions[i].split("½");
            int l = ex[0].length();
            String tab = l < 3 ? "\t\t\t\t\t" : l < 7 ? "\t\t\t\t" : l < 11 ? "\t\t\t" : l < 16 ? "\t\t": "\t" ;
        	DebugHelper.print(DebugHelper.DEBUG, "Exeption "+ i +": "+ ex[0] + tab + ex[1]);
        }
    }

	public String[] getExeptions() {
		return exeptions;
	}

}
