package its.my.time.pages.editable.event;

import its.my.time.pages.editable.event.commentaires.CommentairesFragment;
import its.my.time.pages.editable.event.details.DetailsFragment;
import its.my.time.pages.editable.event.participants.ParticipantsFragment;
import its.my.time.pages.editable.event.pj.PjFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EventPagerAdapter extends FragmentPagerAdapter {
	
	public EventPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public static final String TITLE_PAGE_DETAILS = "Evènement";
	public static final String TITLE_PAGE_PARTICIPANTS = "Participants";
	public static final String TITLE_PAGE_COMMENTAIRES = "Commentaires";
	public static final String TITLE_PAGE_PJ= "Pièces jointes";
	
	public static final int INDEX_PAGE_EVENT = 0;
	public static final int INDEX_PAGE_PARTICIPANTS = 1;
	public static final int INDEX_PAGE_COMMENTAIRES = 2;
	public static final int INDEX_PAGE_PJ= 3;
	
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case INDEX_PAGE_EVENT:return new DetailsFragment();
		case INDEX_PAGE_PARTICIPANTS:return new ParticipantsFragment();
		case INDEX_PAGE_COMMENTAIRES:return new CommentairesFragment();
		case INDEX_PAGE_PJ:return new PjFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 4;
	}


}
