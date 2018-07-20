package net.wit.controller.admin;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Order;
import net.wit.Pageable;

import net.wit.controller.model.ArticleContentModel;
import net.wit.controller.model.ArticleModel;
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

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "weixinUpServiceImpl")
	private WeixinUpService weixinUpService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

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
		mediaTypes.add(new MapEntity("html","系统图文"));
		mediaTypes.add(new MapEntity("article","用户图文"));
		mediaTypes.add(new MapEntity("product","商品详情"));
		mediaTypes.add(new MapEntity("video","短视频"));
		model.addAttribute("mediaTypes",mediaTypes);

		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		return "/admin/article/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
        Admin admin = adminService.getCurrent();
		List<MapEntity> authoritys = new ArrayList<>();
		authoritys.add(new MapEntity("isPublic","公开"));
		authoritys.add(new MapEntity("isShare","不公开"));
		authoritys.add(new MapEntity("isEncrypt","加密"));
		authoritys.add(new MapEntity("isPrivate","私秘"));
		model.addAttribute("authoritys",authoritys);


		List<MapEntity> mediaTypes = new ArrayList<>();
		mediaTypes.add(new MapEntity("html","系统图文"));
		mediaTypes.add(new MapEntity("article","用户图文"));
		mediaTypes.add(new MapEntity("product","商品详情"));
		mediaTypes.add(new MapEntity("video","短视频"));
		model.addAttribute("mediaTypes",mediaTypes);

//
		model.addAttribute("articleCategorys",articleCategoryService.findAll());

		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("member", Filter.Operator.eq,admin.getEnterprise().getMember()));
		model.addAttribute("articleCatalogs",articleCatalogService.findList(null,null,filters,null));

		model.addAttribute("templates",templateService.findList(Template.Type.article));

		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		return "/admin/article/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Article article, Long templateId, Long articleCategoryId,Long articleCatalogId, Long areaId, Long [] tagIds){

		Admin admin = adminService.getCurrent();
		Article entity = new Article();

		entity.setAuthority(article.getAuthority());

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

		entity.setMediaType(Article.ArticleType.html);

		entity.setMusic(article.getMusic());

		entity.setReview(0L);

		entity.setTitle(article.getTitle());

		entity.setArticleCategory(articleCategoryService.find(articleCategoryId));

		entity.setArticleCatalog(articleCatalogService.find(articleCatalogId));

		entity.setDeleted(false);

		entity.setArea(areaService.find(areaId));

		if (admin.isManager()) {
			messageService.GMInit(net.wit.entity.Message.Type.message);

			entity.setMember(memberService.findByUsername("gm_10202"));
		} else {
			entity.setMember(admin.getEnterprise().getMember());
		}

		entity.setTemplate(templateService.find(templateId));

		entity.setIsDraft(true);

		entity.setThumbnail(article.getThumbnail());

		entity.setIsPitch(article.getIsPitch());

		entity.setVotes(null);

		entity.setIsAudit(false);

		entity.setShare(0L);

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
		Admin admin = adminService.getCurrent();

		List<MapEntity> authoritys = new ArrayList<>();
		authoritys.add(new MapEntity("isPublic","公开"));
		authoritys.add(new MapEntity("isShare","不公开"));
		authoritys.add(new MapEntity("isEncrypt","加密"));
		authoritys.add(new MapEntity("isPrivate","私秘"));
		model.addAttribute("authoritys",authoritys);

		List<MapEntity> mediaTypes = new ArrayList<>();
		mediaTypes.add(new MapEntity("html","系统图文"));
		mediaTypes.add(new MapEntity("article","用户图文"));
		mediaTypes.add(new MapEntity("product","商品详情"));
		mediaTypes.add(new MapEntity("video","短视频"));
		model.addAttribute("mediaTypes",mediaTypes);

		model.addAttribute("articleCategorys",articleCategoryService.findAll());

		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("member", Filter.Operator.eq,admin.getEnterprise().getMember()));
		model.addAttribute("articleCatalogs",articleCatalogService.findList(null,null,filters,null));

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

		if (entity.getMediaType().equals(Article.ArticleType.html)) {
			entity.setContent(article.getContent());
		}

		entity.setMediaType(article.getMediaType());

		entity.setTitle(article.getTitle());

		entity.setArticleCategory(articleCategoryService.find(articleCategoryId));

		entity.setArticleCatalog(articleCatalogService.find(articleCatalogId));

		entity.setTemplate(templateService.find(templateId));

		entity.setThumbnail(article.getThumbnail());

		entity.setTags(tagService.findList(tagIds));

		entity.setIsPitch(article.getIsPitch());

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

		entity.setIsAudit(!entity.getIsAudit());

		if (!isValid(entity)) {
			return Message.error("admin.data.valid");
		}

		try {
			articleService.update(entity);
			if (entity.getIsAudit()) {
				return Message.success(entity, "发布成功");
			} else  {
				return Message.success(entity, "取消成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("操作失败");
		}

	}


	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Date beginDate, Date endDate, Long tagIds, Article.Authority authority, Article.MediaType mediaType, Pageable pageable,String searchValue, ModelMap model) {

		Admin admin=adminService.getCurrent();


		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (authority!=null) {
			Filter authorityFilter = new Filter("authority", Filter.Operator.eq, authority);
			filters.add(authorityFilter);
		}
		if (mediaType!=null) {
			Filter mediaTypeFilter = new Filter("mediaType", Filter.Operator.eq, mediaType);
			filters.add(mediaTypeFilter);
		}

		//判断用户公司属于哪种企业类型

		if(!admin.isManager()){

				Filter mediaTypeFilter = new Filter("member", Filter.Operator.eq, admin.getEnterprise().getMember());
				filters.add(mediaTypeFilter);

		}

		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("title", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}

		Page<Article> page = articleService.findPage(beginDate,endDate,tagService.findList(tagIds),pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}

	/**
	 * 文章推广
	 */
	@RequestMapping(value = "/propaganda", method = RequestMethod.POST)
	public @ResponseBody
	Message Propaganda(Long[] ids, HttpServletRequest request){

		Admin admin=adminService.getCurrent();
		//判断用户有没有所属企业
		if(admin.getEnterprise()==null){
			return Message.error("企业不存在");
		}

		//判断用户公司属于哪种企业类型
		Enterprise enterprise=admin.getEnterprise();
		if(enterprise==null){
			return Message.error("您还未绑定企业");
		}

		//判断企业是否被删除
		if(enterprise.getDeleted()){
			Message.error("您的企业不存在");
		}

		if(enterprise.getMember()==null){
			return Message.error("该专栏商家尚未入驻");
		}

		Member member=enterprise.getMember();
		Topic topic=member.getTopic();
		if(topic==null){
			return Message.error("该专栏无效");
		}

		//比较该专栏过期时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date overtime=topic.getExpire();
		Date nowtime=new Date();
		if(overtime.before(nowtime)){
			return Message.error("该专栏已到期");
		}

		//专栏公众号设置
		if(topic.getConfig()==null||topic.getConfig().getWxAppId().equals("")||topic.getConfig().getWxAppSerect().equals("")){
			return Message.error("您未绑定公众号");
		}

		try {
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			weixinUpService.ArticleUpLoad(ids,topic.getConfig().getWxAppId(),topic.getConfig().getWxAppSerect(),rootPath);
			return Message.success("发布成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("发布失败");
		}
	}

}