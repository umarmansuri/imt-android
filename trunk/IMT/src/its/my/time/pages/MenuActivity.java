package its.my.time.pages;

import its.my.time.R;
import its.my.time.util.ActivityUtil;
import its.my.time.view.ControledViewPager;
import its.my.time.view.menu.ELVAdapter;
import its.my.time.view.menu.ELVAdapter.MenuChildViewHolder;
import its.my.time.view.menu.ELVAdapter.OnItemSwitchedListener;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.fonts.mooncake.MooncakeIcone;

public abstract class MenuActivity extends SherlockFragmentActivity implements
OnClickListener {

	private static final long ANIMATION_DURATION = 200;
	private FrameLayout mMainContent;
	private ExpandableListView mMainMenu;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		registerReceiver(this.LOGOUT_Receiver, new IntentFilter(ActivityUtil.ACTION_FINISH));

		overridePendingTransition(R.anim.entry_in, R.anim.entry_out);
		super.setContentView(R.layout.activity_base);

		initialiseActionBar();

		this.mMainContent = (FrameLayout) findViewById(R.id.content);

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


	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(this.LOGOUT_Receiver);
		} catch (Exception e) {}
		super.onDestroy();
	}

	protected void initialiseActionBar() {
		final ActionBar mActionBar = getSupportActionBar();
		mActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_header));
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		final MooncakeIcone icone = new MooncakeIcone(MenuActivity.this);
		icone.setTextSize(50);
		icone.setIconeRes(MooncakeIcone.icon_list);
		icone.setTextColor(getResources().getColor(R.color.grey));
		mActionBar.setLogo(icone.getIconeDrawable());
	}

	private boolean isMenuShowed = false;
	private int mMainMenuWidth;
	private ArrayList<MenuGroupe> menuGroupes;
	private final OnChildClickListener onMenuChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(final ExpandableListView parent,final View v, final int groupPosition, final int childPosition,final long id) {
			if (!MenuActivity.this.menuGroupes.get(groupPosition).getObjets()
					.get(childPosition).isSwitcher()) {
				MenuActivity.this.hasSwitcherValueChanged = false;
				changeMainMenuVisibility(false, true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (groupPosition < MenuActivity.this.menuGroupes.size() - 2) {
							onMenuChildClick(parent, v, groupPosition,childPosition, id);
						} else {
							// TODO si un menu 'constant' contient de enfants
						}
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
			if (MenuActivity.this.menuGroupes.get(groupPosition).getObjets()
					.size() <= 0) {
				MenuActivity.this.hasSwitcherValueChanged = false;
				changeMainMenuVisibility(false, true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (groupPosition < MenuActivity.this.menuGroupes
								.size() - 2) {
							onMenuGroupClick(parent, v, groupPosition, id);
						} else {
							if (groupPosition == MenuActivity.this.menuGroupes
									.size() - 2) {
								Toast.makeText(MenuActivity.this, "A propos",
										Toast.LENGTH_SHORT).show();
							} else if (groupPosition == MenuActivity.this.menuGroupes
									.size() - 1) {
								ActivityUtil.logout(MenuActivity.this);
							}
						}
					}
				}, (long) (ANIMATION_DURATION * 1.5));
			} else if (MenuActivity.this.menuGroupes.get(groupPosition)
					.isSwitcher()) {
				((MenuChildViewHolder) v.getTag()).childSwitcher
				.toggleState(true);
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
			if(MenuActivity.this.isMenuShowed) {
				  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		          imm.hideSoftInputFromWindow(mMainContent.getApplicationWindowToken(), 0);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (MenuActivity.this.isMenuShowed) {
				MenuActivity.this.mMainMenu.bringToFront();
			} else {
				MenuActivity.this.mMainContent.bringToFront();
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

	public void initialiseMenu() {
		this.menuGroupes = new ArrayList<MenuGroupe>();
		this.menuGroupes = onMainMenuCreated(this.menuGroupes);
		if (this.menuGroupes == null) {
			this.menuGroupes = new ArrayList<MenuGroupe>();
		}
		MenuGroupe menuGroupe = new MenuGroupe("A propos",
				MooncakeIcone.icon_info_sign);
		this.menuGroupes.add(menuGroupe);
		menuGroupe = new MenuGroupe("D�connexion", MooncakeIcone.icon_off);
		this.menuGroupes.add(menuGroupe);

		final ELVAdapter adapter = new ELVAdapter(this, this.menuGroupes);
		adapter.setOnItemSwitchedListener(new OnItemSwitchedListener() {
			@Override
			public void onObjetSwitched(View v, int positionGroup,
					int positionObjet, boolean isChecked) {
				MenuActivity.this.hasSwitcherValueChanged = true;
				onMenuItemSwitch(v, positionGroup, positionObjet, isChecked);
			}

			@Override
			public void onGroupSwitched(View v, int positionGroup,
					boolean isChecked) {
				MenuActivity.this.hasSwitcherValueChanged = true;
				onMenuGroupSwitch(v, positionGroup, isChecked);
			}
		});
		this.mMainMenu.setAdapter(adapter);
		this.mMainMenu.setGroupIndicator(null);
		this.mMainMenu.setOnChildClickListener(this.onMenuChildClickListener);
		this.mMainMenu.setOnGroupClickListener(this.onMenuGroupClickListener);
		this.mMainMenu.setOnGroupExpandListener(this.onGroupExpandListener);
	}

	/**
	 * 
	 * @param groupPosition
	 *            le groupe actif
	 * @param itemPosition
	 *            l'item actif, <b>OU -1 !! </b>
	 */
	public void setCurrentItemMenu(int groupPosition, int childPosition) {
		if (childPosition == -1) {
			this.mMainMenu.setSelectedGroup(groupPosition);
		} else {
			this.mMainMenu.setSelectedChild(groupPosition, childPosition, true);
		}
	}

	public void changeMainMenuVisibility(boolean showed, boolean withAnimation) {
		this.isMenuShowed = showed;
		Animation animMainMenu;
		if (showed) {
			animMainMenu = new TranslateAnimation(0, this.mMainMenuWidth, 0, 0);
			enableDisableViewGroup(this.mMainContent, false);
		} else {
			animMainMenu = new TranslateAnimation(this.mMainMenuWidth, 0, 0, 0);
			enableDisableViewGroup(this.mMainContent, true);
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

	OnTouchListener onMenuShowedTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				changeMainMenuVisibility(false, true);
				return true;
			}
			return true;
		}
	};

	public void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
		final int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View view = viewGroup.getChildAt(i);
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, enabled);
			}
			if (view instanceof ControledViewPager) {
				((ControledViewPager) view).setPagingEnabled(enabled);
			}
			if (enabled) {
				view.setOnTouchListener(null);
			} else {
				view.setOnTouchListener(this.onMenuShowedTouchListener);
			}
		}
	}

	protected abstract void onMenuGroupSwitch(View v, int positionGroup,
			boolean isChecked);

	protected abstract void onMenuItemSwitch(View v, int positionGroup,
			int positionObjet, boolean isChecked);

	protected abstract void onMenuGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id);

	protected abstract void onMenuChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id);

	protected abstract ArrayList<MenuGroupe> onMainMenuCreated(
			ArrayList<MenuGroupe> menuGroupes);

	private boolean hasSwitcherValueChanged;

	public abstract void reload();

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (this.isMenuShowed == false) {
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
		this.mMainContent.removeAllViews();
		this.mMainContent.addView(view);
	}

	@Override
	public void setContentView(int layoutResId) {
		this.mMainContent.removeAllViews();
		final View view = getLayoutInflater().inflate(layoutResId, null);
		setContentView(view);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		this.mMainContent.removeAllViews();
		this.mMainContent.addView(view, params);
	}

	protected abstract boolean onBackButtonPressed();

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean res = false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (this.isMenuShowed) {
				changeMainMenuVisibility(false, true);
				return true;
			}
			res = onBackButtonPressed();
			break;
		case KeyEvent.KEYCODE_MENU:
			changeMainMenuVisibility(!this.isMenuShowed, true);
			res = true;
			break;
		}
		if (res == false) {
			return super.onKeyUp(keyCode, event);
		}
		return true;
	}

	private final BroadcastReceiver LOGOUT_Receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			final String action_finish = arg1.getStringExtra("FINISH");

			if (action_finish.equalsIgnoreCase("ACTION.FINISH.LOGOUT")) {
				finish();
				// this line unregister the receiver,so that i run only one time
				// and when next time logout is clicked then again it called
				unregisterReceiver(MenuActivity.this.LOGOUT_Receiver);
			}
		}
	};
}