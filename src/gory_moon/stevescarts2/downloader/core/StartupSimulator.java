package gory_moon.stevescarts2.downloader.core;

import gory_moon.stevescarts2.downloader.Main;

public class StartupSimulator implements Runnable{

	public boolean isRunning = true;
	private int updates = 0;
	private String loading = "Loading! Please wait";
	
	@Override
	public void run() {
		do{
			if(updates == 3)
				updates = 0;
			else
				updates++;
			String dots = "";
			for(int i = 0; i < updates; i++)
				dots += ".";
			String l = loading + dots;
			Main.frame.displayBoxText(l);
			try{
				Thread.sleep(100);
			} catch (Exception ignored){}
		}while (isRunning);
	}

	public void setText(String string) {
		loading = string;
		
	}

	public void addText(String string) {
		loading += string;
	}

}
