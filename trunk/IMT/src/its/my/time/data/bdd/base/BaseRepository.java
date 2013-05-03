package its.my.time.data.bdd.base;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public abstract class BaseRepository<T extends BaseBean> extends DatabaseHandler {

    final Class<T> typeParameterClass;
	
	public BaseRepository(Context context, Class<T> type) {
		super(context);
		typeParameterClass = type;
	}
	
	public abstract String getTableName();
	
	public T getInstance() {
		try {
			return typeParameterClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}


	public String getCreateRequest(){
		String req = "create table " + getTableName() + "(";
		T object = getInstance();
		List<TableAttribut<?>> attrOrig = object.getAllAttributs(new ArrayList<TableAttribut<?>>());
		for (TableAttribut<?> attr: attrOrig) {
			Object obj = attr.getValue();
			String label = attr.getLabel();
			if(obj instanceof Calendar) {
				req+= label + " text";
			} else if(obj instanceof String) {
				req+= label + " text";
			} else if(obj instanceof Long) {
				req+= label + " integer";
				if(label == "id"){
					req+= " primary key autoincrement";
				}
			} else if(obj instanceof Float) {
				req+= label + " integer";
			} else if(obj instanceof Integer) {
				req+= label + " integer";
				if(label == "id"){
					req+= " primary key autoincrement";
				}
			}
			req+=", ";
		}
		req = req.substring(0, req.length() - 2);
		req+=");";

		return req;
	}

	public String[] getAllAttr(){
		String[] res = null;

		return res;
	}

	public List<T> convertCursorToListObject(Cursor c) {
		List<T> liste = new ArrayList<T>();
		if (c.getCount() == 0) {
			return liste;
		}
		c.moveToFirst();
		do {
			T object = convertCursorToObject(c);
			liste.add(object);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public T convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		T object = convertCursorToObject(c);
		c.close();
		return object;
	}

	public T convertCursorToObject(Cursor c){
		T object = getInstance();
		List<TableAttribut<?>> attrOrig = object.getAllAttributs(new ArrayList<TableAttribut<?>>());
		for(int i = 0; i < attrOrig.size(); i++) {
			Object obj = attrOrig.get(i).getValue();
			String label = attrOrig.get(i).getLabel();
			if(obj instanceof Calendar) {
				attrOrig.get(i).setValue(DateUtil.getDateFromISO(c.getString(c.getColumnIndex(label))));
			} else if(obj instanceof String) {
				attrOrig.get(i).setValue(c.getString(c.getColumnIndex(label)));
			} else if(obj instanceof Long) {
				attrOrig.get(i).setValue(c.getLong(c.getColumnIndex(label)));
			} else if(obj instanceof Float) {
				attrOrig.get(i).setValue(c.getFloat(c.getColumnIndex(label)));
			} else if(obj instanceof Integer) {
				attrOrig.get(i).setValue(c.getInt(c.getColumnIndex(label)));
			}			
		}
		object.setAttributs(attrOrig);
		return object;
	}

	public long insert(T object) {
		ContentValues initialValues = new ContentValues();
		List<TableAttribut<?>> attrOrig = object.getAllAttributs(new ArrayList<TableAttribut<?>>());
		for (TableAttribut<?> attr: attrOrig) {
			String label = attr.getLabel();
			if(label != "id") {
				Object obj = attr.getValue();
				if(obj instanceof Calendar) {
					initialValues.put(label, DateUtil.getTimeInIso((Calendar)obj));
				} else if(obj instanceof String) {
					initialValues.put(label, (String)obj);
				} else if(obj instanceof Long) {
					initialValues.put(label, (Long)obj);
				} else if(obj instanceof Float) {
					initialValues.put(label, (Float)obj);
				} else if(obj instanceof Integer) {
					initialValues.put(label, (Integer)obj);
				}
			}
		}

		open();
		int res = (int) this.db.insert(getTableName(), null, initialValues);
		close();
		if(res > -1) {
			object.setId(res);
			objectAdded(object);
		}
		return res;
	}

	public int update(T object) {
		ContentValues initialValues = new ContentValues();
		List<TableAttribut<?>> attrOrig = object.getAllAttributs(new ArrayList<TableAttribut<?>>());
		for (TableAttribut<?> attr: attrOrig) {
			Object obj = attr.getValue();
			String label = attr.getLabel();
			if(obj instanceof Calendar) {
				initialValues.put(label, DateUtil.getTimeInIso((Calendar)obj));
			} else if(obj instanceof String) {
				initialValues.put(label, (String)obj);
			} else if(obj instanceof Long) {
				initialValues.put(label, (Long)obj);
			} else if(obj instanceof Float) {
				initialValues.put(label, (Float)obj);
			} else if(obj instanceof Integer) {
				initialValues.put(label, (Integer)obj);
			}
		}

		open();
		int nbRow = this.db.update(getTableName(), initialValues, "id" + "=?", new String[] { "" + object.getId() });
		close();
		if(nbRow > 0) {
			objectUpdated(object);
		}
		return nbRow;
	}

	public int delete(T object) {
		open();
		int nbRow = this.db.delete(getTableName(),"id" + "=" + object.getId(), null);
		close();
		if(nbRow > 0) {
			objectDeleted(object);
		}
		return nbRow;
	}

	public T getById(long id) {
		open();
		Cursor c = this.db.query(getTableName(), getAllAttr(), "id"
				+ "=?", new String[] { "" + id }, null, null, null);
		T res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public interface OnObjectChangedListener<T> {
		public void onObjectAdded(T object);
		public void onObjectUpdated(T object);
		public void onObjectDeleted(T object);
	}

	protected abstract void objectAdded(T object);
	protected abstract void objectUpdated(T object);
	protected abstract void objectDeleted(T object);
	public abstract void addOnObjectChangedListener(OnObjectChangedListener<T> listener);
	public abstract void removeOnObjectChangedListener(OnObjectChangedListener<T> listener);
}
