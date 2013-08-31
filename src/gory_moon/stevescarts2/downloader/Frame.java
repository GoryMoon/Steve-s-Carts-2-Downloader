package gory_moon.stevescarts2.downloader;

import gory_moon.stevescarts2.downloader.core.Download;
import gory_moon.stevescarts2.downloader.core.ExceptionThread;
import gory_moon.stevescarts2.downloader.core.GetUpdates;
import gory_moon.stevescarts2.downloader.core.StartupSimulator;
import gory_moon.stevescarts2.downloader.core.handlers.ChangelogHandler;
import gory_moon.stevescarts2.downloader.core.handlers.ExceptionsHandler;
import gory_moon.stevescarts2.downloader.core.helper.Status;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * @author Gory_Moon
 */
public class Frame extends JFrame implements Observer {
    
    private static final long serialVersionUID = 1L;
    String[] items = {"No Changelog Avalible"};
    Boolean firstrun = true;
    Boolean hasInternet = true;
    double dspeed = 0;
    String[] log = {"No Changelog Avalible"};
    String[] versions = {"---"};
    String[] vexep = {""};

    private static Download selectedDownload;
    private boolean errorHappend = false;
	private int ChangelogChecks = 0;
	private boolean vexeption = false;
	private boolean exeptionset = false;
	private int itemlenght = 0;
	
	private Thread startsimT;
	private Thread excT;
	private StartupSimulator startsim;
	private ExceptionThread exceptionT;
	private static ChangelogHandler changelogHandler;
    private static ExceptionsHandler exceptionsHandler;
    
    public void runMain(){ 
        hasInternet = Main.gethasInternet();
        
    	if(hasInternet){
	        if(firstrun){
	        	exceptionT = new ExceptionThread();
	        	excT = new Thread(exceptionT);
	        	excT.start();
	        	
	        	while(ExceptionThread.isDone == false){try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}}
	        	
	        	exceptionsHandler = exceptionT.getExceptionsHandler();
	        	changelogHandler = exceptionT.getChangelogHandler();
	        	vexep = exceptionsHandler.getVexeptions();
	            try {
	                items = GetUpdates.getWebArray();
	                
	                vexeption = false;
	                exeptionset = false;
	                itemlenght = items.length-1;
	                for(int i = 0; i <vexep.length; i++){
	                	itemlenght++;
	                }
	                for (int i = itemlenght; i >= 0;i--){
	                	int j = i+4;
	                	for(int k = 0; k <= vexep.length-1; k++){
	                		if(j != Integer.parseInt(vexep[k]) && !vexeption){
	                			exeptionset = true;
	                		}else if(!exeptionset || j != Integer.parseInt(vexep[k])){
	                			exeptionset = true;
	                			vexeption = true;
	                		}
	                		if(!vexeption){
	                			exeptionset = false;
	                		}
	                	}
		                exeptionset = false;
	                	if(!vexeption){
	                		jComboBox1.addItem(makeObj("a"+j));
	                	}
	                	vexeption = false;
	                }
                    List<String> list = Arrays.asList(items);
                    Collections.reverse(list);
                    log = (String[]) list.toArray();
                    
                    setVersionArray(GetUpdates.getMCVersion());
                    //log = removeEmpty(log);
                    
                    startsim.isRunning = false;
	                jComboBox1.setSelectedIndex(0);
	                firstrun = false;
	                changeLog();
                    setMcVersion(GetUpdates.getMCVersion());
                } catch (Exception e) {
                	error(e.toString());
				}
	        }
    	}else{
    		noInternet();
    	}
    }
    
    /**
     * Creates new form Frame
     * @param startsim 
     */
    public Frame() {
    	
		/*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
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
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
		initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        startsim = new StartupSimulator();
        startsimT = new Thread(startsim);
        startsimT.start();
    }
    private Object makeObj(final String item)  {
        return new Object() {public String toString() { return item; } };
    }
    
    private void resetStats(){
        jLabel7.setText("0%");
        jLabel8.setText(null);
        jLabel9.setText(null);
        jLabel10.setText(null);
        jLabel11.setText(null);
        jProgressBar1.setValue(0);
    }
    
    public static String[] removeEmpty(String[] array){
        List<String> Nulllist = new ArrayList<String>();

        for(String s : array) {
            if(s != null && s.length() > 0) {
                Nulllist.add(s);
            }
        }
        return Nulllist.toArray(new String[Nulllist.size()]);
    }
    
    public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    
    private void setMcVersion(Map<String,Integer> vers) {
        Iterator<String> k = vers.keySet().iterator();
        String[] keys = new String[vers.size()];
        int u = 0;
        
        while(k.hasNext()){
            keys[u] = (String) k.next(); 
            u++;
        }
        String mcV = "";
        
        for(int i=0;i<=versions.length-1;i++){
            String[] temp = versions[i].split(",");
            for(int j=0;j<=temp.length-1;j++){
                if(Integer.parseInt(temp[j])==Main.getVersion()){
                    mcV = keys[i];
                    break;
                }
            }
        }
        if(mcV.contains("andabove")) {
        	mcV = mcV.replace("andabove", "+");
        }
        jLabel15.setText(mcV);
    }
    
    private void setVersionArray(Map<String,Integer> vers){
        Iterator<String> k = vers.keySet().iterator();
        String[] keys = new String[vers.size()];
        versions = new String[vers.size()];
        int u = 0;
        while(k.hasNext()){
            keys[u] = (String) k.next(); 
            u++;
        }
        int oldVersions = 1;
        for(int f=0;f<=keys.length-1;f++){
            String tempS = "";
            for(int i=1;i<=vers.get(keys[f]);i++){
                if(i!=vers.get(keys[f])){
                    tempS = tempS+((items.length+4)-oldVersions)+",";
                }else{
                    tempS = tempS+((items.length+4)-oldVersions);
                }  
                oldVersions++;
            }
            versions[f]=tempS;
        }
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
            jComboBox1.addItem(makeObj("No Versions Avalible"));
            jComboBox1.setSelectedIndex(0);
            jComboBox1.setEnabled(false);
            jTextArea1.setAutoscrolls(false);
            jTextArea1.setText("No Changelog Avalible");
            jButton1.setEnabled(false);
            try {
                FileWriter errorFile = new FileWriter("Steve's Carts 2 Downloader Crash Log "+getDateTime()+".txt");
                BufferedWriter out = new BufferedWriter(errorFile);
                out.write(e + "\n\n");
                if ( exception != null){
                	out.write(exception.toString() + "\n");
                	for(StackTraceElement ex : exception.getStackTrace()){
                		out.write("\n" + ex.toString());
                	}
                	
                }
                out.close();
                resetStats();
                Main.sendMessage("An unexpected error oucurred! Send the \"Steve's Carts 2 Downloader Crash Log "+getDateTime()+".txt\" to \"Gory_Moon\" at www.stevescarts2.wikispaces.com", "Error");
            } catch (Exception ex) {
                Main.sendMessage("I don't know how you did it but report this to \"Gory_Moon\" at www.stevescarts2.wikispaces.com\nReport code: 6243", "Error in Error");
            }
        }   
    }
    void noInternet(){
        jComboBox1.addItem(makeObj("No Versions Avalible"));
        jComboBox1.setSelectedIndex(0);
        jComboBox1.setEnabled(false);
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setText(items[0]);
        jButton1.setEnabled(false);
    }
    
    private void changeLog(){
        String id = jComboBox1.getSelectedItem().toString();
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setText(Main.getChangelogHandler().changlogHandler(log, id));
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
        jButton2 = new JButton();
        jComboBox1 = new JComboBox<Object>();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jProgressBar1 = new JProgressBar();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        jLabel11 = new JLabel();
        jLabel14 = new JLabel();
        jLabel15 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Steve's Carts 2 Downloader "+Main.getDownloaderVersion());
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
                    url = new URL("http://dl.dropbox.com/u/46486053/StevesCarts2.0.0."+version+".zip");
            } catch (Exception e) {
                error(e.toString());
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
                setMcVersion(GetUpdates.getMCVersion());
                ChangelogChecks = 1;
        	}else{
        		ChangelogChecks = 0;
        	}
        }
    }

    /**
     * @param args the command line arguments
     */
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JButton jButton2;
    private JComboBox<Object> jComboBox1;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JProgressBar jProgressBar1;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    
    
	@Override
	public void update(Observable arg0, Object arg1) {
		if(!firstrun){
			System.out.println("Updating before intilized");
		}
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
	public static ExceptionsHandler getExceptionsHandler() {
		return exceptionsHandler;
	}

	public void displayBoxText(String l) {
		jTextArea1.setText(l);
	}
}
