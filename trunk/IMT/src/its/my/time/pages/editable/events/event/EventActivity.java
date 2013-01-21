package its.my.time.pages.editable.events.event;

import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.participants.ParticipantsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;

public class EventActivity extends BaseEventActivity {

	private static ArrayList<BasePluginFragment> fragments;

	@Override
	public  void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
			fragments = new ArrayList<BasePluginFragment>();
			BasePluginFragment fragment = new DetailsFragment(event, EventBaseRepository.Types.TYPE_BASE);
			fragments.add(fragment);
			fragment = new ParticipantsFragment(event.getId());
			fragments.add(fragment);
			fragment = new CommentairesFragment(event.getId());
			fragments.add(fragment);
			fragment = new PjFragment(event.getId());
			fragments.add(fragment);
		mPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager()));
	}

	@Override
	protected void onViewCreated() {
	
	}
	
	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvel évènement";
	}
	
	@Override
	protected void initialiseActionBar() {

		super.initialiseActionBar();

		ActionBar mActionBar = getSupportActionBar();

		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		String[] items = new String[] { EventPagerAdapter.TITLE_PAGE_DETAILS,
				EventPagerAdapter.TITLE_PAGE_PARTICIPANTS,
				EventPagerAdapter.TITLE_PAGE_COMMENTAIRES,
				EventPagerAdapter.TITLE_PAGE_PJ };
		mActionBar.setListNavigationCallbacks(new ArrayAdapter<String>(this,
				R.layout.navigation_spinner_item, items), navigationListener);
	}



	@Override
	protected void onMenuGroupSwitch(View v, int positionGroup,
			boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMenuItemSwitch(View v, int positionGroup,
			int positionObjet, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMenuGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMenuChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected ArrayList<MenuGroupe> onMainMenuCreated(
			ArrayList<MenuGroupe> menuGroupes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onBackButtonPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class EventPagerAdapter extends FragmentPagerAdapter {
		public EventPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public static final String TITLE_PAGE_DETAILS = "Evénement";
		public static final String TITLE_PAGE_PARTICIPANTS = "Participants";
		public static final String TITLE_PAGE_COMMENTAIRES = "Commentaires";
		public static final String TITLE_PAGE_PJ= "Pièces jointes";

		public static final int INDEX_PAGE_EVENT = 0;
		public static final int INDEX_PAGE_PARTICIPANTS = 1;
		public static final int INDEX_PAGE_COMMENTAIRES = 2;
		public static final int INDEX_PAGE_PJ= 3;


		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}

}
