/**
 *====================================================
 * 文件名称: OrderEntity.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年10月11日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.entity;
import org.apache.commons.lang.builder.CompareToBuilder;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;

/**
 * @ClassName: OrderEntity
 * @Description: 排序基类
 * @author Administrator
 * @date 2014年10月11日 下午5:59:24
 */

@MappedSuperclass
public abstract class OrderEntity extends BaseEntity implements Comparable<OrderEntity> {

	private static final long serialVersionUID = 5995013015967525827L;

	/** "排序"属性名称 */
	public static final String ORDER_PROPERTY_NAME = "orders";

	/** 排序 */
	@Min(0)
	@Column(name = "orders")
	private Integer orders;

	/**
	 * 获取排序
	 * @return 排序
	 */

	public Integer getOrders() {
		return orders;
	}

	/**
	 * 设置排序
	 * @param orders 排序
	 */
	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	/**
	 * 实现compareTo方法
	 * @param orderEntity 排序对象
	 * @return 比较结果
	 */
	public int compareTo(OrderEntity orderEntity) {
		return new CompareToBuilder().append(getOrders(), orderEntity.getOrders()).append(getId(), orderEntity.getId()).toComparison();
	}

}