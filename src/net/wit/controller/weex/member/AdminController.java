package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.AdminModel;
import net.wit.controller.model.CouponModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: AdminController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberAdminController")
@RequestMapping("/weex/member/admin")
public class AdminController extends BaseController {

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

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

    /**
     *  添加员工
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Message add(Long id,String code,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (code!=null) {
            id = Long.parseLong(code)-10200L;
        }
        Member adminMember = memberService.find(id);
        if (adminMember==null) {
            return Message.error("无效会员id");
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }

        Enterprise enterprise = admin.getEnterprise();

        if (code!=null) {
            Admin r = enterpriseService.addAdmin(enterprise,adminMember);
            if (r==null) {
                return Message.error("就业状态，请先解除就业关系");
            }
            try {
                memberService.create(adminMember,member);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            AdminModel model = new AdminModel();
            model.bind(r);
            return Message.success(model,"添加成功");
        } else {
            return Message.success("暂不支持");
        }
    }

    /**
     *  分配店铺
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Message update(Long id,Long shopId,Long roleId,HttpServletRequest request){

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }

        Admin adminMember = adminService.find(id);
        if (adminMember==null) {
            return Message.error("员工id");
        }

        if (shopId!=null) {
            Shop shop = shopService.find(shopId);
            if (shop == null) {
                return Message.error("店铺id无效");
            }
            adminMember.setShop(shop);
        }
        if (roleId!=null) {
            if (adminMember.isOwner()) {
                return Message.error("店主不能设置角色");
            }
            Role role = roleService.find(roleId);
            List<Role> roles = adminMember.getRoles();
            if (roles==null) {
                roles = new ArrayList<Role>();
            }
            roles.clear();
            roles.add(role);
            adminMember.setRoles(roles);
        }

        adminService.update(adminMember);
        AdminModel model = new AdminModel();
        model.bind(adminMember);
        return Message.success(model,"修改成功");
    }

    /**
     *  删除员工
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Message delete(Long id,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin adminMember = adminService.find(id);
        if (adminMember==null) {
            return Message.error("无效员工id");
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }
        Enterprise enterprise = admin.getEnterprise();

        if (adminMember.isOwner()) {
            return Message.error("不能删除店主");
        }
        if (!adminMember.getEnterprise().equals(enterprise)) {
            return Message.error("不能你的员工");
        }

        adminMember.setEnterprise(null);
        adminMember.setShop(null);

        adminService.update(adminMember);
        return Message.success("删除成功");
    }

    /**
     *  员工列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有点亮专栏");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }
        Enterprise enterprise = admin.getEnterprise();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("enterprise", Filter.Operator.eq,enterprise));
        pageable.setFilters(filters);
        pageable.setOrderProperty("shop");
        pageable.setOrderDirection(Order.Direction.asc);
        Page<Admin> page = adminService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(AdminModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}