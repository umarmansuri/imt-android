package its.my.time.pages.editable.events.plugins.odj;

import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class OdjAdapter extends ArrayAdapter<OdjBean>{

	private Context context;
	private List<OdjBean> odj;

	private int idEvent;

	public OdjAdapter(Context context, int id) {
		super(context, 0);
		this.context = context;
		this.idEvent = id;
		loadNext();
	}

	private void loadNext() {
		if(odj == null) {
			odj = new ArrayList<OdjBean>();
		}
		odj = DatabaseUtil.Plugins.getOdjRepository(context).getAllByEid(idEvent);
	}

	@Override
	public int getCount() {
		if(odj != null) {
			return odj.size();
		}
		return 0; 
	}

	@Override
	public OdjBean getItem(int position) {return odj.get(position);}

	@Override
	public long getItemId(int position) {return odj.get(position).getId();}

	@Override
	public int getItemViewType(int position) {return 0;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return new OdjView(context, odj.get(position));
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
		if(odj == null | odj.size() == 0) {return true;} else {return false;}
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {}

	@Override
	public boolean areAllItemsEnabled() {return false;}

	@Override
	public boolean isEnabled(int position) {return false;}

	public void remove(OdjBean item) {
		odj.remove(item);
	}

	public void insert(OdjBean item, int to) {
		odj.add(to, item);
	}
}
