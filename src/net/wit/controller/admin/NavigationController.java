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
import net.wit.entity.Navigation;
import net.wit.service.NavigationService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: NavigationController
 * @author 降魔战队
 * @date 2018-6-12 10:12:26
 */
 
@Controller("adminNavigationController")
@RequestMapping("/admin/navigation")
public class NavigationController extends BaseController {
	@Resource(name = "navigationServiceImpl")
	private NavigationService navigationService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "articleCatalogServiceImpl")
	private ArticleCatalogService articleCatalogService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Long topicId,ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("product","商品"));
		types.add(new MapEntity("news","新品"));
		types.add(new MapEntity("video","视频"));
		types.add(new MapEntity("images","图集"));
		types.add(new MapEntity("promotion","促销"));
		types.add(new MapEntity("recommend","推荐"));
		types.add(new MapEntity("dragon","拼团"));
		types.add(new MapEntity("mall","商城"));
		model.addAttribute("types",types);
		model.addAttribute("topicId",topicId);

		return "/admin/navigation/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long topicId,ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("product","商品"));
		types.add(new MapEntity("news","新品"));
		types.add(new MapEntity("video","视频"));
		types.add(new MapEntity("images","图集"));
		types.add(new MapEntity("promotion","促销"));
		types.add(new MapEntity("recommend","推荐"));
		types.add(new MapEntity("dragon","拼团"));
		types.add(new MapEntity("mall","商城"));
		model.addAttribute("types",types);
		model.addAttribute("topicId",topicId);

		Topic topic = topicService.find(topicId);

		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("member", Filter.Operator.eq,topic.getMember()));

	    model.addAttribute("articleCatalog",articleCatalogService.findList(null,null,filters,null));

		model.addAttribute("productCatagorys",productCategoryService.findList(null,null,filters,null));


		return "/admin/navigation/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Navigation navigation,Long topicId){
		Navigation entity = new Navigation();	

		entity.setCreateDate(navigation.getCreateDate());

		entity.setModifyDate(navigation.getModifyDate());

		entity.setOrders(navigation.getOrders() == null ? 0 : navigation.getOrders());

		entity.setExtend(navigation.getExtend());

		entity.setLogo(navigation.getLogo());

		entity.setName(navigation.getName());

		entity.setType(navigation.getType());

		Topic topic = topicService.find(topicId);
		entity.setOwner(topic.getMember());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            navigationService.save(entity);
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
            navigationService.delete(ids);
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
		Navigation navigation = navigationService.find(id);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("product","商品"));
		types.add(new MapEntity("news","新品"));
		types.add(new MapEntity("video","视频"));
		types.add(new MapEntity("images","图集"));
		types.add(new MapEntity("promotion","促销"));
		types.add(new MapEntity("recommend","推荐"));
		types.add(new MapEntity("dragon","拼团"));
		types.add(new MapEntity("mall","商城"));
		model.addAttribute("types",types);

		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("member", Filter.Operator.eq,navigation.getOwner()));

		model.addAttribute("articleCatalog",articleCatalogService.findList(null,null,filters,null));

		model.addAttribute("productCatagorys",productCategoryService.findList(null,null,filters,null));


		model.addAttribute("data",navigation);

		return "/admin/navigation/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Navigation navigation){
		Navigation entity = navigationService.find(navigation.getId());
		
		entity.setCreateDate(navigation.getCreateDate());

		entity.setModifyDate(navigation.getModifyDate());

		entity.setOrders(navigation.getOrders() == null ? 0 : navigation.getOrders());

		entity.setExtend(navigation.getExtend());

		entity.setLogo(navigation.getLogo());

		entity.setName(navigation.getName());

		entity.setType(navigation.getType());

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            navigationService.update(entity);
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
	public Message list(Long topicId,Date beginDate, Date endDate, Navigation.Type type, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Topic topic = topicService.find(topicId);
		Filter ownerFilter = new Filter("owner", Filter.Operator.eq, topic.getMember());
		filters.add(ownerFilter);

		Page<Navigation> page = navigationService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
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
		return "/admin/navigation/view/memberView";
	}



}