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

		List<MapEntity> userTypes = new ArrayList<>();
		userTypes.add(new MapEntity("general","普通用户"));
		userTypes.add(new MapEntity("enterprise","企业用户"));
		userTypes.add(new MapEntity("school","学校用户"));
		model.addAttribute("userTypes",userTypes);

		model.addAttribute("products",productService.findAll());

		return "/admin/gauge/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

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

		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		return "/admin/gauge/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Gauge gauge, Long productId){
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

		entity.setType(gauge.getType());

		entity.setUserType(gauge.getUserType());

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

		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		model.addAttribute("data",gaugeService.find(id));

		return "/admin/gauge/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Gauge gauge, Long productId){
		Gauge entity = gaugeService.find(gauge.getId());
		
		entity.setCreateDate(gauge.getCreateDate());

		entity.setModifyDate(gauge.getModifyDate());

		entity.setBrokerage(gauge.getBrokerage());

		entity.setContent(gauge.getContent());

		entity.setDeleted(gauge.getDeleted());

		entity.setDistribution(gauge.getDistribution());

		entity.setEvaluation(gauge.getEvaluation() == null ? 0 : gauge.getEvaluation());

		entity.setMarketPrice(gauge.getMarketPrice());

		entity.setNotice(gauge.getNotice());

		entity.setPrice(gauge.getPrice());

		entity.setRevisionNote(gauge.getRevisionNote());

		entity.setSubTitle(gauge.getSubTitle());

		entity.setTitle(gauge.getTitle());

		entity.setType(gauge.getType());

		entity.setUserType(gauge.getUserType());

		entity.setProduct(productService.find(productId));
		
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
	public Message list(Date beginDate, Date endDate, Gauge.Type type, Gauge.UserType userType, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}
		if (userType!=null) {
			Filter userTypeFilter = new Filter("userType", Filter.Operator.eq, userType);
			filters.add(userTypeFilter);
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



}