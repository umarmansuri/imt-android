package its.my.time.data.bdd.contacts.ContactInfo;

import its.my.time.data.bdd.base.BaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class ContactInfoRepository extends BaseRepository<ContactInfoBean>{

	
	public ContactInfoRepository(Context context) {
		super(context, ContactInfoBean.class);
	}

	@Override
	public String getTableName() {
		return "contactInfo";
	}


	protected void objectAdded(ContactInfoBean object) {
		for (OnObjectChangedListener<ContactInfoBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(ContactInfoBean object) {
		for (OnObjectChangedListener<ContactInfoBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(ContactInfoBean object) {
		for (OnObjectChangedListener<ContactInfoBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<ContactInfoBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<ContactInfoBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<ContactInfoBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<ContactInfoBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}