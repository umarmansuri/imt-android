package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.bdd.contacts.ContactInfo.ContactInfoBean;
import its.my.time.data.bdd.events.plugins.participant.ParticipantBean;
import its.my.time.data.bdd.events.plugins.participant.ParticipantRepository;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.ContactsUtil;
import its.my.time.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.fonts.mooncake.MooncakeIcone;

public class ParticipantsFragment extends BasePluginFragment {

	private ListView mListParticipant;
	private MooncakeIcone mBtnAdd;
	private AutoCompleteTextView mEditSearch;

	private ContactAdapter adapterContact;

	private List<ParticipantBean> participants;
	private ParticipantRepository repo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		repo = new ParticipantRepository(getActivity());
	}

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
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				ParticipantBean participant = new ParticipantBean();
				ContactInfoBean contatcInfo = adapterContact.getContactInfoAt(position);
				participant.setIdContactInfo(contatcInfo.getId());
				participant.setEid(getParentActivity().getEvent().getId());
				repo.insert(participant);
				mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), false));
				mEditSearch.setText("");
				((BaseActivity)getActivity()).launchEdit();
			}
		});
		mEditSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				ContactBean contactBean = new ContactBean();
				
				String text = mEditSearch.getText().toString();
				ContactInfoBean infoBean = new ContactInfoBean(); 
				if(StringUtil.isMail(text)) {
					infoBean.setType(ContactInfoBean.TYPE_MAIL);
				} else {
					infoBean.setType(ContactInfoBean.TYPE_PHONE);
				}
				infoBean.setValue(text);
				
				List<ContactInfoBean> infos = new ArrayList<ContactInfoBean>();
				infos.add(infoBean);
				//TODO contactBean.setInfos(infos);
				contactBean.setNom("");
				contactBean.setPrenom("");
				
				ContactsUtil.addMyTimeContact(getActivity(), contactBean);


				/*
					ContactInfoBean contatcInfo = new ContactInfoBean();
					contatcInfo.setType(ContactInfoBean.TYPE_MAIL);
					contatcInfo.setValue(mEditSearch.getText().toString());
					ContactInfoRepository contactInfoRepo = new ContactInfoRepository(getActivity());
					long id = contactInfoRepo.insertContactInfo(contatcInfo);
					contatcInfo.setId((int)id);

					ParticipantBean participant = new ParticipantBean();
					participant.setIdContactInfo(contatcInfo.getId());
					participant.setEid(getParentActivity().getEvent().getId());
					repo.insertParticipant(participant);
					mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), false));
					mEditSearch.setText("");
					((BaseActivity)getActivity()).launchEdit();
				 */
				return false;
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
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), true));
		super.launchEdit();
	}

	@Override
	public void launchSave() {
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), false));
		super.launchSave();
	}

	@Override
	public void launchCancel() {
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), false));
		super.launchCancel();
	}

	@Override
	public boolean isEditable() {
		return true;
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

		public ParticipantsAdapter(Context context, int id, boolean isInEditMode) {
			this.isInEditMode = isInEditMode;

			contacts = new HashMap<Long, ContactBean>();
			contactsInfo = new ArrayList<ContactInfoBean>();

			participants = repo.getAllByEid(getParentActivity().getEvent().getId());
			if (participants != null) {
				for (ParticipantBean participant : participants) {
					ContactInfoBean contactInfo = ContactsUtil.getContactInfoById(getActivity(), participant.getIdContactInfo());
					contactsInfo.add(contactInfo);
					if(contactInfo == null) {
						contacts.put(-1L, new ContactBean());
					} else if(!contacts.containsKey(contactInfo.getContactId())) {
						contacts.put(contactInfo.getContactId(), ContactsUtil.getContatById(getActivity(), (int) contactInfo.getContactId()));
					}
				}
			}
		}

		@Override public int getCount() {return contactsInfo.size();}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			ContactInfoBean contactInfo = contactsInfo.get(position);
			ContactBean contact = contacts.get(contactInfo != null ? contactInfo.getContactId() : -1);
			final ParticipantsView view = new ParticipantsView(getActivity(),contact, contactInfo, this.isInEditMode);

			if(isInEditMode) {
				view.findViewById(R.id.delete).setVisibility(View.VISIBLE);
				view.setOnDeleteClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						repo.delete(participants.get(position));
						participants.remove(position);
						ParticipantsFragment.this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), true));
					}
				});				
			} else {
				view.findViewById(R.id.delete).setVisibility(View.GONE);
			}
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
			if (participants == null | participants.size() == 0) {
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
