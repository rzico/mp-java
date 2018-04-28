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
import net.wit.entity.Road;
import net.wit.service.RoadService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: RoadController
 * @author 降魔战队
 * @date 2018-4-28 14:19:7
 */
 
@Controller("adminRoadController")
@RequestMapping("/admin/road")
public class RoadController extends BaseController {
	@Resource(name = "roadServiceImpl")
	private RoadService roadService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("areas",areaService.findAll());

		return "/admin/road/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("areas",areaService.findAll());

		return "/admin/road/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Road road, Long areaId){
		Road entity = new Road();	

		entity.setCreateDate(road.getCreateDate());

		entity.setModifyDate(road.getModifyDate());

		entity.setOrders(road.getOrders() == null ? 0 : road.getOrders());

		entity.setCode(road.getCode());

		entity.setName(road.getName());

		entity.setArea(areaService.find(areaId));

//		entity.setLat(road.getLat());
//
//		entity.setLng(road.getLng());
//
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            roadService.save(entity);
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
            roadService.delete(ids);
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

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("data",roadService.find(id));

		return "/admin/road/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Road road, Long areaId){
		Road entity = roadService.find(road.getId());
		
		entity.setCreateDate(road.getCreateDate());

		entity.setModifyDate(road.getModifyDate());

		entity.setOrders(road.getOrders() == null ? 0 : road.getOrders());

		entity.setCode(road.getCode());

		entity.setName(road.getName());

		entity.setArea(areaService.find(areaId));
//
//		entity.setLat(road.getLat());
//
//		entity.setLng(road.getLng());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            roadService.update(entity);
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

		Page<Road> page = roadService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/admin/road/view/areaView";
	}



}