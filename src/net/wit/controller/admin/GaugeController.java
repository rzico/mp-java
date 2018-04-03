package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.calculator.GeneCalculator;
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
import net.wit.entity.Gauge;
import net.wit.service.GaugeService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: GaugeController
 * @author 降魔战队
 * @date 2018-2-12 21:4:39
 */
 
@Controller("adminGaugeController")
@RequestMapping("/admin/gauge")
public class GaugeController extends BaseController {
	@Resource(name = "gaugeServiceImpl")
	private GaugeService gaugeService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "distributionServiceImpl")
	private DistributionService distributionService;

	@Resource(name = "gaugeCategoryServiceImpl")
	private GaugeCategoryService gaugeCategoryService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("single","单常模"));
		types.add(new MapEntity("complex","多常模"));
		model.addAttribute("types",types);

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("combined","组合型"));
		methods.add(new MapEntity("interpret","解释型"));
		model.addAttribute("methods",methods);

		List<MapEntity> userTypes = new ArrayList<>();
		userTypes.add(new MapEntity("general","普通用户"));
		userTypes.add(new MapEntity("enterprise","企业用户"));
		userTypes.add(new MapEntity("school","学校用户"));
		model.addAttribute("userTypes",userTypes);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","发布"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		return "/admin/gauge/list";
	}


	/**
	 * 测谎设置
	 */
	@RequestMapping(value = "/detect", method = RequestMethod.GET)
	public String detect(Long id,ModelMap model) {

		Gauge gauge = gaugeService.find(id);
		model.addAttribute("id",id);
		model.addAttribute("max",gauge.getGaugeQuestions().size());
		model.addAttribute("min",1);
		List<Map<String,Long>> data = new ArrayList<>();
		if (gauge.getDetect()!=null) {
			JSONObject jsonObject = JSONObject.fromObject(gauge.getDetect());
			model.addAttribute("correct",jsonObject.getString("correct"));
			JSONArray ar = jsonObject.getJSONArray("detect");
			for (int i=0;i<ar.size();i++) {
				JSONObject jb = ar.getJSONObject(i);
				Map<String,Long> d = new HashMap<>();
				d.put("A",jb.getLong("A"));
				d.put("B",jb.getLong("B"));
				data.add(d);
			}
		}
		model.addAttribute("detect",data);


		model.addAttribute("expr_txt",
				"1.总分  = total\n" +
						"2.总均分 = tavg\n" +
						"3.阳性项目数 = positive\n" +
						"4.阴性项目数=negative\n" +
						"5.阳性项止均分=pavg\n" +
						"6.因子平均分 = savg\n" +
						"7.全局因子平均分 = stavg\n" +
						"8.全局因子标准份 = devi\n" +
						"9.因子选项数 = P1A,P2B\n" +
						"10.指定题的结果 = Q3\n" +
						"11.测谎通过率 = PASSED\n" +
						"13.因子得份=因子名;表达式说明：[#if P1>P2] (${P1}/23-${P2}*0.38)-283 [#else] 0 [/#if]");

		return "/admin/gauge/view/detect";
	}


	/**
	 * 测谎设置
	 */
	@RequestMapping(value = "/detect", method = RequestMethod.POST)
	@ResponseBody
	public Message detect_submit(Long id, String correct,Long [] A,Long [] B, ModelMap model) {

		Gauge gauge = gaugeService.find(id);
		List<Map<String,Long>> data = new ArrayList<>();
		for (int i=0;i<A.length;i++) {
			Map<String,Long> d = new HashMap<>();
			if (A[i]!=null && A[i]>0) {
				d.put("A", A[i]);
				d.put("B", B[i]);
				data.add(d);
			}
		}

		Map<String,Object> dt = new HashMap<>();
		dt.put("detect",data);
		dt.put("correct",correct);

		gauge.setDetect(JsonUtils.toJson(dt));

		try {
			gaugeService.update(gauge);
			return Message.success(gauge,"admin.save.success");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.save.error");
		}

	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("combined","组合型"));
		methods.add(new MapEntity("interpret","解释型"));
		model.addAttribute("methods",methods);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("single","单常模"));
		types.add(new MapEntity("complex","多常模"));
		model.addAttribute("types",types);

		List<MapEntity> userTypes = new ArrayList<>();
		userTypes.add(new MapEntity("general","普通用户"));
		userTypes.add(new MapEntity("enterprise","企业用户"));
		userTypes.add(new MapEntity("school","学校用户"));
		model.addAttribute("userTypes",userTypes);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","发布"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("gaugeCategorys",gaugeCategoryService.findAll());

		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		return "/admin/gauge/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Gauge gauge, Long gaugeCategoryId, Long [] tagIds){
		Gauge entity = new Gauge();	

		entity.setBrokerage(gauge.getBrokerage());

		entity.setThumbnail(gauge.getThumbnail());

		entity.setContent(gauge.getContent());

		entity.setDeleted(false);

		entity.setDistribution(gauge.getDistribution());

		entity.setEvaluation(gauge.getEvaluation() == null ? 0 : gauge.getEvaluation());

		entity.setMarketPrice(gauge.getMarketPrice());

		entity.setNotice(gauge.getNotice());

		entity.setPrice(gauge.getPrice());

		entity.setRevisionNote(gauge.getRevisionNote());

		entity.setSubTitle(gauge.getSubTitle());

		entity.setTitle(gauge.getTitle());

		entity.setMethod(gauge.getMethod());

		entity.setType(gauge.getType());

		entity.setUserType(gauge.getUserType());

		entity.setTags(tagService.findList(tagIds));

		entity.setDevi(BigDecimal.ZERO);

		entity.setTavg(BigDecimal.ZERO);

		entity.setSpots(gauge.getSpots());

		entity.setStatus(gauge.getStatus());

		if (gaugeCategoryId!=null) {
			entity.setGaugeCategory(gaugeCategoryService.find(gaugeCategoryId));
		}

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gaugeService.save(entity);
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
            gaugeService.delete(ids);
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

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("combined","组合型"));
		methods.add(new MapEntity("interpret","解释型"));
		model.addAttribute("methods",methods);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("single","单常模"));
		types.add(new MapEntity("complex","多常模"));
		model.addAttribute("types",types);

		List<MapEntity> userTypes = new ArrayList<>();
		userTypes.add(new MapEntity("general","普通用户"));
		userTypes.add(new MapEntity("enterprise","企业用户"));
		userTypes.add(new MapEntity("school","学校用户"));
		model.addAttribute("userTypes",userTypes);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","发布"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("gaugeCategorys",gaugeCategoryService.findAll());

		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		model.addAttribute("data",gaugeService.find(id));

		return "/admin/gauge/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Gauge gauge, Long gaugeCategoryId, Long [] tagIds){
		Gauge entity = gaugeService.find(gauge.getId());

		entity.setBrokerage(gauge.getBrokerage());

		entity.setContent(gauge.getContent());

		entity.setDeleted(false);

		entity.setDistribution(gauge.getDistribution());

		entity.setEvaluation(gauge.getEvaluation() == null ? 0 : gauge.getEvaluation());

		entity.setMarketPrice(gauge.getMarketPrice());

		entity.setNotice(gauge.getNotice());

		entity.setPrice(gauge.getPrice());

		entity.setRevisionNote(gauge.getRevisionNote());

		entity.setSubTitle(gauge.getSubTitle());

		entity.setTitle(gauge.getTitle());

		entity.setMethod(gauge.getMethod());

		entity.setType(gauge.getType());

		entity.setTavg(gauge.getTavg());

		entity.setDevi(gauge.getDevi());

		entity.setUserType(gauge.getUserType());

		entity.setTags(tagService.findList(tagIds));

		entity.setStatus(gauge.getStatus());

		entity.setSpots(gauge.getSpots());

		if (gaugeCategoryId!=null) {
			entity.setGaugeCategory(gaugeCategoryService.find(gaugeCategoryId));
		}
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gaugeService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Gauge.Type type, Gauge.UserType userType, Gauge.Status status, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}
		if (userType!=null) {
			Filter userTypeFilter = new Filter("userType", Filter.Operator.eq, userType);
			filters.add(userTypeFilter);
		}
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		Page<Gauge> page = gaugeService.findPage(beginDate,endDate,null,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}

	/**
	 * 产品档案视图
	 */
	@RequestMapping(value = "/productView", method = RequestMethod.GET)
	public String productView(Long id, ModelMap model) {
		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("productCategorys",productCategoryService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("distributions",distributionService.findAll());

		model.addAttribute("product",productService.find(id));
		return "/admin/gauge/view/productView";
	}



	/**
	 *  检查语法
	 */
	@RequestMapping(value = "/expr_check")
	@ResponseBody
	public Message exprCheck(Long id,HttpServletRequest request){

		Gauge gauge = gaugeService.find(id);
		if (gauge==null) {
			return Message.error("无效量表编号");
		}

		if (gauge.getGaugeQuestions().size()==0) {
			return Message.error("请维护题库");
		}

		if (gauge.getGaugeGenes().size()==0) {
			return Message.error("请维护因子");
		}

		if (gauge.getGaugeResults().size()==0) {
			return Message.error("请维护结果");
		}

		Evaluation  eval =  new Evaluation();
		eval.setDeleted(false);
		eval.setEval(0L);
		eval.setThumbnail(gauge.getThumbnail());
		eval.setEvalStatus(Evaluation.EvalStatus.waiting);
		eval.setGauge(gauge);
		eval.setPrice(gauge.getPrice());
		eval.setRebate(BigDecimal.ZERO);
		eval.setTitle(gauge.getTitle());
		eval.setSubTitle(gauge.getSubTitle());
		eval.setTotal(new Long(gauge.getGaugeQuestions().size()));

		eval.setEval(new Long(eval.getEvalAnswers().size()));

		List<EvalAnswer> evals = new ArrayList<EvalAnswer>();

		for (GaugeQuestion q:gauge.getGaugeQuestions()) {
			EvalAnswer eas = new EvalAnswer();
			eas.setType(q.getType());
			eas.setAnswer(1L);
			eas.setContent(q.getContent());
			eas.setTitle(q.getTitle());
			eas.setEvaluation(eval);
			eas.setGauge(eval.getGauge());
			eas.setGaugeQuestion(q);
			eas.setScore(BigDecimal.ONE);
			evals.add(eas);

		}

		eval.setEvalAnswers(evals);

		try {
			GeneCalculator calculator = new GeneCalculator();
			calculator.calcAll(eval);
			if (calculator.getResults().size()==0) {
				return Message.error("无效结果");
			}
			eval.setResult(calculator.getHtml());
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}
		return Message.success("检测合法");
	}


}