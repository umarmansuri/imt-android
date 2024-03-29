package its.my.time.view.menu;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

public class MenuGroupe {
	private String nom;
	private ArrayList<MenuObjet> menuObjets;
	private int iconeRes;
	private boolean isSwitcher;
	private Drawable switcherOnColor = null;
	private boolean firstState;

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<MenuObjet> getObjets() {
		return this.menuObjets;
	}

	public void setObjets(ArrayList<MenuObjet> menuObjets) {
		this.menuObjets = menuObjets;
	}

	public int getIconeRes() {
		return this.iconeRes;
	}

	public void setIconeRes(int iconeRes) {
		this.iconeRes = iconeRes;
	}

	public boolean isSwitcher() {
		return this.isSwitcher;
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

	public Drawable getSwitcherOnColor() {
		return this.switcherOnColor;
	}

	public void setSwitcherOnColor(Drawable switcherOnColor) {
		this.switcherOnColor = switcherOnColor;
	}

	public void setSwitcher(boolean isSwitcher) {
		this.isSwitcher = isSwitcher;
	}

	public boolean getFirstState() {
		return this.firstState;
	}

	public void setFirstState(boolean firstState) {
		this.firstState = firstState;
	}

	public MenuGroupe(String nom, int iconeRes, boolean isSwitcher) {
		this(nom, iconeRes, isSwitcher, false, null);
	}

	public MenuGroupe(String nom, int iconeRes, boolean isSwitcher,
			boolean firstState, Drawable switcherOnColor) {
		super();
		this.nom = nom;
		this.iconeRes = iconeRes;
		this.isSwitcher = isSwitcher;
		this.menuObjets = new ArrayList<MenuObjet>();
		this.switcherOnColor = switcherOnColor;
		this.firstState = firstState;
	}
	

}
