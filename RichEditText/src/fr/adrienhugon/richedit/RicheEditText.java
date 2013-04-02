package fr.adrienhugon.richedit;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import fr.adrienhugon.richedit.CustomEditText.OnSelectionChangedListener;
import fr.adrienhugon.richedit.adapters.colors.ColorAdapter;
import fr.adrienhugon.richedit.adapters.fonts.CustomTypefaceSpan;
import fr.adrienhugon.richedit.adapters.fonts.Font;
import fr.adrienhugon.richedit.adapters.fonts.FontAdapter;
import fr.adrienhugon.richedit.adapters.gravities.GravityAdapter;
import fr.adrienhugon.richedit.adapters.size.SizeAdapter;

public class RicheEditText extends FrameLayout implements OnItemSelectedListener, OnClickListener{

	private CustomEditText mEditText;

	private Spinner mSpinnerFont;
	private Spinner mSpinnerSize;
	private Spinner mSpinnerColor;
	private Spinner mSpinnerGravity;

	private ImageButton mButtonBold;
	private ImageButton mButtonItalic;
	private ImageButton mButtonUnderline;
	private ImageButton mButtonOl;
	private ImageButton mButtonUl;

	private boolean isBold = false;
	private boolean isItalic = false;
	private boolean isUnderline = false;
	private boolean isOl = false;
	private boolean isUl = false;

	private String currentFont = null;
	private int currentSize = -1;
	private int currentColor = -1;

	private FontAdapter fontAdapter;

	private SizeAdapter sizeAdapter;

	private ColorAdapter colorAdapter;

	private GravityAdapter gravityAdapter;

	private final static String BULLET = "\n\t" + Html.fromHtml("&#8226;") + " ";

	public RicheEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RicheEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RicheEditText(Context context) {
		super(context);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.rich_edit_text, this);
		mEditText = (CustomEditText)findViewById(R.id.text);
		mEditText.setOnSelectionChangedListener(new OnSelectionChangedListener() {
			@Override public void onSelectionChanged(Editable text, int start, int end) {
				CharacterStyle[] spans = text.getSpans(start-1, end-1, CharacterStyle.class);
				isBold = false;
				isItalic = false;
				isUnderline = false;
				for (CharacterStyle characterSpan : spans) {
					if(characterSpan instanceof StyleSpan) {
						if(((StyleSpan)characterSpan).getStyle() == Typeface.BOLD) {
							isBold = true;
						} else if(((StyleSpan)characterSpan).getStyle() == Typeface.ITALIC) {
							isItalic = true;
						}
					} else if( characterSpan instanceof UnderlineSpan) {
						if(underlineSpans.contains(characterSpan)) {
							isUnderline = true;
						}
					}
				}
				updateButtons();
			}
		});

		//mEditText.addTextChangedListener(this);

		mSpinnerFont = (Spinner)findViewById(R.id.font);
		fontAdapter = new FontAdapter();
		mSpinnerFont.setAdapter(fontAdapter);
		mSpinnerFont.setOnItemSelectedListener(this);

		mSpinnerSize = (Spinner)findViewById(R.id.size);
		sizeAdapter = new SizeAdapter();
		mSpinnerSize.setAdapter(sizeAdapter);
		mSpinnerSize.setOnItemSelectedListener(this);

		mSpinnerColor = (Spinner)findViewById(R.id.color);
		colorAdapter = new ColorAdapter();
		mSpinnerColor.setAdapter(colorAdapter);
		mSpinnerColor.setOnItemSelectedListener(this);

		mSpinnerGravity = (Spinner)findViewById(R.id.gravity);
		gravityAdapter = new GravityAdapter();
		mSpinnerGravity.setAdapter(gravityAdapter);
		mSpinnerGravity.setOnItemSelectedListener(this);
		mSpinnerGravity.setVisibility(View.GONE);

		mButtonBold = (ImageButton)findViewById(R.id.bold);
		mButtonItalic = (ImageButton)findViewById(R.id.italic);
		mButtonUnderline = (ImageButton)findViewById(R.id.underline);
		mButtonOl = (ImageButton)findViewById(R.id.ol);
		mButtonUl = (ImageButton)findViewById(R.id.ul);

		mButtonBold.setOnClickListener(this);
		mButtonItalic.setOnClickListener(this);
		mButtonUnderline.setOnClickListener(this);
		mButtonOl.setOnClickListener(this);
		mButtonUl.setOnClickListener(this);

		//TODO enlever!
		mEditText.updateText("voici donc un petit exemple qu'un utilisateur pourrait entrer...!");

	}

	@Override
	public void onClick(View v) {
		int start = mEditText.getSelectionStart();
		int end = mEditText.getSelectionEnd();
		if(v.getId() == R.id.bold) {
			isBold = !isBold;
			checkBold(start, end);
		}
		if(v.getId() == R.id.italic){
			isItalic = !isItalic;
			checkItalic(start, end);
		}
		if(v.getId() == R.id.underline){
			isUnderline = !isUnderline;
			checkUnderline(start, end);
		}
		if(v.getId() == R.id.ol){
			isOl = !isOl;
		}
		if(v.getId() == R.id.ul){
			isUl = !isUl;
			checkUl(start, end);
		}
		updateButtons();

	}

	private void updateButtons() {
		mButtonBold.setImageResource(isBold ? R.drawable.bold_active : R.drawable.bold);
		mButtonItalic.setImageResource(isItalic ? R.drawable.italic_active : R.drawable.italic);
		mButtonUnderline.setImageResource(isUnderline ? R.drawable.underline_active : R.drawable.underline);
		mButtonOl.setImageResource(isOl ? R.drawable.ol_active : R.drawable.ol);
		mButtonUl.setImageResource(isUl ? R.drawable.ul_active: R.drawable.ul);
	}


	private void checkBold(int start, int end) {
		CharacterStyle[] spans = mEditText.getText().getSpans(start, end, CharacterStyle.class);
		boolean has = false;
		for (CharacterStyle characterSpan : spans) {
			if(characterSpan instanceof StyleSpan && ((StyleSpan)characterSpan).getStyle() == Typeface.BOLD && !has) {
				int spanStart = mEditText.getText().getSpanStart(characterSpan);
				int spanEnd = mEditText.getText().getSpanEnd(characterSpan);
				if(spanStart >= start || spanEnd <= end) {
					mEditText.getText().removeSpan(characterSpan);
				}
				has = true;
				splitStyleSpan(characterSpan, start, end, Typeface.BOLD, spanStart, spanEnd);
			}
		}
		if(!has && isBold) {mEditText.getText().setSpan(new StyleSpan(Typeface.BOLD), start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);}
	}

	private void checkItalic(int start, int end) {
		CharacterStyle[] spans = mEditText.getText().getSpans(start, end, CharacterStyle.class);
		boolean has = false;
		for (CharacterStyle characterSpan : spans) {
			if(characterSpan instanceof StyleSpan && ((StyleSpan)characterSpan).getStyle() == Typeface.ITALIC && !has) {
				int spanStart = mEditText.getText().getSpanStart(characterSpan);
				int spanEnd = mEditText.getText().getSpanEnd(characterSpan);
				if(spanStart >= start || spanEnd <= end) {
					mEditText.getText().removeSpan(characterSpan);
				}
				has = true;
				splitStyleSpan(characterSpan, start, end, Typeface.ITALIC, spanStart, spanEnd);
			}
		}
		if(!has && isItalic) {mEditText.getText().setSpan(new StyleSpan(Typeface.ITALIC), start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);}
	}

	private void checkUnderline(int start, int end) {
		CharacterStyle[] spans = mEditText.getText().getSpans(start, end, CharacterStyle.class);
		boolean has = false;
		for (CharacterStyle characterSpan : spans) {
			if(characterSpan instanceof UnderlineSpan && !has) {
				int spanStart = mEditText.getText().getSpanStart(characterSpan);
				int spanEnd = mEditText.getText().getSpanEnd(characterSpan);
				if(spanStart >= start || spanEnd <= end) {
					mEditText.getText().removeSpan(characterSpan);
				}
				has = true;
				splitUnderlineSpan(characterSpan, start, end, spanStart, spanEnd);
			}
		}
		if(!has && isUnderline) {addUnderlineSpan(start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);}
	}

	private void checkUl(int start, int end) {
		if(start < end) {
			String text = mEditText.getText().subSequence(start, end).toString();
			int index = text.indexOf("\n");
			while (index >= 0) {
				text = text.substring(0,index) + BULLET  + text.substring(index + 1, text.length());
				mEditText.setSelection(index + BULLET.length());
				index = text.indexOf("\n", index + 1);
			}
			mEditText.setText(mEditText.getText().replace(start, end, text));
		}
	}

	private ArrayList<UnderlineSpan> underlineSpans = new ArrayList<UnderlineSpan>();

	private void addUnderlineSpan(int start, int end, int flag) {
		UnderlineSpan span = new UnderlineSpan();
		underlineSpans.add(span);
		mEditText.getText().setSpan(span, start, end,flag);
	}

	private void splitUnderlineSpan(CharacterStyle styleSpan, int start, int end, int spanStart, int spanEnd) {
		if(spanStart <= start && start > 0) {
			if(!underlineSpans.contains(styleSpan)) {
				underlineSpans.add((UnderlineSpan) styleSpan);
			}
			mEditText.getText().setSpan(styleSpan, spanStart, start, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		if(spanEnd > end) {
			addUnderlineSpan(end, spanEnd, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
		}
	}

	private void splitStyleSpan(CharacterStyle styleSpan, int start, int end, int type, int spanStart, int spanEnd) {
		if(spanStart < start && start > 0) {
			mEditText.getText().setSpan(styleSpan, spanStart, start, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		if(end < spanEnd && end < mEditText.getText().length() - 1) {
			mEditText.getText().setSpan(new StyleSpan(type), end, spanEnd, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		int start = mEditText.getSelectionStart();
		int end = mEditText.getSelectionEnd();
		if(adapterView == mSpinnerFont) {
			checkFont(start,end,fontAdapter.getItem(position));
		} else if(adapterView == mSpinnerSize) {
			checkSize(start,end,sizeAdapter.getItem(position));
		} else if(adapterView == mSpinnerGravity) {

		} else if(adapterView == mSpinnerColor) {
			checkColor(start,end,colorAdapter.getItem(position));
		}
	}

	@Override public void onNothingSelected(AdapterView<?> paramAdapterView) {}


	private void checkFont(int start, int end, Font item) {
		CustomTypefaceSpan[] spans = mEditText.getText().getSpans(start, end, CustomTypefaceSpan.class);
		boolean has = false;
		for (CustomTypefaceSpan CustomTypefaceSpan : spans) {
			if(CustomTypefaceSpan.getFamily() == item.getName() && !has) {
				int spanStart = mEditText.getText().getSpanStart(CustomTypefaceSpan);
				int spanEnd = mEditText.getText().getSpanEnd(CustomTypefaceSpan);
				if(spanStart >= start || spanEnd <= end) {
					mEditText.getText().removeSpan(CustomTypefaceSpan);
				}
				has = true;
				splitStyleSpan(CustomTypefaceSpan, start, end, Typeface.BOLD, spanStart, spanEnd);
			}
		}
		if(!has && currentFont != item.getName()) {
			try{
				mEditText.getText().setSpan(new CustomTypefaceSpan(item.getName(), Typeface.createFromAsset(getContext().getAssets(), Font.FOLDER + item.getFile())) , start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
			} catch (Exception e) {e.printStackTrace();}
			currentFont = item.getName();
		}
	}

	private void checkSize(int start, int end, int size) {
		AbsoluteSizeSpan [] spans = mEditText.getText().getSpans(start, end, AbsoluteSizeSpan.class);
		boolean has = false;
		for (AbsoluteSizeSpan  absoluteSizeSpan : spans) {
			if(absoluteSizeSpan.getSize() == size && !has) {
				int spanStart = mEditText.getText().getSpanStart(absoluteSizeSpan);
				int spanEnd = mEditText.getText().getSpanEnd(absoluteSizeSpan);
				if(spanStart >= start || spanEnd <= end) {
					mEditText.getText().removeSpan(absoluteSizeSpan);
				}
				has = true;
				splitStyleSpan(absoluteSizeSpan, start, end, Typeface.BOLD, spanStart, spanEnd);
			}
		}
		if(!has && currentSize != size) {
			mEditText.getText().setSpan(new AbsoluteSizeSpan(size, true) , start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
			currentSize = size;
		}
	}

	private void checkColor(int start, int end, int color) {
		ForegroundColorSpan [] spans = mEditText.getText().getSpans(start, end, ForegroundColorSpan.class);
		boolean has = false;
		for (ForegroundColorSpan  foregroundColorSpan : spans) {
			if(foregroundColorSpan.getForegroundColor() == color && !has) {
				int spanStart = mEditText.getText().getSpanStart(foregroundColorSpan);
				int spanEnd = mEditText.getText().getSpanEnd(foregroundColorSpan);
				if(spanStart >= start || spanEnd <= end) {
					mEditText.getText().removeSpan(foregroundColorSpan);
				}
				has = true;
				splitStyleSpan(foregroundColorSpan, start, end, Typeface.BOLD, spanStart, spanEnd);
			}
		}
		if(!has && currentColor != color) {
			mEditText.getText().setSpan(new ForegroundColorSpan(color) , start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
			currentColor = color;
		}
	}
}
