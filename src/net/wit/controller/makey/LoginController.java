package net.wit.controller.makey;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Principal;
import net.wit.controller.admin.BaseController;
import net.wit.entity.BindUser;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.plat.im.User;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @ClassName: LoginController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("makeyLoginController")
@RequestMapping("/makey/login")
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

    public  String filterEmoji(String source) {
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find())
            {
                source = emojiMatcher.replaceAll("*");
                return source ;
            }
            return source;
        }
        return source;
    }

    @RequestMapping(value = "")
    public
    @ResponseBody
    Message login(String code,String nickName,String logo,Long xmid, HttpServletRequest request, HttpServletResponse response) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        String appid = bundle.getString("applet.appid");
        String appsecret = bundle.getString("applet.secret");
        if (xmid!=null) {
            Member agent = memberService.find(xmid);
            if (agent.getTopic()!=null && agent.getTopic().getConfig()!=null && agent.getTopic().getConfig().getAppetAppId()!=null) {
                appid = agent.getTopic().getConfig().getAppetAppId();
            }
        }


        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" +appid + "&secret=" + appsecret + "&js_code=" + code + "&grant_type=authorization_code";
        JSONObject result = WeixinApi.httpRequest(url, "GET", null);
        if (result.containsKey("session_key")) {
            HttpSession session = request.getSession();
            String sessionId = session.getId();

            String sessionKey = result.get("session_key").toString();
            String openId = result.get("openid").toString();
            String unionId = null;
            if (result.containsKey("unionid")) {
                unionId = result.get("unionid").toString();
            }
            if (unionId == null) {
                unionId = "#";
            }

            BindUser bindUser = null;
            if (nickName!=null) {
                nickName = filterEmoji(nickName);
            }

            bindUser = bindUserService.findOpenId(openId,appid,BindUser.Type.weixin);

            Member member = null;
            if (bindUser!=null) {
                member = bindUser.getMember();
            }
            if (member==null) {
                member = new Member();
                member.setNickName(nickName);
                member.setLogo(logo);
                member.setPoint(0L);
                member.setAmount(BigDecimal.ZERO);
                member.setBalance(BigDecimal.ZERO);
                member.setFreezeBalance(BigDecimal.ZERO);
                member.setIsEnabled(true);
                member.setIsLocked(false);
                member.setLoginFailureCount(0);
                member.setRegisterIp(request.getRemoteAddr());
                memberService.save(member);
            } else {
                if (nickName!=null) {
                    member.setNickName(nickName);
                    member.setLogo(logo);
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
                    bindUser.setUnionId(unionId);
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
                return Message.success(data,Message.LOGIN_SUCCESS);

            } catch (Exception e) {
                e.printStackTrace();
                return Message.error("登录失败");
            }
        } else {
            return Message.error("登录失败");
        }
    }


}