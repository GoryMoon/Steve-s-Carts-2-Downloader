package gory_moon.stevescarts2.downloader.core.handlers;

import gory_moon.stevescarts2.downloader.core.ExceptionThread;
import gory_moon.stevescarts2.downloader.core.helper.DebugHelper;

public class ChangelogHandler {
	
	private int version = 0;
	
	private String Dot = ";";
    private String Comma = "$";
    private String Apostrophe = "£";
    private String Quote = "§";
	
    private String[] exceptions = {""};
	private String[] vexceptions = {""};
	
	public ChangelogHandler(ExceptionsHandler exHand){
		exceptions = exHand.getExeptions();
		vexceptions = exHand.getVexeptions();
		ExceptionThread.isDone = true;
	}
	
	public String changlogHandler(String[] changelog, String id){
        String print = "ChangeLog:\n";
        String logId = id.replace("a", "");
        version = Integer.parseInt(logId);
        int versionjump = 0;
        
        for(int i = 0; i<=vexceptions.length-1;i++){
        	if(version >= Integer.parseInt(vexceptions[i])){
        		versionjump++;
        	}
        }
        DebugHelper.print(DebugHelper.DEBUG, "Jumped with "+versionjump+" from: "+version+", to: "+(version-versionjump));
        version = version-versionjump-4;

        for(int j = 0;j <= exceptions.length-1;j++){
            String[] temp = exceptions[j].split("½");
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
	
	public int getVersion() {
		return version;
	}
}
