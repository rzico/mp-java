
package net.wit;

/**
 * @ClassName: MapEntity
 * @Description: 自定义 MAP
 */
public class Params {
	/** id */
	private String name;
	/** 名称 */
	private Object value;

	public Params(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}