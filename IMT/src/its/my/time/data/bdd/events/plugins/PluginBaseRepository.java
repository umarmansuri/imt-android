package its.my.time.data.bdd.events.plugins;

import its.my.time.data.bdd.base.BaseRepository;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

public abstract class PluginBaseRepository<T extends PluginBaseBean> extends BaseRepository<T>{
	
	
	public PluginBaseRepository(Context context, Class<T> type) {
		super(context, type);
	}
	

	public List<T> getAllByEid(long eid) {
		open();
		Cursor c = this.db.query(getTableName(), getAllAttr(), "eid"
				+ "=?", new String[] { "" + eid }, null, null, null);
		List<T> res = convertCursorToListObject(c);
		close();
		return res;
	}
	


	public List<T> getAllpdatableByEid(int id) {
		open();
		Cursor c = this.db.query(getTableName(), getAllAttr(), "eid"
				+ "=? AND ((dateModif > dateSync) OR dateSync IS NULL)", new String[] { "" + id }, null, null, null);
		List<T> res = convertCursorToListObject(c);
		close();
		return res;
	}
	
	@Override
	public List<T> convertCursorToListObject(Cursor c) {
		return super.convertCursorToListObject(c);
	}
}
