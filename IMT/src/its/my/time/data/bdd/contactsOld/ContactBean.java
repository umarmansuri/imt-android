package its.my.time.data.bdd.contactsOld;

import its.my.time.data.bdd.contactsOld.ContactInfo.ContactInfoBean;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class ContactBean {

	private int rawContactId;
	private String nom;
	private String prenom;
	private Bitmap image;
	private List<ContactInfoBean> infos = new ArrayList<ContactInfoBean>();
	
	
	public int getRawContactId() {
		return rawContactId;
	}
	public void setRawContactId(int rawContactId) {
		this.rawContactId = rawContactId;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public List<ContactInfoBean> getInfos() {
		return infos;
	}
	public void setInfos(List<ContactInfoBean> infos) {
		this.infos = infos;
	}

}
