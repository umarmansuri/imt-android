package its.my.time.view.menu;

import java.util.ArrayList;

public class MenuGroupe{
	private String nom;
	private ArrayList<MenuObjet> menuObjets;
	private int iconeId;
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
	public int getIconeId() {
		return iconeId;
	}
	public void setIconeId(int iconeId) {
		this.iconeId = iconeId;
	}
	public MenuGroupe(String nom, int iconeId) {
		super();
		this.nom = nom;
		this.menuObjets = new ArrayList<MenuObjet>();
		this.iconeId = iconeId;
	}

}
