package net.wit.controller.makey.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.CounselorModel;
import net.wit.controller.makey.model.CounselorOrderModel;
import net.wit.controller.makey.model.EvaluationListModel;
import net.wit.entity.*;
import net.wit.service.AdminService;
import net.wit.service.CounselorOrderService;
import net.wit.service.CounselorService;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: CounselorController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("makeyMemberCounselorController")
@RequestMapping("/makey/member/counselor")
public class CounselorController extends BaseController {

    @Resource(name = "counselorOrderServiceImpl")
    private CounselorOrderService counselorOrderService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource(name = "counselorServiceImpl")
    private CounselorService counselorService;
    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long xmid, Pageable pageable, HttpServletRequest request){

        Member member = memberService.getCurrent();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq, member));

        Page<CounselorOrder> page = counselorOrderService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(CounselorOrderModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }


    /**
     *  提交
     * mobile 提交
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(Long counselorId,String worry,String mobile,String sex,String name,HttpServletRequest request) {
        Member member = memberService.getCurrent();
        CounselorOrder counselorOrder = new CounselorOrder();
        Counselor counselor = counselorService.find(counselorId);
        counselorOrder.setCounselor(counselor);
        counselorOrder.setEnterprise(counselor.getEnterprise());
        counselorOrder.setMember(member);
        counselorOrder.setMobile(mobile);
        counselorOrder.setName(name);
        counselorOrder.setSex(sex);
        counselorOrder.setWorry(worry);
        counselorOrder.setStatus(CounselorOrder.Status.enabled);
        counselorOrderService.save(counselorOrder);
        return Message.success("success");
    }
}