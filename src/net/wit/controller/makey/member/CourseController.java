package net.wit.controller.makey.member;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.CourseOrderModel;
import net.wit.controller.makey.model.EvaluationListModel;
import net.wit.controller.makey.model.EvaluationModel;
import net.wit.controller.makey.model.PromoterListModel;
import net.wit.controller.model.PaymentModel;
import net.wit.entity.*;
import net.wit.entity.summary.EvaluationSummary;
import net.wit.service.*;
import net.wit.util.MD5Utils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CourseController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("makeyMemberCourseController")
@RequestMapping("/makey/member/course")
public class CourseController extends BaseController {

    @Resource(name = "courseServiceImpl")
    private CourseService courseService;
    @Resource(name = "courseOrderServiceImpl")
    private CourseOrderService courseOrderService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

      /**
     * 创建订单
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public Message create(Long id,Long xuid,Long agent,Long timeStamp,String sign,HttpServletRequest request){
        Course course = courseService.find(id);
        if (course==null) {
            return Message.error("无效量表编号");
        }
        Member member = memberService.getCurrent();
        if (member==null && agent!=null) {
            Enterprise enterprise = enterpriseService.find(agent);
            if (!MD5Utils.getMD5Str(String.valueOf(id) +String.valueOf(xuid) + String.valueOf(agent) + String.valueOf(timeStamp)+MD5Utils.getMD5Str(enterprise.getId().toString()+"rzico.com")).equals(sign)) {
                return Message.error("无效签名");
            }
            member = enterprise.getMember();
        }
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        CourseOrder eval =  new CourseOrder();
        eval.setName(course.getName());
        eval.setThumbnail(course.getThumbnail());
        eval.setOrderStatus(CourseOrder.OrderStatus.unconfirmed);
        eval.setCourse(course);
        eval.setMember(member);
        eval.setPrice(course.getPrice());
        eval.setEnterprise(course.getEnterprise());
        eval.setType(CourseOrder.Type.valueOf(course.getType().name()));

        if (xuid!=null) {
            Member promoter = memberService.find(xuid);
            if (promoter!=null) {
                eval.setPromoter(promoter);
            }
        }
        Payment payment = courseOrderService.create(eval);
        Map<String,Object> data = new HashMap<String,Object>();
        PaymentModel model = new PaymentModel();
        model.bind(payment);
        data.put("payment",model);
        CourseOrderModel evalm = new CourseOrderModel();
        evalm.bind(eval);
        data.put("courseOrder",evalm);
        return Message.bind(data,request);

    }

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(CourseOrder.OrderStatus status,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        List<Filter> filters = new ArrayList<Filter>();
        if (status==null) {
            status = CourseOrder.OrderStatus.confirmed;
        }
        if (status!=null) {
            filters.add(new Filter("orderStatus", Filter.Operator.eq, status));
        }
        filters.add(new Filter("member", Filter.Operator.eq, member));
        pageable.setFilters(filters);
        Page<CourseOrder> page = courseOrderService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(CourseOrderModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     * 付款
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    @ResponseBody
    public Message payment(String sn,Long agent,Long timeStamp,String sign,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null&&agent!=null) {
            Enterprise enterprise = enterpriseService.find(agent);
            if (!MD5Utils.getMD5Str(sn+ String.valueOf(agent) + String.valueOf(timeStamp)+MD5Utils.getMD5Str(enterprise.getId().toString()+"rzico.com")).equals(sign)) {
                return Message.error("无效签名");
            }
            member = enterprise.getMember();
        }
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Payment payment = paymentService.findBySn(sn);
        if (payment==null) {
            return Message.error("无效付款单号");
        }

        try {
            paymentService.handle(payment);
        } catch (Exception e) {
            return Message.error("付款失败");
        }
        return Message.success("success");
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,Long agent,Long timeStamp,String sign,HttpServletRequest request){
        CourseOrder courseOrder = courseOrderService.find(id);
        if (courseOrder==null) {
            return Message.error("无效测评编号");
        }

        Member member = memberService.getCurrent();
        if (member==null&&agent!=null) {
            Enterprise enterprise = enterpriseService.find(agent);
            if (!MD5Utils.getMD5Str(String.valueOf(id)+ String.valueOf(agent) + String.valueOf(timeStamp)+MD5Utils.getMD5Str(enterprise.getId().toString()+"rzico.com")).equals(sign)) {
                return Message.error("无效签名");
            }
            member = enterprise.getMember();
        }
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        CourseOrderModel model =new CourseOrderModel();
        model.bind(courseOrder);
        return Message.bind(model,request);
    }
}