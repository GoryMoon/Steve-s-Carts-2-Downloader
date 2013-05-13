package gory_moon.stevescarts2.downloader.core;

import gory_moon.stevescarts2.downloader.Frame;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gory_Moon
 */
public class GetUpdates {
	
    private static Map<String, Integer> vPlace = new LinkedHashMap<String, Integer>();
    private static Map<String, Integer> mcVersions = new LinkedHashMap<String, Integer>();
    
    public static Map<String, Integer> getMCVersion (){
        return mcVersions;
    }
    
    public static String[] getWebArray() throws MalformedURLException, IOException{
        String[] temp;
        URL url = new URL("http://gorymoon.dx.am/stevescarts2/versionFile.txt");
        URLConnection con = url.openConnection();	
        Pattern p = Pattern.compile("text/text");
        Matcher m = p.matcher(con.getContentType());
        String charset = m.matches() ? m.group(1) : "ISO-8859-1";
        Reader r = new InputStreamReader(con.getInputStream(), charset);
        StringBuilder buf = new StringBuilder();
        while (true) {
            int ch = r.read();
            if (ch < 0)
                break;
            buf.append((char) ch);
        }
        String str = buf.toString();
        temp = str.split("#LINE-END#");
        for(int i = 0;i <= temp.length-1;i++){
            temp[i] = temp[i].replaceFirst(",", "").replace("\n", "");
        }
        for(int i = 0; i<=temp.length-1;i++){
            if(temp[i].contains("%MC%")){
                String key = temp[i].replace(" ", "").replace("\n", "").substring(4);
                int b =(i==0)?0:i;
                vPlace.put(key, Integer.valueOf(b));
                temp[i] = "";
            }
        }
        
        temp = Frame.removeEmpty(temp);
        
        Iterator<String> k = vPlace.keySet().iterator();
        String[] keys = new String[vPlace.size()];
        int u = 0;
        while(k.hasNext()){
            keys[u] = (String) k.next(); 
            u++;
        }
        for(int f=0;f<=keys.length-1;f++){
            int w = f+1;
            if (w<=keys.length-1) {
                mcVersions.put(keys[f], Integer.valueOf(vPlace.get(keys[w])-((vPlace.get(keys[f]))+1)));
            }else{
                mcVersions.put(keys[f], Integer.valueOf(20));
            }
            
        }

        return temp;
    }

}
