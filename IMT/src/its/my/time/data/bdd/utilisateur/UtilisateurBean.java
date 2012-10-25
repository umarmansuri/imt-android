package its.my.time.data.bdd.utilisateur;

import java.util.Calendar;

public class UtilisateurBean {
	
	private String adresse;
	private Calendar dateNaissance;
	private long id = -1;
	private String identifiant;
	private String mdp;
	private String nom;
	private String prenom;
	private String tel;
	private String CDP;
	private String Ville;
	
	public String getAdresse() {
		return adresse;
	}
	public String getCDP() {
		return CDP;
	}
	public void setCDP(String cDP) {
		CDP = cDP;
	}
	public String getVille() {
		return Ville;
	}
	public void setVille(String ville) {
		Ville = ville;
	}
	public Calendar getDateNaissance() {
		return dateNaissance;
	}
	public long getId() {
		return id;
	}
	public String getIdentifiant() {
		return identifiant;
	}
	public String getMdp() {
		return mdp;
	}
	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public String getTel() {
		return tel;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public void setDateNaissance(Calendar dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setIdentifiant(String identifant) {
		this.identifiant = identifant;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
}
