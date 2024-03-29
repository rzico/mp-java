package net.wit.controller.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.service.impl.ProductServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Filters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Goods;
import net.wit.service.GoodsService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: GoodsController
 * @author 降魔战队
 * @date 2017-10-11 15:37:9
 */
 
@Controller("adminGoodsController")
@RequestMapping("/admin/goods")
public class GoodsController extends BaseController {
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {



		return "/admin/goods/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/goods/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Goods goods){
		Goods entity = new Goods();	

		entity.setCreateDate(goods.getCreateDate());

		entity.setModifyDate(goods.getModifyDate());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            goodsService.save(entity);
            return Message.success(entity,"admin.save.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.save.error");
        }
	}


	/**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            return Message.success("admin.delete.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.delete.error");
        }
    }
	
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {



		model.addAttribute("data",goodsService.find(id));

		return "/admin/goods/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Goods goods){
		Goods entity = goodsService.find(goods.getId());
		
		entity.setCreateDate(goods.getCreateDate());

		entity.setModifyDate(goods.getModifyDate());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            goodsService.update(entity);
            return Message.success(entity,"admin.update.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.update.error");
        }
	}
	

	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Date beginDate, Date endDate, Pageable pageable, ModelMap model) {	

		Page<Goods> page = goodsService.findPage(beginDate,endDate,pageable);
//		List<Goods> goodses=page.getContent();
//		for(Goods goods:goodses){
//			System.out.println(goods.getId()+","+goods.getCreateDate()+","+goods.getModifyDate());
//			List<Product> products=goods.getProducts();
//			if(goods.getProducts()!=null) {
//				for (Product product : products) {
//					System.out.println(product.getId() + "," + product.getName() + "," + product.getSpec1()+","+product.getThumbnail());
//				}
//			}
//		}
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}


	/**
	 * 会员管理视图
	 */
	@RequestMapping(value = "/goodsView", method = RequestMethod.GET)
	public String memberView(Long id, ModelMap model) {
		Goods goods=goodsService.find(id);
		List<Product> products=goods.getProducts();
		model.addAttribute("goods",goods);
		if(products.size()>=1) {
			model.addAttribute("products", products);
		}
		List<Tag> tags=tagService.findAll();
		model.addAttribute("tag",tags);
		return "/admin/product/view/goodsView";
	}

}