package net.wit.interceptor;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.Principal;
import net.wit.entity.BindUser;
import net.wit.entity.Member;

import net.wit.service.BindUserService;
import net.wit.service.MemberService;
import net.wit.service.RedisService;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor - 会员权限
 *
 * @author rsico Team
 * @version 3.0
 */
public class WeexInterceptor extends HandlerInterceptorAdapter {

	private static Logger logger = LogManager.getLogger(WeexInterceptor.class);

	@Value("${url_escaping_charset}")
	private String urlEscapingCharset;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "redisServiceImpl")
	private RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String xuid = request.getHeader("x-uid");
		String xapp = request.getHeader("x-app");
		String xtsp = request.getHeader("x-tsp");
		String xtkn = request.getHeader("x-tkn");

		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		logger.debug("xuid="+xuid);
		logger.debug("xtkn="+xtkn);
		logger.debug("xtsp="+xtsp);
		logger.debug("xtkn="+xtkn);

		if (xtkn!=null && xtkn.equals(MD5Utils.getMD5Str(xuid+xapp+xtsp+bundle.getString("app.key")))) {
			Member member = memberService.getCurrent();
			if (member!=null && xuid!=null && xuid.equals(member.getUuid())) {
				return true;
			}
			if (member==null) {
				member = memberService.findByUUID(xuid);
				if (member!=null) {
					Principal principal = new Principal(member.getId(), member.getUsername());
					redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
				}
			}
			return true;
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}
}