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
import net.wit.entity.Article;
import net.wit.service.ArticleService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-10-11 15:37:4
 */
 
@Controller("adminArticleController")
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;

	@Resource(name = "articleCatalogServiceImpl")
	private ArticleCatalogService articleCatalogService;

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "weixinUpServiceImpl")
	private WeixinUpService weixinUpService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> authoritys = new ArrayList<>();
		authoritys.add(new MapEntity("isPublic","公开"));
		authoritys.add(new MapEntity("isShare","不公开"));
		authoritys.add(new MapEntity("isEncrypt","加密"));
		authoritys.add(new MapEntity("isPrivate","私秘"));
		model.addAttribute("authoritys",authoritys);

		List<MapEntity> mediaTypes = new ArrayList<>();
		mediaTypes.add(new MapEntity("html","文本"));
		mediaTypes.add(new MapEntity("image","图文"));
		mediaTypes.add(new MapEntity("audio","音频"));
		mediaTypes.add(new MapEntity("video","视频"));
		model.addAttribute("mediaTypes",mediaTypes);

		model.addAttribute("articleCategorys",articleCategoryService.findAll());

		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		return "/admin/article/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> authoritys = new ArrayList<>();
		authoritys.add(new MapEntity("isPublic","公开"));
		authoritys.add(new MapEntity("isShare","不公开"));
		authoritys.add(new MapEntity("isEncrypt","加密"));
		authoritys.add(new MapEntity("isPrivate","私秘"));
		model.addAttribute("authoritys",authoritys);

		model.addAttribute("articleCategorys",articleCategoryService.findAll());

		model.addAttribute("templates",templateService.findList(Template.Type.article));

		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		return "/admin/article/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Article article, Long templateId, Long articleCategoryId, Long areaId, Long [] tagIds){
		Article entity = new Article();

		entity.setAuthority(article.getAuthority());

		entity.setIsPitch(false);

		entity.setIsPublish(true);

		entity.setIsReview(article.getIsReview());

		entity.setIsReward(article.getIsReward());

		entity.setIsExample(article.getIsExample());

		entity.setIsTop(article.getIsTop());


		entity.setAuthor(article.getAuthor());

		entity.setContent(article.getContent());

		entity.setFavorite(0L);

		entity.setHits(0L);

		entity.setLaud(0L);

		entity.setMediaType(Article.MediaType.html);

		entity.setMusic(article.getMusic());

		entity.setReview(0L);

		entity.setTitle(article.getTitle());

		entity.setArticleCategory(articleCategoryService.find(articleCategoryId));

		entity.setDeleted(false);

		entity.setArea(areaService.find(areaId));

		entity.setMember(memberService.find(1L));

		entity.setTemplate(templateService.find(templateId));

		entity.setIsDraft(true);

		entity.setThumbnail(article.getThumbnail());

		entity.setVotes(null);

		entity.setTags(tagService.findList(tagIds));

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            articleService.save(entity);
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
            articleService.delete(ids);
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

		List<MapEntity> authoritys = new ArrayList<>();
		authoritys.add(new MapEntity("isPublic","公开"));
		authoritys.add(new MapEntity("isShare","不公开"));
		authoritys.add(new MapEntity("isEncrypt","加密"));
		authoritys.add(new MapEntity("isPrivate","私秘"));
		model.addAttribute("authoritys",authoritys);

		List<MapEntity> mediaTypes = new ArrayList<>();
		mediaTypes.add(new MapEntity("html","文本"));
		mediaTypes.add(new MapEntity("image","图文"));
		mediaTypes.add(new MapEntity("audio","音频"));
		mediaTypes.add(new MapEntity("video","视频"));
		model.addAttribute("mediaTypes",mediaTypes);

		model.addAttribute("articleCategorys",articleCategoryService.findAll());

		model.addAttribute("templates",templateService.findList(Template.Type.article));

		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		model.addAttribute("data",articleService.find(id));

		return "/admin/article/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Article article, Long templateId, Long articleCatalogId, Long articleCategoryId, Long areaId, Long memberId, Long [] tagIds){
		Article entity = articleService.find(article.getId());

		entity.setAuthority(article.getAuthority());

		entity.setIsPublish(article.getIsPublish());

		entity.setIsReview(article.getIsReview());

		entity.setIsReward(article.getIsReward());

		entity.setIsExample(article.getIsExample());

		entity.setIsTop(article.getIsTop());


		entity.setAuthor(article.getAuthor());

		entity.setContent(article.getContent());

		entity.setMediaType(article.getMediaType());

		entity.setTitle(article.getTitle());

		entity.setArticleCategory(articleCategoryService.find(articleCategoryId));

		entity.setTemplate(templateService.find(templateId));

		entity.setThumbnail(article.getThumbnail());

		entity.setTags(tagService.findList(tagIds));

		entity.setIsAudit(false);
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }

        try {
            articleService.update(entity);
            return Message.success(entity,"admin.update.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.update.error");
        }

	}


	/**
	 * 发布
	 */
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ResponseBody
	public Message publish(Long articleId){
		Article entity = articleService.find(articleId);

		entity.setIsDraft(false);

		entity.setIsAudit(true);

		if (!isValid(entity)) {
			return Message.error("admin.data.valid");
		}

		try {
			articleService.update(entity);
			return Message.success(entity,"发布成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("发布失败");
		}

	}


	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Date beginDate, Date endDate, Long tagIds, Article.Authority authority, Article.MediaType mediaType, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (authority!=null) {
			Filter authorityFilter = new Filter("authority", Filter.Operator.eq, authority);
			filters.add(authorityFilter);
		}
		if (mediaType!=null) {
			Filter mediaTypeFilter = new Filter("mediaType", Filter.Operator.eq, mediaType);
			filters.add(mediaTypeFilter);
		}

		Page<Article> page = articleService.findPage(beginDate,endDate,tagService.findList(tagIds),pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 模版管理视图
	 */
	@RequestMapping(value = "/templateView", method = RequestMethod.GET)
	public String templateView(Long id, ModelMap model) {
		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("product","商品"));
		model.addAttribute("types",types);

		model.addAttribute("template",templateService.find(id));
		return "/admin/article/view/templateView";
	}


	/**
	 * 文集管理视图
	 */
	@RequestMapping(value = "/articleCatalogView", method = RequestMethod.GET)
	public String articleCatalogView(Long id, ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("articleCatalog",articleCatalogService.find(id));
		return "/admin/article/view/articleCatalogView";
	}


	/**
	 * 文章分类视图
	 */
	@RequestMapping(value = "/articleCategoryView", method = RequestMethod.GET)
	public String articleCategoryView(Long id, ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("articleCategory",articleCategoryService.find(id));
		return "/admin/article/view/articleCategoryView";
	}


	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/admin/article/view/areaView";
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

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/article/view/memberView";
	}


	/**
	 * 文章推广
	 */
	@RequestMapping(value = "/propaganda", method = RequestMethod.POST)
	public @ResponseBody
	Message Propaganda(Long[] ids){
		try {
			//芸店公众号
			String appID="wx88a1ec3b5c3bc9c3";
			String appsecret="f5e7d000d00788053c50ca6b3a442d20";
			weixinUpService.ArticleUpLoad(ids,appID,appsecret);
			return Message.success("admin.delete.success");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.delete.error");
		}
	}


}