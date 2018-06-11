package net.wit.controller.admin;

import net.wit.*;
import net.wit.controller.model.live.UserInfo;
import net.wit.entity.Member;
import net.wit.entity.Role;
import net.wit.plat.im.Push;
import net.wit.plat.im.User;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller("adminRobotController")
@RequestMapping("/admin/robot")
public class RobotController extends BaseController{

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;


    /**
     * 主页
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {



        return "/admin/robot/list";
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {



        return "/admin/robot/add";
    }
    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message save(Member m, HttpServletRequest request){
        Member member = new Member();
        member.setNickName(m.getNickName());
        member.setLogo(m.getLogo());
        member.setPoint(0L);
        member.setAmount(BigDecimal.ZERO);
        member.setBalance(BigDecimal.ZERO);
        member.setFreezeBalance(BigDecimal.ZERO);
        member.setVip(Member.VIP.vip1);
        member.setIsEnabled(true);
        member.setIsLocked(false);
        member.setUserType(Member.UserType.ROBOT);
        member.setLoginFailureCount(0);
        member.setRegisterIp(request.getRemoteAddr());
        memberService.save(member);
        if(!User.userAttr(member)){
            return Message.success("admin.save.success");
        }
        return Message.success("admin.save.success");
    }
    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids,Date beginDate, Date endDate, Pageable pageable, String searchValue, ModelMap model) {
//        memberService.delete(ids);
        //
        ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
        Filter mediaTypeFilter = new Filter("userType", Filter.Operator.eq, Member.UserType.ROBOT);

        filters.add(mediaTypeFilter);
        Page<Member> page = memberService.findPage(beginDate,endDate,pageable);
        List<Member> members = page.getContent();
        Member member = members.get(0);
        UserInfo userInfo = new UserInfo();
        userInfo.id = member.getId();
        userInfo.nickName = member.getNickName();
        userInfo.headPic = member.getLogo();
        userInfo.text = "主播你好呀！";
        userInfo.cmd = "CustomTextMsg";
        try {
            Push.impushgroup("1",userInfo);
            return Message.success("admin.delete.success");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return Message.success("admin.delete.error");
        }

    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {



        model.addAttribute("data",memberService.find(id));

        return "/admin/robot/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Role role){
            return Message.error("admin.update.error");
    }



    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Date beginDate, Date endDate, Pageable pageable, String searchValue, ModelMap model) {
        ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
            Filter mediaTypeFilter = new Filter("userType", Filter.Operator.eq, Member.UserType.ROBOT);

            filters.add(mediaTypeFilter);
        Page<Member> page = memberService.findPage(beginDate,endDate,pageable);
        return Message.success(PageBlock.bind(page), "admin.list.success");
    }
}
