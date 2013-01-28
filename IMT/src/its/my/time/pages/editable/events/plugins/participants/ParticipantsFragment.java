package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.contactsOld.ContactBean;
import its.my.time.data.bdd.contactsOld.ContactInfo.ContactInfoBean;
import its.my.time.data.bdd.events.plugins.participant.ParticipantBean;
import its.my.time.data.bdd.events.plugins.participant.ParticipantRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.ContactsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fonts.mooncake.MooncakeIcone;

public class ParticipantsFragment extends BasePluginFragment {

	private static final int PICK_CONTACT = 0;
	private ListView mListParticipant;
	private MooncakeIcone mBtnAdd;
	private AutoCompleteTextView mEditSearch;
	private ContactAdapter adapterContact;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_participant, null);
		this.mListParticipant = (ListView) mView.findViewById(R.id.event_participants_liste);
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), false));

		mBtnAdd = (MooncakeIcone)mView.findViewById(R.id.event_participants_ajout_btn);
		mBtnAdd.setIconeRes(MooncakeIcone.icon_users);

		
		
		
		mEditSearch = (AutoCompleteTextView)mView.findViewById(R.id.event_participants_ajout_text);
		mEditSearch.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ParticipantBean participant = new ParticipantBean();
				ContactInfoBean contatcInfo = adapterContact.getContactInfoAt(position);
				participant.setIdContactInfo(contatcInfo.getId());
				participant.setEid(getParentActivity().getEvent().getId());
				new ParticipantRepository(getActivity()).insertParticipant(participant);
				mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), false));
			}
		});
		
		return mView;
	}

	@Override
	public void onResume() {
		adapterContact = new ContactAdapter(getActivity());
		mEditSearch.setAdapter(adapterContact);
		super.onResume();
	}
	
	@Override
	public String getTitle() {
		return "Participants";
	}

	@Override
	public void launchEdit() {
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),
				getParentActivity().getEvent().getId(), true));
	}

	@Override
	public void launchSave() {
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),
				getParentActivity().getEvent().getId(), true));
	}

	@Override
	public void launchCancel() {
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),
				getParentActivity().getEvent().getId(), true));
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public boolean isSavable() {
		return true;
	}

	private class ParticipantsAdapter implements ListAdapter {

		private HashMap<Long, ContactBean> contacts;
		private List<ContactInfoBean> contactsInfo;
		private final boolean isInEditMode;
		private List<ParticipantBean> participants;

		public ParticipantsAdapter(Context context, int id, boolean isInEditMode) {
			this.isInEditMode = isInEditMode;
			
			contacts = new HashMap<Long, ContactBean>();
			contactsInfo = new ArrayList<ContactInfoBean>();
			
			participants = new ParticipantRepository(getActivity()).getAllByEid(getParentActivity().getEvent().getId());
			if (participants != null) {
				for (ParticipantBean participant : participants) {
					ContactInfoBean contactInfo = ContactsUtil.getContactInfoById(getActivity(), participant.getIdContactInfo());
					contactsInfo.add(contactInfo);
					if(!contacts.containsKey(contactInfo.getContactId())) {
						contacts.put(contactInfo.getContactId(), ContactsUtil.getContatById(getActivity(), (int) contactInfo.getContactId()));
					}
				}
			}
		}

		@Override
		public int getCount() {
			return contactsInfo.size();
		}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			ContactInfoBean contactInfo = contactsInfo.get(position);
			ContactBean contact = contacts.get((int) contactInfo.getContactId());
			final ParticipantsView view = new ParticipantsView(getActivity(),contact, contactInfo, this.isInEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new ParticipantRepository(getActivity()).deleteParticipant(participants.get(position).getEid(),participants.get(position).getIdContactInfo());
					ParticipantsFragment.this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), true));
				}
			});
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isEmpty() {
			if (this.participants == null | this.participants.size() == 0) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}
	}
}
