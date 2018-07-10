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
import net.wit.entity.RedPackage;
import net.wit.service.RedPackageService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: RedPackageController
 * @author 降魔战队
 * @date 2018-7-6 10:38:48
 */
 
@Controller("adminRedPackageController")
@RequestMapping("/admin/redPackage")
public class RedPackageController extends BaseController {
	@Resource(name = "redPackageServiceImpl")
	private RedPackageService redPackageService;
	
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;

	@Resource(name = "articleCatalogServiceImpl")
	private ArticleCatalogService articleCatalogService;

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

//		model.addAttribute("articles",articleService.findAll());
//
//		model.addAttribute("articles",memberService.findAll());
//
//		model.addAttribute("members",memberService.findAll());

		return "/admin/redPackage/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("articles",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/redPackage/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(RedPackage redPackage, Long articleId, Long memberId){
		RedPackage entity = new RedPackage();	

		entity.setCreateDate(redPackage.getCreateDate());

		entity.setModifyDate(redPackage.getModifyDate());

		entity.setAmount(redPackage.getAmount());

		entity.setArticle(articleService.find(articleId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            redPackageService.save(entity);
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
            redPackageService.delete(ids);
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

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("articles",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("data",redPackageService.find(id));

		return "/admin/redPackage/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(RedPackage redPackage, Long articleId, Long memberId){
		RedPackage entity = redPackageService.find(redPackage.getId());
		
		entity.setCreateDate(redPackage.getCreateDate());

		entity.setModifyDate(redPackage.getModifyDate());

		entity.setAmount(redPackage.getAmount());

		entity.setArticle(articleService.find(articleId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            redPackageService.update(entity);
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

		Page<RedPackage> page = redPackageService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * ????视图
	 */
	@RequestMapping(value = "/articleView", method = RequestMethod.GET)
	public String articleView(Long id, ModelMap model) {
		List<MapEntity> authoritys = new ArrayList<>();
		authoritys.add(new MapEntity("isPublic","??"));
		authoritys.add(new MapEntity("isShare","???"));
		authoritys.add(new MapEntity("isEncrypt","??"));
		authoritys.add(new MapEntity("isPrivate","??"));
		model.addAttribute("authoritys",authoritys);

		List<MapEntity> mediaTypes = new ArrayList<>();
		mediaTypes.add(new MapEntity("image","??"));
		mediaTypes.add(new MapEntity("audio","??"));
		mediaTypes.add(new MapEntity("video","??"));
		model.addAttribute("mediaTypes",mediaTypes);

		model.addAttribute("articleCatalogs",articleCatalogService.findAll());

		model.addAttribute("articleCategorys",articleCategoryService.findAll());

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("templates",templateService.findAll());

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("article",articleService.find(id));
		return "/admin/redPackage/view/articleView";
	}


	/**
	 * ????视图
	 */
	@RequestMapping(value = "/memberView", method = RequestMethod.GET)
	public String memberView(Long id, ModelMap model) {
		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","?"));
		genders.add(new MapEntity("female","?"));
		genders.add(new MapEntity("secrecy","??"));
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

		List<MapEntity> memberTypes = new ArrayList<>();
		memberTypes.add(new MapEntity("????","SELF"));
		memberTypes.add(new MapEntity("?????","COMPONENT"));
		model.addAttribute("memberTypes",memberTypes);

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/redPackage/view/memberView";
	}



}