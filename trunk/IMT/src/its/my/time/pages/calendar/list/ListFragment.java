package its.my.time.pages.calendar.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class ListFragment extends SherlockFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ListView mListView = new ListView(getSherlockActivity());
		mListView.setAdapter(new ListEventAdapter(getActivity()));
		return mListView;
	}
}
