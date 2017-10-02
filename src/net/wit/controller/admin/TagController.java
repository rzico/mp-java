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
import net.wit.entity.Tag;
import net.wit.service.TagService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: TagController
 * @author 降魔战队
 * @date 2017-9-14 19:42:18
 */
 
@Controller("adminTagController")
@RequestMapping("/admin/tag")
public class TagController extends BaseController {
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("member","会员"));
		model.addAttribute("types",types);

		return "/admin/tag/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("member","会员"));
		model.addAttribute("types",types);

		return "/admin/tag/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Tag tag){
		Tag entity = new Tag();	

		entity.setCreateDate(tag.getCreateDate());

		entity.setModifyDate(tag.getModifyDate());

		entity.setOrder(tag.getOrder() == null ? 0 : tag.getOrder());

		entity.setIcon(tag.getIcon());

		entity.setMemo(tag.getMemo());

		entity.setName(tag.getName());

		entity.setType(tag.getType());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            tagService.save(entity);
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
            tagService.delete(ids);
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
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("member","会员"));
		model.addAttribute("types",types);

		model.addAttribute("data",tagService.find(id));

		return "/admin/tag/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Tag tag){
		Tag entity = tagService.find(tag.getId());
		
		entity.setCreateDate(tag.getCreateDate());

		entity.setModifyDate(tag.getModifyDate());

		entity.setOrder(tag.getOrder() == null ? 0 : tag.getOrder());

		entity.setIcon(tag.getIcon());

		entity.setMemo(tag.getMemo());

		entity.setName(tag.getName());

		entity.setType(tag.getType());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            tagService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Tag.Type type, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Tag> page = tagService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}