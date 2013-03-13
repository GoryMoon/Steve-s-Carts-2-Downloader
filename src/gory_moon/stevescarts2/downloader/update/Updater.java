package gory_moon.stevescarts2.downloader.update;

import gory_moon.stevescarts2.downloader.Main;
import gory_moon.stevescarts2.downloader.core.Download;
import gory_moon.stevescarts2.downloader.helper.DebugHelper;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
                    url = new URL("https://dl.dropbox.com/u/65769242/Steves%20Carts%202%20Downloader/SC2DUpdater.zip");
            } catch (Exception e) {
            	DebugHelper.print(DebugHelper.ERROR, "Could not update! Try again later!");
                Main.sendMessage("Error with updating!\nTry again later.", "Error");
            }
        updater = new Download(url); 
        updater.addObserver(Updater.this);
    }
    
    public static final void copyInputStream(InputStream in, OutputStream out)
    throws IOException
    {
        byte[] buffer = new byte[1024];
        int len;

        while((len = in.read(buffer)) >= 0)
        out.write(buffer, 0, len);

        in.close();
        out.close();
    }
    
    private void unZip(){
        ZipFile zip;
        @SuppressWarnings("rawtypes")
		Enumeration entries;

        try {
            zip = new ZipFile("SC2DUpdater.zip");

            entries = zip.entries();

            while(entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry)entries.nextElement();
                copyInputStream(zip.getInputStream(entry),
                new BufferedOutputStream(new FileOutputStream("SC2DUpdater.jar")));
            }
        } catch (IOException ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateDownloader();
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
        if(updater.getStatus()==Download.COMPLETE){
            unZip();
        }
    }
}
