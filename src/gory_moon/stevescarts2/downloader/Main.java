package gory_moon.stevescarts2.downloader;

import gory_moon.stevescarts2.downloader.core.handlers.ChangelogHandler;
import gory_moon.stevescarts2.downloader.core.handlers.VersionHandler;
import gory_moon.stevescarts2.downloader.update.Updater;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JOptionPane;

/**
 * @author Gory_Moon
 */
public class Main {

	public static Main instance;
	private static String SC2DV = "1.5.1";
    private static boolean oldVersion = false;
    private static boolean isUpdating = false;
    private static boolean hasInternet = true;
    
    private static boolean needsUpdate = false;
    public static boolean debug = true;
    
    private static VersionHandler vHandler;
    public static Frame frame;
    
    public static void setNeedsUpdate(boolean needsUpdate) {
		Main.needsUpdate = needsUpdate;
	}

    public Main(){
    	instance = this;
    	
    	SC2DV = VersionHandler.getLocalVersion();
    	
    	frame = new Frame();
    	
    	// TODO Rework the startup
    	removeUpdateFiles();
    	
        try{
            hasInternet = InetAddress.getByName("gorymoon.dx.am").isReachable(10000);
        } catch (IOException ex) {
        	hasInternet = false;
        }
        if(hasInternet){
        	vHandler = new VersionHandler();
        	frame.setTitle("Steve's Carts 2 Downloader " + SC2DV);
            
            if(!needsUpdate){
                frame.runFrame();
            }else{
                oldVersion = true;
                int pick = JOptionPane.showConfirmDialog((Component) null, "A update is avalible.\nOnline Version: "+vHandler.getRemoteVersion()+"\nLocal Version: "+SC2DV+"\nDo you want to download it now?",
                "Updater", JOptionPane.YES_NO_OPTION);
                isUpdating = (pick==0)?true:false;
            }
            if(isUpdating&&oldVersion){
                new Updater();
            }else if(needsUpdate){
                frame.runFrame();
            }
        }else{
        	noInternet();
        	frame.runFrame();
        }
    }
    
	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	new Main();
    }
    
    private static void noInternet() {
    	Main.sendMessage("Couldn't connect to the servers! Try again later and/or check you internet connection.", "Error");
    	hasInternet = false;
	}
    
    public static void removeUpdateFiles(){
        new File("Steve%27s%20Carts%202%20Downloader.jar").delete();
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
    
    public static void setDownloaderVersion(String v) {
        SC2DV = v;
    }
    
    public static int getVersion(){
        return getChangelogHandler().getVersion()+4;
    } 

	public static boolean gethasInternet() {
		return hasInternet;
	}

	public static ChangelogHandler getChangelogHandler() {
		return Frame.getChangelogHandler();
	}

}
