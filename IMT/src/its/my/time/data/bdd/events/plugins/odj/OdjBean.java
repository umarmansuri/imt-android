package its.my.time.data.bdd.events.plugins.odj;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;

import java.util.List;

public class OdjBean extends PluginBaseBean{

	private TableAttribut<String> value;
	private TableAttribut<Integer> order;

	public OdjBean() {
		super();
		this.value = new TableAttribut<String>("val", "");
		this.order = new TableAttribut<Integer>("ordre", -1);
	}

	public String getValue() {
		return this.value.getValue();
	}

	public void setValue(String value) {
		this.value.setValue(value);
	}

	public int getOrder() {
		return this.order.getValue();
	}

	public void setOrder(int order) {
		this.order.setValue(order);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(value);
		list.add(order);
		return super.getAttributs(list);
	}
}
