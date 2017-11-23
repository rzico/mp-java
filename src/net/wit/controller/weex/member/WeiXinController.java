package net.wit.controller.weex.member;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Principal;
import net.wit.controller.admin.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.StringUtils;
import net.wit.plat.weixin.pojo.AccessToken;
import net.wit.plat.weixin.util.WeixinApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * @ClassName: WeiXinController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberWeiXinController")
@RequestMapping("/weex/member/weixin")
public class WeiXinController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    /**
     * 微信登录
     */
    @RequestMapping(value = "/bind")
    @ResponseBody
    public Message bind(String code,HttpServletRequest request){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        AccessToken token = WeixinApi.getOauth2AccessToken(bundle.getString("app.appid"), bundle.getString("app.secret"), code);
        String openId = null;
        if (token!=null) {
            openId = token.getOpenid();
         } else {
            return Message.error("无效授权码");
        }
        JSONObject userinfo = WeixinApi.getUserInfoByCode(token.getToken(), openId);
        String nickName=null;
        String headImg=null;
        String unionId=null;
        if (userinfo.containsKey("nickname")) {
            nickName = StringUtils.filterEmoji(userinfo.getString("nickname"));
            headImg = userinfo.getString("headimgurl");
            if (userinfo.containsKey("unionid")) {
                unionId = userinfo.getString("unionid");
            } else {
                return Message.error("获取用户信息失败");
            }
        } else {
            return Message.error("获取用户信息失败");
        }

        BindUser bindUser = bindUserService.findUnionId(unionId, BindUser.Type.weixin);
        Member member = memberService.getCurrent();
        if (bindUser!=null) {
            if (!bindUser.getMember().equals(member)) {
                return Message.error("已绑定其他账号");
            }
        }
        try {
            bindUser = bindUserService.findOpenId(openId,bundle.getString("app.appid"),BindUser.Type.weixin);
            if (bindUser==null) {
                bindUser = new BindUser();
                bindUser.setAppId(bundle.getString("app.appid"));
                bindUser.setType(BindUser.Type.weixin);
                bindUser.setMember(member);
                bindUser.setOpenId(openId);
                bindUser.setUnionId(unionId);
            } else {
                bindUser.setMember(member);
                bindUser.setUnionId(unionId);
            }
            bindUserService.save(bindUser);

            Principal principal = new Principal(member.getId(),member.getUsername());
            redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));

            String xuid = request.getHeader("x-uid");
            if (xuid!=null) {
                member.setUuid(xuid);
            }
            member.setLoginDate(new Date());
            memberService.save(member);

            return Message.success(Message.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("登录失败");
        }
    }

}