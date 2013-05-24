package its.my.time.pages.calendar.day;

import its.my.time.pages.calendar.base.BaseFragment;
import its.my.time.pages.calendar.base.BaseView;

import java.util.Calendar;

public class DayFragment extends BaseFragment {


	private Calendar calDeb;


	public DayFragment(Calendar calDeb, Calendar calFin) {
		super(calDeb, calFin);
		this.calDeb = calDeb;
	}

	@Override
	protected BaseView createView(OnViewCreated onViewCreated) {
		return new DayView(getSherlockActivity(), calDeb, onViewCreated);
	}
}
