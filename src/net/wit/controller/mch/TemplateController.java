package net.wit.controller.mch;

import net.wit.*;
import net.wit.entity.Tag;
import net.wit.entity.Template;
import net.wit.service.TagService;
import net.wit.service.TemplateService;
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
 * @ClassName: TemplateController
 * @author 降魔战队
 * @date 2017-10-11 15:37:16
 */
 
@Controller("mchTemplateController")
@RequestMapping("/mch/template")
public class TemplateController extends BaseController {
	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("product","商品"));
		types.add(new MapEntity("topic","专栏"));
		model.addAttribute("types",types);
		model.addAttribute("tags",tagService.findList(Tag.Type.template));

		return "/mch/template/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("product","商品"));
		types.add(new MapEntity("topic","专栏"));
		model.addAttribute("types",types);
		model.addAttribute("tags",tagService.findList(Tag.Type.template));

		return "/mch/template/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Template template,Long [] tagIds){
		Template entity = new Template();	

		entity.setCreateDate(template.getCreateDate());

		entity.setModifyDate(template.getModifyDate());

		entity.setOrders(template.getOrders() == null ? 0 : template.getOrders());

		entity.setIsDefault(template.getIsDefault());

		entity.setIsFreed(template.getIsFreed());

		entity.setName(template.getName());

		entity.setSn(template.getSn());

		entity.setType(template.getType());

		entity.setThumbnial(template.getThumbnial());

		entity.setTags(tagService.findList(tagIds));

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            templateService.save(entity);
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
            templateService.delete(ids);
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
		types.add(new MapEntity("product","商品"));
		types.add(new MapEntity("topic","专栏"));
		model.addAttribute("types",types);

		model.addAttribute("data",templateService.find(id));
		model.addAttribute("tags",tagService.findList(Tag.Type.template));

		return "/mch/template/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Template template,Long [] tagIds){
		Template entity = templateService.find(template.getId());
		
		entity.setCreateDate(template.getCreateDate());

		entity.setModifyDate(template.getModifyDate());

		entity.setOrders(template.getOrders() == null ? 0 : template.getOrders());

		entity.setIsDefault(template.getIsDefault());

		entity.setIsFreed(template.getIsFreed());

		entity.setName(template.getName());

		entity.setSn(template.getSn());

		entity.setType(template.getType());

		entity.setThumbnial(template.getThumbnial());

		entity.setTags(tagService.findList(tagIds));

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            templateService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Template.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Template> page = templateService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}