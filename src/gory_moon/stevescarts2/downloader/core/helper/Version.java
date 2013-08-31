package gory_moon.stevescarts2.downloader.core.helper;

public class Version {
	
	public int major;
	public int minor;
	public int fix;
	public int dev;
	private String text;
	
	public Version(int major, int minor, int release, int dev) {
		this.major = major;
		this.minor = minor;
		this.fix = release;
		this.dev = dev;
	}
	
	public Version() {
		this.major = 1;
		this.minor = 0;
		this.fix = 0;
		this.dev = 0;
	}
	
	private void calcuateText() {
		if (dev != 0) {
			text = major + "." + minor +"." + fix +".dev" + dev;
		}else if(fix != 0){
			text = major + "." + minor +"." + fix;
		}else{
			text = major + "." + minor;
		}
	}
	
	@Override
	public String toString() {
		calcuateText();
		return text;
	}
	
	public boolean greaterThenEqual(Version v) {
		if(this.major > v.major) {
			return true;
		}else if(this.major == v.major){
			if(this.minor > v.minor) {
				return true;
			}else if(this.minor == v.minor) {
				if(this.fix > v.fix) {
					return true;
				}else if(this.fix == v.fix) {
					if(this.dev >= v.dev) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
}
