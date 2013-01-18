package its.my.time.view.menu;

import java.util.ArrayList;

import android.view.View;

public class MenuGroupe{
	private String nom;
	private ArrayList<MenuObjet> menuObjets;
	private int iconeRes;
	
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
	public MenuGroupe(String nom, int iconeRes) {
		super();
		this.nom = nom;
		this.menuObjets = new ArrayList<MenuObjet>();
		this.iconeRes = iconeRes;
	}
	
}
