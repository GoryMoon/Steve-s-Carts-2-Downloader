package gory_moon.stevescarts2.downloader.core;

import gory_moon.stevescarts2.downloader.core.handlers.ChangelogHandler;
import gory_moon.stevescarts2.downloader.core.handlers.ExceptionsHandler;

public class ExceptionThread implements Runnable {

	private static ChangelogHandler changelogHandler;
    private static ExceptionsHandler exceptionsHandler;
    public static boolean isDone = false;
	
	public ChangelogHandler getChangelogHandler() {
		return changelogHandler;
	}

	public ExceptionsHandler getExceptionsHandler() {
		return exceptionsHandler;
	}

	@Override
	public void run() {
		exceptionsHandler = new ExceptionsHandler();
    	changelogHandler = new ChangelogHandler(exceptionsHandler);
	}
	
	

}
