package gory_moon.stevescarts2.downloader.update;

import gory_moon.stevescarts2.downloader.Main;
import gory_moon.stevescarts2.downloader.core.Download;
import gory_moon.stevescarts2.downloader.core.helper.DebugHelper;
import gory_moon.stevescarts2.downloader.core.helper.Status;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gory_Moon
 */
public class Updater implements Observer{
   
    private static Download updater;
    
    public Updater(){
        downloadUpdate();
    }
    private void downloadUpdate(){
        URL url = null;
            try {
                    url = new URL("https://dl.dropbox.com/u/65769242/Steves%20Carts%202%20Downloader/SC2DUpdater.jar");
            } catch (Exception e) {
            	DebugHelper.print(DebugHelper.ERROR, "Could not update! Try again later!");
                Main.sendMessage("Error with updating!\nTry again later.", "Error");
            }
        updater = new Download(url); 
        updater.addObserver(this);
    }
    
    private void updateDownloader(){
        ProcessBuilder pb = new ProcessBuilder(System.getProperty("java.home")+"\\bin\\java", "-jar", "SC2DUpdater.jar");
        pb.directory(null);
        try {
            pb.start();
            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(updater.getStatus() == Status.COMPLETE){
            updateDownloader();
        }
    }
}
