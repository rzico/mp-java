package net.wit.controller.admin;

import net.wit.*;
import net.wit.entity.Admin;
import net.wit.entity.Tag;
import net.wit.entity.Transfer;
import net.wit.plat.unspay.UnsPay;
import net.wit.service.*;
import net.wit.util.MD5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: PasswordController
 * @author 降魔战队
 * @date 2017-10-17 21:43:40
 */
 
@Controller("adminPasswordController")
@RequestMapping("/admin/password")
public class PasswordController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("data",adminService.getCurrent());

		return "/admin/password/index";
	}

	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(String oldPassword,String newPassword,String cfmPassword){
		Admin admin = adminService.getCurrent();
        try {
        	String op = MD5Utils.getMD5Str(oldPassword);
        	String np = MD5Utils.getMD5Str(newPassword);

        	if (!admin.getPassword().equals(op)) {
        		return Message.error("原密码无效");
			}
			if (!cfmPassword.equals(newPassword)) {
        		return Message.error("输入的密码不一致");
			}

			admin.setPassword(np);
        	adminService.update(admin);

			return Message.success("admin.save.success");

		} catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
	}


}