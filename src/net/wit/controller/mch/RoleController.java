package net.wit.controller.mch;

import net.wit.Message;
import net.wit.Page;
import net.wit.PageBlock;
import net.wit.Pageable;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Role;
import net.wit.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @ClassName: RoleController
 * @author 降魔战队
 * @date 2017-10-11 15:37:14
 */
 
@Controller("mchRoleController")
@RequestMapping("/mch/role")
public class RoleController extends BaseController {
	@Resource(name = "roleServiceImpl")
	private RoleService roleService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {



		return "/mch/role/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/mch/role/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Role role){
		Role entity = new Role();	

		entity.setCreateDate(role.getCreateDate());

		entity.setModifyDate(role.getModifyDate());

		entity.setDescription(role.getDescription());

		entity.setIsSystem(role.getIsSystem());

		entity.setName(role.getName());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            roleService.save(entity);
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
            roleService.delete(ids);
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



		model.addAttribute("data",roleService.find(id));

		return "/mch/role/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Role role){
		Role entity = roleService.find(role.getId());
		
		entity.setCreateDate(role.getCreateDate());

		entity.setModifyDate(role.getModifyDate());

		entity.setDescription(role.getDescription());

		entity.setIsSystem(role.getIsSystem());

		entity.setName(role.getName());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            roleService.update(entity);
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

		Page<Role> page = roleService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}