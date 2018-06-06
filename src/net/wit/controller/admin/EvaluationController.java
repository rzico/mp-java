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
import net.wit.entity.Evaluation;
import net.wit.service.EvaluationService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: EvaluationController
 * @author 降魔战队
 * @date 2018-3-14 23:23:53
 */
 
@Controller("adminEvaluationController")
@RequestMapping("/admin/evaluation")
public class EvaluationController extends BaseController {
	@Resource(name = "evaluationServiceImpl")
	private EvaluationService evaluationService;
	
	@Resource(name = "gaugeServiceImpl")
	private GaugeService gaugeService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "gaugeCategoryServiceImpl")
	private GaugeCategoryService gaugeCategoryService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "organizationServiceImpl")
	private OrganizationService organizationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> evalStatuss = new ArrayList<>();
		evalStatuss.add(new MapEntity("waiting","待付款"));
		evalStatuss.add(new MapEntity("paid","已付款"));
		evalStatuss.add(new MapEntity("completed","已完成"));
		evalStatuss.add(new MapEntity("cancelled","已取消"));
		model.addAttribute("evalStatuss",evalStatuss);
		model.addAttribute("organizations",organizationService.findAll());



		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("single","单常模"));
		types.add(new MapEntity("complex","多常模"));
		model.addAttribute("types",types);

		List<MapEntity> userTypes = new ArrayList<>();
		userTypes.add(new MapEntity("general","普通用户"));
		userTypes.add(new MapEntity("enterprise","企业用户"));
		userTypes.add(new MapEntity("school","学校用户"));
		model.addAttribute("userTypes",userTypes);

		model.addAttribute("gaugeCategorys",gaugeCategoryService.findAll());

		return "/admin/evaluation/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> evalStatuss = new ArrayList<>();
		evalStatuss.add(new MapEntity("waiting","待付款"));
		evalStatuss.add(new MapEntity("paid","已付款"));
		evalStatuss.add(new MapEntity("completed","已完成"));
		evalStatuss.add(new MapEntity("cancelled","已取消"));
		model.addAttribute("evalStatuss",evalStatuss);

		return "/admin/evaluation/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Evaluation evaluation, Long gaugeId, Long memberId, Long promoterId){
		Evaluation entity = new Evaluation();	

		entity.setCreateDate(evaluation.getCreateDate());

		entity.setModifyDate(evaluation.getModifyDate());

		entity.setDeleted(evaluation.getDeleted());

		entity.setEval(evaluation.getEval() == null ? 0 : evaluation.getEval());

		entity.setEvalStatus(evaluation.getEvalStatus());

		entity.setPrice(evaluation.getPrice());

		entity.setResult(evaluation.getResult());

		entity.setSn(evaluation.getSn());

		entity.setSubTitle(evaluation.getSubTitle());

		entity.setTitle(evaluation.getTitle());

		entity.setTotal(evaluation.getTotal() == null ? 0 : evaluation.getTotal());

		entity.setGauge(gaugeService.find(gaugeId));

		entity.setMember(memberService.find(memberId));

		entity.setThumbnail(evaluation.getThumbnail());

		entity.setPromoter(memberService.find(promoterId));

		entity.setAttr1(evaluation.getAttr1());

		entity.setAttr2(evaluation.getAttr2());

		entity.setAttr3(evaluation.getAttr3());

		entity.setAttr4(evaluation.getAttr4());

		entity.setRebate(evaluation.getRebate());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            evaluationService.save(entity);
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
            evaluationService.delete(ids);
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

		List<MapEntity> evalStatuss = new ArrayList<>();
		evalStatuss.add(new MapEntity("waiting","待付款"));
		evalStatuss.add(new MapEntity("paid","已付款"));
		evalStatuss.add(new MapEntity("completed","已完成"));
		evalStatuss.add(new MapEntity("cancelled","已取消"));
		model.addAttribute("evalStatuss",evalStatuss);

		model.addAttribute("data",evaluationService.find(id));

		return "/admin/evaluation/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Evaluation evaluation, Long gaugeId, Long memberId, Long promoterId){
		Evaluation entity = evaluationService.find(evaluation.getId());
		
		entity.setCreateDate(evaluation.getCreateDate());

		entity.setModifyDate(evaluation.getModifyDate());

		entity.setDeleted(evaluation.getDeleted());

		entity.setEval(evaluation.getEval() == null ? 0 : evaluation.getEval());

		entity.setEvalStatus(evaluation.getEvalStatus());

		entity.setPrice(evaluation.getPrice());

		entity.setResult(evaluation.getResult());

		entity.setSn(evaluation.getSn());

		entity.setSubTitle(evaluation.getSubTitle());

		entity.setTitle(evaluation.getTitle());

		entity.setTotal(evaluation.getTotal() == null ? 0 : evaluation.getTotal());

		entity.setGauge(gaugeService.find(gaugeId));

		entity.setMember(memberService.find(memberId));

		entity.setThumbnail(evaluation.getThumbnail());

		entity.setPromoter(memberService.find(promoterId));

		entity.setAttr1(evaluation.getAttr1());

		entity.setAttr2(evaluation.getAttr2());

		entity.setAttr3(evaluation.getAttr3());

		entity.setAttr4(evaluation.getAttr4());

		entity.setRebate(evaluation.getRebate());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            evaluationService.update(entity);
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
	public Message list(Date beginDate, Date endDate,Long gaugeCategoryId,Gauge.UserType userType,Gauge.Type type,String organization,String searchValue, Evaluation.EvalStatus evalStatus, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (evalStatus!=null) {
			Filter evalStatusFilter = new Filter("evalStatus", Filter.Operator.eq, evalStatus);
			filters.add(evalStatusFilter);
		}
		if (organization!=null) {
			Filter organizationFilter = new Filter("attr1", Filter.Operator.eq, organization);
			filters.add(organizationFilter);
		}
		if (userType!=null) {
			Filter userTypeFilter = new Filter("userType", Filter.Operator.eq, userType);
			filters.add(userTypeFilter);
		}
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		if (searchValue!=null) {
			Filter titleFilter = new Filter("title", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(titleFilter);
		}

		if (gaugeCategoryId!=null) {
			Filter categoryFilter = new Filter("gaugeCategory", Filter.Operator.eq, gaugeCategoryService.find(gaugeCategoryId));
			filters.add(categoryFilter);
		}

		Page<Evaluation> page = evaluationService.findPage(beginDate,endDate,pageable);
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

		model.addAttribute("gauge",gaugeService.find(id));
		return "/admin/evaluation/view/gaugeView";
	}


	/**
	 * 会员管理视图
	 */
	@RequestMapping(value = "/memberView", method = RequestMethod.GET)
	public String memberView(Long id, ModelMap model) {
		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("occupations",occupationService.findAll());

		model.addAttribute("topics",topicService.findAll());

		List<MapEntity> vips = new ArrayList<>();
		vips.add(new MapEntity("vip1","vip1"));
		vips.add(new MapEntity("vip2","vip2"));
		vips.add(new MapEntity("vip3","vip3"));
		model.addAttribute("vips",vips);

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/evaluation/view/memberView";
	}



}