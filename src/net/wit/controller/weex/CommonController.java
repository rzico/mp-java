
package net.wit.controller.weex;

import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

/**
 * Controller - 共用
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("weexCommonController")
@RequestMapping("/weex/common")
public class CommonController extends BaseController {

	@Value("${system.name}")
	private String systemName;
	@Value("${system.version}")
	private String systemVersion;
	@Value("${system.description}")
	private String systemDescription;
	@Value("${system.show_powered}")
	private Boolean systemShowPowered;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "pluginConfigServiceImpl")
	private PluginConfigService pluginConfigService;

	/**
	 * 公钥
	 */
	@RequestMapping(value = "/public_key", method = RequestMethod.GET)
	public @ResponseBody
	Message publicKey(HttpServletRequest request) {
		RSAPublicKey publicKey = rsaService.generateKey(request);
		Map<String, String> data = new HashMap<String, String>();
		data.put("modulus", StringUtils.base64Encode(publicKey.getModulus().toByteArray()));
		data.put("exponent", StringUtils.base64Encode(publicKey.getPublicExponent().toByteArray()));
		data.put("key", StringUtils.base64Encode(publicKey.getEncoded()));
		return Message.bind(data,request);
	}

	/**
	 * 检查是版本
	 */
	@RequestMapping(value = "/resources", method = RequestMethod.GET)
	@ResponseBody
	public Message resources(HttpServletRequest request, HttpServletResponse response) {
//		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Map<String,Object> data = new HashMap<String,Object>();
		String ua = request.getHeader("user-agent");
//		if (ua != null) {
//			if (ua.indexOf("iOS")!=-1) {
//				data.put("appVersion",bundle.getString("ios.version"));
//				data.put("minVersion",bundle.getString("ios.min.version"));
//				data.put("appUrl",bundle.getString("ios.url"));
//			} else {
//				data.put("appVersion",bundle.getString("android.version"));
//				data.put("minVersion",bundle.getString("android.min.version"));
//				data.put("appUrl",bundle.getString("android.url"));
//			}
//		} else {
//			data.put("appVersion",bundle.getString("android.version"));
//			data.put("minVersion",bundle.getString("android.min.version"));
//			data.put("appUrl",bundle.getString("android.url"));
//		}
//		data.put("resVersion",bundle.getString("resource.version"));
//		data.put("resUrl",bundle.getString("resource.url"));
//
//		data.put("key",bundle.getString("app.key"));
//		return Message.bind(data,request);
//		String ua = request.getHeader("user-agent");
		if (ua != null) {
			if (ua.indexOf("iOS")!=-1) {
				PluginConfig pluginConfig=pluginConfigService.findByPluginId("iosVersionPlugin");
				if(pluginConfig==null){
					return Message.error("您已断开链接请稍候再试!");
				}
				data.put("appVersion",pluginConfig.getAttribute("iosVersion"));
				data.put("minVersion",pluginConfig.getAttribute("iosMinVersion"));
				data.put("appUrl",pluginConfig.getAttribute("iosUrl"));
			} else {
				PluginConfig pluginConfig=pluginConfigService.findByPluginId("androidVersionPlugin");
				if(pluginConfig==null){
					return Message.error("您已断开链接请稍候再试!");
				}
				data.put("appVersion",pluginConfig.getAttribute("androidVersion"));
				data.put("minVersion",pluginConfig.getAttribute("androidMinVersion"));
				data.put("appUrl",pluginConfig.getAttribute("androidUrl"));
			}
		} else {
			PluginConfig pluginConfig=pluginConfigService.findByPluginId("androidVersionPlugin");
			if(pluginConfig==null){
				return Message.error("您已断开链接请稍候再试!");
			}
			data.put("appVersion",pluginConfig.getAttribute("androidVersion"));
			data.put("minVersion",pluginConfig.getAttribute("androidMinVersion"));
			data.put("appUrl",pluginConfig.getAttribute("androidUrl"));
		}
		PluginConfig pluginConfig=pluginConfigService.findByPluginId("resourceVersionPlugin");
		if(pluginConfig==null){
			return Message.error("您已断开链接请稍候再试!");
		}
		data.put("resVersion",pluginConfig.getAttribute("resourceVersion"));
		data.put("resUrl",pluginConfig.getAttribute("resourceUrl"));
		return Message.bind(data,request);
	}


	/**
	 * 页面路由
	 */
	@RequestMapping(value = "/router", method = RequestMethod.GET)
	@ResponseBody
	public Message router(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> data = new HashMap<>();
		Map<String,String> menu = new HashMap<>();
		Member member = memberService.getCurrent();
		String ua = request.getHeader("user-agent");
		if (ua.indexOf("V1")>0) {
			menu.put("home","file://view/shop/cashier/index.js?index=true");
			menu.put("add", "file://view/shop/card/add.js");
			menu.put("friend", "file://view/shop/card/list.js");
			menu.put("message","file://view/message/list.js");
			menu.put("member", "file://view/member/index.js");
		} else {
			if (member!=null) {
				Admin admin = adminService.findByMember(member);
				if (admin!=null && admin.getEnterprise()!=null) {
					member = admin.getEnterprise().getMember();
				}
				if (member.getTopic()!=null && member.getTopic().getConfig()!=null && member.getTopic().getConfig().getUseCashier()) {
					if (admin !=null && !admin.isRole("125")) {
						menu.put("home", "file://view/home/index.js");
					} else {
						menu.put("home", "file://view/shop/cashier/index.js?index=true");
					}
				} else  {
					menu.put("home", "file://view/home/index.js");
				}
			} else {
				menu.put("home", "file://view/home/index.js");
			}
			menu.put("add", "file://view/member/editor/editor.js");
			menu.put("friend", "file://view/friend/list.js");
			menu.put("message","file://view/message/list.js");
			menu.put("member", "file://view/member/index.js");
		}
		data.put("tabnav",menu);
		return Message.bind(data,request);
	}

}