package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.controller.makey.model.GaugeGeneAttributeModel;
import net.wit.controller.makey.model.GaugeQuestionOptionModel;
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
import net.wit.entity.GaugeGene;
import net.wit.service.GaugeGeneService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: GaugeGeneController
 * @author 降魔战队
 * @date 2018-2-12 21:4:40
 */
 
@Controller("adminGaugeGeneController")
@RequestMapping("/admin/gaugeGene")
public class GaugeGeneController extends BaseController {

	@Resource(name = "gaugeQuestionServiceImpl")
	private GaugeQuestionService gaugeQuestionService;

	@Resource(name = "gaugeGeneServiceImpl")
	private GaugeGeneService gaugeGeneService;
	
	@Resource(name = "gaugeServiceImpl")
	private GaugeService gaugeService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Long gaugeId,ModelMap model) {

		model.addAttribute("gaugeId",gaugeId);
		return "/admin/gaugeGene/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long gaugeId,ModelMap model) {

		model.addAttribute("gaugeId",gaugeId);

		List<MapEntity> scoreTypes = new ArrayList<>();
		scoreTypes.add(new MapEntity("total","因子得分总和"));
		scoreTypes.add(new MapEntity("smax","因子最大得分"));
		model.addAttribute("scoreTypes",scoreTypes);

		List<MapEntity> ranks = new ArrayList<>();
		ranks.add(new MapEntity("rank1","常规因子"));
		ranks.add(new MapEntity("rank2","二阶因子"));
		model.addAttribute("ranks",ranks);

		Gauge gauge = gaugeService.find(gaugeId);
		model.addAttribute("gaugeQuestions",gauge.getGaugeQuestions());

		model.addAttribute("expr_txt","[#if P1>P2] (${P1_S}/23-${P2_S}*0.38)-283 [#else] 0 [/#if]");

		return "/admin/gaugeGene/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(String name, Integer orders, GaugeGene.ScoreType scoreType,GaugeGene.Rank rank,Long gaugeId,String expr, Long [] questions, String [] sname,BigDecimal[] smin,BigDecimal [] smax){
		GaugeGene entity = new GaugeGene();	

		entity.setOrders(orders);

		if (scoreType==null) {
			entity.setScoreType(GaugeGene.ScoreType.total);
		} else {
			entity.setScoreType(scoreType);
		}

		entity.setName(name);


		if (rank.equals(GaugeGene.Rank.rank1)) {
			entity.setQuestions(gaugeQuestionService.findList(questions));

			List<Map<String, Object>> data = new ArrayList<>();
			for (int i = 0; i < sname.length; i++) {
				if (sname[i] != null && !"".equals(sname[i])) {
					Map<String, Object> q = new HashMap<String, Object>();
					q.put("sname", sname[i]);
					q.put("smin", smin[i]);
					q.put("smax", smax[i]);
					data.add(q);
				}
			}
			entity.setAttribute(JsonUtils.toJson(data));

		} else {
			entity.setAttribute(expr);
		}

		entity.setRank(rank);
		entity.setGauge(gaugeService.find(gaugeId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gaugeGeneService.save(entity);
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
            gaugeGeneService.delete(ids);
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
	public String edit(Long id,Long gaugeId, ModelMap model) {
		GaugeGene gaugeGene = gaugeGeneService.find(id);

		model.addAttribute("gaugeId",gaugeId);
		List<GaugeGeneAttributeModel> opts = null;
		if (gaugeGene.getRank().equals(GaugeGene.Rank.rank1)) {
			opts = JsonUtils.toObject(gaugeGene.getAttribute(),List.class);
		}

		List<MapEntity> ranks = new ArrayList<>();
		ranks.add(new MapEntity("rank1","常规因子"));
		ranks.add(new MapEntity("rank2","二阶因子"));
		model.addAttribute("ranks",ranks);

		List<MapEntity> scoreTypes = new ArrayList<>();
		scoreTypes.add(new MapEntity("total","因子得分总和"));
		scoreTypes.add(new MapEntity("smax","因子最大得分"));
		model.addAttribute("scoreTypes",scoreTypes);
		if (opts!=null) {
			model.addAttribute("options", opts);
			model.addAttribute("options_length", opts.size());
		} else {
			model.addAttribute("options_length", 0);
		}

		Gauge gauge = gaugeGene.getGauge();
		model.addAttribute("gaugeQuestions",gauge.getGaugeQuestions());

		model.addAttribute("expr_txt","[#if P1>P2] (${P1}/23-${P2}*0.38)-283 [#else] 0 [/#if]");

		model.addAttribute("data",gaugeGene);

		return "/admin/gaugeGene/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Long id,String name, Integer orders, GaugeGene.ScoreType scoreType,GaugeGene.Rank rank,String expr,Long gaugeId, Long [] questions, String [] sname,BigDecimal[] smin,BigDecimal [] smax){
		GaugeGene entity = gaugeGeneService.find(id);


		entity.setOrders(orders);


		if (scoreType==null) {
			entity.setScoreType(GaugeGene.ScoreType.total);
		} else {
			entity.setScoreType(scoreType);
		}


		entity.setName(name);



		if (rank.equals(GaugeGene.Rank.rank1)) {
			entity.setQuestions(gaugeQuestionService.findList(questions));

			List<Map<String, Object>> data = new ArrayList<>();
			for (int i = 0; i < sname.length; i++) {
				if (sname[i] != null && !"".equals(sname[i])) {
					Map<String, Object> q = new HashMap<String, Object>();
					q.put("sname", sname[i]);
					q.put("smin", smin[i]);
					q.put("smax", smax[i]);
					data.add(q);
				}
			}
			entity.setAttribute(JsonUtils.toJson(data));

		} else {
			entity.setAttribute(expr);
		}

		entity.setRank(rank);

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gaugeGeneService.update(entity);
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

		Page<GaugeGene> page = gaugeGeneService.findPage(beginDate,endDate,pageable);
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
		return "/admin/gaugeGene/view/gaugeView";
	}



}