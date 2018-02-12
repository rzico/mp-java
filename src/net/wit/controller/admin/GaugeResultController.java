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
import net.wit.entity.GaugeResult;
import net.wit.service.GaugeResultService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: GaugeResultController
 * @author 降魔战队
 * @date 2018-2-12 21:4:40
 */
 
@Controller("adminGaugeResultController")
@RequestMapping("/admin/gaugeResult")
public class GaugeResultController extends BaseController {
	@Resource(name = "gaugeResultServiceImpl")
	private GaugeResultService gaugeResultService;
	
	@Resource(name = "gaugeServiceImpl")
	private GaugeService gaugeService;

	@Resource(name = "gaugeGeneServiceImpl")
	private GaugeGeneService gaugeGeneService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("gauges",gaugeService.findAll());

		model.addAttribute("gaugeGenes",gaugeGeneService.findAll());

		return "/admin/gaugeResult/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("gauges",gaugeService.findAll());

		model.addAttribute("gaugeGenes",gaugeGeneService.findAll());

		return "/admin/gaugeResult/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(GaugeResult gaugeResult, Long gaugeId, Long gaugeGeneId){
		GaugeResult entity = new GaugeResult();	

		entity.setCreateDate(gaugeResult.getCreateDate());

		entity.setModifyDate(gaugeResult.getModifyDate());

		entity.setOrders(gaugeResult.getOrders() == null ? 0 : gaugeResult.getOrders());

		entity.setContent(gaugeResult.getContent());

		entity.setMaxscore(gaugeResult.getMaxscore());

		entity.setMinscore(gaugeResult.getMinscore());

		entity.setGauge(gaugeService.find(gaugeId));

		entity.setGaugeGene(gaugeGeneService.find(gaugeGeneId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gaugeResultService.save(entity);
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
            gaugeResultService.delete(ids);
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

		model.addAttribute("gauges",gaugeService.findAll());

		model.addAttribute("gaugeGenes",gaugeGeneService.findAll());

		model.addAttribute("data",gaugeResultService.find(id));

		return "/admin/gaugeResult/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(GaugeResult gaugeResult, Long gaugeId, Long gaugeGeneId){
		GaugeResult entity = gaugeResultService.find(gaugeResult.getId());
		
		entity.setCreateDate(gaugeResult.getCreateDate());

		entity.setModifyDate(gaugeResult.getModifyDate());

		entity.setOrders(gaugeResult.getOrders() == null ? 0 : gaugeResult.getOrders());

		entity.setContent(gaugeResult.getContent());

		entity.setMaxscore(gaugeResult.getMaxscore());

		entity.setMinscore(gaugeResult.getMinscore());

		entity.setGauge(gaugeService.find(gaugeId));

		entity.setGaugeGene(gaugeGeneService.find(gaugeGeneId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gaugeResultService.update(entity);
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

		Page<GaugeResult> page = gaugeResultService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * Gauge视图
	 */
	@RequestMapping(value = "/gaugeView", method = RequestMethod.GET)
	public String gaugeView(Long id, ModelMap model) {
		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("single","单常模"));
		types.add(new MapEntity("complex","多常模"));
		model.addAttribute("types",types);

		List<MapEntity> userTypes = new ArrayList<>();
		userTypes.add(new MapEntity("general","普通用户"));
		userTypes.add(new MapEntity("enterprise","企业用户"));
		userTypes.add(new MapEntity("school","学校用户"));
		model.addAttribute("userTypes",userTypes);

		model.addAttribute("products",productService.findAll());

		model.addAttribute("gauge",gaugeService.find(id));
		return "/admin/gaugeResult/view/gaugeView";
	}


	/**
	 * GaugeGene视图
	 */
	@RequestMapping(value = "/gaugeGeneView", method = RequestMethod.GET)
	public String gaugeGeneView(Long id, ModelMap model) {
		model.addAttribute("gauges",gaugeService.findAll());

		model.addAttribute("gaugeGene",gaugeGeneService.find(id));
		return "/admin/gaugeResult/view/gaugeGeneView";
	}



}