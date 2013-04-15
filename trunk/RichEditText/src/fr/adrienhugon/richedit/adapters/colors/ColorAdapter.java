package fr.adrienhugon.richedit.adapters.colors;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ColorAdapter extends BaseAdapter{

	private ArrayList<Integer> colors;

	public ColorAdapter() {
		colors = new ArrayList<Integer>();
		colors.add(Color.BLACK);
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		colors.add(Color.MAGENTA);
		colors.add(Color.YELLOW);
	}
	
	@Override
	public int getCount() {
		return colors.size();
	}

	@Override
	public Integer getItem(int position) {
		return colors.get(position);
	}

	@Override
	public long getItemId(int position) {return 0;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(parent.getContext());
		textView.setText("              ");
		textView.setBackgroundColor(getItem(position));
		return textView;
	}

}
