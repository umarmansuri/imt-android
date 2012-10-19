package its.my.time.pages.calendar.list;

import its.my.time.data.bdd.event.EventBean;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class ListEventAdapter implements ListAdapter{

	private static final int NB_EVENT_LOADED = 10;

	private Context context;
	private List<EventBean> events;
	private int indexEvent;

	public ListEventAdapter(Context context) {
		this.context = context;
		indexEvent = 0;
		loadNextEvents();
	}

	private void loadNextEvents() {
		if(events == null) {
			events = new ArrayList<EventBean>();
		}

		//TODO supprimer et recuperer les events de la bdd
		EventBean eventBean;
		for(int i = 0; i < NB_EVENT_LOADED; i++){
			eventBean = new EventBean();
			eventBean.setId(indexEvent + i);
			eventBean.setTitle("Titre event " + (indexEvent + i) );
			eventBean.setDetails("Détails de -l'évènement " + (indexEvent + i) + ": Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi molestie, leo eget viverra luctus, massa sem lacinia est, at bibendum ipsum tellus eu tortor. Donec quis interdum leo. Curabitur in magna magna. Aliquam a odio sem, eu tempus lorem. Praesent consectetur odio nec massa dignissim a luctus purus rhoncus. Donec vitae risus non arcu cursus sodales et nec enim. Nam.");
			events.add(eventBean);
		}
		indexEvent+=NB_EVENT_LOADED;
	}

	@Override
	public int getCount() {
		if(events != null) {
			return events.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {return null;}

	@Override
	public long getItemId(int position) {return events.get(position).getId();}

	@Override
	public int getItemViewType(int position) {return 0;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return new LittleEventView(context, events.get(position));
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
		if(events == null | events.size() == 0) {return true;} else {return false;}
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {}

	@Override
	public boolean areAllItemsEnabled() {return false;}

	@Override
	public boolean isEnabled(int position) {return false;}



}
