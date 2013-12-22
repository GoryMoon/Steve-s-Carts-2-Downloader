package gory_moon.stevescarts2.downloader.core.handlers;


import gory_moon.stevescarts2.downloader.core.ExceptionThread;
import gory_moon.stevescarts2.downloader.core.items.VersionItem;

public class ChangelogHandler {

    private String[] exceptions = {""};

    public ChangelogHandler(ExceptionsHandler exHand){
		exceptions = exHand.getExeptions();
        ExceptionThread.isDone = true;
	}
	
	public String changlogHandler(VersionItem item){
        String print = "";
        String text = item.getChange();
        
        for(int j = 0;j <= exceptions.length-1;j++){
            String[] temp = exceptions[j].split("½");
            text = text.replace(temp[0], temp[1]);
        }

        String[] printArray = text.split("¤");
        for (int i = 0; i <= printArray.length-1;i++){
            String quote = "§";
            String apostrophe = "£";
            String comma = "$";
            String dot = ";";
            if(printArray[i].contains(dot)||printArray[i].contains(comma)||printArray[i].contains(apostrophe)||printArray[i].contains(quote))
            {
                printArray[i] = printArray[i].replace(dot, ".");
                printArray[i] = printArray[i].replace(comma, ",");
                printArray[i] = printArray[i].replace(apostrophe, "\'");
                printArray[i] = printArray[i].replace(quote, "\"");
            }
            print += printArray[i]+"\n";
        }
        return print;
    }

}
