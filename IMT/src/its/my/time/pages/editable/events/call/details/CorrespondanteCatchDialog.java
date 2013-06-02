package its.my.time.pages.editable.events.call.details;

import its.my.time.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CorrespondanteCatchDialog extends AlertDialog implements OnItemClickListener{

	public static final int TYPE_PHONE = 0;
	public static final int TYPE_REPERTOIRE = 1;
	public static final int TYPE_MYTIME = 2;

	private OnCorrespondantChange listener;
	private int type;
	private EditText mEditText;
	private ListView mList;
	private ProgressBar mProgress;

	public CorrespondanteCatchDialog(Context context, int type, OnCorrespondantChange listener) {
		super(context);
		this.listener = listener;
		this.type = type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		setContentView(R.layout.dialog_correspondant_catch);

		mEditText = (EditText)findViewById(R.id.correspondant_catch_edit);
		mList = (ListView)findViewById(R.id.correspondant_catch_list);
		mList.setVisibility(View.GONE);
		mProgress = (ProgressBar)findViewById(R.id.correspondant_catch_progress);
		mProgress.setVisibility(View.GONE);
		if(type == TYPE_PHONE) {
			mEditText.setInputType(InputType.TYPE_CLASS_PHONE);
			mList.setVisibility(View.GONE);
			mProgress.setVisibility(View.GONE);
			findViewById(R.id.correspondant_catch_valider).setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					String value = mEditText.getText().toString();
					returnValues(value, value, "");
				}
			});
			setOnShowListener(new OnShowListener() {
				@Override
				public void onShow(DialogInterface arg0) {
					InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
				}
			});
		} else {
			mList.setOnItemClickListener(this);
			findViewById(R.id.correspondant_catch_valider).setVisibility(View.GONE);
			mEditText.addTextChangedListener(new TextWatcher() {

				private Calendar lastChange;
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					mProgress.setVisibility(View.VISIBLE);
					mList.setVisibility(View.GONE);
					lastChange = Calendar.getInstance();
					new Handler().postDelayed(
							new Runnable() {
								@Override
								public void run() {
									Calendar cal = Calendar.getInstance();
									if(cal.getTimeInMillis() >= lastChange.getTimeInMillis() + 1950) {
										if(type == TYPE_MYTIME) {

										} else if(type == TYPE_REPERTOIRE) {
											new LoadRepertoire(mEditText.getText().toString()).execute();
										}
									}
								}
							},
							2000);
				}

				@Override public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
				@Override public void afterTextChanged(Editable s) {}
			});
		}

	}


	public void returnValues(String user, String phone, String identifiant) {
		if(listener != null) {
			listener.onCorrespondantChange(user, phone, identifiant);
		}	
		dismiss();
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Contact contact = ((ContactAdapter)adapterView.getAdapter()).getItem(position);
		String user = contact.user;
		String phone = contact.phone;
		String identifiant = contact.identifiant;
		returnValues(user, phone, identifiant);
	}
	
	
	private class LoadRepertoire extends AsyncTask<Void, Void, List<Contact>> {
		
		private String search;

		public LoadRepertoire(String search) {
			this.search = search;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress.setVisibility(View.VISIBLE);
			mList.setVisibility(View.GONE);
		}
		
		@Override
		protected List<Contact> doInBackground(Void... params) {
			ContentResolver cr = getContext().getContentResolver();
			Cursor cur = cr.query(
				     ContactsContract.Contacts.CONTENT_URI, 
				     null,
				     ContactsContract.Contacts.DISPLAY_NAME + " like ?", 
				     new String[]{ "%" + search + "%"}, 
				     null);
			List<Contact> contacts = new ArrayList<Contact>(); 
			while(cur.moveToNext()) {
				String user = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String phone = "";
				String identifiant = "";
				contacts.add(new Contact(user, phone, identifiant));
			}
			return contacts;
		}
		
		@Override
		protected void onPostExecute(List<Contact> result) {
			super.onPostExecute(result);
			mList.setAdapter(new ContactAdapter(result));
			mProgress.setVisibility(View.GONE);
			mList.setVisibility(View.VISIBLE);
		}
	}
	
	private class ContactAdapter extends ArrayAdapter<Contact> {

		public ContactAdapter(List<Contact> objects) {
			super(CorrespondanteCatchDialog.this.getContext(), android.R.layout.simple_expandable_list_item_1, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView v = (TextView) super.getView(position, convertView, parent);
			v.setTextColor(getContext().getResources().getColor(R.color.grey));
			return v;
		}
		
	}
	
	private class Contact {
		String user;
		String phone;
		String identifiant;
		
		public Contact(String user, String phone, String identifiant) {
			super();
			this.user = user;
			this.phone = phone;
			this.identifiant = identifiant;
		}
		
		@Override
		public String toString() {
			if(user != null && !user.equals("")) {return user;}
			if(phone != null && !phone.equals("")) {return phone;}
			if(identifiant != null && !identifiant.equals("")) {return identifiant;}
			return "";
		}
	}
}
