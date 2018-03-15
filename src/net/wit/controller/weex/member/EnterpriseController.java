package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.AdminModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: EnterpriseController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberEnterpriseController")
@RequestMapping("/weex/member/enterprise")
public class EnterpriseController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

    /**
     *   企业信息
     */
    @RequestMapping(value = "/view")
    @ResponseBody
    public Message view(HttpServletRequest request){

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Map<String,Object> data = new HashMap<String,Object>();

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            data.put("status",admin.getEnterprise().getStatus());
            Enterprise enterprise = admin.getEnterprise();

            data.put("logo",enterprise.getLogo());
            data.put("name",enterprise.getName());
            if (admin.getShop()!=null) {
                data.put("shopName",admin.getShop().getName());
            } else {
                data.put("shopName","未分配");
            }
            data.put("isOwner",admin.isOwner());
            data.put("creditLine",enterprise.getCreditLine());
            if (admin.isOwner()) {
                data.put("roleName","店主");
            } else {
                String s = "";
                for (Role role:admin.getRoles()) {
                    if (s.equals("")) {
                        s = s + ",";
                    }
                    s = s +role.getName();
                }
                data.put("roleName",s);
            }
        } else {
            data.put("status","none");
        }

        return Message.bind(data,request);

    }

    /**
     *  申请代理
     */
    @RequestMapping(value = "/create_agent")
    @ResponseBody
    public Message create_enterprise(String name,String mobile,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (member.getName()==null) {
            return Message.error("请先绑定银行卡");
        }
        Enterprise enterprise = enterpriseService.createAgent(member);
        enterprise.setPhone(mobile);
        enterprise.setLinkman(name);
        enterpriseService.update(enterprise);

        return Message.success("申请成功");

    }

    /**
     *  解除就业
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Message delete(HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        Enterprise enterprise = admin.getEnterprise();
        if (enterprise==null) {
            return Message.error("不在就业状态");
        }

        admin.setEnterprise(null);
        admin.setShop(null);

        adminService.update(admin);
        return Message.success("解除就业成功");
    }

}