package its.my.time.data.bdd.compte;

import java.util.List;

import its.my.time.data.bdd.base.BaseBean;
import its.my.time.data.bdd.base.TableAttribut;

public class CompteBean extends BaseBean{

	private TableAttribut<String> title;
	private TableAttribut<String> color;
	private TableAttribut<Integer> type;
	private TableAttribut<Integer> isShowed;
	private TableAttribut<Long> uid;

	public CompteBean() {
		super();
		title = new TableAttribut<String>("title", "");
		color = new TableAttribut<String>("color", "fc-event-orange");
		type = new TableAttribut<Integer>("type", -1);
		isShowed = new TableAttribut<Integer>("isShowed", 0);
		uid = new TableAttribut<Long>("uid", -1l);
	}

	public String getTitle() {
		return this.title.getValue();
	}

	public void setTitle(String title) {
		this.title.setValue(title);
	}

	public String getColor() {
		return this.color.getValue();
	}

	public void setColor(String color) {
		this.color.setValue(color);
	}

	public int getType() {
		return this.type.getValue();
	}

	public void setType(int type) {
		this.type.setValue(type);
	}

	public boolean isShowed() {
		return this.isShowed.getValue() == 1 ? true : false;
	}

	public void setShowed(boolean isShowed) {
		this.isShowed.setValue(isShowed == true ? 1 : 0);
	}

	public long getUid() {
		return this.uid.getValue();
	}

	public void setUid(long uid) {
		this.uid.setValue(uid);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(title);
		list.add(color);
		list.add(type);
		list.add(isShowed);
		list.add(uid);
		return list;
	}
}
