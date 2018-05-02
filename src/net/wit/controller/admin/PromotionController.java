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
import net.wit.entity.Promotion;
import net.wit.service.PromotionService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: PromotionController
 * @author 降魔战队
 * @date 2018-4-28 22:28:57
 */
 
@Controller("adminPromotionController")
@RequestMapping("/admin/promotion")
public class PromotionController extends BaseController {
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "distributionServiceImpl")
	private DistributionService distributionService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("give","买N送N"));
		types.add(new MapEntity("gift","满A赠B"));
		model.addAttribute("types",types);

		model.addAttribute("gifts",productService.findAll());

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("owners",memberService.findAll());

		return "/admin/promotion/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("give","买N送N"));
		types.add(new MapEntity("gift","满A赠B"));
		model.addAttribute("types",types);

		model.addAttribute("gifts",productService.findAll());

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("owners",memberService.findAll());

		return "/admin/promotion/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Promotion promotion, Long goodsId, Long giftId, Long ownerId){
		Promotion entity = new Promotion();	

		entity.setCreateDate(promotion.getCreateDate());

		entity.setModifyDate(promotion.getModifyDate());

		entity.setOrders(promotion.getOrders() == null ? 0 : promotion.getOrders());

		entity.setDeleted(promotion.getDeleted());

		entity.setGiftQuantity(promotion.getGiftQuantity() == null ? 0 : promotion.getGiftQuantity());

		entity.setMinimumPrice(promotion.getMinimumPrice());

		entity.setQuantity(promotion.getQuantity() == null ? 0 : promotion.getQuantity());

		entity.setType(promotion.getType());

		entity.setGift(productService.find(giftId));

		entity.setGoods(goodsService.find(goodsId));

		entity.setOwner(memberService.find(ownerId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            promotionService.save(entity);
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
            promotionService.delete(ids);
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
		types.add(new MapEntity("give","买N送N"));
		types.add(new MapEntity("gift","满A赠B"));
		model.addAttribute("types",types);

		model.addAttribute("gifts",productService.findAll());

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("owners",memberService.findAll());

		model.addAttribute("data",promotionService.find(id));

		return "/admin/promotion/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Promotion promotion, Long goodsId, Long giftId, Long ownerId){
		Promotion entity = promotionService.find(promotion.getId());
		
		entity.setCreateDate(promotion.getCreateDate());

		entity.setModifyDate(promotion.getModifyDate());

		entity.setOrders(promotion.getOrders() == null ? 0 : promotion.getOrders());

		entity.setDeleted(promotion.getDeleted());

		entity.setGiftQuantity(promotion.getGiftQuantity() == null ? 0 : promotion.getGiftQuantity());

		entity.setMinimumPrice(promotion.getMinimumPrice());

		entity.setQuantity(promotion.getQuantity() == null ? 0 : promotion.getQuantity());

		entity.setType(promotion.getType());

		entity.setGift(productService.find(giftId));

		entity.setGoods(goodsService.find(goodsId));

		entity.setOwner(memberService.find(ownerId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            promotionService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Promotion.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Promotion> page = promotionService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 商品管理视图
	 */
	@RequestMapping(value = "/goodsView", method = RequestMethod.GET)
	public String goodsView(Long id, ModelMap model) {


		model.addAttribute("goods",goodsService.find(id));
		return "/admin/promotion/view/goodsView";
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

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("product","实物"));
		types.add(new MapEntity("dummy","虚拟"));
		model.addAttribute("types",types);

		model.addAttribute("product",productService.find(id));
		return "/admin/promotion/view/productView";
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

		model.addAttribute("agents",enterpriseService.findAll());

		model.addAttribute("operates",enterpriseService.findAll());

		model.addAttribute("personals",enterpriseService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/promotion/view/memberView";
	}



}