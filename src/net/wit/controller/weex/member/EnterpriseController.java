package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.AdminModel;
import net.wit.controller.model.CompanyViewsModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Resource(name = "companyLabelServiceImpl")
    private CompanyLabelService companyLabelService;
    

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

    /**
     *   添加企业信息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Message add(
            String name,
            String logo,
            String phone,
            Long area,
            String address,
            String startime,
            String endtime,
            Long[] label,
            String[] img,
            HttpServletRequest request){

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);

        if(admin==null){
            return Message.error("该用户没有企业");
        }

        Enterprise enterprise = admin.getEnterprise();

        if(enterprise==null||enterprise.getDeleted()){
            return Message.error("该企业不存在");
        }

        if(admin.getIsLocked()){
            return Message.error("该管理员已锁定,暂无权限");
        }

        if(!admin.getIsEnabled()){
            return Message.error("该管理员已停用");
        }

        if(!(admin.isRole("店长/主管")||admin.isRole("管理员"))){
            return Message.error("权限不足");
        }

        enterprise.setAddress(address);
        enterprise.setName(name);
        enterprise.setLogo(logo);
        enterprise.setStarTime(startime);
        enterprise.setEndTime(endtime);
        enterprise.setPhone(phone);
        enterprise.setArea(areaService.find(area));
        List<CompanyLabel> list=companyLabelService.findList(label);
        enterprise.setLabel(list);
        enterprise.setImage1(img[0]);
        enterprise.setImage2(img[1]);
        enterprise.setImage3(img[2]);
        enterprise.setImage4(img[3]);
        enterprise.setImage5(img[4]);
        enterprise.setImage6(img[5]);
        return Message.success("添加成功");
    }

    /**
     *   获取企业标签
     */
    @RequestMapping(value = "/getLabel")
    @ResponseBody
    public Message getLabel(ModelMap model,HttpServletRequest request, HttpServletResponse response){
        List<MapEntity> list=new ArrayList<>();
        List<CompanyLabel> companyLabelList= companyLabelService.findAll();
        for(CompanyLabel companyLabel:companyLabelList){
            list.add(new MapEntity(companyLabel.getId().toString(),companyLabel.getName()));
        }
       return Message.success(list,"success");
    }

    /**
     *   获取当前企业信息
     */
    @RequestMapping(value = "/views")
    @ResponseBody
    public Message views(
            HttpServletRequest request){

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);

        if(admin==null){
            return Message.error("该用户没有企业");
        }

        Enterprise enterprise = admin.getEnterprise();

        if(enterprise==null||enterprise.getDeleted()){
            return Message.error("该企业不存在");
        }

        if(admin.getIsLocked()){
            return Message.error("该管理员已锁定,暂无权限");
        }

        if(!admin.getIsEnabled()){
            return Message.error("该管理员已停用");
        }

        if(!(admin.isRole("店长/主管")||admin.isRole("管理员"))){
            return Message.error("权限不足");
        }
        CompanyViewsModel model=new CompanyViewsModel();

        model.bind(enterprise);
        return Message.success(model,"打开成功");
    }
}