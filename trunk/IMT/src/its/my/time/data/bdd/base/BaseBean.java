package its.my.time.data.bdd.base;

import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public abstract class BaseBean {
	private TableAttribut<Integer> id;
	private TableAttribut<String> idDistant;
	private TableAttribut<Calendar> dateModif;
	private TableAttribut<Calendar> dateSync;

	public BaseBean() {
		idDistant = new TableAttribut<String>("idDistant", "");
		id = new TableAttribut<Integer>("id", -1);
		dateModif = new TableAttribut<Calendar>("dateModif", DateUtil.createCalendar());
		dateSync = new TableAttribut<Calendar>("dateSync", DateUtil.createCalendar());
	}

	public String getIdDistant() {
		return idDistant.getValue();
	}
	public void setIdDistant(String idDistant) {
		this.idDistant.setValue(idDistant);
	}
	public int getId() {
		return id.getValue();
	}
	public void setId(int id) {
		this.id.setValue(id);
	}
	public Calendar getDateModif() {
		return dateModif.getValue();
	}
	public void setDateModif(Calendar dateModif) {
		this.dateModif.setValue(dateModif);
	}
	public Calendar getDateSync() {
		return dateSync.getValue();
	}
	public void setDateSync(Calendar dateSync) {
		this.dateSync.setValue(dateSync);
	}

	public List<TableAttribut<?>> getAllAttributs(List<TableAttribut<?>> res){
		res.add(idDistant);
		res.add(id);
		res.add(dateModif);
		res.add(dateSync);
		return getAttributs(res);
	}

	public abstract List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list);

	public void setAttributs(List<TableAttribut<?>> attrNew) {
		List<TableAttribut<?>> attrsOrig = getAllAttributs(new ArrayList<TableAttribut<?>>());
		for (TableAttribut<?> tableAttribut : attrsOrig) {
			tableAttribut.setValue(attrNew.get(attrsOrig.indexOf(tableAttribut)).getValue());
		}		
	}

}