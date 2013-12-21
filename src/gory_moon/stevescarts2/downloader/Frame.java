package gory_moon.stevescarts2.downloader;

import gory_moon.stevescarts2.downloader.core.Download;
import gory_moon.stevescarts2.downloader.core.ExceptionThread;
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

    ArrayList<VersionItem> items = new ArrayList<VersionItem>();
    Boolean firstrun = true;
    Boolean hasInternet = true;

    private static Download selectedDownload;
    private boolean errorHappend = false;
	private int ChangelogChecks = 0;

    private StartupSimulator startsim;
    private static ChangelogHandler changelogHandler;

    public void runMain(){ 
        hasInternet = Main.gethasInternet();
        
    	if(hasInternet){
	        if(firstrun){
                ExceptionThread exceptionT = new ExceptionThread();
                Thread excT = new Thread(exceptionT);
	        	excT.start();
	        	
	        	while(!ExceptionThread.isDone){
                    try {
					    Thread.sleep(500);
				    } catch (InterruptedException ignored) {}
                }

                changelogHandler = exceptionT.getChangelogHandler();
	            try {
	                items = Main.getWebArray();

                    for (VersionItem item : items) {
                        jComboBox1.addItem(makeObj(item));
                    }
                    
                    startsim.isRunning = false;
	                jComboBox1.setSelectedIndex(0);
	                firstrun = false;
	                changeLog();
                    jButton1.setEnabled(true);
                } catch (Exception e) {
                	error(null, e);
				}
	        }
    	}else{
    		noInternet();
    	}
    }
    
    /**
     * Creates new form Frame
     */
    public Frame() {
    	
		/*
         * Set the Nimbus look and feel
         */

        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            error(null, e);
        } catch (UnsupportedLookAndFeelException e) {
            error(null, e);
        } catch (InstantiationException e) {
            error(null, e);
        } catch (IllegalAccessException e) {
            error(null, e);
        }

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
    
    private void resetStats(){
        jLabel7.setText("0%");
        jLabel8.setText(null);
        jLabel9.setText(null);
        jLabel10.setText(null);
        jLabel11.setText(null);
        jProgressBar1.setValue(0);
    }
    
    private void setMcVersion(VersionItem item) {
        String mcV = item.getMcV();
    	
        mcV = mcV.replace("andabove", "+");
        jLabel15.setText(mcV);
    }
    
    private void error(String e){
    	error(e, null);
    }
    
    private void error(String e, Exception exception){
        if(!errorHappend){
            errorHappend = true;
            hasInternet = false;
            firstrun = true;
            if(selectedDownload != null&& selectedDownload.getStatus() == Status.DOWNLOADING){
                selectedDownload.setStatus(Status.CANCELLED);
            }
            jComboBox1.removeAllItems();
            jComboBox1.addItem(new Object() {public String toString() { return "No Versions Avalible"; }});
            jComboBox1.setSelectedIndex(0);
            jComboBox1.setEnabled(false);
            jTextArea1.setAutoscrolls(false);
            jTextArea1.setText("No Changelog Avalible");
            jButton1.setEnabled(false);
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
    	jComboBox1.addItem(new Object() {public String toString() { return "No Versions Avalible"; }});
        jComboBox1.setSelectedIndex(0);
        jComboBox1.setEnabled(false);
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setText("No changelog avalible");
        jButton1.setEnabled(false);
    }
    
    private void changeLog(){
        VersionItem item = (VersionItem) jComboBox1.getSelectedItem();
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setText(Main.getChangelogHandler().changlogHandler(item));
        setMcVersion(item);
    }
    public String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
        return sdf.format(date);
    }
    
    private void updateProgress(float p, Status status){
    	if(status == Status.COMPLETE){
            jButton1.setEnabled(true);
    	}else if(status == Status.DOWNLOADING){
            jLabel7.setText((int)p+"%");
            jProgressBar1.setValue((int)p);
    	}
    }

    private void initComponents() {

        jButton1 = new JButton();
        jButton1.setEnabled(false);
        JButton jButton2 = new JButton();
        jComboBox1 = new JComboBox<Object>();
        JScrollPane jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jProgressBar1 = new JProgressBar();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();
        JLabel jLabel4 = new JLabel();
        JLabel jLabel5 = new JLabel();
        JLabel jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        jLabel11 = new JLabel();
        JLabel jLabel14 = new JLabel();
        jLabel15 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("SC2D");
        setResizable(false);

        jButton1.setText("Download");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed();
            }
        });

        jButton2.setText("Close");
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed();
            }
        });

        jComboBox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                jComboBox1ItemStateChanged();
            }
        });
        
        addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					jButton1ActionPerformed();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {}
			
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
        
        jComboBox1.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					jButton1ActionPerformed();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Version:");

        jLabel2.setText("Size: ");

        jLabel3.setText("Done:");

        jLabel4.setText("Version:");

        jLabel5.setText("Left: ");

        jLabel6.setText("Progress:");
        jLabel6.setToolTipText("");

        jLabel7.setText("0%");

        jLabel14.setText("Minecraft required:");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 32, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(8, 8, 8))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel10))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel11))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGap(27, 27, 27))))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }

    private void jButton1ActionPerformed() {
    	if(hasInternet){
            resetStats();
            jButton1.setEnabled(false);
            String version = jComboBox1.getSelectedItem().toString();
            URL url = null;
            try {
            	url = new URL("http://dl.dropbox.com/u/46486053/StevesCarts2.0.0." + version + ".zip");
            } catch (Exception e) {
                error(null, e);
            }
            selectedDownload = new Download(url); 
            selectedDownload.addObserver(Frame.this);
            jLabel8.setText(version);
        }
    }

    private void jButton2ActionPerformed() {
        System.exit(-1);
    }

    private void jComboBox1ItemStateChanged() {
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
    private JButton jButton1;
    private JComboBox<Object> jComboBox1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel15;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JProgressBar jProgressBar1;
    private JTextArea jTextArea1;
    
    
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
            jLabel9.setText(size+ext);
            jLabel10.setText(done+ext);
            jLabel11.setText(left+ext);
            
        }
	}
	public static ChangelogHandler getChangelogHandler() {
		return changelogHandler;
	}

    public void displayBoxText(String l) {
		jTextArea1.setText(l);
	}
	
	public StartupSimulator getStartsim() {
		return startsim;
	}
}
