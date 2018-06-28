package net.wit.controller.admin;

import net.wit.*;
import net.wit.entity.Barrel;
import net.wit.entity.Category;
import net.wit.service.BarrelService;
import net.wit.service.CategoryService;
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
 * @ClassName: BarrelController
 * @author 降魔战队
 * @date 2017-10-11 15:37:8
 */
 
@Controller("adminBarrelController")
@RequestMapping("/admin/barrel")
public class BarrelController extends BaseController {
	@Resource(name = "barrelServiceImpl")
	private BarrelService barrelService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		return "/admin/barrel/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {


		return "/admin/barrel/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Barrel barrel){
		Barrel entity = new Barrel();

		entity.setOrders(barrel.getOrders() == null ? 0 : barrel.getOrders());

		entity.setName(barrel.getName());

		entity.setLogo(barrel.getLogo());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
			barrelService.save(entity);
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
			barrelService.delete(ids);
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

		model.addAttribute("data",barrelService.find(id));

		return "/admin/category/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Barrel barrel){
		Barrel entity = barrelService.find(barrel.getId());

		entity.setOrders(barrel.getOrders() == null ? 0 : barrel.getOrders());

		entity.setName(barrel.getName());

		entity.setLogo(barrel.getLogo());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
			barrelService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Category.Status status, Pageable pageable,String searchValue, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("name", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<Barrel> page = barrelService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}