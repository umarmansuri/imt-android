package its.my.time.view.menu;

import android.view.View;

public class MenuObjet{

	private MenuGroupe menuGroupe;
	private String nom;
	private int iconeRes;
	
	public MenuGroupe getMenuGroupe() {
		return menuGroupe;
	}
	public void setMenuGroupe(MenuGroupe menuGroupe) {
		this.menuGroupe = menuGroupe;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getIconeRes() {
		return iconeRes;
	}
	public void setIconeRes(int iconeRes) {
		this.iconeRes = iconeRes;
	}
	public MenuObjet(MenuGroupe menuGroupe, String nom, int iconeRes) {
		super();
		this.menuGroupe = menuGroupe;
		this.nom = nom;
		this.iconeRes = iconeRes;
	}
}