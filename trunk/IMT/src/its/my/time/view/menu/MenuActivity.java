package its.my.time.view.menu;

import its.my.time.R;
import its.my.time.view.menu.ELVAdapter.MenuChildViewHolder;
import its.my.time.view.menu.ELVAdapter.OnItemSwitchedListener;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.fonts.mooncake.MooncakeIcone;

public abstract class MenuActivity extends SherlockFragmentActivity implements OnClickListener {

	private static final long ANIMATION_DURATION = 200;
	private RelativeLayout mMainContent;
	private ExpandableListView mMainMenu;
	private View mBlanck;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		overridePendingTransition(R.anim.entry_in, R.anim.entry_out);
		super.setContentView(R.layout.activity_base);

		initialiseActionBar();

		this.mMainContent = (RelativeLayout) findViewById(R.id.content);
		mBlanck = new View(this);
		mBlanck.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mBlanck.setOnTouchListener(onMenuShowedTouchListener);

		final Display display = getWindowManager().getDefaultDisplay();
		this.mMainContent.getLayoutParams().width = display.getWidth();
		this.mMainContent.invalidate();
		this.mMainMenu = (ExpandableListView) findViewById(R.id.main_menu);
		final TypedValue tempVal = new TypedValue();
		getResources().getValue(R.dimen.menu_weight, tempVal, true);
		final float weight = tempVal.getFloat();
		this.mMainMenuWidth = (int) (display.getWidth() * weight);
		this.mMainMenu.getLayoutParams().width = this.mMainMenuWidth;
		this.mMainMenu.invalidate();
	}

	@Override
	protected void onResume() {
		initialiseMenu();
		super.onResume();
	}


	protected void initialiseActionBar() {
		final ActionBar mActionBar = getSupportActionBar();
		mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_header));
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		final MooncakeIcone icone = new MooncakeIcone(MenuActivity.this);
		icone.setTextSize(50);
		icone.setIconeRes(MooncakeIcone.icon_list);
		icone.setTextColor(getResources().getColor(R.color.grey));
		mActionBar.setLogo(icone.getIconeDrawable());
	}

	private static boolean isMenuShowed;
	private int mMainMenuWidth;
	private ArrayList<MenuGroupe> menuGroupes;
	private final OnChildClickListener onMenuChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(final ExpandableListView parent,final View v, final int groupPosition, final int childPosition,final long id) {
			if (!MenuActivity.this.menuGroupes.get(groupPosition).getObjets().get(childPosition).isSwitcher()) {
				MenuActivity.this.hasSwitcherValueChanged = false;
				changeMainMenuVisibility(false, true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						onMenuChildClick(parent, menuAdapter.getGroup(groupPosition), menuAdapter.getChild(groupPosition, childPosition), id);
					}
				}, (long) (ANIMATION_DURATION * 1.5));
			} else {
				((MenuChildViewHolder) v.getTag()).childSwitcher.toggleState(true);
			}
			return false;
		}
	};
	private final OnGroupClickListener onMenuGroupClickListener = new OnGroupClickListener() {
		@Override
		public boolean onGroupClick(final ExpandableListView parent,
				final View v, final int groupPosition, final long id) {
			if (MenuActivity.this.menuGroupes.get(groupPosition).getObjets().size() <= 0) {
				MenuActivity.this.hasSwitcherValueChanged = false;
				changeMainMenuVisibility(false, true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (groupPosition < MenuActivity.this.menuGroupes.size()) {
							onMenuGroupClick(parent, menuAdapter.getGroup(groupPosition), id);
						}
					}
				}, (long) (ANIMATION_DURATION * 1.5));
			} else if (MenuActivity.this.menuGroupes.get(groupPosition).isSwitcher()) {
				((MenuChildViewHolder) v.getTag()).childSwitcher.toggleState(true);
			}
			return false;
		}
	};

	private final OnGroupExpandListener onGroupExpandListener = new OnGroupExpandListener() {
		@Override
		public void onGroupExpand(int groupPosition) {
			for (int i = 0; i < MenuActivity.this.menuGroupes.size(); i++) {
				if (i != groupPosition) {
					MenuActivity.this.mMainMenu.collapseGroup(i);
				}
			}
		}
	};
	private final AnimationListener menuAnimationListener = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
			MenuActivity.this.mMainMenu.setVisibility(View.VISIBLE);
			MenuActivity.this.mMainContent.bringToFront();
			if(isMenuShowed) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mMainContent.getApplicationWindowToken(), 0);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (isMenuShowed) {
				MenuActivity.this.mMainMenu.bringToFront();
				mBlanck.setVisibility(View.VISIBLE);
				mBlanck.bringToFront();
			} else {
				MenuActivity.this.mMainContent.bringToFront();
				mBlanck.setVisibility(View.GONE);
			}
			if (MenuActivity.this.hasSwitcherValueChanged) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						reload();
					}
				}, (long) (ANIMATION_DURATION * 1.5));
				MenuActivity.this.hasSwitcherValueChanged = false;
			}
		}
	};
	private ELVAdapter menuAdapter;

	public void initialiseMenu() {
		this.menuGroupes = new ArrayList<MenuGroupe>();
		this.menuGroupes = onCreateMenu(this.menuGroupes);
		if (this.menuGroupes == null) {
			this.menuGroupes = new ArrayList<MenuGroupe>();
		}


		menuAdapter = new ELVAdapter(this, this.menuGroupes);
		menuAdapter.setOnItemSwitchedListener(new OnItemSwitchedListener() {
			@Override
			public void onObjetSwitched(View v, int positionGroup,int positionObjet, boolean isChecked) {
				MenuActivity.this.hasSwitcherValueChanged = true;
				onMenuChildSwitch(menuAdapter.getGroup(positionGroup), menuAdapter.getChild(positionGroup, positionObjet), isChecked);
			}

			@Override
			public void onGroupSwitched(View v, int positionGroup,boolean isChecked) {
				MenuActivity.this.hasSwitcherValueChanged = true;
				onMenuGroupSwitch(menuAdapter.getGroup(positionGroup), isChecked);
			}
		});
		this.mMainMenu.setAdapter(menuAdapter);
		this.mMainMenu.setGroupIndicator(null);
		this.mMainMenu.setOnChildClickListener(this.onMenuChildClickListener);
		this.mMainMenu.setOnGroupClickListener(this.onMenuGroupClickListener);
		this.mMainMenu.setOnGroupExpandListener(this.onGroupExpandListener);
	}
	/**
	 * 
	 * @param groupPosition le groupe actif
	 * @param itemPosition l'item actif, <b>OU -1 !! </b>
	 */
	public void setCurrentItemMenu(int groupPosition, int childPosition) {
		if (childPosition == -1) {
			this.mMainMenu.setSelectedGroup(groupPosition);
		} else {
			this.mMainMenu.setSelectedChild(groupPosition, childPosition, true);
		}
	}


	public void changeMainMenuVisibility(boolean showed, boolean withAnimation) {
		isMenuShowed = showed;
		Animation animMainMenu;
		if (showed) {
			animMainMenu = new TranslateAnimation(0, this.mMainMenuWidth, 0, 0);
		} else {
			animMainMenu = new TranslateAnimation(this.mMainMenuWidth, 0, 0, 0);
		}
		animMainMenu.setFillAfter(true);
		if (withAnimation) {
			animMainMenu.setDuration(ANIMATION_DURATION);
		} else {
			animMainMenu.setDuration(1);
		}
		animMainMenu.setAnimationListener(this.menuAnimationListener);
		this.mMainContent.startAnimation(animMainMenu);
	}

	private OnTouchListener onMenuShowedTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				changeMainMenuVisibility(false, true);
			}
			return true;
		}
	};


	protected abstract void onMenuGroupSwitch(MenuGroupe group,boolean isChecked);
	protected abstract void onMenuChildSwitch(MenuGroupe group, MenuObjet objet, boolean isChecked);
	protected abstract void onMenuGroupClick(ExpandableListView parent, MenuGroupe group, long id);
	protected abstract void onMenuChildClick(ExpandableListView parent, MenuGroupe group,  MenuObjet objet, long id);



	protected abstract ArrayList<MenuGroupe> onCreateMenu(ArrayList<MenuGroupe> menuGroupes);
	protected abstract void reload();

	private boolean hasSwitcherValueChanged;


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
		this.setContentView(view,null);
	}

	@Override
	public void setContentView(int layoutResId) {
		this.setContentView(getLayoutInflater().inflate(layoutResId, null), null);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		this.mMainContent.removeAllViews();
		if(params != null) {
			this.mMainContent.addView(view, params);
		} else {
			this.mMainContent.addView(view);
		}
		mMainContent.addView(mBlanck);
		mBlanck.setVisibility(View.GONE);
	}

	protected abstract boolean onBackButtonPressed();

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean res = false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (isMenuShowed) {
				changeMainMenuVisibility(false, true);
				return true;
			}
			res = onBackButtonPressed();
			break;
		case KeyEvent.KEYCODE_MENU:
			changeMainMenuVisibility(!isMenuShowed, true);
			res = true;
			break;
		}
		if (res == false) {
			return super.onKeyUp(keyCode, event);
		}
		return true;
	}

}
