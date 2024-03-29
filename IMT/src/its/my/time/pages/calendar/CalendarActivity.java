package its.my.time.pages.calendar;

import its.my.time.R;
import its.my.time.data.bdd.base.BaseRepository.OnObjectChangedListener;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.ws.Callback;
import its.my.time.data.ws.WSManager;
import its.my.time.pages.MyTimeActivity;
import its.my.time.pages.calendar.base.BasePagerAdapter;
import its.my.time.pages.calendar.day.DayPagerAdapter;
import its.my.time.pages.calendar.list.ListEventAdapter;
import its.my.time.pages.calendar.month.MonthPagerAdapter;
import its.my.time.pages.reporting.ReportingActivity;
import its.my.time.util.ActivityUtil;
import its.my.time.util.ColorUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.ControledViewPager;
import its.my.time.view.menu.MenuGroupe;
import its.my.time.view.menu.MenuObjet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.fonts.mooncake.MooncakeIcone;

public class CalendarActivity extends MyTimeActivity implements OnPageChangeListener, OnObjectChangedListener<CompteBean>, OnDateSetListener {

	public static final long ANIM_DURATION = 200;

	private static final int ID_PAGER = 888889;
	private static final int DURATION_WAITING_END = 300;

	private static final int INDEX_MENU_AGENDA_MONTH = 1;
	private static final int INDEX_MENU_AGENDA_DAY = 2;
	private static final int INDEX_MENU_AGENDA_LISTE = 3;

	private int indexCurrentPager = -1;
	private FrameLayout mMainFramePager;
	private ControledViewPager mViewPager;
	private TextView mTextTitle;
	private List<CompteBean> comptes;

	private MenuGroupe menuProfil;
	private MenuGroupe menuAgenda;
	private MenuGroupe menuCompte;
	private MenuGroupe menuReporting;

	private MenuObjet menuAgendaToday;
	private MenuObjet menuAgendaJour;
	private MenuObjet menuAgendaMois;


	private SparseBooleanArray accountVisibility;

	private CompteRepository compteRepo;

	public static Calendar curentCal;
	private static boolean isWaitingEnd;


	@Override
	protected void onStart() {
		isWaitingEnd = false;
		setContentView(R.layout.activity_calendar);

		this.mMainFramePager = (FrameLayout) findViewById(R.id.main_pager);

		if (curentCal == null) {
			curentCal = Calendar.getInstance();
		}
		if (this.indexCurrentPager != -1) {
			new ChangePageTask().execute(this.indexCurrentPager);
		} else {
			new ChangePageTask().execute(INDEX_MENU_AGENDA_MONTH);
		}

		super.onStart();
	}
	
	@Override
	protected void initialiseActionBar() {
		super.initialiseActionBar();

		final ActionBar mActionBar = getSupportActionBar();
		mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_header));
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);

		mActionBar.setDisplayShowCustomEnabled(true);
		this.mTextTitle = new TextView(this);
		this.mTextTitle.setGravity(Gravity.CENTER);
		this.mTextTitle.setTextSize(20);
		this.mTextTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.mTextTitle.setTextColor(getResources().getColor(R.color.grey));
		mActionBar.setCustomView(this.mTextTitle);
		mTextTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*EventBaseRepository adapter = new EventBaseRepository(CalendarActivity.this); 
				EventBaseBean bean;
				Calendar calDeb2; 
				Calendar calFin2; 
				bean = new EventBaseBean();
				bean.setId(1);
				bean.setCid(1); 
				bean.setTitle("H�h�!!");
				calDeb2 = Calendar.getInstance(); 
				bean.sethDeb(calDeb2); 
				calFin2 = Calendar.getInstance(); 
				calFin2.add(Calendar.HOUR, 2);
				bean.sethFin(calFin2);
				bean.setTypeId(Types.Event.MEETING);
				bean.setDetailsId(0); 
				adapter.insert(bean);*/
				
				DatePickerDialog dialog = new DatePickerDialog(
						CalendarActivity.this, 
						CalendarActivity.this, 
						curentCal.get(Calendar.YEAR),
						curentCal.get(Calendar.MONTH),
						curentCal.get(Calendar.DAY_OF_MONTH));
				dialog.show();
				
			}
		});
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		gotoDate(new GregorianCalendar(year, monthOfYear, dayOfMonth));	
	}
	
	
	@Override
	protected ArrayList<MenuGroupe> onCreateMenu(ArrayList<MenuGroupe> menuGroupes) {
		final int iconeColor = getResources().getColor(R.color.grey);
		final int iconeSize = 30;

		menuProfil = new MenuGroupe("Profil",MooncakeIcone.icon_user);
		menuGroupes.add(menuProfil);

		menuAgenda = new MenuGroupe("Agenda", MooncakeIcone.icon_table);
		ArrayList<MenuObjet> donnees = new ArrayList<MenuObjet>();
		final TextView textView = new TextView(CalendarActivity.this);
		textView.setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
		textView.setTextColor(iconeColor);
		textView.setTextSize(iconeSize);
		textView.setGravity(Gravity.CENTER);
		menuAgendaToday = new MenuObjet(menuAgenda, "Aujourd'hui",MooncakeIcone.icon_time);
		donnees.add(menuAgendaToday);
		menuAgendaJour = new MenuObjet(menuAgenda, "Jour",MooncakeIcone.icon_calendar);
		donnees.add(menuAgendaJour);
		menuAgendaMois = new MenuObjet(menuAgenda, "Mois",MooncakeIcone.icon_calendar_month);
		donnees.add(menuAgendaMois);
		menuAgenda.setObjets(donnees);
		menuGroupes.add(menuAgenda);

		menuCompte = new MenuGroupe("Comptes", MooncakeIcone.icon_database);
		donnees = new ArrayList<MenuObjet>();
		compteRepo = new CompteRepository(this);
		compteRepo.addOnObjectChangedListener(this);
		accountVisibility = new SparseBooleanArray();
		compteRepo = new CompteRepository(this);
		this.comptes = compteRepo.getAllByUid(PreferencesUtil.getCurrentUid());
		for (final CompteBean compteBean : this.comptes) {
			donnees.add(new MenuObjet(menuCompte, compteBean.getTitle(),MooncakeIcone.icon_business_card, true, compteBean.isShowed(), getResources().getDrawable(ColorUtil.getDrawableRes(compteBean.getColor()))));
			accountVisibility.put(compteBean.getId(), compteBean.isShowed());
		}
		donnees.add(new MenuObjet(menuCompte, "G�rer", MooncakeIcone.icon_cog));
		menuCompte.setObjets(donnees);
		menuGroupes.add(menuCompte);

		menuReporting = new MenuGroupe("Reporting", MooncakeIcone.icon_stats);
		menuGroupes.add(menuReporting);

		return  super.onCreateMenu(menuGroupes);
	}

	@Override
	public void onObjectAdded(CompteBean compteBean) {
		MenuObjet object = new MenuObjet(menuCompte, compteBean.getTitle(),MooncakeIcone.icon_business_card, true, compteBean.isShowed(), getResources().getDrawable(ColorUtil.getDrawableRes(compteBean.getColor())));
		menuCompte.getObjets().add(0, object);
	}
	
	@Override
	public void onObjectDeleted(CompteBean object) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onObjectUpdated(CompteBean object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMenuGroupClick(ExpandableListView parent,MenuGroupe group, long id) {
		if(group == menuProfil) {
			ActivityUtil.startProfilActivity(this);
		}else if(group == menuReporting) {
			startActivity(new Intent(CalendarActivity.this, ReportingActivity.class));
		}
		super.onMenuGroupClick(parent, group, id);
	}

	@Override
	protected void onMenuChildClick(ExpandableListView parent,MenuGroupe group, MenuObjet objet, long id) {
		if(group == menuCompte && menuCompte.getObjets().indexOf(objet) == menuCompte.getObjets().size()-1) {
			ActivityUtil.startComptesActivity(this);
		} else if (objet == menuAgendaToday) {	
			if (this.indexCurrentPager == INDEX_MENU_AGENDA_LISTE) {
				Toast.makeText(this,"Vous ne pouvez pas faire cette op�ration en vue liste!",Toast.LENGTH_SHORT).show();
			} else {
				gotoDate(Calendar.getInstance());
			}
		} else if (objet == menuAgendaJour) {	
			if (this.indexCurrentPager == INDEX_MENU_AGENDA_DAY) {
				Toast.makeText(this, "Vous �tes d�j� en vue jour!",Toast.LENGTH_SHORT).show();
			} else {
				new ChangePageTask().execute(INDEX_MENU_AGENDA_DAY);
			}
		} else if (objet == menuAgendaMois) {	
			if (this.indexCurrentPager == INDEX_MENU_AGENDA_MONTH) {
				Toast.makeText(this, "Vous �tes d�j� en vue mois!",Toast.LENGTH_SHORT).show();
			} else {
				new ChangePageTask().execute(INDEX_MENU_AGENDA_MONTH);
			}
		}
		super.onMenuChildClick(parent, group, objet, id);

	}

	@Override
	protected void onMenuChildSwitch(MenuGroupe group, MenuObjet objet,boolean isChecked) {
		if(group == menuCompte) {
			int index = menuCompte.getObjets().indexOf(objet);
			if(index != -1 && index != menuCompte.getObjets().size()-1) {
				comptes.get(index).setShowed(!comptes.get(index).isShowed());
				new CompteRepository(this).update(comptes.get(index));
			}
		}
		super.onMenuChildSwitch(group, objet, isChecked);
	}

	@Override
	protected boolean onBackButtonPressed() {
		switch (this.indexCurrentPager) {
		case INDEX_MENU_AGENDA_DAY:
			showMonths(curentCal);
			return true;
		case INDEX_MENU_AGENDA_MONTH:
			if (isWaitingEnd) {
				finish();
			} else {
				isWaitingEnd = true;
				Toast.makeText(this, "Appuyer une nouvelle fois pour quitter",
						DURATION_WAITING_END).show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(DURATION_WAITING_END * 10);
						} catch (final Exception e) {
						}
						isWaitingEnd = false;
					}
				}).start();
			}
			return true;
		}
		return false;
	}

	public void showDays(Calendar cal) {
		curentCal = cal;
		new ChangePageTask().execute(INDEX_MENU_AGENDA_DAY);
	}

	public void showListe() {
		new ChangePageTask().execute(INDEX_MENU_AGENDA_LISTE);
	}

	public void showMonths(Calendar cal) {
		curentCal = cal;
		new ChangePageTask().execute(INDEX_MENU_AGENDA_MONTH);
	}

	private void gotoDate(Calendar cal) {
		curentCal = cal;
		reload();
	}

	@Override
	protected void reload() {
		super.reload();
		new ChangePageTask().execute(this.indexCurrentPager);
	}
	
	private void changeTitle(final String title) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CalendarActivity.this.mTextTitle.setText(title);
			}
		});
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(final int position) {
		BasePagerAdapter currentAdapter = ((BasePagerAdapter) this.mViewPager.getAdapter()); 
		changeTitle(currentAdapter.getTitle(position));
		curentCal = currentAdapter.getCalendarAtPosition(position);
	}

	private class ChangePageTask extends AsyncTask<Integer, Void, View> {

		private Integer indexNextPage;

		@Override
		protected void onPreExecute() {
			final Animation anim = new AlphaAnimation(1, 0);
			anim.setFillAfter(true);
			anim.setDuration(ANIM_DURATION);
			CalendarActivity.this.mMainFramePager.startAnimation(anim);
		}

		@Override
		protected View doInBackground(Integer... params) {
			this.indexNextPage = params[0];
			CalendarActivity.this.mViewPager = new ControledViewPager(getApplicationContext());
			CalendarActivity.this.mViewPager.setId(ID_PAGER);
			switch (this.indexNextPage) {
			case INDEX_MENU_AGENDA_DAY:
				CalendarActivity.this.mViewPager.setAdapter(new DayPagerAdapter(getSupportFragmentManager(), curentCal));
				CalendarActivity.this.indexCurrentPager = INDEX_MENU_AGENDA_DAY;
				break;
			case INDEX_MENU_AGENDA_MONTH:
				CalendarActivity.this.mViewPager.setAdapter(new MonthPagerAdapter(getSupportFragmentManager(), curentCal));
				CalendarActivity.this.indexCurrentPager = INDEX_MENU_AGENDA_MONTH;
				break;
			case INDEX_MENU_AGENDA_LISTE:
				final ListView mListView = new ListView(getApplicationContext());
				mListView.setAdapter(new ListEventAdapter(CalendarActivity.this));
				CalendarActivity.this.indexCurrentPager = INDEX_MENU_AGENDA_LISTE;
				changeTitle("Liste");
				return mListView;
			}
			CalendarActivity.this.mViewPager.setOnPageChangeListener(CalendarActivity.this);
			CalendarActivity.this.mViewPager.setCurrentItem(BasePagerAdapter.NB_PAGE / 2);

			return CalendarActivity.this.mViewPager;
		}

		@Override
		protected void onPostExecute(View result) {
			CalendarActivity.this.mMainFramePager.removeAllViews();
			CalendarActivity.this.mMainFramePager.addView(result);
			final Animation anim = new AlphaAnimation(0, 1);
			anim.setFillAfter(true);
			anim.setDuration(ANIM_DURATION);
			CalendarActivity.this.mMainFramePager.startAnimation(anim);
		}
	}

	@Override
	public boolean isUpdatable() {
		return false;
	}
	
	@Override
	public void onMajCalled() {
		super.onMajCalled();
		WSManager.updateAllData(CalendarActivity.this, new Callback() {

			@Override
			public void done(Exception e) {
				majFinished(e);
			}
		});
	}
}
