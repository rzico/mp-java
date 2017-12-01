
package net.wit.controller;

import net.wit.Message;
import net.wit.controller.weex.BaseController;
import net.wit.service.AreaService;
import net.wit.service.RSAService;
import net.wit.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Controller - 共用
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("commonController")
@RequestMapping("/common")
public class CommonController extends BaseController {
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 404页面
	 */

	@RequestMapping(value = "/resource_not_found.jhtml", method = RequestMethod.GET)
	public String publicKey(HttpServletRequest request,HttpServletResponse response) {
		response.setStatus(200);
		return "forward:/index.html";
	}

	/**
	 * 获取城市js
	 */

	@RequestMapping(value = "/area.jhtml", method = RequestMethod.GET)
	public String area(ModelMap model,HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("areas",areaService.findRoots());
		return "/common/area";
	}

}