package gory_moon.stevescarts2.downloader.core.handlers;

import gory_moon.stevescarts2.downloader.core.items.VersionItem;

public class ChangelogHandler {
	
	public static String changlogHandler(VersionItem item){
        String print = "";
        String text = item.getChange();

        text = text.replaceAll("\\\\\"", "\"");
        text = text.replaceAll("\\\\'", "\'");
        String[] printArray = text.split("¤");
        for (int i = 0; i <= printArray.length-1;i++){
            print += printArray[i]+"\n";
        }
        return print;
    }

}
