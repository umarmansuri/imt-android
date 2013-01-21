package its.my.time.pages.calendar.list;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class ListEventAdapter implements ListAdapter {

	private final Context context;
	private List<EventBaseBean> events;

	public ListEventAdapter(Context context) {
		this.context = context;
		loadNextEvents();
	}

	private void loadNextEvents() {
		this.events = new EventBaseRepository(this.context).getAllNextFromNow();
	}

	@Override
	public int getCount() {
		if (this.events != null) {
			return this.events.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return this.events.get(position).getId();
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return new LittleEventView(this.context, this.events.get(position));
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		if (this.events == null | this.events.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

}
