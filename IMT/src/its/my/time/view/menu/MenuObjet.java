package its.my.time.view.menu;

import android.graphics.drawable.Drawable;

public class MenuObjet {

	private MenuGroupe menuGroupe;
	private String nom;
	private int iconeRes = -1;
	private boolean isSwitcher;
	private Drawable switcherOnColor = null;
	private boolean firstState;

	public MenuGroupe getMenuGroupe() {
		return this.menuGroupe;
	}

	public void setMenuGroupe(MenuGroupe menuGroupe) {
		this.menuGroupe = menuGroupe;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
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

	public Drawable getSwitcherOnColor() {
		return this.switcherOnColor;
	}

	public MenuObjet(MenuGroupe menuGroupe, String nom, int iconeRes) {
		this(menuGroupe, nom, iconeRes, false);
	}

	public MenuObjet(MenuGroupe menuGroupe, String nom, int iconeRes,
			boolean isSwitcher) {
		this(menuGroupe, nom, iconeRes, isSwitcher, false, null);
	}

	public boolean getFirstState() {
		return this.firstState;
	}

	public void setFirstState(boolean firstState) {
		this.firstState = firstState;
	}

	public MenuObjet(MenuGroupe menuGroupe, String nom, int iconeRes,
			boolean isSwitcher, boolean firstState, Drawable drawable) {
		super();
		this.menuGroupe = menuGroupe;
		this.nom = nom;
		this.iconeRes = iconeRes;
		this.isSwitcher = isSwitcher;
		this.switcherOnColor = drawable;
		this.firstState = firstState;
	}

}