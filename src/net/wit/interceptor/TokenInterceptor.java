package net.wit.interceptor;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.entity.Cart;
import net.wit.service.CartService;
import net.wit.service.MemberService;
import net.wit.service.RedisService;
import net.wit.util.JsonUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.wit.Principal;
import net.wit.entity.Member;
import net.wit.util.MD5Utils;
import net.wit.util.WebUtils;

/**
 * Interceptor - 令牌
 *
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
	private static Logger logger = LogManager.getLogger(TokenInterceptor.class);

	@Value("${url_escaping_charset}")
	private String urlEscapingCharset;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "redisServiceImpl")
	private RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String xuid = request.getHeader("x-uid");
		String xapp = request.getHeader("x-app");
		String xtsp = request.getHeader("x-tsp");
		String xtkn = request.getHeader("x-tkn");

		System.out.print("prehandle");
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		if (xtkn!=null && xtkn.equals(MD5Utils.getMD5Str(xuid+xapp+xtsp+bundle.getString("app.key")))) {
			Member member = memberService.getCurrent();
			if (member!=null && xuid!=null && xuid.equals(member.getUuid())) {
				return true;
			}
			if (member==null) {
				member = memberService.findByUUID(xuid);
				if (member!=null) {

					Cart cart = cartService.getCurrent();
					if (cart != null) {
						if (cart.getMember() == null) {
							cartService.merge(member, cart);
							redisService.remove(Cart.KEY_COOKIE_NAME);
						}
					}

					Principal principal = new Principal(member.getId(), member.getUsername());
					redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
				}
			}
			return true;
		} else {
			return true;
		}
	}

}