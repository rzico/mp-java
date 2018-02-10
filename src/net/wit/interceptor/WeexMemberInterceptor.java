package net.wit.interceptor;

import net.wit.Message;
import net.wit.Principal;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.service.CartService;
import net.wit.service.MemberService;
import net.wit.service.RedisService;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Interceptor - 会员权限
 *
 * @author rsico Team
 * @version 3.0
 */
public class WeexMemberInterceptor extends HandlerInterceptorAdapter {

	private static Logger logger = LogManager.getLogger(WeexMemberInterceptor.class);

	@Value("${url_escaping_charset}")
	private String urlEscapingCharset;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "redisServiceImpl")
	private RedisService redisService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Member member = memberService.getCurrent();
		String xuid = request.getHeader("x-uid");
		if (member!=null ) {// && xuid!=null && xuid.equals(member.getUuid())) {
			return true;
		}
		if (member==null) {
			member = memberService.findByUUID(xuid);
			if (member != null) {
				Cart cart = cartService.getCurrent();
				if (cart != null) {
					if (cart.getMember() == null) {
						cartService.merge(member, cart);
						redisService.remove(Cart.KEY_COOKIE_NAME);
					}
				}

				Principal principal = new Principal(member.getId(), member.getUsername());
				redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
				return true;
			}
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(JsonUtils.toJson(Message.error(Message.SESSION_INVAILD)));
		out.flush();
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}
}