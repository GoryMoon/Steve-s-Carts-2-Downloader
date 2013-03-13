package gory_moon.stevescarts2.downloader;

import gory_moon.stevescarts2.downloader.core.GetUpdates;
import gory_moon.stevescarts2.downloader.helper.DebugHelper;
import gory_moon.stevescarts2.downloader.update.Updater;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Gory_Moon
 */
public class Main {

    private static String Dot = ";";
    private static String Comma = "$";
    private static String Apostrophe = "£";
    private static String Quote = "§";
    private static int version = 0;
    
    private static String SC2DV = "1.0.0";
    private static boolean oldVersion = false;
    private static boolean isUpdating = false;
    private static boolean hasInternet = true;
    private static InputStream in;
    private static boolean needsUpdate = false;
    public static boolean debug = true;

    /*
     * These are exeptions that is hard to fix.
     */
    private static String[] exeptions = {""};
    private static String[] vexeptions = {""};
    
    
    public static void setNeedsUpdate(boolean needsUpdate) {
		Main.needsUpdate = needsUpdate;
	}

	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    	removeUpdateFiles();
        setSC2DVersion();
        setExeptions();
        setVersionExeptions();
        try{
            hasInternet = InetAddress.getByName("gorymoon.dx.am").isReachable(10000);
        } catch (IOException ex) {
        	hasInternet = false;
        }
        if(hasInternet){
            GetUpdates.dVersionCheck();
            
            if(!needsUpdate){
                Frame.runFrame();
                removeUpdateFiles();
            }else{
                oldVersion = true;
                int pick = JOptionPane.showConfirmDialog((Component) null, "A update is avalible.\nOnline Version: "+GetUpdates.getSVersion()+"\nLocal Version: "+SC2DV+"\nDo you want to download it now?",
                "Updater", JOptionPane.YES_NO_OPTION);
                isUpdating = (pick==0)?true:false;
            }
            if(isUpdating&&oldVersion){
                new Updater();
            }else if(needsUpdate){
                Frame.runFrame();
                removeUpdateFiles();
            }
        }else{
        	noInternet();
        	removeUpdateFiles();
        	Frame.runFrame();
        }
    }
    
    private static void noInternet() {
    	Main.sendMessage("Couldn't connect to the servers! Try again later and/or check you internet connection.", "Error");
    	hasInternet = false;
	}

	public static void setExeptions(){
        exeptions = getXMLData(1);
        for(int i=0;i<=exeptions.length-1;i++){
        	DebugHelper.print(DebugHelper.DEBUG, "Exeption "+i+": "+exeptions[i]);
        }
    }
    
    public static void setSC2DVersion(){
        SC2DV = getXMLData(0)[0];
        DebugHelper.print(DebugHelper.INFO, "Version: "+SC2DV);
    }
    
    public static void setVersionExeptions(){
    	vexeptions = getXMLData(2);
    	for(int i = 0; i <= vexeptions.length-1;i++){
    		DebugHelper.print(DebugHelper.DEBUG, "Version Exeption "+ i +": " + vexeptions[i]);
    	}
    }
    
    private static String[] getXMLData(int type){
        
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            in = Main.class.getResourceAsStream("resources/SC2D.xml");
            Document doc = docBuilder.parse(in);

            doc.getDocumentElement ().normalize ();

            if(type == 0){
                NodeList listoftags = doc.getElementsByTagName("version");
                Node node = listoftags.item(0);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element)node;
                    String[] TempVersion = {"1.0.0"}; 
                    NodeList versionList = element.getElementsByTagName("v");
                    Element versionElement = (Element)versionList.item(0);
                    NodeList textVersionList = versionElement.getChildNodes();
                    TempVersion[0] = ((Node)textVersionList.item(0)).getNodeValue().trim();
                    return TempVersion;
                }
            }else if(type == 1){
                NodeList listoftags = doc.getElementsByTagName("exeption");
                String[] TempExeptions = new String[listoftags.getLength()];
                for(int s=0; s<listoftags.getLength() ; s++){
                    Node node = listoftags.item(s);
                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element element = (Element)node;

                        NodeList orginalList = element.getElementsByTagName("orginal");
                        Element orginalElement = (Element)orginalList.item(0);
                        NodeList textORList = orginalElement.getChildNodes();

                        NodeList replacementList = element.getElementsByTagName("replacement");
                        Element replacementElement = (Element)replacementList.item(0);
                        NodeList textRPList = replacementElement.getChildNodes();

                        TempExeptions[s] = ((Node)textORList.item(0)).getNodeValue().trim()+"%SPLIT%"
                                +((Node)textRPList.item(0)).getNodeValue().trim();
                    }
                }
                return TempExeptions;
            }else if(type == 2){
            	NodeList listoftags = doc.getElementsByTagName("versionexeption");
                String[] VersionExeptions = new String[listoftags.getLength()];
                for(int s=0; s<listoftags.getLength() ; s++){
                    Node node = listoftags.item(s);
                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element element = (Element)node;

                        NodeList orginalList = element.getElementsByTagName("version");
                        Element orginalElement = (Element)orginalList.item(0);
                        NodeList textORList = orginalElement.getChildNodes();

                        VersionExeptions[s] = ((Node)textORList.item(0)).getNodeValue().trim();
                    }
                }
                return VersionExeptions;
            }
            
        }catch (SAXParseException err){}catch (SAXException e){}catch (Throwable t){}
        return null;
    }
    
    public static void removeUpdateFiles(){
        new File("Steve%27s%20Carts%202%20Downloader.zip").delete();
        new File("SC2DUpdater.zip").delete();
        new File("SC2DUpdater.jar").delete();
    }
    
    public static void sendMessage(String message, String name){
        JOptionPane.showMessageDialog((Component) null, message,
        name, JOptionPane.PLAIN_MESSAGE);
    }
    
    //Get the Downloader version
    public static String getDownloaderVersion() {
        return SC2DV;
    }
    
    public static int getVersion(){
        return version+4;
    }
    
    //Get the version exeptions array
    public static String[] getVersionExeptions(){
    	return vexeptions;
    }
    
    public static String changlogHandler(String[] changelog, String id){
        String print = "Change Log:\n";
        String logId = id.replace("a", "");
        version = Integer.parseInt(logId);
        int versionjump = 0;
        
        for(int i = 0; i<=vexeptions.length-1;i++){
        	if(version >= Integer.parseInt(vexeptions[i])){
        		versionjump++;
        	}
        }
        DebugHelper.print(DebugHelper.DEBUG, "Jumped with "+versionjump+" from: "+version+", to: "+(version-versionjump));
        version = version-versionjump-4;

        for(int j = 0;j <= exeptions.length-1;j++){
            String[] temp = exeptions[j].split("%SPLIT%");
            changelog[version] = changelog[version].replace(temp[0], temp[1]);
        }

        String[] printArray = changelog[version].replace(".,", ",").split("[,\\.]");
        for (int i = 0; i <= printArray.length-1;i++){
            if(printArray[i].contains(Dot)||printArray[i].contains(Comma)||printArray[i].contains(Apostrophe)||printArray[i].contains(Quote))
            {
                printArray[i] = printArray[i].replace(Dot, ".");
                printArray[i] = printArray[i].replace(Comma, ",");
                printArray[i] = printArray[i].replace(Apostrophe, "\'");
                printArray[i] = printArray[i].replace(Quote, "\"");
            }
            print = print+ printArray[i]+".\n";
        }
        return print;
    }

	public static boolean hasInternet() {
		return hasInternet;
	}
}
