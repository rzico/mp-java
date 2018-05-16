package net.wit.controller.applet.templet;

import net.wit.Message;
import net.wit.controller.model.CompanyDetailsModel;
import net.wit.entity.Admin;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.service.AdminService;
import net.wit.service.EnterpriseService;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Eric-Yang on 2018/5/9.
 */
@Controller("appletCompanyDetailsController")
@RequestMapping("applet/templet")
public class CompanyDetails {

    @Resource(name = "enterpriseServiceImpl")
    EnterpriseService enterpriseService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     * 公司详情
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id, HttpServletRequest request) {
        Member member = memberService.find(id);
        Admin admin = adminService.findByMember(member);
        Enterprise enterprise = admin.getEnterprise();
        CompanyDetailsModel model = new CompanyDetailsModel();
        model.bind(enterprise);
        return Message.bind(model, request);
    }
}
