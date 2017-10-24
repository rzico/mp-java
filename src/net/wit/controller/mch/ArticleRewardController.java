package net.wit.controller.mch;

import net.wit.*;
import net.wit.entity.ArticleReward;
import net.wit.entity.BaseEntity.Save;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: ArticleRewardController
 * @author 降魔战队
 * @date 2017-10-11 15:37:6
 */
 
@Controller("mchArticleRewardController")
@RequestMapping("/mch/articleReward")
public class ArticleRewardController extends BaseController {

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "articleRewardServiceImpl")
	private ArticleRewardService articleRewardService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;

	@Resource(name = "articleCatalogServiceImpl")
	private ArticleCatalogService articleCatalogService;

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("authors",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("payments",paymentService.findAll());

		return "/admin/articleReward/list";

	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("authors",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("payments",paymentService.findAll());

		return "/admin/articleReward/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(ArticleReward articleReward, Long articleId, Long authorId, Long memberId){
		ArticleReward entity = new ArticleReward();	

		entity.setCreateDate(articleReward.getCreateDate());

		entity.setModifyDate(articleReward.getModifyDate());

		entity.setAmount(articleReward.getAmount());

		entity.setFee(articleReward.getFee());

		entity.setIp(articleReward.getIp());

		entity.setStatus(articleReward.getStatus());

		entity.setArticle(articleService.find(articleId));

		entity.setAuthor(memberService.find(authorId));

		entity.setMember(memberService.find(memberId));

		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            articleRewardService.save(entity);
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
            articleRewardService.delete(ids);
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

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("authors",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("payments",paymentService.findAll());

		model.addAttribute("data",articleRewardService.find(id));

		return "/admin/articleReward/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(ArticleReward articleReward, Long articleId, Long authorId, Long memberId){
		ArticleReward entity = articleRewardService.find(articleReward.getId());
		
		entity.setCreateDate(articleReward.getCreateDate());

		entity.setModifyDate(articleReward.getModifyDate());

		entity.setAmount(articleReward.getAmount());

		entity.setFee(articleReward.getFee());

		entity.setIp(articleReward.getIp());

		entity.setStatus(articleReward.getStatus());

		entity.setArticle(articleService.find(articleId));

		entity.setAuthor(memberService.find(authorId));

		entity.setMember(memberService.find(memberId));

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            articleRewardService.update(entity);
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
	public Message list(Date beginDate, Date endDate, ArticleReward.Status status, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		Page<ArticleReward> page = articleRewardService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 收款单视图
	 */
	@RequestMapping(value = "/paymentView", method = RequestMethod.GET)
	public String paymentView(Long id, ModelMap model) {
		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","在线支付"));
		methods.add(new MapEntity("offline","线下支付"));
		methods.add(new MapEntity("deposit","钱包支付"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("payment","消费支付"));
		types.add(new MapEntity("recharge","钱包充值"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("articleRewards",articleRewardService.findAll());

		model.addAttribute("payees",memberService.findAll());

		model.addAttribute("payment",paymentService.find(id));
		return "/admin/articleReward/view/paymentView";
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

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("templates",templateService.findAll());

		List<MapEntity> titleTypes = new ArrayList<>();
		titleTypes.add(new MapEntity("image1","单图"));
		titleTypes.add(new MapEntity("image2","2张图"));
		titleTypes.add(new MapEntity("image3","3张图"));
		titleTypes.add(new MapEntity("image4","4张图"));
		titleTypes.add(new MapEntity("image5","5张图"));
		titleTypes.add(new MapEntity("image6","6张图"));
		model.addAttribute("titleTypes",titleTypes);

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("article",articleService.find(id));
		return "/admin/articleReward/view/articleView";
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
		return "/admin/articleReward/view/memberView";
	}



}