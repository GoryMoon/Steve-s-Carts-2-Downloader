package gory_moon.stevescarts2.downloader.core.items;

public class VersionItem {

	private Type type;
	private String mcV;
	private int version;
	private String change;
	
	public VersionItem(int version, String change, Type type, String mcV) {
		this.type = type;
		this.mcV = mcV;

		this.version = version;
		this.change = change;
	}
	
	public VersionItem(VersionItem v) {
		this.type = v.getType();
		this.mcV = v.getMcV();

		this.version = v.getVersion();
		this.change = v.getChange();
	}
	
	public Type getType() {
		return type;
	}
	
	public String getMcV() {
		return mcV;
	}
	
	public int getVersion() {
		return version;
	}

	public String getChange() {
		return change;
	}
	
	@Override
	public String toString() {
		return (type == Type.ALPHA ? "a": "b") + getVersion();
	}

}
