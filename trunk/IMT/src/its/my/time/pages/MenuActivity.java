package its.my.time.pages;

import fonts.mooncake.MooncakeIcone;
import its.my.time.R;
import its.my.time.view.menu.ELVAdapter;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public abstract class MenuActivity extends SherlockFragmentActivity implements OnClickListener{

	private FrameLayout mMainContent;
	private ExpandableListView mMainMenu;

	private View mMainLayout;

	private boolean isMenuShowed = false;
	private int mMainMenuWidth;
	private ArrayList<MenuGroupe> menuGroupes;
	private OnChildClickListener onMenuChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			onMenuChildClick(parent, v, groupPosition, childPosition, id);
			return false;
		}
	};

	private OnGroupClickListener onMenuGroupClickListener = new OnGroupClickListener() {
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
			onMenuGroupClick(parent, v, groupPosition, id);
			return false;
		}
	};


	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		super.setContentView(R.layout.activity_base);

		mMainContent = (FrameLayout) findViewById(R.id.content);

		Display display = getWindowManager().getDefaultDisplay();
		mMainContent.getLayoutParams().width = display.getWidth();
		mMainContent.invalidate();
		mMainMenu = (ExpandableListView) findViewById(R.id.main_menu);
		TypedValue tempVal = new TypedValue();
		getResources().getValue(R.dimen.menu_weight, tempVal, true);
		float weight = tempVal.getFloat();
		mMainMenuWidth = (int) (display.getWidth() * weight);
		mMainMenu.getLayoutParams().width = mMainMenuWidth;
		mMainMenu.invalidate();

		mMainLayout = findViewById(R.id.main_layout);

		initialiseMenu();
	}

	public void initialiseMenu() {
		menuGroupes = new ArrayList<MenuGroupe>();

		menuGroupes = onMainMenuCreated(menuGroupes);

		ELVAdapter adapter = new ELVAdapter(this, menuGroupes);
		mMainMenu.setAdapter(adapter);
		mMainMenu.setGroupIndicator(null);
		mMainMenu.setOnChildClickListener(onMenuChildClickListener);
		mMainMenu.setOnGroupClickListener(onMenuGroupClickListener);
	}

	protected abstract void onMenuGroupClick(ExpandableListView parent, View v, int groupPosition, long id);
	protected abstract void onMenuChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id);

	protected abstract ArrayList<MenuGroupe> onMainMenuCreated(ArrayList<MenuGroupe> menuGroupes);

	protected void initialiseActionBar() {
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_header));
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		MooncakeIcone icone = new MooncakeIcone(MenuActivity.this, MooncakeIcone.icon_list, 100);
		icone.setTextColor(getResources().getColor(R.color.light_grey));
		mActionBar.setLogo(icone.getIconeDrawable());
	}
	

	private AnimationListener menuAnimationListener = new AnimationListener() {
		@Override public void onAnimationStart(Animation animation) {
			mMainMenu.setVisibility(View.VISIBLE);
			mMainContent.bringToFront();
		}
		@Override public void onAnimationRepeat(Animation animation) {}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			if(isMenuShowed) {
				mMainMenu.bringToFront();	
			} else {
				mMainContent.bringToFront();
			}
		}
	};
	public void changeMainMenuVisibility(boolean showed, boolean withAnimation) {
		isMenuShowed = showed;
		Animation animMainMenu;
		if (showed) {
			animMainMenu = new TranslateAnimation(0, mMainMenuWidth, 0, 0);
		} else {
			animMainMenu = new TranslateAnimation(mMainMenuWidth, 0, 0, 0);
		}
		animMainMenu.setFillAfter(true);
		if (withAnimation) {
			animMainMenu.setDuration(200);
		} else {
			animMainMenu.setDuration(1);
		}
        animMainMenu.setAnimationListener(menuAnimationListener );
        mMainContent.startAnimation(animMainMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (isMenuShowed == false) {
				changeMainMenuVisibility(true, true);
			} else {
				changeMainMenuVisibility(false, true);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setContentView(View view) {
		mMainContent.removeAllViews();
		mMainContent.addView(view);
	}

	@Override
	public void setContentView(int layoutResId) {
		mMainContent.removeAllViews();
		View view = getLayoutInflater().inflate(layoutResId, null);
		setContentView(view);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		mMainContent.removeAllViews();
		mMainContent.addView(view, params);
	}

	@Override
	public void onClick(View v) {

	}
}
