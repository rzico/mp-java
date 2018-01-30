package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.DistributionModel;
import net.wit.controller.model.ProductCategoryModel;
import net.wit.entity.Admin;
import net.wit.entity.Distribution;
import net.wit.entity.Member;
import net.wit.entity.ProductCategory;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: DistributionController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberDistributionController")
@RequestMapping("/weex/member/distribution")
public class DistributionController extends BaseController {

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

    @Resource(name = "distributionServiceImpl")
    private DistributionService distributionService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            member = admin.getEnterprise().getMember();
        }

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        List<Distribution> categories = distributionService.findList(null,null,filters,null);

        return Message.bind(DistributionModel.bindList(categories),request);
    }

    /**
     *  排序
     */
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    @ResponseBody
    public Message sort(Long[] ids,HttpServletRequest request){
        int i=0;
        for (Long id:ids) {
            Distribution catalog = distributionService.find(id);
            i=i+1;
            catalog.setOrders(i);
            distributionService.update(catalog);
        }
        return Message.success("success");
    }

    /**
     *  添加文集
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Message add(String name, BigDecimal percent1,BigDecimal percent2, BigDecimal percent3, BigDecimal point,  Integer orders, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            member = admin.getEnterprise().getMember();
        }

        Distribution catalog = new Distribution();
        catalog.setOrders(orders);
        catalog.setName(name);
        catalog.setMember(member);

        if (percent1!=null) {
            catalog.setPercent1(percent1);
        } else {
            catalog.setPercent1(BigDecimal.ZERO);
        }
        if (percent2!=null) {
            catalog.setPercent2(percent2);
        } else {
            catalog.setPercent2(BigDecimal.ZERO);
        }
        if (percent3!=null) {
            catalog.setPercent3(percent3);
        } else {
            catalog.setPercent3(BigDecimal.ZERO);
        }
        if (point!=null) {
            catalog.setPoint(point);
        } else {
            catalog.setPoint(BigDecimal.ZERO);
        }

        distributionService.save(catalog);

        DistributionModel model = new DistributionModel();
        model.bind(catalog);
        return Message.success(model,"添加成功");
    }

    /**
     *  修改文集
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Long id,String name,BigDecimal percent1,BigDecimal percent2, BigDecimal percent3, BigDecimal point, Integer orders,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }


        Admin admin = adminService.findByMember(member);
        if (admin!=null && admin.getEnterprise()!=null) {
            member = admin.getEnterprise().getMember();
        }

        Distribution catalog = distributionService.find(id);
        if (catalog==null) {
            return Message.error("无效策略id");
        }
        if (orders!=null) {
            catalog.setOrders(orders);
        }
        catalog.setName(name);
        catalog.setMember(member);

        if (percent1!=null) {
            catalog.setPercent1(percent1);
        } else {
            catalog.setPercent1(BigDecimal.ZERO);
        }
        if (percent2!=null) {
            catalog.setPercent2(percent2);
        } else {
            catalog.setPercent2(BigDecimal.ZERO);
        }
        if (percent3!=null) {
            catalog.setPercent3(percent3);
        } else {
            catalog.setPercent3(BigDecimal.ZERO);
        }
        if (point!=null) {
            catalog.setPoint(point);
        } else {
            catalog.setPoint(BigDecimal.ZERO);
        }

        distributionService.save(catalog);
        DistributionModel model = new DistributionModel();
        model.bind(catalog);
        return Message.success(model,"添加成功");
    }


    /**
     *  删除文集
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long id,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Distribution catalog = distributionService.find(id);
        if (catalog==null) {
            return Message.error("无效策略id");
        }
        if (catalog.getProducts().size()>0) {
            return Message.error("有商品不能删");
        }

        distributionService.delete(id);
        return Message.success("删除成功");
    }
}