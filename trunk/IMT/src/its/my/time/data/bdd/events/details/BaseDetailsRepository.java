package its.my.time.data.bdd.events.details;

import its.my.time.data.bdd.base.BaseRepository;
import android.content.Context;
import android.database.Cursor;

public abstract class BaseDetailsRepository<T extends DetailsBaseBean> extends BaseRepository<T>{
	
	
	public BaseDetailsRepository(Context context, Class<T> type) {
		super(context, type);
	}
	

	public T getByEid(long eid) {
		open();
		Cursor c = this.db.query(getTableName(), getAllAttr(), "eid"
				+ "=?", new String[] { "" + eid }, null, null, null);
		T res = convertCursorToOneObject(c);
		close();
		return res;
	}
}
