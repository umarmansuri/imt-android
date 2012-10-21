package its.my.time.pages.editable.event.commentaires;

import its.my.time.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class CommentairesFragment extends SherlockFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ListView mListView = (ListView)findViewById(R.id.event_coment_listeComent);
		mListView.setAdapter(new CommentairesAdapter(getSherlockActivity()));
		return mListView;
	}
}
