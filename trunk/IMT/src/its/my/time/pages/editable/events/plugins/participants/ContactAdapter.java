package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.bdd.contacts.ContactInfo.ContactInfoBean;
import its.my.time.util.ContactsUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.fonts.mooncake.MooncakeIcone;

public class ContactAdapter extends BaseAdapter implements Filterable {

	private ArrayList<ContactBean> mOriginalValues;
	private ArrayList<ContactBean> contacts;
	private Context context;

	public ContactAdapter(Context context) {
		mOriginalValues = new ArrayList<ContactBean>();

		this.context = context;
		new LoadContact().execute();
	}

	@Override
	public int getCount() {
		int total = 0;
		for (ContactBean contactBean : contacts) {
			//TODO total+= contactBean.getInfos().size();
		}
		return total;
	}

	public ContactBean getContactAt(int position) {
		int res = 0;
		for (ContactBean contact : contacts) {
			if(res == position) {
				return contact;
			} else {
				res++;
				/*TODO for (int i = 1; i < contact.getInfos().size(); i++) {
					if(res == position) {
						return contact;
					}
					res++;
				}*/
			}
		}
		return null;
	}

	public ContactInfoBean getContactInfoAt(int position) {
		int res = 0;
		for (ContactBean contact : contacts) {
			if(res == position) {
				return null; //TODO contact.getInfos().get(0);
			} else {
				res++;
				/*TODO for (int i = 1; i < contact.getInfos().size(); i++) {
					if(res == position) {
						return contact.getInfos().get(i);
					}
					res++;
				}*/
			}
		}
		return null;
	}

	@Override
	public Object getItem(int position) {
		int res = 0;
		for (ContactBean contact : contacts) {
			if(res == position) {
				return contact;
			} else {
				res++;
				/*TODO for (int i = 1; i < contact.getInfos().size(); i++) {
					if(res == position) {
						return contact.getInfos().get(i);
					}
					res++;
				}*/
			}
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object item = getItem(position);
		if(item != null) {
			if(item instanceof ContactBean) {
				return getContactView(position, convertView, parent,(ContactBean)item);
			} else if(item instanceof ContactInfoBean) {
				return getInfoView(position, convertView, parent,(ContactInfoBean)item);
			}
		}
		return new View(parent.getContext());
	}

	private View getInfoView(int position, View convertView, ViewGroup parent, ContactInfoBean contactInfo) {
		View v = View.inflate(parent.getContext(), R.layout.complex_dropdown_item_single, null);
		((TextView)v.findViewById(R.id.text2)).setText(contactInfo.getValue());
		return v;
	}

	private View getContactView(int position, View convertView, ViewGroup parent, ContactBean contact) {
		View v = View.inflate(parent.getContext(), R.layout.complex_dropdown_item_image, null);
		ViewGroup imageParent = (ViewGroup)v.findViewById(R.id.flag);
		if(false){//TODO contact.getImage()!= null) {
			ImageView imageView = new ImageView(parent.getContext());
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			//TODO imageView.setImageBitmap(contact.getImage());

			imageParent.removeAllViews();
			imageParent.addView(imageView);
		} else {
			MooncakeIcone imageView = ((MooncakeIcone)(imageParent).getChildAt(0));
			imageView.setIconeRes(MooncakeIcone.icon_user);
		}
		((TextView)v.findViewById(R.id.text1)).setText(contact.getNom());
		//TODO ((TextView)v.findViewById(R.id.text2)).setText(contact.getInfos().get(0).getValue());
		return v;
	}



	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,FilterResults results) {
				contacts = (ArrayList<ContactBean>) results.values; // has the filtered values
				notifyDataSetChanged();  // notifies the data with new filtered values
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
				List<ContactBean> FilteredArrList = new ArrayList<ContactBean>();

				if (mOriginalValues == null) {
					mOriginalValues = new ArrayList<ContactBean>(contacts); // saves the original data in mOriginalValues
				}

				if (constraint == null || constraint.length() == 0) {
					results.count = mOriginalValues.size();
					results.values = mOriginalValues;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < mOriginalValues.size(); i++) {
						ContactBean data = mOriginalValues.get(i);
					
						if(compareValue(data, constraint)) {
							FilteredArrList.add(data);
						}

					}
					results.count = FilteredArrList.size();
					results.values = FilteredArrList;
				}
				return results;
			}

			private boolean compareValue(ContactBean data,CharSequence constraint) {
				if (data.getNom().toLowerCase().startsWith(constraint.toString())) {
					return true;
				} else {
					/* TODO for (ContactInfoBean info : data.getInfos()) {
						if (info.getValue().toLowerCase().startsWith(constraint.toString())) {
							return true;
						}
					}*/
				}
				return false;
			}
		};
		return filter;
	}

	public class LoadContact extends AsyncTask<Void, Void, ArrayList<ContactBean>> {

		@Override
		protected ArrayList<ContactBean> doInBackground(Void... params) {
			return ContactsUtil.getAll(context);
		}

		@Override
		protected void onPostExecute(ArrayList<ContactBean> result) {
			mOriginalValues = result;
		}




	}


}
