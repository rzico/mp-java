/**
 *====================================================
 * 文件名称: MemberInfoController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.pos;

import net.wit.Setting;
import net.wit.entity.Member;
import net.wit.entity.Redis;
import net.wit.entity.SafeKey;
import net.wit.entity.Smssend;
import net.wit.service.*;
import net.wit.util.Base64Util;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @ClassName: MemberInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("posPasswordAreaController")
@RequestMapping("/pos/password")
public class PasswordController extends BaseController {

	public static final String CAPTCHA_SECURITYCODE_SESSION = "captcha_safe_key";
	public static final String CAPTCHA_CONTENT_SESSION = "captcha_code";
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	

	@Resource(name = "mailServiceImpl")
	private MailService mailService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "smssendServiceImpl")
	private SmssendService smsSendService;

	@Resource(name = "redisServiceImpl")
	private RedisService redisService;


	/**
	 * 发送手机
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock sendMobile(String mobile,String key, HttpServletRequest request) {
		String myKey = DigestUtils.md5Hex(mobile+"myjsy2014$$");
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		HttpSession session = request.getSession();
		Setting setting = SettingUtils.get();
		int challege = net.wit.util.StringUtils.Random6Code();
		String securityCode = String.valueOf(challege);
		SafeKey tmp = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		if (tmp!=null && !tmp.hasExpired()) {
			securityCode = tmp.getValue();
			if (!tmp.canReset()) {
				return DataBlock.error("系统忙，稍等几秒重试");
			}
		}
        Member member = memberService.findByMobile(mobile);
        if (member==null) {
			return DataBlock.error("当前用户名无效");
        }

		SafeKey safeKey = new SafeKey();
		safeKey.setKey(mobile);
		safeKey.setValue(securityCode);
		safeKey.setExpire( DateUtils.addMinutes(new Date(),120));
		redisService.put(Member.MEMBER_PASSWORD_CAPTCHA, JsonUtils.toJson(safeKey));

		Smssend smsSend = new Smssend();
		smsSend.setMobile(mobile);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人");
		smsSendService.smsSend(smsSend);
		return DataBlock.success("success","发送成功");
	}

	/**
	 * 找回登录密码
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody DataBlock update(String captcha,String newPass,HttpServletRequest request) {
		Redis redis = redisService.findKey(Member.MEMBER_PASSWORD_CAPTCHA);
		if (redis==null) {
			return DataBlock.error("验证码已过期");
		}
		redisService.remove(Member.MEMBER_PASSWORD_CAPTCHA);

		String newPwd = newPass;

		Member member = memberService.findByMobile(redis.getKey());
		if (member==null) {
			return DataBlock.error("无效用户名");
		}

		SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码过期了");
		}

		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("验证码不正确");
		}

		member.setPassword(newPwd);
		member.setLoginFailureCount(0);
		member.setIsLocked(false);
		member.setLockedDate(null);
    	memberService.update(member);
		return DataBlock.success("success","修改成功");
		
	}
	
}
