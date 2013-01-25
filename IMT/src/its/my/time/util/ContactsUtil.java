package its.my.time.util;

import its.my.time.R;
import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.bdd.contacts.ContactInfo.ContactInfoBean;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
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
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Entity;
import android.util.Log;

public class ContactsUtil {


	private static final String TAG = "ContactsUtil";
	private static ContentResolver mContentResolver = null;

	public static ContentProviderResult[] addContact(Context context, Account account, String name) {
		Log.i(TAG, "Adding contact: " + name);
		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

		//Create our RawContact
		ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
		builder.withValue(RawContacts.ACCOUNT_NAME, account.name);
		builder.withValue(RawContacts.ACCOUNT_TYPE, account.type);
		builder.withValue(RawContacts.SYNC1, name);
		operationList.add(builder.build());

		//Create a Data record of common type 'StructuredName' for our RawContact
		builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
		builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
		builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
		operationList.add(builder.build());

		//Create a Data record of custom type "vnd.android.cursor.item/vnd.fm.last.android.profile" to display a link to the Last.fm profile
		builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
		builder.withValue(ContactsContract.Data.MIMETYPE, context.getString(R.string.contacts_mime_type));
		builder.withValue(ContactsContract.Data.DATA1, name);
		builder.withValue(ContactsContract.Data.DATA2, context.getString(R.string.contacts_profil));
		builder.withValue(ContactsContract.Data.DATA3, context.getString(R.string.contacts_view_profil));
		operationList.add(builder.build());

		try {
			mContentResolver = context.getContentResolver();
			return mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
		} catch (Exception e) {
			Log.e(TAG, "Something went wrong during creation! " + e);
			e.printStackTrace();
			return null;
		}
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


	public static void testUpdate(Context context, Account account) {
		ArrayList<Long> localContactsId = new ArrayList<Long>();
		mContentResolver = context.getContentResolver();
		Log.i(TAG, "performSync: " + account.toString());

		// Load the local Last.fm contacts
		Uri rawContactUri = RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(RawContacts.ACCOUNT_NAME, account.name).appendQueryParameter(
				RawContacts.ACCOUNT_TYPE, account.type).build();
		Cursor c1 = mContentResolver.query(rawContactUri, new String[] { BaseColumns._ID, RawContacts.SYNC1 }, null, null, null);
		while (c1.moveToNext()) {
			localContactsId.add(c1.getLong(0));
		}

		ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
		try {
			for (long id: localContactsId) {
				updateContactStatus(context, operationList, id, true);
			}
			if(operationList.size() > 0)
				mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
		} catch (Exception e1) {
			e1.printStackTrace();
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

			ContactBean contact = new ContactBean();
			contact.setNom(contactName);
			contact.setImage(image);
			List<ContactInfoBean> infos = new ArrayList<ContactInfoBean>();
			while (emails.moveToNext()) {
				String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				ContactInfoBean bean = new ContactInfoBean();
				bean.setContactId(contactId);
				bean.setType(ContactInfoBean.TYPE_MAIL);
				bean.setValue(emailAddress);
				infos.add(bean);
			}
			contact.setInfos(infos);
			result.add(contact);
			emails.close();
		}
		cursor.close();
		return result;
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
