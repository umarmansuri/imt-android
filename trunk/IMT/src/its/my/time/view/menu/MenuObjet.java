package its.my.time.view.menu;

public class MenuObjet{

	private MenuGroupe menuGroupe;
	private String nom;
	private int iconeId;
	
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
	public int getIconeId() {
		return iconeId;
	}
	public void setIconeId(int iconeId) {
		this.iconeId = iconeId;
	}
	public MenuObjet(MenuGroupe menuGroupe, String nom, int iconeId) {
		super();
		this.menuGroupe = menuGroupe;
		this.nom = nom;
		this.iconeId = iconeId;
	}

	

}
