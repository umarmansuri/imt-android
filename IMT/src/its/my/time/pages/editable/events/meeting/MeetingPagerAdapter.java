package its.my.time.pages.editable.events.meeting;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.editable.events.meeting.details.MeetingDetailsFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.odj.OdjFragment;
import its.my.time.pages.editable.events.plugins.participants.ParticipantsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MeetingPagerAdapter extends FragmentPagerAdapter {
	
	private EventBaseBean event;


	public MeetingPagerAdapter(FragmentManager fm, EventBaseBean event) {
		super(fm);
		this.event = event;
	}

	public static final String TITLE_PAGE_DETAILS = "Réunion";
	public static final String TITLE_PAGE_ODJ = "Ordre du Jour";
	public static final String TITLE_PAGE_PARTICIPANTS = "Participants";
	public static final String TITLE_PAGE_COMMENTAIRES = "Commentaires";
	public static final String TITLE_PAGE_PJ= "Pièces jointes";
	
	public static final int INDEX_PAGE_MEETING = 0;
	public static final int INDEX_PAGE_ODJ = 1;
	public static final int INDEX_PAGE_PARTICIPANTS = 2;
	public static final int INDEX_PAGE_COMMENTAIRES = 3;
	public static final int INDEX_PAGE_PJ= 4;
	
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case INDEX_PAGE_MEETING:return new MeetingDetailsFragment(event, EventBaseRepository.Types.TYPE_MEETING);
		case INDEX_PAGE_ODJ:return new OdjFragment(event.getId());
		case INDEX_PAGE_PARTICIPANTS:return new ParticipantsFragment(event.getId());
		case INDEX_PAGE_COMMENTAIRES:return new CommentairesFragment(event.getId());
		case INDEX_PAGE_PJ:return new PjFragment(event.getId());
		}
		return null;
	}

	@Override
	public int getCount() {
		return 5;
	}


}
