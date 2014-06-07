package gory_moon.stevescarts2.downloader;

import gory_moon.stevescarts2.downloader.core.handlers.VersionHandler;
import gory_moon.stevescarts2.downloader.core.helper.DebugHelper;
import gory_moon.stevescarts2.downloader.core.helper.Version;
import gory_moon.stevescarts2.downloader.core.items.Type;
import gory_moon.stevescarts2.downloader.core.items.VersionItem;
import gory_moon.stevescarts2.downloader.update.Updater;

import javax.swing.*;
import java.io.*;
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

    public static boolean debug = true;
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
        Startup startup = new Startup();
        frame.setTitle("SC2 Downloader " + SC2DV);
    	removeUpdateFiles();

        try{
            startup.setProgressText("Connecting to server").setUnknownTime(true);
            hasInternet = InetAddress.getByName("gorymoon.dx.am").isReachable(10000);
        } catch (IOException ex) {
        	hasInternet = false;
            startup.setProgressText("Error connecting");
            startup.setUnknownTime(false);
        }
        if(hasInternet){
            startup.setProgressText("Checking if updating is needed").setUnknownTime(true);
            boolean needsUpdate = vHandler.getVersions().needsUpdate();
            startup.setProgressText((needsUpdate ? "Checking if should update" : "Loading data")).setUnknownTime(true);
        	frame.setTitle("SC2 Downloader " + SC2DV);

            if(!needsUpdate){
                runFrame();
            }else{
                oldVersion = true;
                int pick = JOptionPane.showConfirmDialog(null, "A update is avalible.\nOnline Version: " + vHandler.getRemoteVersion() + "\nLocal Version: " + SC2DV + "\nDo you want to download it now?",
                "Updater", JOptionPane.YES_NO_OPTION);
                isUpdating = (pick == 0);
                startup.setProgressText(isUpdating ? "Updating" : "Loading data");
            }
            if(isUpdating && oldVersion){
                new Updater();
            }else if(needsUpdate){
                runFrame();
            }
            startup.setVisible(false);
        }else{
        	noInternet();
        	frame.runFrame();
            startup.setVisible(false);
        }
    }

    private void runFrame() {
        try {
            frame.items = getWebArray();
        } catch (IOException ignored) {}
        frame.runFrame();
    }
    
	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	new Main();
    }
    
    private static void noInternet() {
    	Main.sendMessage("Couldn't connect to the server! Try again later and/or check you internet connection.", "Error");
    	hasInternet = false;
	}
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
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

    public static ArrayList<VersionItem> getWebArray() throws IOException{
        String[] temp;
        URL url = new URL("http://gorymoon.dx.am/stevescarts2/versionFile.txt");
        URLConnection con = url.openConnection();

        InputStreamReader r = new InputStreamReader(new BufferedInputStream(con.getInputStream()), "UTF-8");
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
