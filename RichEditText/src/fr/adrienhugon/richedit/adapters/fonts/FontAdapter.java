package fr.adrienhugon.richedit.adapters.fonts;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FontAdapter extends BaseAdapter{

	private ArrayList<Font> fonts;

	public FontAdapter() {
		fonts = new ArrayList<Font>();
		fonts.add(new Font("Arial", "arial.ttf"));
		fonts.add(new Font("Courrier New", "courrier_new.ttf"));
		fonts.add(new Font("Georgia", "georgia.ttf"));
		fonts.add(new Font("Tahoma", "tahoma.ttf"));
		fonts.add(new Font("Trebuchet MS", "trebuchet_ms.ttf"));
		fonts.add(new Font("Verdana", "verdana.ttf"));
	}

	@Override
	public int getCount() {
		return fonts.size();
	}

	@Override
	public Font getItem(int position) {
		return fonts.get(position);
	}

	@Override
	public long getItemId(int position) {return 0;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Font font = getItem(position);
		TextView textView = new TextView(parent.getContext());
		textView.setText(font.getName());
		Typeface type = Typeface.createFromAsset(parent.getContext().getAssets(),Font.FOLDER + font.getFile()); 
		textView.setTypeface(type);
		textView.setGravity(Gravity.CENTER);
		return textView;
	}
}
