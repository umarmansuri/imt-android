package its.my.time.data.bdd.utilisateur;

import its.my.time.data.bdd.base.BaseBean;
import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.util.DateUtil;

import java.util.Calendar;
import java.util.List;

public class UtilisateurBean extends BaseBean{

	private TableAttribut<String> nom;
	private TableAttribut<String> prenom;
	private TableAttribut<String> pseudo;
	private TableAttribut<Calendar> dateAniv;
	private TableAttribut<String> mdp;
	private TableAttribut<String> tel;
	private TableAttribut<String> mail;
	private TableAttribut<String> adresse;
	private TableAttribut<String> codePostal;
	private TableAttribut<String> ville;
	private TableAttribut<String> pays;
	
	public UtilisateurBean() {
		super();
		nom = new TableAttribut<String>("nom", "");
		prenom = new TableAttribut<String>("prenom", "");
		pseudo = new TableAttribut<String>("pseudo", "");
		dateAniv = new TableAttribut<Calendar>("dateAniv", DateUtil.createCalendar());
		mdp = new TableAttribut<String>("mdp", "");
		tel = new TableAttribut<String>("tel", "");
		mail = new TableAttribut<String>("mail", "");
		adresse = new TableAttribut<String>("adresse", "");
		codePostal = new TableAttribut<String>("codePostal", "");
		ville = new TableAttribut<String>("ville", "");
		pays= new TableAttribut<String>("pays", "");
	}
	
	public String getNom() {
		return nom.getValue();
	}
	public void setNom(String nom) {
		this.nom.setValue(nom);
	}
	public String getPrenom() {
		return prenom.getValue();
	}
	public void setPrenom(String prenom) {
		this.prenom.setValue(prenom);
	}
	public String getPseudo() {
		return pseudo.getValue();
	}
	public void setPseudo(String pseudo) {
		this.pseudo.setValue(pseudo);
	}
	public Calendar getDateAniv() {
		return dateAniv.getValue();
	}
	public void setDateAniv(Calendar dateAniv) {
		this.dateAniv.setValue(dateAniv);
	}
	public String getMdp() {
		return mdp.getValue();
	}
	public void setMdp(String mdp) {
		this.mdp.setValue(mdp);
	}
	public String getTel() {
		return tel.getValue();
	}
	public void setTel(String tel) {
		this.tel.setValue(tel);
	}
	public String getMail() {
		return mail.getValue();
	}
	public void setMail(String mail) {
		this.mail.setValue(mail);
	}
	public String getAdresse() {
		return adresse.getValue();
	}
	public void setAdresse(String adresse) {
		this.adresse.setValue(adresse);
	}
	public String getCodePostal() {
		return codePostal.getValue();
	}
	public void setCodePostal(String codePostal) {
		this.codePostal.setValue(codePostal);
	}
	public String getVille() {
		return ville.getValue();
	}
	public void setVille(String ville) {
		this.ville.setValue(ville);
	}
	public String getPays() {
		return pays.getValue();
	}
	public void setPays(String pays) {
		this.pays.setValue(pays);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(nom);
		list.add(prenom);
		list.add(pseudo);
		list.add(dateAniv);
		list.add(mdp);
		list.add(tel);
		list.add(mail);
		list.add(adresse);
		list.add(codePostal);
		list.add(ville);
		list.add(pays);
		return list;
	}
	
}
