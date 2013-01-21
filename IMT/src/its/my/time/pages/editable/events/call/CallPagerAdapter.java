package its.my.time.pages.editable.events.call;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CallPagerAdapter extends FragmentPagerAdapter {
	
	private EventBaseBean event;

	public CallPagerAdapter(FragmentManager fm, EventBaseBean event) {
		super(fm);
		this.event = event;
	}

	public static final String TITLE_PAGE_DETAILS = "Evénement";
	public static final String TITLE_PAGE_COMMENTAIRES = "Commentaires";
	
	public static final int INDEX_PAGE_EVENT = 0;
	public static final int INDEX_PAGE_PJ= 1;
	
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case INDEX_PAGE_EVENT:return new DetailsFragment(event, EventBaseRepository.Types.TYPE_CALL);
		case INDEX_PAGE_PJ:return new PjFragment(event.getId());
		}
		return null;
	}

	@Override
	public int getCount() {
		return 4;
	}


}
