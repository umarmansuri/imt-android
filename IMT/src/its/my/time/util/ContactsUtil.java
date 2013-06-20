package its.my.time.util;

import its.my.time.R;
import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.bdd.contacts.ContactInfo.ContactInfoBean;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import android.util.Log;

public class ContactsUtil {


	private static final String TAG = "ContactsUtil";
	private static ContentResolver mContentResolver = null;

	public static ContentProviderResult[] addContact(Context context, Account account, ContactBean contact) {
		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

		//Create our RawContact
		ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
		builder.withValue(RawContacts.ACCOUNT_NAME, account.name);
		builder.withValue(RawContacts.ACCOUNT_TYPE, account.type);
		builder.withValue(RawContacts.SYNC1, contact.getNom() + " " + contact.getPrenom());
		operationList.add(builder.build());

		//Create a Data record of common type 'StructuredName' for our RawContact
		builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
		builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
		builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getNom() + " " + contact.getPrenom());
		builder.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.getPrenom());
		builder.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, contact.getNom());
		operationList.add(builder.build());

		//Create a Data record of custom type "vnd.android.cursor.item/vnd.fm.last.android.profile" to display a link to the Last.fm profile
		builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
		builder.withValue(ContactsContract.Data.MIMETYPE, context.getString(R.string.contacts_mime_type));
		builder.withValue(ContactsContract.Data.DATA1, contact.getNom() + " " + contact.getPrenom());
		builder.withValue(ContactsContract.Data.DATA2, context.getString(R.string.contacts_profil));
		builder.withValue(ContactsContract.Data.DATA3, context.getString(R.string.contacts_view_profil));
		operationList.add(builder.build());

		/*TODO voir pour remettre avec la nouvelle BDD
		 for (ContactInfoBean info : contact.getInfos()) {
		 
			builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
			builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
			if(info.getType() == ContactInfoBean.TYPE_MAIL) {
				builder.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
				builder.withValue(Email.DATA1, info.getValue());
			} else if(info.getType() == ContactInfoBean.TYPE_PHONE) {
				builder.withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				builder.withValue(Phone.NUMBER, info.getValue());
			}
			operationList.add(builder.build());
		}
		*/

		try {
			mContentResolver = context.getContentResolver();
			return mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
		} catch (Exception e) {
			Log.e(TAG, "Something went wrong during creation! " + e);
			e.printStackTrace();
			return null;
		}
	}

	public static void addMyTimeContact(Context context,ContactBean contactBean) {
		Account account = AccountManager.get(context).getAccountsByType(context.getString(R.string.ACCOUNT_TYPE))[0];
		addContact(context, account, contactBean);
	}

	public static void updateContactStatus(Context context, ArrayList<ContentProviderOperation> operationList, long rawContactId, boolean isOccuped) {
		Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, rawContactId);
		Uri entityUri = Uri.withAppendedPath(rawContactUri, Entity.CONTENT_DIRECTORY);
		mContentResolver = context.getContentResolver();
		Cursor c = mContentResolver.query(entityUri, new String[] { RawContacts.SOURCE_ID, Entity.DATA_ID, Entity.MIMETYPE, Entity.DATA1 }, null, null, null);
		try {
			while (c.moveToNext()) {
				if (!c.isNull(1)) {
					String mimeType = c.getString(2);
					String status = "";
					if (isOccuped)
						status = "En rendez-vous";
					else
						status = "Disponible";

					if (mimeType.equals(context.getString(R.string.contacts_mime_type))) {
						ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.StatusUpdates.CONTENT_URI);
						builder.withValue(ContactsContract.StatusUpdates.DATA_ID, c.getLong(1));
						builder.withValue(ContactsContract.StatusUpdates.STATUS, status);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_RES_PACKAGE, "its.my.time");
						builder.withValue(ContactsContract.StatusUpdates.STATUS_LABEL, R.string.app_name);
						builder.withValue(ContactsContract.StatusUpdates.STATUS_ICON, R.drawable.ic_launcher);
						operationList.add(builder.build());

						builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
						builder.withSelection(BaseColumns._ID + " = '" + c.getLong(1) + "'", null);
						builder.withValue(ContactsContract.Data.DATA3, status);
						operationList.add(builder.build());
					}
				}
			}
		} finally {
			c.close();
		}
	}


	public static ArrayList<ContactBean> getAll(Context context) {
		ArrayList<ContactBean> result = new ArrayList<ContactBean>();
		Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null); 
		while (cursor.moveToNext()) { 
			long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			Bitmap image = loadContactPhoto(context, contactId);
			Cursor emails = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
			if(emails.getCount() > 0) {
				ContactBean contact = new ContactBean();
				contact.setNom(contactName);
				//TODO contact.setImage(image);
				contact.setRawContactId((int) contactId);
				List<ContactInfoBean> infos = new ArrayList<ContactInfoBean>();
				while (emails.moveToNext()) {
					long emailId = emails.getLong(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email._ID));
					String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					ContactInfoBean bean = new ContactInfoBean();
					bean.setContactId(contactId);
					bean.setType(ContactInfoBean.TYPE_MAIL);
					bean.setValue(emailAddress);
					bean.setId((int) emailId);
					infos.add(bean);
				}
				//TODO contact.setInfos(infos);
				result.add(contact);
			}
			emails.close();
		}
		cursor.close();
		return result;
	}

	public static ContactInfoBean getContactInfoById(Context context, long id) {
		Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email._ID + " = " + id, null, null);
		if(cursor.getCount() > 0) {
			cursor.moveToNext();


			ContactInfoBean contactInfo = new ContactInfoBean();
			contactInfo.setContactId(cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)));
			contactInfo.setValue(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
			return contactInfo;
		} else {
			return null;
		}
	}

	public static ContactBean getContatById(Context context, int id) {
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query( 
				ContactsContract.Contacts.CONTENT_URI, 
				null,
				ContactsContract.Contacts._ID + " = ?", 
				new String[]{String.valueOf(id)}, null); 
		if(cursor.getCount() > 0) {
			cursor.moveToNext();
			ContactBean contact = new ContactBean();
			contact.setNom(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
			//TODO contact.setImage(loadContactPhoto(context, id));
			return contact;
		} else {
			return null;
		}
	}

	public static Bitmap loadContactPhoto(Context context, long id) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, id);
		Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] {Contacts.Photo.PHOTO}, null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
				}
			}
		} finally {
			cursor.close();
		}
		return null;
	}

}
