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
import net.wit.entity.ArticleCategory;
import net.wit.service.ArticleCategoryService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ArticleCategoryController
 * @author 降魔战队
 * @date 2017-9-14 19:42:10
 */
 
@Controller("adminArticleCategoryController")
@RequestMapping("/admin/articleCategory")
public class ArticleCategoryController extends BaseController {
	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;
	
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("articles",articleService.findAll());

		return "/admin/articleCategory/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("articles",articleService.findAll());

		return "/admin/articleCategory/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(ArticleCategory articleCategory, Long parentId){
		ArticleCategory entity = new ArticleCategory();	

		entity.setCreateDate(articleCategory.getCreateDate());

		entity.setModifyDate(articleCategory.getModifyDate());

		entity.setOrder(articleCategory.getOrder() == null ? 0 : articleCategory.getOrder());

		entity.setGrade(articleCategory.getGrade() == null ? 0 : articleCategory.getGrade());

		entity.setName(articleCategory.getName());

		entity.setSeoDescription(articleCategory.getSeoDescription());

		entity.setSeoKeywords(articleCategory.getSeoKeywords());

		entity.setSeoTitle(articleCategory.getSeoTitle());

		entity.setStatus(articleCategory.getStatus());

		entity.setTreePath(articleCategory.getTreePath());

		entity.setParent(articleCategoryService.find(parentId));

		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            articleCategoryService.save(entity);
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
            articleCategoryService.delete(ids);
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
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("data",articleCategoryService.find(id));

		return "/admin/articleCategory/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(ArticleCategory articleCategory, Long parentId){
		ArticleCategory entity = articleCategoryService.find(articleCategory.getId());
		
		entity.setCreateDate(articleCategory.getCreateDate());

		entity.setModifyDate(articleCategory.getModifyDate());

		entity.setOrder(articleCategory.getOrder() == null ? 0 : articleCategory.getOrder());

		entity.setGrade(articleCategory.getGrade() == null ? 0 : articleCategory.getGrade());

		entity.setName(articleCategory.getName());

		entity.setSeoDescription(articleCategory.getSeoDescription());

		entity.setSeoKeywords(articleCategory.getSeoKeywords());

		entity.setSeoTitle(articleCategory.getSeoTitle());

		entity.setStatus(articleCategory.getStatus());

		entity.setTreePath(articleCategory.getTreePath());

		entity.setParent(articleCategoryService.find(parentId));

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            articleCategoryService.update(entity);
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
	public Message list(Date beginDate, Date endDate, ArticleCategory.Status status, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		Page<ArticleCategory> page = articleCategoryService.findPage(beginDate,endDate,pageable);
		return Message.success(PageModel.bind(page), "admin.list.success");
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
		return "/admin/articleCategory/view/articleCategoryView";
	}



}