package its.my.time.view.menu;

import its.my.time.R;

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

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final MenuObjet menuObjet = (MenuObjet) getChild(groupPosition,
				childPosition);

		ChildViewHolder childViewHolder;

		if (convertView == null) {
			childViewHolder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.menu_child, null);
			childViewHolder.childTitle = (TextView) convertView.findViewById(R.id.menu_child_title);
			childViewHolder.childIcone = (MooncakeIcone) convertView.findViewById(R.id.menu_child_icone);
			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		childViewHolder.childTitle.setText(menuObjet.getNom());
		childViewHolder.childIcone.setIconeRes(menuObjet.getIconeRes());
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

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		final MenuGroupe objet = (MenuGroupe) getGroup(groupPosition);
		GroupViewHolder groupViewHolder;
		if (convertView == null) {
			groupViewHolder = new GroupViewHolder();
			convertView = inflater.inflate(R.layout.menu_row, null);
			groupViewHolder.groupTitle = (TextView) convertView.findViewById(R.id.menu_group_title);
			groupViewHolder.groupIcone = (MooncakeIcone) convertView.findViewById(R.id.menu_group_icone);
			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		groupViewHolder.groupTitle.setText(objet.getNom());
		groupViewHolder.groupIcone.setIconeRes(objet.getIconeRes());
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	class ChildViewHolder {
		public TextView childTitle;
		public MooncakeIcone childIcone;
	}

	class GroupViewHolder {
		public TextView groupTitle;
		public MooncakeIcone groupIcone;
	}

}
