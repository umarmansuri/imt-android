package its.my.time.data.bdd.events.event;

import its.my.time.data.bdd.base.BaseBean;
import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.util.DateUtil;

import java.util.Calendar;
import java.util.List;

public class EventBaseBean extends BaseBean{

	private TableAttribut<String> details;
	private TableAttribut<Calendar> hDeb;
	private TableAttribut<Calendar> hFin;
	private TableAttribut<String> title;
	private TableAttribut<Integer> cid;
	private TableAttribut<Integer> typeId;
	private TableAttribut<Integer> detailsId;
	private TableAttribut<Integer> isAllDay;
	private TableAttribut<Integer> rappel;
	private TableAttribut<Integer> isMine;

	public EventBaseBean() {
		super();
		this.details = new TableAttribut<String>("details", "");
		this.hDeb  = new TableAttribut<Calendar>("hDeb", DateUtil.createCalendar());
		this.hFin = new TableAttribut<Calendar>("hFin", DateUtil.createCalendar());
		this.title = new TableAttribut<String>("title", "");
		this.cid = new TableAttribut<Integer>("cid", -1);
		this.typeId = new TableAttribut<Integer>("typeId", -1);
		this.detailsId = new TableAttribut<Integer>("detailsId", -1);
		this.isAllDay = new TableAttribut<Integer>("isAllDay", -1);
		this.rappel = new TableAttribut<Integer>("rappel", -1);
		this.isMine = new TableAttribut<Integer>("isMine", -1);
	}

	public boolean isAllDay() {
		return this.isAllDay.getValue() == 1 ? true : false;
	}

	public void setAllDay(boolean isAllDay) {
		this.isAllDay.setValue(isAllDay == true ? 1 : 0);
	}

	public boolean isMine() {
		return this.isMine.getValue() == 1 ? true : false;
	}

	public void setMine(boolean isMine) {
		this.isMine.setValue(isMine == true ? 1 : 0);
	}

	public String getDetails() {
		return this.details.getValue();
	}

	public Calendar gethDeb() {
		return this.hDeb.getValue();
	}

	public Calendar gethFin() {
		return this.hFin.getValue();
	}

	public String getTitle() {
		return this.title.getValue();
	}

	public void setDetails(String details) {
		this.details.setValue(details);
	}

	public void sethDeb(Calendar hDeb) {
		this.hDeb.setValue(hDeb);
	}

	public void sethFin(Calendar hFin) {
		this.hFin.setValue(hFin);
	}

	public void setTitle(String title) {
		this.title.setValue(title);
	}

	public int getCid() {
		return this.cid.getValue();
	}

	public void setCid(int cid) {
		this.cid.setValue(cid);
	}

	public int getTypeId() {
		return this.typeId.getValue();
	}

	public void setTypeId(int typeId) {
		this.typeId.setValue(typeId);
	}

	public int getDetailsId() {
		return this.detailsId.getValue();
	}

	public void setDetailsId(int detailsId) {
		this.detailsId.setValue(detailsId);
	}

	public int getRappel() {
		return this.rappel.getValue();
	}

	public void setRappel(int rappel) {
		this.rappel.setValue(rappel);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(details);
		list.add(hDeb);
		list.add(hFin);
		list.add(title);
		list.add(cid);
		list.add(typeId);
		list.add(detailsId);
		list.add(isAllDay);
		list.add(rappel);
		list.add(isMine);
		return list;
	}
}
