/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.applet;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.model.GoodsListModel;
import net.wit.controller.model.GoodsModel;
import net.wit.controller.model.GoodsViewModel;
import net.wit.controller.weex.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import org.hibernate.annotations.Filters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller - 商品
 * 
 */
@Controller("appletProductController")
@RequestMapping("/applet/product")
public class ProductController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "memberFollowServiceImpl")
	private MemberFollowService memberFollowService;

	@Resource(name = "articleFavoriteServiceImpl")
	private ArticleFavoriteService articleFavoriteService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody
	Message view(Long id,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		Goods goods = goodsService.find(id);

		goods.setHits(goods.getHits()+1);
		goodsService.update(goods);

		GoodsViewModel model =new GoodsViewModel();
		model.bind(goods);

		Article article = goods.article();

		if (article!=null) {
			model.setArticleId(article.getId());
		}
		if (member!=null) {

			MemberFollow memberFollow = memberFollowService.find(member, goods.product().getMember());
			model.getMember().setHasFollow(memberFollow != null);

			if (article!=null) {
				List<Filter> arfilters = new ArrayList<Filter>();
				arfilters.add(new Filter("member", Filter.Operator.eq, member));
				arfilters.add(new Filter("article", Filter.Operator.eq, article));
				List<ArticleFavorite> favorites = articleFavoriteService.findList(null, null, arfilters, null);
				model.setHasFavorite(favorites.size() > 0);
			}
		}

		return Message.bind(model,request);
	}

	/**
	 * 文章详情
	 */
	@RequestMapping(value = "/article", method = RequestMethod.GET)
	public @ResponseBody
	Message article(Long id,HttpServletRequest request) {
		Goods goods = goodsService.find(id);
		Article article = goods.article();
        if (article==null) {
        	return Message.error("没有详情");
		}
		return Message.success((Object) article.getId(),"获取成功");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	Message list(Long productCategoryId,Long tagId,String keyword,Pageable pageable,HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<Filter> filters = new ArrayList<Filter>();
		if (productCategory!=null) {
			filters.add(new Filter("productCategory", Filter.Operator.eq,productCategory));
		}
		filters.add(new Filter("isList", Filter.Operator.eq,true));
		pageable.setFilters(filters);
		pageable.setOrderDirection(Order.Direction.desc);
		pageable.setOrderProperty("modifyDate");

		Tag tag = null;
		if (tagId!=null) {
			tag = tagService.find(tagId);
		}

		if (keyword!=null) {
			filters.add(Filter.like("name", "%" + keyword + "%"));
		}

		Page<Product> page = productService.findPage(null,null,null,pageable);
		PageBlock model = PageBlock.bind(page);
		model.setData(GoodsListModel.bindList(page.getContent()));
		return Message.bind(model,request);
	}

}