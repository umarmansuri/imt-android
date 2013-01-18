package its.my.time.view.menu;

import java.util.ArrayList;

public class MenuGroupe{
	private String nom;
	private ArrayList<MenuObjet> menuObjets;
	private int iconeRes;
	private boolean isSwitcher;
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public ArrayList<MenuObjet> getObjets() {
		return menuObjets;
	}
	public void setObjets(ArrayList<MenuObjet> menuObjets) {
		this.menuObjets = menuObjets;
	}
	public int getIconeRes() {
		return iconeRes;
	}
	public void setIconeRes(int iconeRes) {
		this.iconeRes = iconeRes;
	}
	public boolean isSwitcher() {
		return isSwitcher;
	}
	public void setIsSwitcher(boolean isSwitcher) {
		this.isSwitcher = isSwitcher;
	}
	public MenuGroupe(String nom, int iconeRes) {
		this(nom, iconeRes, false);
	}
	public MenuGroupe(String nom, boolean isSwitcher) {
		this(nom, -1, isSwitcher);
	}
	public MenuGroupe(String nom, int iconeRes, boolean isSwitcher) {
		super();
		this.nom = nom;
		this.iconeRes = iconeRes;
		this.isSwitcher = isSwitcher;
		this.menuObjets = new ArrayList<MenuObjet>();
	}
	
	
}
