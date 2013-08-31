package gory_moon.stevescarts2.downloader;

import gory_moon.stevescarts2.downloader.core.handlers.ChangelogHandler;
import gory_moon.stevescarts2.downloader.core.handlers.VersionHandler;
import gory_moon.stevescarts2.downloader.core.helper.Version;
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
	private static Version SC2DV;
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
    	vHandler = new VersionHandler();
    	SC2DV = vHandler.getLocalVersion();
    	
    	frame = new Frame();
    	removeUpdateFiles();
    	
        try{
        	frame.getStartsim().setText("Connecting to server");
            hasInternet = InetAddress.getByName("gorymoon.dx.am").isReachable(10000);
        } catch (IOException ex) {
        	frame.getStartsim().addText(": Failed");
        	hasInternet = false;
        }
        if(hasInternet){
        	frame.getStartsim().addText(": Success\nChecking if updating is needed");
        	needsUpdate = vHandler.getVersions().needsUpdate();
        	frame.getStartsim().addText((needsUpdate ? ": Needs update\nChecking if should update": "Up to date\nLoading Exeptions"));
        	frame.setTitle("SC 2 Downloader " + SC2DV);
            
            if(!needsUpdate){
                frame.runFrame();
            }else{
                oldVersion = true;
                int pick = JOptionPane.showConfirmDialog((Component) null, "A update is avalible.\nOnline Version: "+vHandler.getRemoteVersion()+"\nLocal Version: "+SC2DV+"\nDo you want to download it now?",
                "Updater", JOptionPane.YES_NO_OPTION);
                isUpdating = (pick==0)?true:false;
                frame.getStartsim().addText(isUpdating ? ": Updating": "Carrying on");
            }
            if(isUpdating && oldVersion){
            	frame.getStartsim().isRunning = false;
                new Updater();
            }else if(needsUpdate){
                frame.runFrame();
            }
        }else{
        	frame.getStartsim().isRunning = false;
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
    
    public static String getDownloaderVersion() {
        return SC2DV.toString();
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
