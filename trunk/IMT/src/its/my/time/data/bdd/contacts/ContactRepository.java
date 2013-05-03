package its.my.time.data.bdd.contacts;

import its.my.time.data.bdd.base.BaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class ContactRepository extends BaseRepository<ContactBean> {

	public ContactRepository(Context context) {
		super(context, ContactBean.class);
	}

	@Override
	public String getTableName() {
		return "contact";
	}


	protected void objectAdded(ContactBean object) {
		for (OnObjectChangedListener<ContactBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(ContactBean object) {
		for (OnObjectChangedListener<ContactBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(ContactBean object) {
		for (OnObjectChangedListener<ContactBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<ContactBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<ContactBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<ContactBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<ContactBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}