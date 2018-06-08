
package net.wit.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.Setting;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.DateUtils;

/**
 * Entity - 购物车
 * 
 */
@Entity
@Table(name = "wx_cart")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_cart_sequence")
public class Cart extends BaseEntity {

	private static final long serialVersionUID = 18L;

	/** 超时时间 */
	public static final int TIMEOUT = 604800;

	/** 最大商品数 */
	public static final Integer MAX_PRODUCT_COUNT = 100;

	/** "ID"Cookie名称 */
	public static final String ID_COOKIE_NAME = "cartId";

	/** "密钥"Cookie名称 */
	public static final String KEY_COOKIE_NAME = "cartKey";

	/** 密钥 */
	@Column(name = "cart_key", nullable = false, updatable = false)
	private String key;

	/** 买方 */
	@OneToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Member member;

	/** 购物车项 */
	@OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<CartItem> cartItems = new HashSet<CartItem>();
//
//
//	/**
//	 * 删除前处理
//	 */
//	@PreRemove
//	public void preRemove() {
//		getCartItems().clear();
//	}

	/**
	 * 获取密钥
	 * 
	 * @return 密钥
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 设置密钥
	 * 
	 * @param key
	 *            密钥
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 获取购物车项
	 * 
	 * @return 购物车项
	 */
	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	/**
	 * 设置购物车项
	 * 
	 * @param cartItems
	 *            购物车项
	 */
	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	/**
	 * 获取商品重量
	 * 
	 * @return 商品重量
	 */
	@Transient
	public int getWeight() {
		int weight = 0;
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null) {
					weight += cartItem.getWeight();
				}
			}
		}
		return weight;
	}

	/**
	 * 获取商品数量
	 * 
	 * @return 商品数量
	 */
	@Transient
	public int getQuantity() {
		int quantity = 0;
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getQuantity() != null) {
					quantity += cartItem.getQuantity();
				}
			}
		}
		return quantity;
	}

	/**
	 * 获取赠送积分
	 * 
	 * @return 赠送积分
	 */
	@Transient
	public long getPoint() {
		long point = 0L;
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null) {
					point += cartItem.getPoint();
				}
			}
		}
		return point;
	}

	/**
	 * 获取有效赠送积分
	 * 
	 * @return 有效赠送积分
	 */
	@Transient
	public long getEffectivePoint() {
		long effectivePoint = getPoint();
		return effectivePoint > 0L ? effectivePoint : 0L;
	}

	/**
	 * 获取商品价格
	 * 
	 * @return 商品价格
	 */
	@Transient
	public BigDecimal getPrice() {
		BigDecimal price = new BigDecimal(0);
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getSubtotal() != null) {
					price = price.add(cartItem.getSubtotal());
				}
			}
		}
		return price;
	}

	/**
	 * 获取有效商品价格
	 * 
	 * @return 有效商品价格
	 */
	@Transient
	public BigDecimal getEffectivePrice() {
		BigDecimal effectivePrice = getPrice();
		return effectivePrice.compareTo(new BigDecimal(0)) > 0 ? effectivePrice : new BigDecimal(0);
	}

	/**
	 * 判断优惠券是否有效
	 * 
	 * @param coupon
	 *            优惠券
	 * @return 优惠券是否有效
	 */
	@Transient
	public boolean isValid(Coupon coupon) {
		if (coupon == null || coupon.getScope().equals(Coupon.Scope.shop) || coupon.getDeleted() || !coupon.hasBegun() || coupon.hasExpired()) {
			return false;
		}
		if ((coupon.getMinimumPrice() != null && coupon.getMinimumPrice().compareTo(getEffectivePrice()) > 0)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取购物车项
	 * 
	 * @param product
	 *            商品
	 * @return 购物车项
	 */
	@Transient
	public CartItem getCartItem(Product product) {
		if (product != null && getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null && cartItem.getProduct().equals(product)) {
					return cartItem;
				}
			}
		}
		return null;
	}

	/**
	 * 判断是否包含商品
	 * 
	 * @param product
	 *            商品
	 * @return 是否包含商品
	 */
	@Transient
	public boolean contains(Product product) {
		if (product != null && getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null && cartItem.getProduct().equals(product)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取令牌
	 * 
	 * @return 令牌
	 */
	@Transient
	public String getToken() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37).append(getKey());
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				hashCodeBuilder.append(cartItem.getProduct()).append(cartItem.getQuantity()).append(cartItem.getEffectivePrice());
			}
		}
		return DigestUtils.md5Hex(hashCodeBuilder.toString());
	}

	/**
	 * 获取是否库存不足
	 * 
	 * @return 是否库存不足
	 */
	@Transient
	public boolean getIsLowStock() {
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getIsLowStock()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否已过期
	 * 
	 * @return 是否已过期
	 */
	@Transient
	public boolean hasExpired() {
		return new Date().after(DateUtils.addSeconds(getModifyDate(), TIMEOUT));
	}

	/**
	 * 判断是否允许使用优惠券
	 * 
	 * @return 是否允许使用优惠券
	 */
	@Transient
	public boolean isCouponAllowed() {
		return true;
	}

	/**
	 * 判断是否为空
	 * 
	 * @return 是否为空
	 */
	@Transient
	public boolean isEmpty() {
		return getCartItems() == null || getCartItems().isEmpty();
	}

}