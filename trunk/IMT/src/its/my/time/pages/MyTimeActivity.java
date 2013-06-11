package its.my.time.pages;

import its.my.time.R;
import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.data.ws.Callback;
import its.my.time.data.ws.WSManager;
import its.my.time.util.ActivityUtil;
import its.my.time.view.menu.MenuActivity;
import its.my.time.view.menu.MenuGroupe;
import its.my.time.view.menu.MenuObjet;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.fonts.mooncake.MooncakeIcone;

public abstract class MyTimeActivity extends MenuActivity implements OnMenuItemClickListener{

	private final BroadcastReceiver LOGOUT_Receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			final String action_finish = arg1.getStringExtra("FINISH");

			if (action_finish.equalsIgnoreCase("ACTION.FINISH.LOGOUT")) {
				deleteDatabase(DatabaseHandler.DATABASE_NAME);
				finish();
				unregisterReceiver(MyTimeActivity.this.LOGOUT_Receiver);
			}
		}
	};
	private MenuGroupe menuGroupePropos;
	private MenuGroupe menuGroupeDeconnexion;
	private MenuItem menuItemMaj;
	private ProgressBar mProgressBar;
	private MenuGroupe menuReglages;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(true) {
			menuItemMaj = menu.add("Mise à jour");
			menuItemMaj.setIcon(R.drawable.ic_menu_refresh);
			mProgressBar = new ProgressBar(this);
			mProgressBar.setIndeterminate(true);
			menuItemMaj.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			menuItemMaj.setOnMenuItemClickListener(this);
		}
		return super.onCreateOptionsMenu(menu);		
	}

	public abstract boolean isUpdatable();

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if(item == menuItemMaj) {
			onMajCalled();
			return true;
		}
		return false;
	}

	public void onMajCalled() {
		menuItemMaj.setActionView(mProgressBar);
		
		WSManager.updateAllData(MyTimeActivity.this, new Callback() {
			
			@Override
			public void done(Exception e) {
				majFinished(e);
			}
		});
	}

	public final void majFinished(Exception e) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				menuItemMaj.setActionView(null);
				menuItemMaj.setIcon(R.drawable.ic_menu_refresh);	
			}
		});
	}

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(this.LOGOUT_Receiver);
		} catch (Exception e) {}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(this.LOGOUT_Receiver, new IntentFilter(ActivityUtil.ACTION_FINISH));
	}

	@Override
	protected ArrayList<MenuGroupe> onCreateMenu(ArrayList<MenuGroupe> menuGroupes) {

		menuReglages = new MenuGroupe("Réglages", MooncakeIcone.icon_settings);
		menuGroupes.add(menuReglages);

		
		menuGroupePropos = new MenuGroupe("A propos",MooncakeIcone.icon_info_sign);
		menuGroupes.add(menuGroupePropos);

		menuGroupeDeconnexion = new MenuGroupe("Déconnexion", MooncakeIcone.icon_off);
		menuGroupes.add(menuGroupeDeconnexion);


		return menuGroupes; 
	}


	@Override
	protected void onMenuGroupClick(ExpandableListView parent,MenuGroupe group, long id) {
		if(group == menuGroupeDeconnexion) {
			ActivityUtil.logout(this);
		} else if(group == menuGroupePropos) {
			Intent myIntent = new Intent(MyTimeActivity.this, AboutActivity.class);
			startActivity(myIntent);
		}else if(group == menuReglages) {
			Intent myIntent = new Intent(this, SettingsActivity.class);
			startActivity(myIntent);
		}
	}

	@Override protected void onMenuGroupSwitch(MenuGroupe group, boolean isChecked) {}
	@Override protected void onMenuChildSwitch(MenuGroupe group,MenuObjet objet, boolean isChecked) {}
	@Override protected void onMenuChildClick(ExpandableListView parent, MenuGroupe group, MenuObjet objet,long id) {}
	@Override protected boolean onBackButtonPressed() {return false;}
	@Override protected void reload() {}
}
