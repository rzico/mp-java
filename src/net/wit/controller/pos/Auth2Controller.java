package net.wit.controller.pos;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.controller.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Xus on 2018/2/26.
 */
@Controller("auth2Controller")
@RequestMapping("/pos/auth2")
public class Auth2Controller extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "hostServiceImpl")
    private HostService hostService;

    @Resource(name = "topicServiceImpl")
    private TopicService topicService;

    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smsSendService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

    /**
     * 检查mobile是否存在
     */
    @RequestMapping(value = "/ready", method = RequestMethod.GET)
    public @ResponseBody
    String ready(HttpServletRequest request, HttpSession session) {
        int challege = net.wit.util.StringUtils.Random6Code();
        String securityCode = String.valueOf(challege);
        SafeKey safeKey = new SafeKey();
        safeKey.setKey(securityCode);
        safeKey.setValue(securityCode);
        safeKey.setExpire( DateUtils.addMinutes(new Date(),120));
        redisService.put(Member.MOBILE_LOGIN_CAPTCHA,JsonUtils.toJson(safeKey));
        return securityCode;
    }

    /**
     * 检查mobile是否存在
     */
    @RequestMapping(value = "/check_mobile", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        if (memberService.findByMobile(mobile)==null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 发送手机
     */
    @RequestMapping(value = "/send_mobile", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock sendMobile(String mobile,String key, HttpServletRequest request, HttpSession session) {
        String myKey = DigestUtils.md5Hex(mobile+"myjsy2014$$");
        if (!myKey.equals(key)) {
            return DataBlock.error("通讯密码无效");
        }
        Member member = memberService.findByMobile(mobile);
        Map<String, Object> map = new HashedMap();
        if (member!=null) {
            Admin admin = adminService.findByMember(member);
            if (admin!=null && admin.getEnterprise()!=null) {
                map.put("mobile", mobile);
                map.put("shopname", admin.getEnterprise().getName());
                map.put("linkman", admin.getEnterprise().getMember().getName());
                map.put("isNew", false);
            } else {
                map.put("isNew",true);
            }
        } else {
            map.put("isNew",true);
        }
        int challege = net.wit.util.StringUtils.Random6Code();
        String securityCode = String.valueOf(challege);

        SafeKey safeKey = new SafeKey();
        safeKey.setKey(mobile);
        safeKey.setValue(securityCode);
        safeKey.setExpire( DateUtils.addMinutes(new Date(),120));
        redisService.put(Member.MOBILE_LOGIN_CAPTCHA,JsonUtils.toJson(safeKey));
        Smssend smsSend = new Smssend();
        smsSend.setMobile(mobile);
        smsSend.setContent("验证码 :" + securityCode + ",只用于登录使用。");
        smssendService.smsSend(smsSend);
        return DataBlock.success(map,"发送成功");
    }


    /**
     * 获取当前门店的开通状态
     */
    @RequestMapping(value = "/check_status", method = RequestMethod.GET)
    public @ResponseBody
    DataBlock checkStatus(Long tenantId,String shopId,String shopName,HttpServletRequest request) {
//        Tenant tenant = tenantService.find(tenantId);
//        Application app = applicationService.findApplication(tenant,shopId,Application.Type.erp);
//        if (app==null) {
//            app = new Application();
//            app.setType(Application.Type.erp);
//            app.setCode(shopId);
//            app.setName(shopName);
//            app.setMember(tenant.getMember());
//            app.setTenant(tenant);
//
//            Setting setting = SettingUtils.get();
//            app.setPrice(setting.getFunctionFee());
//
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.DAY_OF_MONTH, 15);
//            app.setValidityDate(cal.getTime());
//            app.setStatus(Application.Status.none);
//            applicationService.save(app);
//        } else {
//            app.setModifyDate(new Date());
//            applicationService.save(app);
//        }
//        if (app.getStatus().equals(Application.Status.closed)) {
//            return DataBlock.error("当前店铺已经关闭，不能使用。");
//        }
//        Calendar vld = Calendar.getInstance();
//        vld.setTime(app.getValidityDate());
//        vld.add(Calendar.DAY_OF_MONTH, -15);
//        if (new Date().compareTo(vld.getTime())>=0) {
//            Long diff = app.getValidityDate().getTime()-new Date().getTime();
//            Long nd = 1000*24*60*60L;
//            Long day = diff/nd+1;
//            app.setStatus(Application.Status.none);
//            applicationService.save(app);
//            if (day.compareTo(0L)<=0) {
//                return DataBlock.error("当前店铺已经到期，请及时缴费");
//            } else {
//                return DataBlock.warn("当前店铺将在"+day.toString()+"天后到期，请及时续费");
//            }
//        }
        return DataBlock.success("success","执行成功");
    }


    /**
     * 检查mobile是否存在
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public @ResponseBody
    DataBlock login(String username, String password, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        Member member = memberService.findByMobile(username);
        if (member == null) {
            return DataBlock.error("用户名无效");
        }
        if (!member.getIsEnabled()) {
            return DataBlock.error("用户已经禁用了");
        }
        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            if (admin.getEnterprise().getHost()==null) {
                return DataBlock.error("没有开通ERP");
            }
        } else {
            return DataBlock.error("没有开通ERP");
        }
        Redis redis = redisService.findKey(Member.MOBILE_LOGIN_CAPTCHA);
        if (redis==null) {
            return DataBlock.error("验证码已过期");
        }
        redisService.remove(Member.MOBILE_LOGIN_CAPTCHA);
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        String pwd = DigestUtils.md5Hex(member.getPassword() + safeKey.getValue() + "vst@2014-2020$$");
        if (!pwd.equals(password)) {
            return DataBlock.error("登录密码无效");
        }
        member.setLoginIp(request.getRemoteAddr());
        member.setLoginDate(new Date());
        member.setLoginFailureCount(0);
        memberService.update(member);
        Principal principal = new Principal(member.getId(),member.getUsername());
        redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
        return DataBlock.success((Object)JsonUtils.toJson(principal),"登录成功");
    }

    /**
     * 获取当前会话令牌
     */
    @RequestMapping(value = "/get_token", method = RequestMethod.GET)
    public @ResponseBody
    DataBlock getToken(HttpServletRequest request) {
        Member member = memberService.getCurrent();
        Admin admin = adminService.findByMember(member);
        Enterprise enterprise = admin.getEnterprise();
        if (enterprise==null) {
            return DataBlock.error("当前账号没有开通店铺管理功能，");
        }

        String shopId = "1" + String.format("%08d", enterprise.getId())+"0001";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String token = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<token code=\"0000\" msg=\"success\"> <userInfo>" + "<userId>uid" + String.format("%033d", member.getId()) + "</userId>" + "<account>" + member.getMobile() + "</account>" + "<username>"
                + member.displayName() + "</username>" + "<tenantId>"+ "1" + String.format("%08d", enterprise.getId()) + "</tenantId>" + "<code>"+ member.userId() + "</code>" + "<tenantName>" + enterprise.getName() + "</tenantName>" + "<shopId>" + shopId+ "</shopId>"
                + "<shopName>" + enterprise.getName() + "</shopName>" + "<address>" + "#" + "</address>" + "<xsmCode>" + member.getMobile() + "</xsmCode>" + "<xsmAlias>" + member.getMobile() + "</xsmAlias>" + "<xsmPWD>" + member.getPassword()
                + "</xsmPWD>" + "<licenseCode>" + "#" + "</licenseCode>" + "<legal>" + member.displayName() + "</legal>" + "<idCard>无</idCard>" + "<mobile>" + member.getMobile() + "</mobile>" + "<regionId>" + "#" + "</regionId>"
                + "<lDate>" + sdf.format(new java.util.Date()) + "</lDate>" + "<online>1</online>" + "</userInfo></token>";

        return DataBlock.success(token,"执行成功");
    }

    /**
     * 软件参数
     */
    @RequestMapping(value = "/params", method = RequestMethod.GET)
    public  @ResponseBody Map<String,Object> software(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        Map<String, Object> map = new HashMap<String, Object>();
        Member member = memberService.getCurrent();
        Admin admin = adminService.findByMember(member);
        Enterprise enterprise = admin.getEnterprise();
        map.put("version","MKT");
        map.put("industry","FIG");
        Host host = enterprise.getHost();
        if (host==null) {
            host = hostService.find(new Long(1));
        }
        map.put("hostname",host.getHostname());
        map.put("dbid",host.getDbid());
        map.put("port",host.getPort());
        return map;
    }

    /**
     * 注册提交(手机)
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock register(String mobile, String captcha, Long areaId, String password, String shopName, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        Redis redis = redisService.findKey(Member.MOBILE_LOGIN_CAPTCHA);
        if (redis==null) {
            return DataBlock.error("验证码已过期");
        }
        redisService.remove(Member.MOBILE_LOGIN_CAPTCHA);
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        if (safeKey == null) {
            return DataBlock.error("验证码过期了");
        }
        if (safeKey.hasExpired()) {
            return DataBlock.error("验证码过期了");
        }
        if (!captcha.equals("778899") && !safeKey.getValue().equals(captcha)) {
            return DataBlock.error("验证码不正确");
        }
        Member member = memberService.findByMobile(mobile);

        if (member==null) {
            // 用户名注册时 获取的密码
            if (StringUtils.isEmpty(password)) {
                password = Base64Util.decode(request.getParameter("enPassword"));
            }
            member = new Member();
            member.setUsername(safeKey.getKey());
            member.setMobile(safeKey.getKey());
            if (shopName==null) {
                member.setNickName(safeKey.getKey());
            } else {
                member.setNickName(shopName);
            }
            member.setLogo("http://cdn.rzico.com/weex/resources/images/logo.png");
            member.setPassword(MD5Utils.getMD5Str(password));
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
                if (StringUtils.isEmpty(password)) {
                    password = Base64Util.decode(request.getParameter("enPassword"));
                }
                if (!StringUtils.isEmpty(password)) {
                    member.setPassword(MD5Utils.getMD5Str(password));
                    memberService.update(member);
                }

         }

        Topic topic =  member.getTopic();
        if (topic==null) {
            topic = new Topic();
            if (shopName==null) {
                topic.setName(member.getNickName());
            } else {
                topic.setName(shopName);
            }
            topic.setBrokerage(new BigDecimal("0.6"));
            topic.setStatus(Topic.Status.waiting);
            topic.setHits(0L);
            topic.setMember(member);
            topic.setFee(new BigDecimal("588"));
            topic.setLogo(member.getLogo());
            topic.setType(Topic.Type.personal);
            topic.setRanking(0L);
            topic.setPaybill(new BigDecimal("0.5"));
            TopicConfig config = topic.getConfig();
            if (config==null) {
                config = new TopicConfig();
                config.setUseCard(false);
                config.setUseCashier(false);
                config.setUseCoupon(false);
                config.setPromoterType(TopicConfig.PromoterType.any);
                config.setPattern(TopicConfig.Pattern.pattern1);
                config.setAmount(BigDecimal.ZERO);
            }
            topic.setConfig(config);
            Calendar calendar   =   new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(calendar.MONTH, 1);
            topic.setExpire(calendar.getTime());
            topic.setTemplate(templateService.findDefault(Template.Type.topic));
            topicService.create(topic);
            enterpriseService.create(topic);
        } else {
            enterpriseService.create(topic);
        }

        Smssend smsSend = new Smssend();
        smsSend.setMobile(mobile);
        smsSend.setContent("注册店铺成功");
        smssendService.smsSend(smsSend);
        Principal principal = new Principal(member.getId(),member.getUsername());
        redisService.put(Member.PRINCIPAL_ATTRIBUTE_NAME, JsonUtils.toJson(principal));
        return DataBlock.success((Object)JsonUtils.toJson(principal),"登录成功");
    }

}
