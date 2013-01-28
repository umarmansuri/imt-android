package its.my.time.pages;

import its.my.time.util.ActivityUtil;
import its.my.time.view.menu.MenuActivity;
import its.my.time.view.menu.MenuGroupe;
import its.my.time.view.menu.MenuObjet;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ExpandableListView;

import com.fonts.mooncake.MooncakeIcone;

public abstract class MyTimeActivity extends MenuActivity{

	private final BroadcastReceiver LOGOUT_Receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			final String action_finish = arg1.getStringExtra("FINISH");

			if (action_finish.equalsIgnoreCase("ACTION.FINISH.LOGOUT")) {
				finish();
				unregisterReceiver(MyTimeActivity.this.LOGOUT_Receiver);
			}
		}
	};
	private MenuGroupe menuGroupePropos;
	private MenuGroupe menuGroupeDeconnexion;
	
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
		}
	}

	@Override protected void onMenuGroupSwitch(MenuGroupe group, boolean isChecked) {}
	@Override protected void onMenuChildSwitch(MenuGroupe group,MenuObjet objet, boolean isChecked) {}
	@Override protected void onMenuChildClick(ExpandableListView parent, MenuGroupe group, MenuObjet objet,long id) {}
	@Override protected boolean onBackButtonPressed() {return false;}
	@Override protected void reload() {}
	
}
