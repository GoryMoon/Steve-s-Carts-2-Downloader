package gory_moon.stevescarts2.downloader.core.items;

public enum Type {
	ALPHA("A"),
	BETA("B");
	
	private String s;
	private Type(String s) {
		this.s = s;
	}
	
	public boolean equals(String s) {
		return this.s.equals(s);
	}
	
	public static Type getType(String s) {
		for(Type type: values()) {
			if(type.equals(s)) {
				return type;
			}
		}
		return ALPHA;
	}
}
