package its.my.time.data.bdd.contacts;

import its.my.time.data.bdd.base.BaseBean;
import its.my.time.data.bdd.base.TableAttribut;

import java.util.List;

public class ContactBean extends BaseBean{

	private TableAttribut<Integer> rawContactId;
	private TableAttribut<String> nom;
	private TableAttribut<String> prenom;

	public ContactBean() {
		super();
		rawContactId = new TableAttribut<Integer>("rawContactId", 0);
		nom = new TableAttribut<String>("nom", "");
		prenom = new TableAttribut<String>("prenmo", "");
	}

	public int getRawContactId() {
		return rawContactId.getValue();
	}
	public void setRawContactId(int rawContactId) {
		this.rawContactId.setValue(rawContactId);
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

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(rawContactId);
		list.add(nom);
		list.add(prenom);
		return list;
	}
}
