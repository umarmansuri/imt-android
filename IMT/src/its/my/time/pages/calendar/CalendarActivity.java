package its.my.time.pages.calendar;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.pages.MenuActivity;
import its.my.time.pages.calendar.base.BasePagerAdapter;
import its.my.time.pages.calendar.day.DayPagerAdapter;
import its.my.time.pages.calendar.list.ListEventAdapter;
import its.my.time.pages.calendar.month.MonthPagerAdapter;
import its.my.time.util.ActivityUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.ControledViewPager;
import its.my.time.view.menu.MenuGroupe;
import its.my.time.view.menu.MenuObjet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.fonts.mooncake.MooncakeIcone;

public class CalendarActivity extends MenuActivity implements
OnPageChangeListener {

	public static final long ANIM_DURATION = 500;

	private static final int ID_PAGER = 888889;
	private static final int DURATION_WAITING_END = 300;

	private static final int ID_MENU_TODAY = 0;

	private int indexCurrentPager = -1;

	private FrameLayout mMainFramePager;
	private ControledViewPager mViewPager;

	private TextView mTextTitle;

	private List<CompteBean> comptes;

	public static Calendar curentCal;

	private static boolean isWaitingEnd;

	@Override
	protected void onResume() {
		isWaitingEnd = false;
		setContentView(R.layout.activity_calendar);

		mMainFramePager = (FrameLayout) findViewById(R.id.main_pager);

		if (curentCal == null) {
			curentCal = Calendar.getInstance();
		}
		if(indexCurrentPager != -1) {
			new ChangePageTask().execute(indexCurrentPager);	
		} else {
			new ChangePageTask().execute(INDEX_MENU_AGENDA_MONTH);
		}
		
		super.onResume();
	}

	protected void initialiseActionBar() {
		super.initialiseActionBar();

		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_header));
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);

		mActionBar.setDisplayShowCustomEnabled(true);
		mTextTitle = new TextView(this);
		mTextTitle.setGravity(Gravity.CENTER);
		mTextTitle.setTextSize(20);
		mTextTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		mTextTitle.setTextColor(getResources().getColor(R.color.grey));
		mActionBar.setCustomView(mTextTitle);
	}

	private static final int INDEX_MENU_PROFIL = 0;

	private static final int INDEX_MENU_AGENDA = 1;
	private static final int INDEX_MENU_AGENDA_TODAY = 0;
	private static final int INDEX_MENU_AGENDA_MONTH = 1;
	private static final int INDEX_MENU_AGENDA_DAY = 2;
	private static final int INDEX_MENU_AGENDA_LISTE = 3;

	private static final int INDEX_MENU_COMPTE = 2;
	private static int INDEX_MENU_COMPTE_GERER = 0;
	private static final int INDEX_MENU_LIBELLE = 3;
	private static final int INDEX_MENU_PARAMETRES = 4;

	@Override
	protected ArrayList<MenuGroupe> onMainMenuCreated(ArrayList<MenuGroupe> menuGroupes) {
		int iconeColor = getResources().getColor(R.color.grey);
		int iconeSize = 30;

		MenuGroupe menuGroupe = new MenuGroupe("Profil", MooncakeIcone.icon_user);
		menuGroupes.add(menuGroupe);

		menuGroupe = new MenuGroupe("Agenda", MooncakeIcone.icon_table);
		ArrayList<MenuObjet> donnees = new ArrayList<MenuObjet>();
		TextView textView = new TextView(CalendarActivity.this);
		textView.setText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
		textView.setTextColor(iconeColor);
		textView.setTextSize(iconeSize);
		textView.setGravity(Gravity.CENTER);
		donnees.add(new MenuObjet(menuGroupe, "Aujourd'hui", MooncakeIcone.icon_time));
		donnees.add(new MenuObjet(menuGroupe, "Mois",  MooncakeIcone.icon_calendar_month));
		donnees.add(new MenuObjet(menuGroupe, "Jour",  MooncakeIcone.icon_calendar));
		donnees.add(new MenuObjet(menuGroupe, "Liste", MooncakeIcone.icon_list_2));
		menuGroupe.setObjets(donnees);
		menuGroupes.add(menuGroupe);

		menuGroupe = new MenuGroupe("Comptes", MooncakeIcone.icon_database);
		donnees = new ArrayList<MenuObjet>();
		CompteRepository compteRepo = new CompteRepository(this);
		comptes = compteRepo.getAllCompteByUid(PreferencesUtil.getCurrentUid(this));
		for (CompteBean compteBean : comptes) {
			donnees.add(new MenuObjet(menuGroupe, compteBean.getTitle(), MooncakeIcone.icon_business_card, true, compteBean.isShowed(), compteBean.getColor()));
			INDEX_MENU_COMPTE_GERER++;
		}
		donnees.add(new MenuObjet(menuGroupe, "Gérer", MooncakeIcone.icon_cog));
		menuGroupe.setObjets(donnees);
		menuGroupes.add(menuGroupe);

		menuGroupe = new MenuGroupe("Libellés", MooncakeIcone.icon_tags);
		donnees = new ArrayList<MenuObjet>();
		donnees.add(new MenuObjet(menuGroupe, "Libellé 1", MooncakeIcone.icon_tag, true));
		donnees.add(new MenuObjet(menuGroupe, "Libellé 2", MooncakeIcone.icon_tag, true, false, Color.BLUE));
		donnees.add(new MenuObjet(menuGroupe, "Libellé 3", MooncakeIcone.icon_tag, true));
		donnees.add(new MenuObjet(menuGroupe, "Gérer", MooncakeIcone.icon_cog));
		menuGroupe.setObjets(donnees);
		menuGroupes.add(menuGroupe);

		menuGroupe = new MenuGroupe("Réglages",  MooncakeIcone.icon_settings);
		menuGroupes.add(menuGroupe);

		return menuGroupes;
	}

	@Override
	protected void onMenuChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
		switch (groupPosition) {
		case INDEX_MENU_AGENDA:
			switch (childPosition) {
			case INDEX_MENU_AGENDA_DAY:
				if (indexCurrentPager == INDEX_MENU_AGENDA_DAY) {
					Toast.makeText(this, "Vous êtes déjà en vue jour!",
							Toast.LENGTH_SHORT).show();
				} else {
					new ChangePageTask().execute(INDEX_MENU_AGENDA_DAY);
				}
				break;
			case INDEX_MENU_AGENDA_MONTH:
				if (indexCurrentPager == INDEX_MENU_AGENDA_MONTH) {
					Toast.makeText(this, "Vous êtes déjà en vue mois!",
							Toast.LENGTH_SHORT).show();
				} else {
					new ChangePageTask().execute(INDEX_MENU_AGENDA_MONTH);
				}
				break;
			case INDEX_MENU_AGENDA_LISTE:
				if (indexCurrentPager == INDEX_MENU_AGENDA_LISTE) {
					Toast.makeText(this, "Vous êtes déjà en vue liste!",Toast.LENGTH_SHORT).show();
				} else {
					new ChangePageTask().execute(INDEX_MENU_AGENDA_LISTE);
				}
				break;
			case INDEX_MENU_AGENDA_TODAY:
				if (indexCurrentPager == INDEX_MENU_AGENDA_LISTE) {
					Toast.makeText(this, "Vous ne pouvez pas faire cette opération en vue liste!",Toast.LENGTH_SHORT).show();
				} else {
					gotoDate(Calendar.getInstance());
				}
				break;
			}
			break;
		case INDEX_MENU_COMPTE:
			if(childPosition == INDEX_MENU_COMPTE_GERER) {
				ActivityUtil.startComptesActivity(this);
			}
			break;
		}
	}

	@Override
	protected void onMenuGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		switch (groupPosition) {
		case INDEX_MENU_PROFIL:
			ActivityUtil.startProfilActivity(this);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onMenuGroupSwitch(View v, int positionGroup,boolean isChecked) {}
	@Override
	protected void onMenuItemSwitch(View v, int positionGroup, int positionObjet, boolean isChecked) {
		switch (positionGroup) {
		case INDEX_MENU_COMPTE:
			comptes.get(positionObjet).setShowed(!comptes.get(positionObjet).isShowed());
			new CompteRepository(this).update(comptes.get(positionObjet));
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MooncakeIcone icone = new MooncakeIcone(this);
		icone.setTextSize(18);
		icone.setIconeRes(MooncakeIcone.icon_calendar);

		icone.setId(ID_MENU_TODAY);
		icone.setOnClickListener(this);
		icone.setTextColor(getResources().getColor(R.color.grey));
		menu.add(Menu.NONE, ID_MENU_TODAY, Menu.NONE, "").setActionView(icone);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void reload() {
		new ChangePageTask().execute(indexCurrentPager);
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
		((BasePagerAdapter) ((ViewPager) mMainFramePager.getChildAt(0)).getAdapter()).setCurrentCalendar(cal);
		((ViewPager) mMainFramePager.getChildAt(0))
		.setCurrentItem(BasePagerAdapter.NB_PAGE / 2);
	}


	@Override
	protected boolean onBackButtonPressed() {
		switch (indexCurrentPager) {
		case INDEX_MENU_AGENDA_DAY:
			showMonths(curentCal);
			return true;
		case INDEX_MENU_AGENDA_MONTH:
			if (isWaitingEnd) {
				finish();
			} else {
				isWaitingEnd = true;
				Toast.makeText(this,
						"Appuyer une nouvelle fois pour quitter",
						DURATION_WAITING_END).show();
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(DURATION_WAITING_END * 10);
						} catch (Exception e) {
						}
						isWaitingEnd = false;
					}
				}).start();
			}
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case ID_MENU_TODAY:
			gotoDate(Calendar.getInstance());
			return;
		}
		super.onClick(v);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(final int position) {
		changeTitle(((BasePagerAdapter) mViewPager.getAdapter()).getTitle(position));
	}

	private void changeTitle(final String title) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTextTitle.setText(title);
			}
		});
	}

	private class ChangePageTask extends AsyncTask<Integer, Void, View> {

		private Integer indexNextPage;

		@Override
		protected void onPreExecute() {
			Animation anim = new AlphaAnimation(1, 0);
			anim.setFillAfter(true);
			anim.setDuration(ANIM_DURATION);
			mMainFramePager.startAnimation(anim);
		}

		@Override
		protected View doInBackground(Integer... params) {
			indexNextPage = params[0];
			mViewPager = new ControledViewPager(getApplicationContext());
			mViewPager.setId(ID_PAGER);
			switch (indexNextPage) {
			case INDEX_MENU_AGENDA_DAY:
				mViewPager.setAdapter(new DayPagerAdapter(
						getSupportFragmentManager(), curentCal));
				indexCurrentPager = INDEX_MENU_AGENDA_DAY;
				break;
			case INDEX_MENU_AGENDA_MONTH:
				mViewPager.setAdapter(new MonthPagerAdapter(
						getSupportFragmentManager(), (Calendar) curentCal));
				mViewPager.getAdapter().notifyDataSetChanged();
				indexCurrentPager = INDEX_MENU_AGENDA_MONTH;
				break;
			case INDEX_MENU_AGENDA_LISTE:
				ListView mListView = new ListView(getApplicationContext());
				mListView
				.setAdapter(new ListEventAdapter(CalendarActivity.this));
				indexCurrentPager = INDEX_MENU_AGENDA_LISTE;
				changeTitle("Liste");
				return mListView;
			}
			mViewPager.setOnPageChangeListener(CalendarActivity.this);
			mViewPager.setCurrentItem(BasePagerAdapter.NB_PAGE / 2);

			return mViewPager;
		}

		@Override
		protected void onPostExecute(View result) {
			mMainFramePager.removeAllViews();
			mMainFramePager.addView(result);
			Animation anim = new AlphaAnimation(0, 1);
			anim.setFillAfter(true);
			anim.setDuration(ANIM_DURATION);
			mMainFramePager.startAnimation(anim);
		}
	}
}
