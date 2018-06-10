package net.wit.controller.applet.member;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.HttpUtils;
import net.wit.util.JsonUtils;
import net.wit.util.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;


/**
 * @ClassName: BankcardController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletBankcardController")
@RequestMapping("/applet/member/bankcard")
public class BankcardController extends BaseController {

    private String queryApi="http://api43.market.alicloudapi.com";

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

    /**
     * 银行信息查询
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Message query(String banknum,HttpServletRequest request){
        try {
            banknum = new String(org.apache.commons.codec.binary.Base64.decodeBase64(banknum),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // rsaService.decryptParameter("banknum", request);
//        rsaService.removePrivateKey(request);
        if (banknum==null) {
            return Message.error("银行卡号解密为空");
        }
        String method = "GET";
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String appcode =  bundle.getString("bank.appcode");// "7af4ab729dd345eaad8eebb918ada80c";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("bankcard", banknum);
        try {
            HttpResponse response = HttpUtils.doGet(queryApi, "/api/c43", method, headers, querys);
            String resp =  EntityUtils.toString(response.getEntity());
            Map<String,Object> data = JsonUtils.toObject(resp,Map.class);
            if ("0".equals(data.get("error_code").toString())) {
                return Message.success(data.get("result"),"success");
            } else {
                return Message.error(data.get("reason").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("获取银行信息失败");
        }
    }

    /**
     * 手机验证码登录时，发送验证码
     * mobile 手机号
     */
    @RequestMapping(value = "/send_mobile")
    @ResponseBody
    public Message sendMobile(String mobile,HttpServletRequest request) {


        String m = null;

        if (mobile!=null) {
            try {
                m = new String(org.apache.commons.codec.binary.Base64.decodeBase64(mobile), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

//        String m = rsaService.decryptParameter("mobile", request);
//        rsaService.removePrivateKey(request);

        if (m==null) {
            Member member = memberService.getCurrent();
            if (member!=null & member.getMobile()!=null) {
                m = member.getMobile();
            } else {
                return Message.error("无效手机号");
            }
        }

        int challege = StringUtils.Random6Code();
        String securityCode = String.valueOf(challege);

        SafeKey safeKey = new SafeKey();
        safeKey.setKey(m);
        safeKey.setValue(securityCode);
        safeKey.setExpire( DateUtils.addMinutes(new Date(),300));
        redisService.put(Member.MOBILE_BIND_CAPTCHA,JsonUtils.toJson(safeKey));

        Smssend smsSend = new Smssend();
        smsSend.setMobile(m);
        smsSend.setContent("验证码 :" + securityCode + ",只用于绑定银行卡。");
        smssendService.smsSend(smsSend);
        return Message.success("发送成功");
    }

    /**
     * 验证合法性
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.POST)
    @ResponseBody
    public Message captcha(String captcha,HttpServletRequest request){
        Redis redis = redisService.findKey(Member.MOBILE_BIND_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        Member member =memberService.getCurrent();
        try {
            if (!member.getMobile().equals(safeKey.getKey())) {
                return Message.error("无效验证码");
            }
            try {
                captcha = new String(org.apache.commons.codec.binary.Base64.decodeBase64(captcha),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            String captcha = rsaService.decryptParameter("captcha", request);
//            rsaService.removePrivateKey(request);
            if (member==null) {
                return Message.error("无效验证码");
            }
            if (captcha==null) {
                return Message.error("无效验证码");
            }
            if (safeKey.hasExpired()) {
                return Message.error("验证码已过期");
            }
            if (!captcha.equals(safeKey.getValue())) {
                return Message.error("无效验证码");
            }
            return Message.success(member,"验证成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("验证失败");
        }
    }

    /**
     *  绑定银行卡
     */
    @RequestMapping(value = "/submit")
    @ResponseBody
    public Message submit(String captcha,String body,HttpServletRequest request){
        Member member = memberService.getCurrent();
        System.out.println(body);
        System.out.println(captcha);
        Redis redis = redisService.findKey(Member.MOBILE_BIND_CAPTCHA);
        redisService.remove(Member.MOBILE_BIND_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        try {
            if (captcha==null) {
                return Message.error("无效验证码");
            }
            if (safeKey.hasExpired()) {
                return Message.error("验证码已过期");
            }
            if (!captcha.equals(safeKey.getValue())) {
                return Message.error("无效验证码");
            }

            String mima = null;
            try {
                mima = new String(org.apache.commons.codec.binary.Base64.decodeBase64(body),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            System.out.println(mima);

//            String mima = rsaService.decryptValue(body, request);
//            rsaService.removePrivateKey(request);

            if (mima==null) {
                return Message.error("数据解密失败");
            }
            Map<String,String> data = JsonUtils.toObject(mima,Map.class);
            if (!safeKey.getKey().equals(data.get("mobile"))) {
                return Message.error("手机验证不合法");
            }

            String host = "https://aliyun-bankcard-verify.apistore.cn";
//            String host = "https://aliyun-bankcard4-verify.apistore.cn";
            String path = "/bank";
//            String path = "/bank4";
            String method = "GET";

            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            String appcode =  bundle.getString("bank.appcode");// "7af4ab729dd345eaad8eebb918ada80c";

            Map<String, String> headers = new HashMap<String, String>();

            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            Map<String, String> querys = new HashMap<String, String>();
//            querys.put("Mobile", data.get("mobile"));
            querys.put("bankcard", data.get("cardno"));
//            querys.put("cardNo", data.get("identity"));
            querys.put("realName",data.get("name"));
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String resp =  EntityUtils.toString(response.getEntity());
            JSONObject result = JSONObject.fromObject(resp);

            System.out.println(result);
            if ("0".equals(result.getString("error_code"))) {
                JSONObject inf = result.getJSONObject("result").getJSONObject("information");
                if (!"1".equals(inf.getString("iscreditcard"))){
                    return Message.error("只支持借记卡");
                }
                Boolean isNew = false;
                Bankcard bankcard = bankcardService.findDefault(member);
                if (bankcard==null) {
                    bankcard = new Bankcard();
                    isNew = true;
                }
                bankcard.setBankimage(inf.getString("bankimage"));
                bankcard.setBankname(inf.getString("bankname"));
                bankcard.setBanknum(inf.getString("banknum"));
                bankcard.setCardname(inf.getString("cardname"));
                bankcard.setCardtype(inf.getString("cardtype"));
                bankcard.setCity(data.get("city"));
                bankcard.setProvince(data.get("province"));
                bankcard.setCardno(data.get("cardno"));
                bankcard.setIdentity(data.get("identity"));
                bankcard.setMobile(data.get("mobile"));
                bankcard.setName(data.get("name"));
                bankcard.setDefault(true);
                bankcard.setMember(member);
                if (!isNew) {
                    bankcardService.update(bankcard);
                } else {
                    bankcardService.save(bankcard);
                }
                System.out.println(bankcard);
                if (member.getMobile()==null) {
                    Member m = memberService.findByMobile(data.get("mobile"));
                    if (m!=null) {
                        m.setUsername(null);
                        m.setMobile(null);
                        memberService.save(m);
                    }
                    member.setUsername(data.get("mobile"));
                    member.setMobile(data.get("mobile"));
                }
                member.setName(data.get("name"));
                memberService.update(member);
                for (Card card:member.getCards()) {
                    card.setMobile(member.getMobile());
                    card.setName(member.getName());
                    cardService.update(card);
                }
                Admin admin = adminService.findByMember(member);
                if (admin!=null) {
                    admin.setName(member.getName());
                    adminService.update(admin);
                }
                return Message.success("绑定成功");
            } else {
                return Message.error(result.get("reason").toString());
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Message.error("绑定失败");
        }
    }

}