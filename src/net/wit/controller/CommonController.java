
package net.wit.controller;

import net.wit.Message;
import net.wit.controller.model.ArticleModel;
import net.wit.controller.weex.BaseController;
import net.wit.entity.Article;
import net.wit.service.AreaService;
import net.wit.service.ArticleService;
import net.wit.service.RSAService;
import net.wit.service.WeixinUpService;
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
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "weixinUpServiceImpl")
	private WeixinUpService weixinUpService;

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

	/**
	 * 获取城市
	 */

	@RequestMapping(value = "/test.jhtml", method = RequestMethod.GET)
	public String test(ModelMap model,HttpServletRequest request, HttpServletResponse response) {

		Article article = articleService.find(199L);
		ArticleModel m = new ArticleModel();
		m.bind(article);
		model.addAttribute("articles",m.getTemplates());
		return "/common/article";
	}

	/**
	 * 测试群发图文信息
	 */

	@RequestMapping(value = "/ttaa.jhtml", method = RequestMethod.GET)
	public String upload(ModelMap model,HttpServletRequest request, HttpServletResponse response) {

		Article article = articleService.find(222L);
		ArticleModel m = new ArticleModel();
		m.bind(article);
		Long[] l=new Long[4];
		l[0]=367l;
		l[1]=368l;
		l[2]=369l;
		l[3]=370l;
		weixinUpService.ArticleUpLoad(l);
		model.addAttribute("articles",m.getTemplates());
		return "/common/article";
	}
}