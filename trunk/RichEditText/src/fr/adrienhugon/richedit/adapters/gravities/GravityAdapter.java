package fr.adrienhugon.richedit.adapters.gravities;

import java.util.ArrayList;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import fr.adrienhugon.richedit.R;

public class GravityAdapter extends BaseAdapter {

	private ArrayList<Integer> gravities;

	public GravityAdapter() {
		gravities = new ArrayList<Integer>();
		gravities.add(R.drawable.left);
		gravities.add(R.drawable.center);
		gravities.add(R.drawable.right);
	}
	
	@Override
	public int getCount() {
		return gravities.size();
	}

	@Override
	public Integer getItem(int position) {
		return gravities.get(position);
	}

	@Override
	public long getItemId(int position) {return 0;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imgView = new ImageView(parent.getContext());
		imgView.setImageResource(getItem(position));
		int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, parent.getContext().getResources().getDisplayMetrics());
		imgView.setLayoutParams(new AbsListView.LayoutParams(px, px));
		imgView.setScaleType(ScaleType.CENTER_INSIDE);
		return imgView;
	}


}
