package net.wit.controller.applet.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ReceiverModel;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Receiver;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.service.ReceiverService;
import net.wit.service.RoadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: ArticleCatalogController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberReceiverController")
@RequestMapping("/applet/member/receiver")
public class ReceiverController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "roadServiceImpl")
    private RoadService roadService;

    @Resource(name = "receiverServiceImpl")
    private ReceiverService receiverService;

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long tagIds,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        List<Order> orders = new ArrayList<Order>();
        orders.add(new Order("isDefault", Order.Direction.desc));

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        List<Receiver> receivers = receiverService.findList(null,null,filters,orders);

        return Message.bind(ReceiverModel.bindList(receivers),request);
    }


    /**
     *  添加文集
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Message add(Long areaId,String address,String consignee,String phone,Boolean isDefault,Integer level,Double lat,Double lng,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (areaId==null) {
            return Message.error("所在地区无效");
        }
        Receiver receiver = new Receiver();
        receiver.setAddress(address);
        receiver.setConsignee(consignee);
        receiver.setArea(areaService.find(areaId));
        receiver.setAreaName(receiver.getArea().getFullName());
        receiver.setPhone(phone);
        receiver.setIsDefault(isDefault);
        receiver.setZipCode("000000");
        receiver.setMember(member);
        if (level==null) {
            level = 0;
        }
        receiver.setLevel(level);

//        if (roadId!=null) {
//            receiver.setRoad(roadService.find(roadId));
//        }

        if (lat!=null && lng!=null) {
            Location location = new Location();
            location.setLat(lat);
            location.setLng(lng);
            receiver.setLocation(location);
        }

        receiverService.save(receiver);

        ReceiverModel model = new ReceiverModel();
        model.bind(receiver);
        return Message.success(model,"添加成功");
    }

    /**
     *  修改文集
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Long id,Long areaId,String address,String consignee,String phone,Boolean isDefault,Integer level,Double lat,Double lng,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Receiver receiver = receiverService.find(id);
        if (receiver==null) {
            return Message.error("无效地址id");
        }
        receiver.setAddress(address);
        receiver.setConsignee(consignee);
        receiver.setArea(areaService.find(areaId));
        receiver.setAreaName(receiver.getArea().getFullName());
        receiver.setPhone(phone);
        receiver.setIsDefault(isDefault);
        receiver.setZipCode("000000");
        receiver.setMember(member);
        if (level!=null) {
            receiver.setLevel(level);
        }
//        if (roadId!=null) {
//            receiver.setRoad(roadService.find(roadId));
//        }
        if (lat!=null && lng!=null) {
            Location location = new Location();
            location.setLat(lat);
            location.setLng(lng);
            receiver.setLocation(location);
        }
//
        receiverService.update(receiver);
        ReceiverModel model = new ReceiverModel();
        model.bind(receiver);
        return Message.success(model,"修改成功");
    }

    /**
     *  设为默认
     */
    @RequestMapping(value = "/default", method = RequestMethod.POST)
    @ResponseBody
    public Message setDefault(Long id,Boolean isDefault,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Receiver receiver = receiverService.find(id);
        if (receiver==null) {
            return Message.error("无效地址id");
        }
        receiver.setIsDefault(isDefault);
        receiverService.update(receiver);
        ReceiverModel model = new ReceiverModel();
        model.bind(receiver);
        return Message.success(model,"修改成功");
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

        receiverService.delete(id);
        return Message.success("删除成功");
    }
}