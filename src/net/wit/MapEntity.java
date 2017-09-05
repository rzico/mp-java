
package net.wit;

/**
 * @ClassName: MapEntity
 * @Description: 自定义 MAP
 */
public class MapEntity {
	/** id */
	private String id;
	/** 名称 */
	private String name;

	public MapEntity(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}