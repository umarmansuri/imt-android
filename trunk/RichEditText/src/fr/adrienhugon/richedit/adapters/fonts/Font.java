package fr.adrienhugon.richedit.adapters.fonts;

public class Font {

	public final static String FOLDER = "fonts/";
	
	private String name;
	private String file;
	public Font(String name, String file) {
		super();
		this.name = name;
		this.file = file;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	
}
