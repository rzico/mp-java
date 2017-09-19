package net.wit.interceptor;

import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.Principal;
import net.wit.entity.Member;

import net.wit.service.MemberService;
import net.wit.service.RedisService;
import org.apache.commons.lang.StringUtils;
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

	@Value("${url_escaping_charset}")
	private String urlEscapingCharset;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Member member = memberService.getCurrent();
		String xuid = request.getHeader("x-uid");
		if (member!=null && xuid!=null && xuid.equals(member.getUuid())) {
			return true;
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(Message.error(Message.SESSION_INVAILD));
		out.flush();
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}
}