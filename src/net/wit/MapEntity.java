
package net.wit;

/**
 * @ClassName: MapEntity
 * @Description: 自定义 MAP
 */
public class MapEntity {
	/** id */
	private Long id;
	/** 名称 */
	private String name;

	public MapEntity(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}