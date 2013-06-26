package its.my.time.data.bdd.events.plugins.comment;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;
import its.my.time.util.DateUtil;

import java.util.Calendar;
import java.util.List;

public class CommentBean extends PluginBaseBean{

	private TableAttribut<String> comment;
	private TableAttribut<Calendar> date;
	private TableAttribut<String> author;
	private TableAttribut<Long> uid;

	public CommentBean() {
		super();
		this.comment = new TableAttribut<String>("comment", "");
		this.author = new TableAttribut<String>("author", "");
		this.date = new TableAttribut<Calendar>("date", DateUtil.createCalendar());
		this.uid = new TableAttribut<Long>("uid", 0l);
	}

	public String getComment() {
		return this.comment.getValue();
	}

	public void setComment(String comment) {
		this.comment.setValue(comment);
	}

	public String getAuthor() {
		return this.author.getValue();
	}

	public void setAuthor(String author) {
		this.author.setValue(author);
	}

	public long getUid() {
		return this.uid.getValue();
	}

	public void setUid(long uid) {
		this.uid.setValue(uid);
	}

	public Calendar getDate() {
		return this.date.getValue();
	}

	public void setDate(Calendar date) {
		this.date.setValue(date);
	}
	
	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(comment);
		list.add(date);
		list.add(author);
		list.add(uid);
		return super.getAttributs(list);
	}
}
