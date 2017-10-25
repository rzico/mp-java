package net.wit.controller.mch;

import net.wit.*;
import net.wit.entity.ArticleProduct;
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
 * @ClassName: ArticleProductController
 * @author 降魔战队
 * @date 2017-10-11 15:37:5
 */
 
@Controller("mchArticleProductController")
@RequestMapping("/mch/articleProduct")
public class ArticleProductController extends BaseController {
	@Resource(name = "articleProductServiceImpl")
	private ArticleProductService articleProductService;
	
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

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

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("products",productService.findAll());

		return "/mch/articleProduct/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("products",productService.findAll());

		return "/mch/articleProduct/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(ArticleProduct articleProduct, Long articleId, Long productId){
		ArticleProduct entity = new ArticleProduct();	

		entity.setCreateDate(articleProduct.getCreateDate());

		entity.setModifyDate(articleProduct.getModifyDate());

		entity.setArticle(articleService.find(articleId));

		entity.setProduct(productService.find(productId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            articleProductService.save(entity);
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
            articleProductService.delete(ids);
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

		model.addAttribute("products",productService.findAll());

		model.addAttribute("data",articleProductService.find(id));

		return "/mch/articleProduct/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(ArticleProduct articleProduct, Long articleId, Long productId){
		ArticleProduct entity = articleProductService.find(articleProduct.getId());
		
		entity.setCreateDate(articleProduct.getCreateDate());

		entity.setModifyDate(articleProduct.getModifyDate());

		entity.setArticle(articleService.find(articleId));

		entity.setProduct(productService.find(productId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            articleProductService.update(entity);
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

		Page<ArticleProduct> page = articleProductService.findPage(beginDate,endDate,pageable);
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
		return "/mch/articleProduct/view/articleView";
	}


	/**
	 * 产品档案视图
	 */
	@RequestMapping(value = "/productView", method = RequestMethod.GET)
	public String productView(Long id, ModelMap model) {
		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("productCategorys",productCategoryService.findAll());

		model.addAttribute("product",productService.find(id));
		return "/mch/articleProduct/view/productView";
	}



}