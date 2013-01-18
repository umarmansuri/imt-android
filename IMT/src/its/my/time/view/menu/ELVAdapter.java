package its.my.time.view.menu;

import its.my.time.R;
import its.my.time.view.Switcher;
import its.my.time.view.Switcher.OnStateChangedListener;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fonts.mooncake.MooncakeIcone;

public class ELVAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<MenuGroupe> menuGroupes;
	private OnItemSwitchedListener onItemSwitchedListener;
	private LayoutInflater inflater;

	public ELVAdapter(Context context, ArrayList<MenuGroupe> menuGroupes) {
		this.context = context;
		this.menuGroupes = menuGroupes;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	public Object getChild(int gPosition, int cPosition) {
		return menuGroupes.get(gPosition).getObjets().get(cPosition);
	}

	public long getChildId(int gPosition, int cPosition) {
		return cPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final MenuObjet menuObjet = (MenuObjet) getChild(groupPosition,
				childPosition);

		final MenuChildViewHolder menuChildViewHolder;

		if (convertView == null) {
			menuChildViewHolder = new MenuChildViewHolder();
			convertView = inflater.inflate(R.layout.menu_child, null);
			menuChildViewHolder.childTitle = (TextView) convertView.findViewById(R.id.menu_child_title);
			menuChildViewHolder.childIcone = (MooncakeIcone) convertView.findViewById(R.id.menu_child_icone);
			menuChildViewHolder.childSwitcher = (Switcher) convertView.findViewById(R.id.menu_child_switcher);
			convertView.setTag(menuChildViewHolder);
		} else {
			menuChildViewHolder = (MenuChildViewHolder) convertView.getTag();
		}
		menuChildViewHolder.childTitle.setText(menuObjet.getNom());

		if(menuObjet.getIconeRes()>=0) {
			menuChildViewHolder.childIcone.setIconeRes(menuObjet.getIconeRes());
		}
		if(!menuObjet.isSwitcher()) {
			menuChildViewHolder.childSwitcher.setVisibility(View.GONE);
		} else if(onItemSwitchedListener != null) {
			menuChildViewHolder.childSwitcher.setOnStateChangedListener(new OnStateChangedListener() {
				@Override
				public void onStateCHangedListener(Switcher switcher, boolean isChecked) {
					onItemSwitchedListener.onObjetSwitched(menuChildViewHolder.childSwitcher, groupPosition, childPosition, isChecked);
				}
			});
		}
		return convertView;
	}

	public int getChildrenCount(int gPosition) {
		return menuGroupes.get(gPosition).getObjets().size();
	}

	public Object getGroup(int gPosition) {
		return menuGroupes.get(gPosition);
	}

	public int getGroupCount() {
		return menuGroupes.size();
	}

	public long getGroupId(int gPosition) {
		return gPosition;
	}

	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		final MenuGroupe objet = (MenuGroupe) getGroup(groupPosition);
		final MenuGroupViewHolder menuGroupViewHolder;
		if (convertView == null) {
			menuGroupViewHolder = new MenuGroupViewHolder();
			convertView = inflater.inflate(R.layout.menu_row, null);
			menuGroupViewHolder.groupTitle = (TextView) convertView.findViewById(R.id.menu_group_title);
			menuGroupViewHolder.groupIcone = (MooncakeIcone) convertView.findViewById(R.id.menu_group_icone);
			menuGroupViewHolder.groupSwitcher = (Switcher) convertView.findViewById(R.id.menu_group_switcher);
			convertView.setTag(menuGroupViewHolder);
		} else {
			menuGroupViewHolder = (MenuGroupViewHolder) convertView.getTag();
		}
		menuGroupViewHolder.groupTitle.setText(objet.getNom());
		if(objet.getIconeRes()>=0) {
			menuGroupViewHolder.groupIcone.setIconeRes(objet.getIconeRes());
		}
		if(!objet.isSwitcher()) {
			menuGroupViewHolder.groupSwitcher.setVisibility(View.GONE);
		} else if(onItemSwitchedListener != null) {
			menuGroupViewHolder.groupSwitcher.setOnStateChangedListener(new OnStateChangedListener() {
				@Override
				public void onStateCHangedListener(Switcher switcher, boolean isChecked) {
					onItemSwitchedListener.onGroupSwitched(menuGroupViewHolder.groupSwitcher, groupPosition, isChecked);
				}
			});
		}
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
	public OnItemSwitchedListener getOnItemSwitchedListener() {
		return onItemSwitchedListener;
	}
	public void setOnItemSwitchedListener(OnItemSwitchedListener onItemSwitchedListener) {
		this.onItemSwitchedListener = onItemSwitchedListener;
	}


	public class MenuChildViewHolder {
		public TextView childTitle;
		public MooncakeIcone childIcone;
		public Switcher childSwitcher;
	}

	public class MenuGroupViewHolder {
		public TextView groupTitle;
		public MooncakeIcone groupIcone;
		public Switcher groupSwitcher;
	}

	public interface OnItemSwitchedListener{
		public void onGroupSwitched(View v, int positionGroup, boolean isChecked);
		public void onObjetSwitched(View v, int positionGroup, int positionObjet, boolean isChecked);
	}
}
