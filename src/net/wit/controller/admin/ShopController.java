package net.wit.controller.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

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
import net.wit.entity.Shop;
import net.wit.service.ShopService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ShopController
 * @author 降魔战队
 * @date 2017-11-4 18:12:40
 */
 
@Controller("adminShopController")
@RequestMapping("/admin/shop")
public class ShopController extends BaseController {
	@Resource(name = "shopServiceImpl")
	private ShopService shopService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {



		return "/admin/shop/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/shop/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Shop shop, Long areaId, Long enterpriseId){
		Shop entity = new Shop();	

		entity.setCreateDate(shop.getCreateDate());

		entity.setModifyDate(shop.getModifyDate());

		entity.setAddress(shop.getAddress());

		entity.setDeleted(shop.getDeleted());

		entity.setLicense(shop.getLicense());

		entity.setLicenseCode(shop.getLicenseCode());

		entity.setName(shop.getName());

		entity.setScene(shop.getScene());

		entity.setThedoor(shop.getThedoor());

		entity.setArea(areaService.find(areaId));

		entity.setCode(shop.getCode());

		entity.setEnterprise(enterpriseService.find(enterpriseId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            shopService.save(entity);
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
            shopService.delete(ids);
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



		model.addAttribute("data",shopService.find(id));

		return "/admin/shop/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Shop shop, Long areaId, Long enterpriseId){
		Shop entity = shopService.find(shop.getId());
		
		entity.setCreateDate(shop.getCreateDate());

		entity.setModifyDate(shop.getModifyDate());

		entity.setAddress(shop.getAddress());

		entity.setDeleted(shop.getDeleted());

		entity.setLicense(shop.getLicense());

		entity.setLicenseCode(shop.getLicenseCode());

		entity.setName(shop.getName());

		entity.setScene(shop.getScene());

		entity.setThedoor(shop.getThedoor());

		entity.setArea(areaService.find(areaId));

		entity.setCode(shop.getCode());

		entity.setEnterprise(enterpriseService.find(enterpriseId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            shopService.update(entity);
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

		Page<Shop> page = shopService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/admin/shop/view/areaView";
	}


	/**
	 * 企业管理视图
	 */
	@RequestMapping(value = "/enterpriseView", method = RequestMethod.GET)
	public String enterpriseView(Long id, ModelMap model) {
		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("operate","运营商"));
		types.add(new MapEntity("agent","代理商"));
		model.addAttribute("types",types);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("enterprise",enterpriseService.find(id));
		return "/admin/shop/view/enterpriseView";
	}



}