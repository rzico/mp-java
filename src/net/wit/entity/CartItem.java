
package net.wit.entity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.wit.Setting;
import net.wit.util.SettingUtils;

/**
 * Entity - 购物车项
 * 
 */
@Entity
@Table(name = "wx_cart_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_cart_item_sequence")
public class CartItem extends BaseEntity {

	private static final long serialVersionUID = 19L;

	/** 最大数量 */
	public static final Integer MAX_QUANTITY = 10000;

	/** 数量 */
	@Column(nullable = false)
	private Integer quantity;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Product product;

	/** 购物车 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Cart cart;

	/** 卖方 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member seller;

	public Member getSeller() {
		return seller;
	}

	public void setSeller(Member seller) {
		this.seller = seller;
	}

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 设置数量
	 * 
	 * @param quantity
	 *            数量
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置商品
	 * 
	 * @param product
	 *            商品
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * 获取购物车
	 * 
	 * @return 购物车
	 */
	public Cart getCart() {
		return cart;
	}

	/**
	 * 设置购物车
	 * 
	 * @param cart
	 *            购物车
	 */
	public void setCart(Cart cart) {
		this.cart = cart;
	}

	/**
	 * 获取赠送积分
	 * 
	 * @return 赠送积分
	 */
	@Transient
	public long getPoint() {
		if (getProduct() != null && getProduct().getPoint() != null && getQuantity() != null) {
			return getProduct().getPoint() * getQuantity();
		} else {
			return 0L;
		}
	}

	/**
	 * 获取商品重量
	 * 
	 * @return 商品重量
	 */
	@Transient
	public int getWeight() {
		if (getProduct() != null && getProduct().getWeight() != null && getQuantity() != null) {
			return getProduct().getWeight() * getQuantity();
		} else {
			return 0;
		}
	}

	@Transient
	public Friends.Type friendsType() {
		if (getSeller()==null) {
			return Friends.Type.friend;
		}
		if (getCart().getMember()==null) {
			return Friends.Type.friend;
		}
		Set<Friends> fds = getSeller().getFriends();
		for (Friends fd:fds) {
			if (fd.getFriend().equals(getCart().getMember())) {
				return fd.getType();
			}
		}
		return Friends.Type.friend;
	}

	/**
	 * 获取价格
	 * 
	 * @return 价格
	 */
	@Transient
	public BigDecimal getPrice() {
		if (getProduct() != null && getProduct().getPrice() != null) {
			Setting setting = SettingUtils.get();
			BigDecimal price = BigDecimal.ZERO;
			Friends.Type vip = friendsType();
			if (vip.equals(Friends.Type.friend)) {
				price = getProduct().getPrice();
			} else
			if (vip.equals(Friends.Type.customer)) {
				price = getProduct().getPrice();
			} else
			if (vip.equals(Friends.Type.vip1)) {
				price = getProduct().getVip1Price();
			} else
			if (vip.equals(Friends.Type.vip2)) {
				price = getProduct().getVip2Price();
			} else
			if (vip.equals(Friends.Type.vip3)) {
				price = getProduct().getVip3Price();
			}
			if (price==null || price.compareTo(BigDecimal.ZERO)==0) {
				price = getProduct().getPrice();
			}
			return setting.setScale(getProduct().getPrice());
		} else {
			return new BigDecimal(0);
		}
	}

	/**
	 * 获取小计
	 * 
	 * @return 小计
	 */
	@Transient
	public BigDecimal getSubtotal() {
		if (getQuantity() != null) {
			return getPrice().multiply(new BigDecimal(getQuantity()));
		} else {
			return new BigDecimal(0);
		}
	}

	/**
	 * 获取是否库存不足
	 * 
	 * @return 是否库存不足
	 */
	@Transient
	public boolean getIsLowStock() {
		if (getQuantity() != null && getProduct() != null && getQuantity() > getProduct().getAvailableStock(getSeller())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 增加商品数量
	 * 
	 * @param quantity
	 *            数量
	 */
	@Transient
	public void add(int quantity) {
		if (quantity > 0) {
			if (getQuantity() != null) {
				setQuantity(getQuantity() + quantity);
			} else {
				setQuantity(quantity);
			}
		}
	}

}