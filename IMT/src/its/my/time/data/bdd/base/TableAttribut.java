package its.my.time.data.bdd.base;


public class TableAttribut<T> {
	private String label;
	private T value;

	public TableAttribut(String label, T value) {
		super();
		this.label = label;
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public T getValue() {
		return value;
	}
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		this.value = (T) value;
	}
	public Class<?> getClasse() {
		return value.getClass();
	}


}
