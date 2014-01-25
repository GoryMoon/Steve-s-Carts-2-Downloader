package gory_moon.stevescarts2.downloader;

import gory_moon.stevescarts2.downloader.core.Download;
import gory_moon.stevescarts2.downloader.core.StartupSimulator;
import gory_moon.stevescarts2.downloader.core.handlers.ChangelogHandler;
import gory_moon.stevescarts2.downloader.core.helper.Status;
import gory_moon.stevescarts2.downloader.core.items.VersionItem;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Gory_Moon
 */
public class Frame extends JFrame implements Observer {

    MainWindow mainWindow;

    ArrayList<VersionItem> items = new ArrayList<VersionItem>();
    Boolean firstrun = true;
    Boolean hasInternet = true;

    public static Download selectedDownload;
    public boolean errorHappend = false;
	private int ChangelogChecks = 0;

    private StartupSimulator startsim;

    public void runMain(){ 
        hasInternet = Main.gethasInternet();
        
    	if(hasInternet){
	        if(firstrun){

	            try {
	                items = Main.getWebArray();

                    for (VersionItem item : items) {
                        mainWindow.versionBox.addItem(makeObj(item));
                    }

	                mainWindow.versionBox.setSelectedIndex(0);
	                firstrun = false;
	                changeLog();
                    mainWindow.downloadButton.setEnabled(true);
                } catch (Exception e) {
                	error(null, e);
				}
	        }
    	}else{
    		noInternet();
    	}
    }

    public Frame(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        startsim = new StartupSimulator();
        Thread startsimT = new Thread(startsim);
        startsimT.start();
    }
    private VersionItem makeObj(final VersionItem item)  {
        return new VersionItem(item) {public String toString() { return item.toString(); }};
    }
    
    public void resetStats(){
        mainWindow.downloadVersion.setText(null);
        mainWindow.fileSize.setText(null);
        mainWindow.fileDone.setText(null);
        mainWindow.fileLeft.setText(null);
        mainWindow.downloadDoneBar.setValue(0);
    }
    
    private void setMcVersion(VersionItem item) {
        String mcV = item.getMcV();
    	
        mcV = mcV.replace("andabove", "+");
        mainWindow.versionReq.setText(mcV);
    }
    
    public void error(String e){
    	error(e, null);
    }

    public void error(String e, Exception exception){
        if(!errorHappend){
            errorHappend = true;
            hasInternet = false;
            firstrun = true;
            if(selectedDownload != null&& selectedDownload.getStatus() == Status.DOWNLOADING){
                selectedDownload.setStatus(Status.CANCELLED);
            }
            mainWindow.versionBox.removeAllItems();
            mainWindow.versionBox.addItem(new Object() {
                public String toString() {
                    return "No Versions Avalible";
                }
            });
            mainWindow.versionBox.setSelectedIndex(0);
            mainWindow.versionBox.setEnabled(false);
            mainWindow.changeLogArea.setAutoscrolls(false);
            mainWindow.changeLogArea.setText("No Changelog Avalible");
            mainWindow.downloadButton.setEnabled(false);
            if (!Main.dev) {
                try {
                    File errorFile = new File("Steve's Carts 2 Downloader Crash Log " + getDateTime() + ".txt");
                    PrintStream ps = new PrintStream(errorFile);

                    if (e != null) ps.append(e);
                    if (exception != null) exception.printStackTrace(ps);

                    ps.close();
                    resetStats();
                    Main.sendMessage("An unexpected error oucurred! Send the \""+ errorFile.getName() +"\" to \"Gory_Moon\" at www.stevescarts2.wikispaces.com", "Error");
                } catch (Exception ex) {
                    Main.sendMessage("I don't know how you did it but report this to \"Gory_Moon\" at www.stevescarts2.wikispaces.com\nReport code: 6243", "Error in Error");
                }
            } else {
                exception.printStackTrace();
                resetStats();
            }
        }
    }

    void noInternet(){
    	mainWindow.versionBox.addItem(new Object() {
            public String toString() {
                return "No Versions Avalible";
            }
        });
        mainWindow.versionBox.setSelectedIndex(0);
        mainWindow.versionBox.setEnabled(false);
        mainWindow.changeLogArea.setText("No changelog avalible");
        mainWindow.downloadButton.setEnabled(false);
    }
    
    private void changeLog(){
        VersionItem item = (VersionItem) mainWindow.versionBox.getSelectedItem();
        mainWindow.changeLogArea.setAutoscrolls(false);
        mainWindow.changeLogArea.setText(ChangelogHandler.changlogHandler(item));
        setMcVersion(item);
    }
    public String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
        return sdf.format(date);
    }
    
    private void updateProgress(float p, Status status){
    	if(status == Status.COMPLETE){
            mainWindow.downloadButton.setEnabled(true);
    	}else if(status == Status.DOWNLOADING){
            mainWindow.downloadDoneBar.setValue((int)p);
    	}
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("SC2D");
        setResizable(false);
        setContentPane(mainWindow.panel1);
        mainWindow.downloadButton.setEnabled(false);

        mainWindow.downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed();
            }
        });
        mainWindow.closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                closeButtonActionPerformed();
            }
        });
        mainWindow.versionBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                versionBoxItemStateChanged();
            }
        });
        
        addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					downloadButtonActionPerformed();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyPressed(KeyEvent arg0) {}
		});

        mainWindow.versionBox.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					downloadButtonActionPerformed();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
        });

        pack();
    }

    private void downloadButtonActionPerformed() {
    	if(hasInternet){
            resetStats();
            mainWindow.downloadButton.setEnabled(false);
            String version = mainWindow.versionBox.getSelectedItem().toString();
            URL url = null;
            try {
            	url = new URL("http://dl.dropbox.com/u/46486053/StevesCarts2.0.0." + version + ".zip");
            } catch (Exception e) {
                error(null, e);
            }
            selectedDownload = new Download(url); 
            selectedDownload.addObserver(Frame.this);
            mainWindow.downloadVersion.setText(version);
        }
    }

    private void closeButtonActionPerformed() {
        System.exit(-1);
    }

    private void versionBoxItemStateChanged() {
        if(!firstrun){
        	if(ChangelogChecks == 0){
        		changeLog();
                ChangelogChecks = 1;
        	}else{
        		ChangelogChecks = 0;
        	}
        }
    }

    public void runFrame() {                       
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Main.frame.runMain();
            }
        });
    }
    
	@Override
	public void update(Observable arg0, Object arg1) {
        if(selectedDownload.getStatus() == Status.ERROR){
            if(selectedDownload.getErrorCode().equals(Download.EDOWNLOADFAILED)){
                error(selectedDownload.getErrorCode() + selectedDownload.getUrl(), Download.getEXCEPTION());
            }else{
                error(selectedDownload.getErrorCode());
            }
            resetStats();
        }else{
            String ext = "b";
            int size = (int)Math.ceil(((double)selectedDownload.getSize()));
            int done = (int)Math.ceil(((double)selectedDownload.getDownloaded()));
            int left = (int)Math.ceil((((double)selectedDownload.getSize())-((double)selectedDownload.getDownloaded())));
            if(size >= 4096){
                ext = "Kb";
                size = (int)Math.ceil(((double)size)/1024);
                done = (int)Math.ceil(((double)done)/1024);
                left = (int)Math.ceil((((double)size)-((double)done)));
                if (size >= 4096){
                    ext = "Mb";
                    size = (int)Math.ceil(((double)size)/1024);
                    done = (int)Math.ceil(((double)done)/1024);
                    left = (int)Math.ceil((((double)size)-((double)done)));
                    if(size >= 4096){
                        ext = "Gb";
                        size = (int)Math.ceil(((double)size)/1024);
                        done = (int)Math.ceil(((double)done)/1024);
                        left = (int)Math.ceil((((double)size)-((double)done)));
                    }
                }
            }
            updateProgress(selectedDownload.getProgress(),selectedDownload.getStatus());
            mainWindow.fileSize.setText(size+ext);
            mainWindow.fileDone.setText(done+ext);
            mainWindow.fileLeft.setText(left+ext);
            
        }
	}

    public void displayBoxText(String l) {
		mainWindow.changeLogArea.setText(l);
	}
	
	public StartupSimulator getStartsim() {
		return startsim;
	}
}
