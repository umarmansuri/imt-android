package its.my.time.pages.editable.events;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.data.bdd.events.plugins.comment.CommentRepository;
import its.my.time.data.bdd.events.plugins.note.NoteBean;
import its.my.time.data.bdd.events.plugins.note.NoteRepository;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.data.bdd.events.plugins.odj.OdjRepository;
import its.my.time.data.bdd.events.plugins.participant.ParticipantBean;
import its.my.time.data.bdd.events.plugins.participant.ParticipantRepository;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.bdd.events.plugins.pj.PjRepository;
import its.my.time.data.ws.WSManager;
import its.my.time.data.ws.events.PostEventReturn;
import its.my.time.data.ws.events.WSSendEvent;
import its.my.time.data.ws.events.plugins.commentaire.WSSendCommentaire;
import its.my.time.data.ws.events.plugins.note.WSSendNote;
import its.my.time.data.ws.events.plugins.odj.WSSendOdj;
import its.my.time.data.ws.events.plugins.participation.WSSendParticipation;
import its.my.time.data.ws.events.plugins.pj.WSSendPj;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.pages.editable.events.plugins.PluginFragment;
import its.my.time.util.ActivityUtil;
import its.my.time.util.ConnectionManager;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.util.Types;
import its.my.time.view.ControledViewPager;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.fonts.mooncake.MooncakeIcone;

public abstract class BaseEventActivity extends BaseActivity {

	protected ControledViewPager mPager;
	private ArrayList<PluginFragment> fragments;
	private boolean isNew;
	protected EventBaseBean event;
	private MenuGroupe menuSuppression;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		setContentView(R.layout.activity_event);

		final Bundle bundle = getIntent().getExtras();
		isNew = false;
		if (bundle.getInt(ActivityUtil.KEY_EXTRA_ID) > 0) {
			this.event = new EventBaseRepository(this).getById(bundle.getInt(ActivityUtil.KEY_EXTRA_ID));
		}
		if (this.event == null) {
			this.event = new EventBaseBean();
			this.event.setTypeId(Types.Event.BASE);

			event.setMine(true);

			boolean isAllDay = bundle.getBoolean(ActivityUtil.KEY_EXTRA_ALL_DAY, false);
			Calendar hDeb = DateUtil.getDateFromISO(bundle.getString(ActivityUtil.KEY_EXTRA_ISO_TIME));
			Calendar hFin;
			if(isAllDay) {
				hDeb.set(Calendar.HOUR_OF_DAY, 0);
				hDeb.set(Calendar.HOUR, 0);
				hDeb.set(Calendar.MINUTE, 0);
				hDeb.set(Calendar.SECOND, 0);
				hFin = (Calendar)hDeb.clone();
				hFin.set(Calendar.HOUR, 23);
				hFin.set(Calendar.MINUTE, 59);
				hFin.set(Calendar.SECOND, 59);
			} else {
				hFin = (Calendar)hDeb.clone();
				hFin.add(Calendar.HOUR, 2);
			}
			this.event.sethDeb(hDeb);
			this.event.sethFin(hFin);

			this.event.setAllDay(isAllDay);
			isNew = true;
		}
		fragments = getPages();
		if(fragments == null) {
			fragments = new ArrayList<PluginFragment>();
		}
		this.mPager = (ControledViewPager) findViewById(R.id.event_pager);
		this.mPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager()));
		this.mPager.setOnPageChangeListener(this.pageListener);

		final ActionBar mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final List<String> titles = new ArrayList<String>();
		for (final PluginFragment fragment : fragments) {
			titles.add(fragment.getTitle());
		}

		mActionBar.setListNavigationCallbacks(
				new ArrayAdapter<String>(this,
						R.layout.navigation_spinner_item, titles
						.toArray(new String[] {})),
						this.navigationListener);
	}

	@Override
	protected ArrayList<MenuGroupe> onCreateMenu(ArrayList<MenuGroupe> menuGroupes) {
		menuSuppression = new MenuGroupe("Supp. événement", MooncakeIcone.icon_remove);
		menuGroupes.add(menuSuppression);
		return super.onCreateMenu(menuGroupes);
	}

	@Override
	protected void onMenuGroupClick(ExpandableListView parent,
			MenuGroupe group, long id) {
		if(group==menuSuppression) {
			AlertDialog.Builder alertSupperssion = new AlertDialog.Builder(this);
			alertSupperssion.setTitle("Supprimer l'événement ?");

			DialogInterface.OnClickListener listenerButon = new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) { 
						EventBaseRepository evRepo = new EventBaseRepository(getApplicationContext());
						evRepo.delete(getEvent());
						dialog.dismiss();
						finish();
					}
					else { 
						dialog.cancel();

					}

				}
			};
			alertSupperssion.setPositiveButton("Oui", listenerButon);
			alertSupperssion.setNeutralButton("Non", listenerButon);
			alertSupperssion.show();

		} else {
			super.onMenuGroupClick(parent, group, id);
		}
	}

	public abstract ArrayList<PluginFragment> getPages();

	@Override
	protected void initialiseActionBar() {
		super.initialiseActionBar();
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	protected OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			getSupportActionBar().setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	protected OnNavigationListener navigationListener = new OnNavigationListener() {

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			if (BaseEventActivity.this.mPager.isPagingEnabled()) {
				BaseEventActivity.this.mPager.setCurrentItem(itemPosition);
			} else if (itemPosition != BaseEventActivity.this.mPager
					.getCurrentItem()) {
				getSupportActionBar().setSelectedNavigationItem(
						BaseEventActivity.this.mPager.getCurrentItem());
				Toast.makeText(BaseEventActivity.this,
						"Veuillez enregistrer avant de changer d'onglet",
						Toast.LENGTH_SHORT).show();
			}
			return true;
		}
	};


	public PluginFragment getActiveFragment() {
		if (this.mPager.getAdapter() instanceof FragmentStatePagerAdapter) {
			final FragmentStatePagerAdapter a = (FragmentStatePagerAdapter) this.mPager.getAdapter();
			return (PluginFragment) a.instantiateItem(this.mPager,this.mPager.getCurrentItem());
		} else {
			final String name = makeFragmentName(this.mPager.getId(),this.mPager.getCurrentItem());
			return (PluginFragment) getSupportFragmentManager().findFragmentByTag(name);
		}
	}

	public EventBaseBean getEvent() {
		return event;
	}
	public void setEvent(EventBaseBean event) {
		this.event = event;
	}

	private static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

	@Override
	protected void showEdit() {
		this.mPager.setPagingEnabled(false);
		getActiveFragment().launchEdit();
	}

	@Override
	protected void showCancel() {
		this.mPager.setPagingEnabled(true);
		getActiveFragment().launchCancel();
	}

	@Override
	protected void showSave() {
		this.mPager.setPagingEnabled(true);
		getActiveFragment().launchSave();
		if(ConnectionManager.isOnline(BaseEventActivity.this)) {
			onMajCalled();
		}
	}

	@Override
	protected void onViewCreated() {
		if(isNew) {
			launchEdit();
		}
	}

	@Override
	protected boolean onBackButtonPressed() {
		for (PluginFragment fragment : fragments) {
			if(fragment.isInEditMode()) {
				Toast.makeText(this, "Attention, certaines modifications n'ont pas été enregistrées.", Toast.LENGTH_SHORT).show();
				return true;
			}
		}
		return super.onBackButtonPressed();
	}

	@Override
	public void onMajCalled() {
		super.onMajCalled();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String result = new WSSendEvent(BaseEventActivity.this, event, null).run();
					ObjectMapper mapper = new ObjectMapper();
					PostEventReturn object = mapper.readValue(result, PostEventReturn.class);
					event.setIdDistant(object.getIdEvent());
					if(event.getId() <= 0) {
						event.setId((int)new EventBaseRepository(BaseEventActivity.this).insert(event));
					} else {
						new EventBaseRepository(BaseEventActivity.this).update(event);
					}
					List<CommentBean> comments = new CommentRepository(BaseEventActivity.this).getAllpdatableByEid(event.getId());
					for (CommentBean comment : comments) {
						new WSSendCommentaire(BaseEventActivity.this, comment, null).run();
					}
					
					List<NoteBean> notes = new NoteRepository(BaseEventActivity.this).getAllpdatableByEid(event.getId());
					for (NoteBean note : notes) {
						new WSSendNote(BaseEventActivity.this, note, null).run();
					}
					
					List<OdjBean> odjs = new OdjRepository(BaseEventActivity.this).getAllpdatableByEid(event.getId());
					for (OdjBean odj : odjs) {
						new WSSendOdj(BaseEventActivity.this, odj, null).run();
					}
					
					List<ParticipantBean> participants = new ParticipantRepository(BaseEventActivity.this).getAllpdatableByEid(event.getId());
					for (ParticipantBean participant : participants) {
						new WSSendParticipation(BaseEventActivity.this, participant, null).run();
					}

					
					List<PjBean> pjs = new PjRepository(BaseEventActivity.this).getAllpdatableByEid(event.getId());
					for (PjBean pj : pjs) {
						new WSSendPj(BaseEventActivity.this, pj, null).run();
					}
					
					
					CompteRepository compteRepo = new CompteRepository(BaseEventActivity.this);
					SparseArray<CompteBean> comptes = new SparseArray<CompteBean>(); 
					for (CompteBean compte : compteRepo.getAllByUid(PreferencesUtil.getCurrentUid())) {
						comptes.put(compte.getIdDistant(), compte);
					}
					WSManager.init(BaseEventActivity.this);
					WSManager.retreiveEvent(getEvent().getIdDistant(), -1, true, comptes);
					event = new EventBaseRepository(BaseEventActivity.this).getById(event.getId());
					runOnUiThread(new Runnable() {
						public void run() {
							for (PluginFragment fragment : fragments) {
								try {
									fragment.refresh();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							majFinished(null);
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							majFinished(e);
						}
					});
				}
			}
		}).start();
	}

	public class EventPagerAdapter extends FragmentStatePagerAdapter {
		public EventPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return (Fragment)fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}
}
