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
import net.wit.entity.ProductCategory;
import net.wit.service.ProductCategoryService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ProductCategoryController
 * @author 降魔战队
 * @date 2017-10-11 15:37:12
 */
 
@Controller("adminProductCategoryController")
@RequestMapping("/admin/productCategory")
public class ProductCategoryController extends BaseController {
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		return "/admin/productCategory/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return "/admin/productCategory/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(ProductCategory productCategory, Long parentId, Long memberId){
		ProductCategory entity = new ProductCategory();	

		entity.setCreateDate(productCategory.getCreateDate());

		entity.setModifyDate(productCategory.getModifyDate());

		entity.setOrders(productCategory.getOrders() == null ? 0 : productCategory.getOrders());

		entity.setGrade(productCategory.getGrade() == null ? 0 : productCategory.getGrade());

		entity.setName(productCategory.getName());

		entity.setTreePath(productCategory.getTreePath());

		entity.setMember(memberService.find(memberId));

		entity.setParent(productCategoryService.find(parentId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            productCategoryService.save(entity);
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
            productCategoryService.delete(ids);
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


		model.addAttribute("data",productCategoryService.find(id));

		return "/admin/productCategory/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(ProductCategory productCategory, Long parentId, Long memberId){
		ProductCategory entity = productCategoryService.find(productCategory.getId());

		entity.setOrders(productCategory.getOrders() == null ? 0 : productCategory.getOrders());

		entity.setName(productCategory.getName());

		entity.setThumbnail(productCategory.getThumbnail());


		entity.setMember(memberService.find(memberId));


        try {
            productCategoryService.update(entity);
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
	public Message list(Date beginDate, Date endDate,String searchValue, Pageable pageable, ModelMap model) {

		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("name", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<ProductCategory> page = productCategoryService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 商品分类视图
	 */
	@RequestMapping(value = "/productCategoryView", method = RequestMethod.GET)
	public String productCategoryView(Long id, ModelMap model) {
		model.addAttribute("members",memberService.findAll());

		model.addAttribute("productCategory",productCategoryService.find(id));
		return "/admin/productCategory/view/productCategoryView";
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
		return "/admin/productCategory/view/memberView";
	}



}