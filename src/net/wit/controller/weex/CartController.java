/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.weex;

import net.wit.Message;
import net.wit.controller.model.CartItemModel;
import net.wit.controller.model.CartModel;
import net.wit.controller.website.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Controller - 购物车
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Controller("weexCartController")
@RequestMapping("/weex/cart")
public class CartController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "cartItemServiceImpl")
	private CartItemService cartItemService;
	@Resource(name = "redisServiceImpl")
	private RedisService redisService;
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	Message add(Long id, Integer quantity,BigDecimal price,Long promotionId, HttpServletRequest request, HttpServletResponse response) {
//		if (quantity == null || quantity < 1) {
//			return Message.error("请输入购买数量");
//		}
		Product product = productService.find(id);
		if (product == null) {
			return Message.error("无效商品id");
		}
		if (!product.getIsMarketable()) {
			return Message.error("商品已下架");
		}

		Cart cart = cartService.getCurrent();
		Member member = memberService.getCurrent();

		if (cart == null) {
			cart = new Cart();
			cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
			cart.setMember(member);
			cartService.save(cart);
		}

		if (Cart.MAX_PRODUCT_COUNT != null && cart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
			return Message.error("不能大于100种商品", Cart.MAX_PRODUCT_COUNT);
		}

		Promotion promotion = null;
		if (promotionId!=null) {
			promotion = promotionService.find(promotionId);
		}

		if (cart.contains(product)) {
			CartItem cartItem = cart.getCartItem(product);
			if (cartItem.getQuantity() + quantity > product.getAvailableStock()) {
				return Message.warn("库存不足,稍等试试");
			}
			cartItem.add(quantity);
			if (promotion!=null) {
				cartItem.setPromotion(promotion);
			}
			cartItemService.update(cartItem);
		} else {
			if (quantity > product.getAvailableStock()) {
				return Message.warn("库存不足,稍等试试");
			}
			CartItem cartItem = new CartItem();
			cartItem.setPrice(price);
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setSeller(product.getMember());
			if (promotion!=null) {
				cartItem.setPromotion(promotion);
			}
			cartItemService.save(cartItem);
			cart.getCartItems().add(cartItem);
		}

		if (member == null) {
			Map<String,String> vkey = new HashMap<>();
			vkey.put("id",cart.getId().toString());
			vkey.put("key",cart.getKey());
			redisService.put(Cart.KEY_COOKIE_NAME, JsonUtils.toJson(vkey));
		}
		CartModel model = new CartModel();
		model.bindHeader(cart);
		return Message.success(model,"添加成功");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public  @ResponseBody Message list(HttpServletRequest request) {
		Cart cart = cartService.getCurrent();
		CartModel model = new CartModel();
		if (cart!=null) {
			model.bind(cart);
		} else {
			model.setQuantity(0);
			model.setEffectivePrice(BigDecimal.ZERO);
			model.setCartItems(new ArrayList<CartItemModel>());

		}
		return Message.bind(model,request);
	}

	/**
	 * 统计
	 */
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public  @ResponseBody Message count(HttpServletRequest request) {
		Cart cart = cartService.getCurrent();
		if (cart!=null) {
			return Message.bind(cart.getQuantity(), request);
		} else {
			return Message.bind(0, request);
		}
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody
	Message edit(Long id, Integer quantity,BigDecimal price,Long promotionId) {
		Map<String, Object> data = new HashMap<String, Object>();

		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("购物车不能为空");
		}

		if (quantity == null || quantity < 1) {
			return Message.error("数据不能为零");
		}

		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			return Message.error("商品没找到");
		}

		Promotion promotion = null;
		if (promotionId!=null) {
			promotion = promotionService.find(promotionId);
		}
		if (promotion!=null) {
			cartItem.setPromotion(promotion);
		}

		Product product = cartItem.getProduct();
		if (quantity > product.getAvailableStock()) {
			return Message.error("库存不足");
		}
		cartItem.setPrice(price);
		cartItem.setQuantity(quantity);
		cartItemService.update(cartItem);

		CartModel model = new CartModel();
		model.bindHeader(cart);
		return Message.success(model,"添加成功");
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("购物车不能为空");
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			return Message.error("商品没找到");
		}
		cartItems.remove(cartItem);
		cartItemService.delete(cartItem);

		CartModel model = new CartModel();
		model.bindHeader(cart);
		return Message.success(model,"添加成功");
	}

	/**
	 * 清空
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody
	Message clear() {
		Cart cart = cartService.getCurrent();
		if (cart!=null) {
			cartService.delete(cart);
		}
		return Message.success("清理成功");
	}

}