package its.my.time.pages;

import its.my.time.R;
import its.my.time.view.ControledViewPager;
import its.my.time.view.menu.ELVAdapter;
import its.my.time.view.menu.ELVAdapter.MenuChildViewHolder;
import its.my.time.view.menu.ELVAdapter.OnItemSwitchedListener;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.fonts.mooncake.MooncakeIcone;

public abstract class MenuActivity extends SherlockFragmentActivity implements OnClickListener{

	private static final long ANIMATION_DURATION = 200;
	private FrameLayout mMainContent;
	private ExpandableListView mMainMenu;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.entry_in, R.anim.entry_out);
		super.setContentView(R.layout.activity_base);


		initialiseActionBar();

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
	}
	
	@Override
	protected void onResume() {
		initialiseMenu();
		super.onResume();
	}

	protected void initialiseActionBar() {
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_header));
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		MooncakeIcone icone = new MooncakeIcone(MenuActivity.this);
		icone.setTextSize(50);
		icone.setIconeRes(MooncakeIcone.icon_list);
		icone.setTextColor(getResources().getColor(R.color.grey));
		mActionBar.setLogo(icone.getIconeDrawable());
	}

	private boolean isMenuShowed = false;
	private int mMainMenuWidth;
	private ArrayList<MenuGroupe> menuGroupes;
	private OnChildClickListener onMenuChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition, final int childPosition, final long id) {
			if(!menuGroupes.get(groupPosition).getObjets().get(childPosition).isSwitcher()) {
				hasSwitcherValueChanged = false;
				changeMainMenuVisibility(false, true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						onMenuChildClick(parent, v, groupPosition, childPosition, id);	
					}
				}, (long) (ANIMATION_DURATION*1.5));
			} else {
				((MenuChildViewHolder)v.getTag()).childSwitcher.toggleState(true);
			}
			return false;
		}
	};
	private OnGroupClickListener onMenuGroupClickListener = new OnGroupClickListener() {
		@Override
		public boolean onGroupClick(final ExpandableListView parent, final View v, final int groupPosition, final long id) {
			if(menuGroupes.get(groupPosition).getObjets().size() <= 0) {
				hasSwitcherValueChanged = false;
				changeMainMenuVisibility(false, true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						onMenuGroupClick(parent, v, groupPosition, id);	
					}
				}, (long) (ANIMATION_DURATION*1.5));			
			} else if(menuGroupes.get(groupPosition).isSwitcher()) {
				((MenuChildViewHolder)v.getTag()).childSwitcher.toggleState(true);
			}
			return false;
		}
	};

	private OnGroupExpandListener onGroupExpandListener = new OnGroupExpandListener() {
		@Override
		public void onGroupExpand(int groupPosition) {
			for (int i = 0; i < menuGroupes.size(); i++) {
				if (i != groupPosition) {
					mMainMenu.collapseGroup(i);
				}
			}	
		}
	};
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
			if(hasSwitcherValueChanged) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						reload();
					}
				}, (long) (ANIMATION_DURATION*1.5));
				hasSwitcherValueChanged = false;
			}
		}
	};

	public void initialiseMenu() {
		menuGroupes = new ArrayList<MenuGroupe>();
		menuGroupes = onMainMenuCreated(menuGroupes);
		ELVAdapter adapter = new ELVAdapter(this, menuGroupes);
		adapter.setOnItemSwitchedListener(new OnItemSwitchedListener() {
			@Override
			public void onObjetSwitched(View v, int positionGroup, int positionObjet, boolean isChecked) {
				hasSwitcherValueChanged = true;
				onMenuItemSwitch(v, positionGroup, positionObjet, isChecked);
			}

			@Override
			public void onGroupSwitched(View v, int positionGroup, boolean isChecked) {
				hasSwitcherValueChanged = true;
				onMenuGroupSwitch(v, positionGroup, isChecked);
			}
		});
		mMainMenu.setAdapter(adapter);
		mMainMenu.setGroupIndicator(null);
		mMainMenu.setOnChildClickListener(onMenuChildClickListener);
		mMainMenu.setOnGroupClickListener(onMenuGroupClickListener);
		mMainMenu.setOnGroupExpandListener(onGroupExpandListener );
	}

	/**
	 * 
	 * @param groupPosition le groupe actif
	 * @param itemPosition l'item actif, <b>OU -1 !! </b>
	 */
	public void setCurrentItemMenu(int groupPosition, int childPosition) {
		if(childPosition ==-1) {
			mMainMenu.setSelectedGroup(groupPosition);	
		} else {
			mMainMenu.setSelectedChild(groupPosition, childPosition, true);
		}
	}

	public void changeMainMenuVisibility(boolean showed, boolean withAnimation) {
		isMenuShowed = showed;
		Animation animMainMenu;
		if (showed) {
			animMainMenu = new TranslateAnimation(0, mMainMenuWidth, 0, 0);
			enableDisableViewGroup(mMainContent, false);
		} else {
			animMainMenu = new TranslateAnimation(mMainMenuWidth, 0, 0, 0);
			enableDisableViewGroup(mMainContent, true);
		}
		animMainMenu.setFillAfter(true);
		if (withAnimation) {
			animMainMenu.setDuration(ANIMATION_DURATION);
		} else {
			animMainMenu.setDuration(1);
		}
		animMainMenu.setAnimationListener(menuAnimationListener);
		mMainContent.startAnimation(animMainMenu);
	}

	OnTouchListener onMenuShowedTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_UP) {
				changeMainMenuVisibility(false, true);
				return true;
			}
			return true;
		}
	};

	public void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
		int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = viewGroup.getChildAt(i);
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, enabled);
			}
			if (view instanceof ControledViewPager) {
				((ControledViewPager)view).setPagingEnabled(enabled);
			}
			if(enabled) {
				view.setOnTouchListener(null);
			} else {
				view.setOnTouchListener(onMenuShowedTouchListener);	
			}
		}
	}

	protected abstract void onMenuGroupSwitch(View v, int positionGroup,boolean isChecked);
	protected abstract void onMenuItemSwitch(View v, int positionGroup,int positionObjet, boolean isChecked);
	protected abstract void onMenuGroupClick(ExpandableListView parent, View v, int groupPosition, long id);
	protected abstract void onMenuChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id);
	protected abstract ArrayList<MenuGroupe> onMainMenuCreated(ArrayList<MenuGroupe> menuGroupes);

	private boolean hasSwitcherValueChanged;
	public abstract void reload();

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
	public void onClick(View v) {

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

	protected abstract boolean onBackButtonPressed();
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(isMenuShowed) {
				changeMainMenuVisibility(false, true);
				return true;
			}
			return onBackButtonPressed();
		case KeyEvent.KEYCODE_MENU:
			changeMainMenuVisibility(!isMenuShowed, true);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
