package gory_moon.stevescarts2.downloader.core.helper;

public enum Status {
	DOWNLOADING,
	PAUSED,
	COMPLETE,
	CANCELLED,
	ERROR;
	
	
	@Override
	public String toString() {
		return super.toString().toLowerCase().substring(0, 1).toUpperCase();
	}
	
}
