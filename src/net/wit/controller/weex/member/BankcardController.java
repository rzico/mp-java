package net.wit.controller.weex.member;

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
import java.util.*;


/**
 * @ClassName: BankcardController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberBankcardController")
@RequestMapping("/weex/member/bankcard")
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

    /**
     * 银行信息查询
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Message query(String banknum,HttpServletRequest request){
        String method = "GET";
        String appcode = "7af4ab729dd345eaad8eebb918ada80c";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("bankcard", banknum);
        try {
            HttpResponse response = HttpUtils.doGet(queryApi, "/api/c43", method, headers, querys);
            String resp =  EntityUtils.toString(response.getEntity());
            Map<String,Object> data = JsonUtils.toObject(resp,Map.class);
            if ("0".equals(data.get("error_code"))) {
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
    @RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Message sendMobile(HttpServletRequest request) {
        String m = rsaService.decryptParameter("mobile", request);
        rsaService.removePrivateKey(request);
        if (m==null) {
            return Message.error("无效手机号");
        }
        int challege = StringUtils.Random6Code();
        String securityCode = String.valueOf(challege);

        SafeKey safeKey = new SafeKey();
        safeKey.setKey(m);
        safeKey.setValue(securityCode);
        safeKey.setExpire( DateUtils.addMinutes(new Date(),120));
        redisService.put(Member.MOBILE_LOGIN_CAPTCHA,JsonUtils.toJson(safeKey));

        Smssend smsSend = new Smssend();
        smsSend.setMobile(m);
        smsSend.setContent("验证码 :" + securityCode + ",只用于登录使用。");
        smssendService.smsSend(smsSend);
        return Message.success("发送成功");
    }

    /**
     *  绑定银行卡
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(String body,HttpServletRequest request){
        Member member = memberService.getCurrent();
        Redis redis = redisService.findKey(Member.MOBILE_LOGIN_CAPTCHA);
        if (redis==null) {
            return Message.error("验证码已过期");
        }
        redisService.remove(Member.MOBILE_LOGIN_CAPTCHA);
        SafeKey safeKey = JsonUtils.toObject(redis.getValue(),SafeKey.class);
        try {
            String captcha = rsaService.decryptParameter("captcha", request);
            logger.debug("绑定银行卡验证码："+captcha);
            rsaService.removePrivateKey(request);
            if (captcha==null) {
                return Message.error("无效验证码");
            }
            if (safeKey.hasExpired()) {
                return Message.error("验证码已过期");
            }
            if (!captcha.equals(safeKey.getValue())) {
                return Message.error("无效验证码");
            }

            Map<String,String> data = JsonUtils.toObject(body,Map.class);
            if (!safeKey.getKey().equals(data.get("mobile"))) {
                return Message.error("数据验证不合法");
            }

            String host = "https://aliyun-bankcard4-verify.apistore.cn";
            String path = "/bank4";
            String method = "GET";
            String appcode = "7af4ab729dd345eaad8eebb918ada80c";
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("Mobile", data.get("mobile"));
            querys.put("bankcard", data.get("cardNo"));
            querys.put("cardNo", data.get("identity"));
            querys.put("realName",data.get("name"));
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String resp =  EntityUtils.toString(response.getEntity());
            Map<String,Object> result = JsonUtils.toObject(resp,Map.class);
            if ("0".equals(result.get("error_code"))) {
                Map<String,Object> inf = JsonUtils.toObject(result.get("information").toString(),Map.class);
                if (!"1".equals(inf.get("iscreditcard"))){
                    return Message.error("只支持借记卡");
                }
                Bankcard bankcard = bankcardService.findDefault(member);
                if (bankcard==null) {
                    bankcard = new Bankcard();
                }
                bankcard.setBankimage(data.get("bankimage"));
                bankcard.setBankname(data.get("bankname"));
                bankcard.setBanknum(data.get("banknum"));
                bankcard.setCardname(data.get("cardname"));
                bankcard.setCardtype(data.get("cardtype"));
                bankcard.setCity(data.get("city"));
                bankcard.setProvince(data.get("province"));
                bankcard.setCardno(data.get("cardno"));
                bankcard.setIdentity(data.get("identity"));
                bankcard.setMobile(data.get("mobile"));
                bankcard.setName(data.get("name"));
                bankcard.setDefault(true);
                bankcard.setMember(member);
                if (bankcard.getId()!=null) {
                    memberService.update(member);
                } else {
                    memberService.save(member);
                }
                member.setName(data.get("name"));
                memberService.update(member);
                return Message.success("success");
            } else {
                return Message.error(result.get("reason").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("绑定失败");
        }
    }

}