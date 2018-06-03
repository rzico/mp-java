package net.wit.controller.applet;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Principal;
import net.wit.controller.admin.BaseController;
import net.wit.entity.*;
import net.wit.plat.im.User;
import net.wit.plat.weixin.pojo.AccessToken;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import net.wit.util.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: LoginController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletLoginController")
@RequestMapping("/applet/login")
public class LoginController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    @RequestMapping(value = "")
    public
    @ResponseBody
    Message login(String code,String nickName,String logo,Long mid, HttpServletRequest request, HttpServletResponse response) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        String appid = bundle.getString("applet.appid");
        String appsecret = bundle.getString("applet.secret");
        if (mid!=null) {
           Member agent = memberService.getCurrent();
           if (agent.getTopic()!=null) {
               return Message.error("没有开通");
           }
           if (agent.getTopic().getConfig().getAppetAppId()==null)  {
               return Message.error("没有设置");
           }

            appid = agent.getTopic().getConfig().getAppetAppId();
            appsecret = agent.getTopic().getConfig().getAppetAppSerect();
        }

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appsecret + "&js_code=" + code + "&grant_type=authorization_code";
        JSONObject result = WeixinApi.httpRequest(url, "GET", null);
        if (result.containsKey("session_key")) {
            HttpSession session = request.getSession();
            String sessionId = session.getId();

            String sessionKey = result.get("session_key").toString();
            String openId = result.get("openid").toString();
            String unionId = "#";
            if (result.containsKey("unionid") && (mid==null)) {
                unionId = result.get("unionid").toString();
            }

            BindUser bindUser = null;
//            if (unionId!=null && !"#".equals(unionId)) {
//                bindUser = bindUserService.findUnionId(unionId, BindUser.Type.weixin);
//            } else {
                bindUser = bindUserService.findOpenId(openId,appid,BindUser.Type.weixin);
//            }

            Member member = null;
            if (bindUser!=null) {
                member = bindUser.getMember();
            }
            if (member==null) {
//                if ("#".equals(unionId) && (mid==null)) {
//                   return Message.error("无效授权");
//                }
                member = new Member();
                member.setNickName(nickName);
                member.setLogo(logo);
                member.setPoint(0L);
                member.setAmount(BigDecimal.ZERO);
                member.setBalance(BigDecimal.ZERO);
                member.setFreezeBalance(BigDecimal.ZERO);
                member.setVip(Member.VIP.vip1);
                member.setIsEnabled(true);
                member.setIsLocked(false);
                member.setLoginFailureCount(0);
                member.setRegisterIp(request.getRemoteAddr());
                memberService.save(member);
            } else {
                if (nickName!=null && member.getUuid()==null) {
                    member.setNickName(nickName);
                    if (member.getLogo()!=null && member.getLogo().startsWith("http://cdn")) {

                    } else {
                        member.setLogo(logo);
                    }
                    memberService.update(member);
                }
            }

            try {
                bindUser = bindUserService.findOpenId(openId,appid,BindUser.Type.weixin);
                if (bindUser==null) {
                    bindUser = new BindUser();
                    bindUser.setAppId(appid);
                    bindUser.setType(BindUser.Type.weixin);
                    bindUser.setMember(member);
                    bindUser.setUnionId(unionId);
                    bindUser.setOpenId(openId);
                } else {
                    bindUser.setMember(member);
                    if (!"#".equals(unionId)) {
                        bindUser.setUnionId(unionId);
                    }
                }
                bindUserService.save(bindUser);

                Cart cart = cartService.getCurrent();
                if (cart != null) {
                    if (cart.getMember() == null) {
                        cartService.merge(member, cart);
                        redisService.remove(Cart.KEY_COOKIE_NAME);
                    }
                }

                Principal principal = new Principal(member.getId(),member.getUsername());
                redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
                String xuid = request.getHeader("x-uid");
                if (xuid!=null) {
                    Member u = memberService.findByUUID(xuid);
                    if (u!=null && !u.equals(member)) {
                        u.setUuid(null);
                        memberService.save(u);
                    }
                    member.setUuid(xuid);
                    String ua = request.getHeader("user-agent");
                    if (ua!=null) {
                        member.setScene(ua);
                    }
                }
                member.setLoginDate(new Date());
                memberService.save(member);

                Map<String,String> data = new HashMap<>();
                data.put("jsessionId",sessionId);
                data.put("session_key",sessionKey);
//
//
                //水达人有卡才能登录
                if (bundle.containsKey("weex") && bundle.getString("weex").equals("3")) {
                    if (member.getCards().size()==0) {
                        return Message.success(data,"card.no");
                    }
                }

                if (!User.userAttr(member)) {
                    return Message.success(data,Message.LOGIN_SUCCESS);
                };

//              data.put("userId", Base64.encodeBase64String(openId.getBytes()));
                return Message.success(data,Message.LOGIN_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
                return Message.error("登录失败");
            }
        } else {
            return Message.error("登录失败");
        }
    }


    /**
     * 检查是否登录
     */
    @RequestMapping("/isAuthenticated")
    @ResponseBody
    public Message authorized(String scope,HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> data = new HashMap<String,Object>();
        Member member = memberService.getCurrent();
        if ("user".equals(scope)) {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            BindUser bindUser = bindUserService.findMember(member,bundle.getString("weixin.appid"),BindUser.Type.weixin);
            data.put("loginStatus",member!=null && bindUser!=null && !bindUser.getUnionId().equals("#"));
        } else {
            data.put("loginStatus", member != null);
        }
        if (member!=null) {
            data.put("uid", member.getId());
            data.put("userId",member.userId());
            data.put("authed",(member.getLogo()!=null));
        }
        return Message.success(data,"success");
    }

}