package its.my.time.view.menu;


public class MenuObjet{

	private MenuGroupe menuGroupe;
	private String nom;
	private int iconeRes = -1;
	private boolean isSwitcher;
	
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
	public boolean isSwitcher() {
		return isSwitcher;
	}
	public void setIsSwitcher(boolean isSwitcher) {
		this.isSwitcher = isSwitcher;
	}
	public MenuObjet(MenuGroupe menuGroupe, String nom, int iconeRes) {
		this(menuGroupe, nom, iconeRes, false);
	}
	public MenuObjet(MenuGroupe menuGroupe, String nom, int iconeRes,boolean isSwitcher) {
		super();
		this.menuGroupe = menuGroupe;
		this.nom = nom;
		this.iconeRes = iconeRes;
		this.isSwitcher = isSwitcher;
	}

}