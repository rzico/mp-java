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
import net.wit.entity.ArticleReview;
import net.wit.service.ArticleReviewService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ArticleReviewController
 * @author 降魔战队
 * @date 2017-9-14 19:42:11
 */
 
@Controller("adminArticleReviewController")
@RequestMapping("/admin/articleReview")
public class ArticleReviewController extends BaseController {
	@Resource(name = "articleReviewServiceImpl")
	private ArticleReviewService articleReviewService;
	
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

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/articleReview/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/articleReview/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(ArticleReview articleReview, Long articleId, Long forArticleReviewId, Long memberId){
		ArticleReview entity = new ArticleReview();	

		entity.setCreateDate(articleReview.getCreateDate());

		entity.setModifyDate(articleReview.getModifyDate());

		entity.setContent(articleReview.getContent());

		entity.setIp(articleReview.getIp());

		entity.setArticle(articleService.find(articleId));

		entity.setForArticleReview(articleReviewService.find(forArticleReviewId));

		entity.setMember(memberService.find(memberId));

		entity.setDeleted(articleReview.getDeleted());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            articleReviewService.save(entity);
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
            articleReviewService.delete(ids);
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

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("data",articleReviewService.find(id));

		return "/admin/articleReview/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(ArticleReview articleReview, Long articleId, Long forArticleReviewId, Long memberId){
		ArticleReview entity = articleReviewService.find(articleReview.getId());
		
		entity.setCreateDate(articleReview.getCreateDate());

		entity.setModifyDate(articleReview.getModifyDate());

		entity.setContent(articleReview.getContent());

		entity.setIp(articleReview.getIp());

		entity.setArticle(articleService.find(articleId));

		entity.setForArticleReview(articleReviewService.find(forArticleReviewId));

		entity.setMember(memberService.find(memberId));

		entity.setDeleted(articleReview.getDeleted());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            articleReviewService.update(entity);
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

		Page<ArticleReview> page = articleReviewService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 文章管理视图
	 */
	@RequestMapping(value = "/articleView", method = RequestMethod.GET)
	public String articleView(Long id, ModelMap model) {
		List<MapEntity> authoritys = new ArrayList<>();
		authoritys.add(new MapEntity("isPublic","公开"));
		authoritys.add(new MapEntity("isShare","不会开"));
		authoritys.add(new MapEntity("isEncrypt","加密"));
		authoritys.add(new MapEntity("isPrivate","私秘"));
		model.addAttribute("authoritys",authoritys);

		List<MapEntity> mediaTypes = new ArrayList<>();
		mediaTypes.add(new MapEntity("image","图文"));
		mediaTypes.add(new MapEntity("audio","音频"));
		mediaTypes.add(new MapEntity("video","视频"));
		model.addAttribute("mediaTypes",mediaTypes);

		model.addAttribute("articleCatalogs",articleCatalogService.findAll());

		model.addAttribute("articleCategorys",articleCategoryService.findAll());

		model.addAttribute("templates",templateService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("article",articleService.find(id));
		return "/admin/articleReview/view/articleView";
	}


	/**
	 * 评价管理视图
	 */
	@RequestMapping(value = "/articleReviewView", method = RequestMethod.GET)
	public String articleReviewView(Long id, ModelMap model) {
		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("articleReview",articleReviewService.find(id));
		return "/admin/articleReview/view/articleReviewView";
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

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/articleReview/view/memberView";
	}



}