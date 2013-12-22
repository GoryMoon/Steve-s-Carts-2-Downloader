package gory_moon.stevescarts2.downloader;

import gory_moon.stevescarts2.downloader.core.handlers.ChangelogHandler;
import gory_moon.stevescarts2.downloader.core.handlers.VersionHandler;
import gory_moon.stevescarts2.downloader.core.helper.DebugHelper;
import gory_moon.stevescarts2.downloader.core.helper.Version;
import gory_moon.stevescarts2.downloader.core.items.Type;
import gory_moon.stevescarts2.downloader.core.items.VersionItem;
import gory_moon.stevescarts2.downloader.update.Updater;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * @author Gory_Moon
 */
public class Main {

	public static Main instance;
    private static boolean oldVersion = false;
    private static boolean isUpdating = false;
    private static boolean hasInternet = true;

    public static boolean debug = false;
    public static boolean dev = false;

    public static Frame frame;

    public Main(){
    	instance = this;
        VersionHandler vHandler = new VersionHandler();
        Version SC2DV = vHandler.getLocalVersion();

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ignored) {}
        catch (UnsupportedLookAndFeelException ignored) {}
        catch (InstantiationException ignored) {}
        catch (IllegalAccessException ignored) {}

        MainWindow mainWindow = new MainWindow();
    	frame = new Frame(mainWindow);
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
            boolean needsUpdate = vHandler.getVersions().needsUpdate();
        	frame.getStartsim().addText((needsUpdate ? ": Needs update\nChecking if should update": ": Up to date\nLoading Exeptions"));
        	frame.setTitle("SC 2 Downloader " + SC2DV);
            
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
            if(!needsUpdate){
                frame.runFrame();
            }else{
                oldVersion = true;
                int pick = JOptionPane.showConfirmDialog(null, "A update is avalible.\nOnline Version: " + vHandler.getRemoteVersion() + "\nLocal Version: " + SC2DV + "\nDo you want to download it now?",
                "Updater", JOptionPane.YES_NO_OPTION);
                isUpdating = (pick == 0);
                frame.getStartsim().addText(isUpdating ? ": Updating": ": Carrying on");
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
        JOptionPane.showMessageDialog(null, message,
        name, JOptionPane.PLAIN_MESSAGE);
    }

    public static boolean gethasInternet() {
		return hasInternet;
	}

    public static ChangelogHandler getChangelogHandler() {
		return Frame.getChangelogHandler();
	}

    public static ArrayList<VersionItem> getWebArray() throws IOException{
        String[] temp;
        URL url = new URL("http://gorymoon.dx.am/stevescarts2/versionFile.txt");
        URLConnection con = url.openConnection();
        InputStreamReader r = new InputStreamReader(con.getInputStream(), "UTF-8");
        StringBuilder buf = new StringBuilder();
        while (true) {
            int ch = r.read();
            if (ch < 0)
                break;
            buf.append((char) ch);
        }
        String str = buf.toString();
        temp = str.split("#LINE-END#");

        String MCversion = "";

        ArrayList<VersionItem> versions = new ArrayList<VersionItem>();

        for(String s: temp){
        	s = s.replace("\n", "");

            if(s.contains("%MC%")){
            	MCversion = s.replace(" ", "").substring(4);

            } else if(s != null && !s.isEmpty()){
            	DebugHelper.print(DebugHelper.DEBUG, "||" + s + "||");
            	String s1 = s.substring(1, 2);
            	Type type = Type.getType(s1);

            	int version = Integer.valueOf(s.substring(3, s.indexOf("%/" + s1 + "%")));
            	String change = s.split("%/" + s1 + "%")[1];
            	versions.add(new VersionItem(version, change, type, MCversion));
            }
        }

        return versions;
    }
}
