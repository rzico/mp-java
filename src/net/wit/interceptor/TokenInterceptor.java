package net.wit.interceptor;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

}