package net.wit.controller.applet.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.MemberAttributeModel;
import net.wit.controller.model.MemberModel;
import net.wit.controller.model.MemberOptionModel;
import net.wit.entity.*;
import net.wit.plat.im.User;
import net.wit.service.*;
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
 * @ClassName: MemberController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberController")
@RequestMapping("/applet/member")
public class MemberController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "occupationServiceImpl")
    private OccupationService occupationService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "friendsServiceImpl")
    private FriendsService friendsService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     * 获取当前会员信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        MemberModel model =new MemberModel();
        model.bind(member);
        return Message.bind(model,request);
   }

    /**
     * 获取会员属性
     */
    @RequestMapping(value = "/attribute", method = RequestMethod.GET)
    @ResponseBody
    public Message attribute(HttpServletRequest request){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        MemberAttributeModel model =new MemberAttributeModel();
        model.bind(member);
        BindUser bindUser = bindUserService.findMember(member,bundle.getString("app.appid"), BindUser.Type.weixin);
        model.setBindWeiXin(bindUser!=null);
        return Message.bind(model,request);
    }


    /**
     * 获取会员选项
     */
    @RequestMapping(value = "/option", method = RequestMethod.GET)
    @ResponseBody
    public Message option(HttpServletRequest request){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        MemberOptionModel model =new MemberOptionModel();
        model.bind(member);
        Long black = friendsService.count(
                new Filter("member", Filter.Operator.eq,member)
                ,
                new Filter("status", Filter.Operator.eq, Friends.Status.black)
        );
        model.setBlack(black.intValue());
        return Message.bind(model,request);
    }

    /**
     * 修改会员属性
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(String username, String nickName, String autograph, Date birth, String logo, Member.Gender gender, Long areaId, Long occupationId,String qrcode, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        if (username!=null) {
           Member chk = memberService.findByUsername(username);
           if (chk!=null && !chk.equals(member)) {
               return Message.error("账号已经被使用");
           }
           member.setUsername(username);
        }
        if (nickName!=null) {
            member.setNickName(nickName);
            Admin admin = adminService.findByMember(member);
            if (admin!=null) {
                if (member.getName()!=null) {
                    admin.setName(member.getName());
                } else {
                    admin.setName(nickName);
                }
                adminService.update(admin);
            }
        }
        if (autograph!=null) {
            member.setAutograph(autograph);
        }
        if (logo!=null) {
            member.setLogo(logo);
        }
        if (gender!=null) {
            member.setGender(gender);
        }
        if (birth!=null) {
            member.setBirth(birth);
        }
        if (qrcode!=null) {
            member.setQrcode(qrcode);
        }
        if (areaId!=null) {
            Area area = areaService.find(areaId);
            if (area==null) {
                return Message.error("无效城市代码");
            }
            member.setArea(area);
        }
        if (occupationId!=null) {
            Occupation occupation = occupationService.find(occupationId);
            if (occupation==null) {
                return Message.error("无效职业代码");
            }
            member.setOccupation(occupation);
        }
        memberService.update(member);
        if (nickName!=null || logo!=null) {
           if (!User.userAttr(member)) {
               return Message.success("上传IM失败");
           };
        }
        return Message.success("获取会员信息成功");
    }

}