package net.wit.controller.mch;

import net.wit.Message;
import net.wit.Page;
import net.wit.PageBlock;
import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.service.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @ClassName: AreaController
 * @author 降魔战队
 * @date 2017-10-11 15:37:3
 */
 
@Controller("adminAreaController")
@RequestMapping("/admin/area")
public class AreaController extends BaseController {
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {



		return "/admin/area/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/area/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Area area, Long parentId){
		Area entity = new Area();	

		entity.setCreateDate(area.getCreateDate());

		entity.setModifyDate(area.getModifyDate());

		entity.setOrders(area.getOrders() == null ? 0 : area.getOrders());

		entity.setFullName(area.getFullName());

		entity.setName(area.getName());

		entity.setTreePath(area.getTreePath());

		entity.setParent(areaService.find(parentId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            areaService.save(entity);
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
            areaService.delete(ids);
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



		model.addAttribute("data",areaService.find(id));

		return "/admin/area/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Area area, Long parentId){
		Area entity = areaService.find(area.getId());
		
		entity.setCreateDate(area.getCreateDate());

		entity.setModifyDate(area.getModifyDate());

		entity.setOrders(area.getOrders() == null ? 0 : area.getOrders());

		entity.setFullName(area.getFullName());

		entity.setName(area.getName());

		entity.setTreePath(area.getTreePath());

		entity.setParent(areaService.find(parentId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            areaService.update(entity);
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

		Page<Area> page = areaService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/admin/area/view/areaView";
	}



}