
package net.wit.controller.weex;

import net.wit.Message;
import net.wit.entity.Area;
import net.wit.entity.Member;
import net.wit.service.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;
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

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 公钥
	 */
	@RequestMapping(value = "/public_key", method = RequestMethod.GET)
	public @ResponseBody
	Message publicKey(HttpServletRequest request) {
		RSAPublicKey publicKey = rsaService.generateKey(request);
		Map<String, String> data = new HashMap<String, String>();
		data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		return Message.success(data,"success");
	}

	/**
	 * 检查是版本
	 */
	@RequestMapping("/resources")
	@ResponseBody
	public Message resources(HttpServletRequest request, HttpServletResponse response) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		Map<String,Object> data = new HashMap<String,Object>();
		data.put("resVersion","0.0.1");
		data.put("resUrl","http://cdn.rzico.com/weex/resources/release/res-0.0.1.zip");

		data.put("appVersion","0.0.1");
		data.put("appUrl","http://cdn.rzico.com/weex/resources/release/app-0.0.1.zip");

		data.put("key",bundle.getString("app.key"));
		return Message.success(data,"success");
	}

}