package fr.adrienhugon.richedit.adapters.size;

import java.util.ArrayList;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SizeAdapter extends BaseAdapter{

	private ArrayList<Integer> sizes;

	public SizeAdapter() {
		sizes = new ArrayList<Integer>();
		sizes.add(12);
		sizes.add(14);
		sizes.add(16);
		sizes.add(18);
		sizes.add(20);
		sizes.add(22);

	}
	
	@Override
	public int getCount() {
		return sizes.size();
	}

	@Override
	public Integer getItem(int position) {
		return sizes.get(position);
	}

	@Override
	public long getItemId(int position) {return 0;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(parent.getContext());
		textView.setText(String.valueOf(getItem(position)));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getItem(position));
		textView.setGravity(Gravity.CENTER);
		return textView;
	}

}
