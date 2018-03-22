package net.wit.controller.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.controller.makey.model.GaugeGeneAttributeModel;
import net.wit.util.JsonUtils;
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
	public String index(Long gaugeId,ModelMap model) {
		model.addAttribute("gaugeId",gaugeId);

		model.addAttribute("gauges",gaugeService.findAll());

		model.addAttribute("gaugeGenes",gaugeGeneService.findAll());

		return "/admin/gaugeResult/list";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long gaugeId,ModelMap model) {

		model.addAttribute("gaugeId",gaugeId);
		Gauge gauge = gaugeService.find(gaugeId);
		model.addAttribute("gauge",gauge);
		model.addAttribute("gaugeGenes",gauge.getGaugeGenes());

		model.addAttribute("expr_txt",
				"1.总分  = total\n" +
						"2.总均分 = tavg\n" +
						"3.阳性项目数 = positive\n" +
						"4.阴性项目数=negative\n" +
						"5.阳性项止均分=pavg\n" +
						"6.因子平均分 = savg\n" +
						"7.因子得份=因子名;表达式说明：[#if P1>P2] (${P1}/23-${P2}*0.38)-283 [#else] 0 [/#if]");

		return "/admin/gaugeResult/add";
	}

	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(GaugeResult gaugeResult,String expr, Long gaugeId,Long [] genes,String [] attrs,Long [] scompare, Long gaugeGeneId){
		GaugeResult entity = new GaugeResult();	

		entity.setOrders(gaugeResult.getOrders() == null ? 0 : gaugeResult.getOrders());
		entity.setTitle(gaugeResult.getTitle());
		entity.setContent(gaugeResult.getContent());
        Gauge gauge = gaugeService.find(gaugeId);
		entity.setGauge(gauge);

		if (gauge.getMethod().equals(Gauge.Method.combined)) {
			List<Map<String, Object>> attributes = new ArrayList<>();

			for (int i = 0; i < genes.length; i++) {
				Map<String, Object> attr = new HashMap<>();
				attr.put("gene", genes[i]);
				attr.put("attribute", attrs[i]);
				attributes.add(attr);
			}

			entity.setAttribute(JsonUtils.toJson(attributes));

			entity.setScompare(JsonUtils.toJson(scompare));
		} else {
			entity.setAttribute(expr);
		}

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

		GaugeResult gaugeResult = gaugeResultService.find(id);
		Gauge gauge = gaugeResult.getGauge();
		model.addAttribute("gauge",gauge);
		model.addAttribute("data",gaugeResult);
		model.addAttribute("gaugeGenes",gauge.getGaugeGenes());

		model.addAttribute("expr_txt",
				"1.总分  = total\n" +
				"2.总均分 = tavg\n" +
				"3.阳性项目数 = positive\n" +
				"4.阴性项目数=negative\n" +
				"5.阳性项止均分=pavg\n" +
				"6.因子平均分 = savg\n" +
				"7.因子得份=因子名;表达式说明：[#if P1>P2] (${P1}/23-${P2}*0.38)-283 [#else] 0 [/#if]");

		return "/admin/gaugeResult/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(GaugeResult gaugeResult, String expr, Long gaugeId,Long [] genes,String [] attrs,Long [] scompare, Long gaugeGeneId){
		GaugeResult entity = gaugeResultService.find(gaugeResult.getId());
		
		entity.setCreateDate(gaugeResult.getCreateDate());

		entity.setModifyDate(gaugeResult.getModifyDate());

		entity.setOrders(gaugeResult.getOrders() == null ? 0 : gaugeResult.getOrders());

		entity.setContent(gaugeResult.getContent());
		Gauge gauge = entity.getGauge();
		entity.setGauge(gauge);

		if (gauge.getMethod().equals(Gauge.Method.combined)) {
			List<Map<String, Object>> attributes = new ArrayList<>();

			for (int i = 0; i < genes.length; i++) {
				Map<String, Object> attr = new HashMap<>();
				attr.put("gene", genes[i]);
				attr.put("attribute", attrs[i]);
				attributes.add(attr);
			}

			entity.setAttribute(JsonUtils.toJson(attributes));

			entity.setScompare(JsonUtils.toJson(scompare));
		} else {
			entity.setAttribute(expr);
		}

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
	public Message list(Long gaugeId,Date beginDate, Date endDate, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		Filter typeFilter = new Filter("gauge", Filter.Operator.eq, gaugeService.find(gaugeId));
		filters.add(typeFilter);

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