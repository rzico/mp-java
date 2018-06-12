package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ReceiverModel;
import net.wit.entity.Card;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Receiver;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: ReceiverController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

@Controller("weexMemberReceiverController")
@RequestMapping("/weex/member/receiver")
public class ReceiverController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "roadServiceImpl")
    private RoadService roadService;

    @Resource(name = "receiverServiceImpl")
    private ReceiverService receiverService;

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

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
     *  列表
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long cardId,HttpServletRequest request){

        Card card =  cardService.find(cardId);

        if (card==null) {
            return Message.error("卡无效");
        }

        //卡主
        Member member = card.getMember();
        if (member==null) {
           if (card.getMembers().size()==1) {
               member = card.getMembers().get(0);
           }
        }

        Receiver receiver = member.defaultReceiver();

        ReceiverModel model = new ReceiverModel();
        if (receiver!=null) {
            model.bind(receiver);
        }

        return Message.bind(model,request);
    }

    /**
     *  添加文集
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Message add(Long areaId,String address,String consignee,String phone,Boolean isDefault,Integer level,HttpServletRequest request){
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
        receiver.setShop(null);

//        if (roadId!=null) {
//            receiver.setRoad(roadService.find(roadId));
//        }
//        if (lat!=null && lng!=null) {
//            Location location = new Location();
//            location.setLat(lat);
//            location.setLng(lng);
//            receiver.setLocation(location);
//        }

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
    public Message update(Long id,Long areaId,String address,String consignee,String phone,Boolean isDefault,Integer level,HttpServletRequest request){
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
        receiver.setShop(null);
//        if (roadId!=null) {
//            receiver.setRoad(roadService.find(roadId));
//        }
//        if (lat!=null && lng!=null) {
//            Location location = new Location();
//            location.setLat(lat);
//            location.setLng(lng);
//            receiver.setLocation(location);
//        }
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


    /**
     *  添加文集
     */
    @RequestMapping(value = "/addcard", method = RequestMethod.POST)
    @ResponseBody
    public Message addCard(Long areaId,String address,String consignee,String phone,Boolean isDefault,Integer level,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (areaId==null) {
            return Message.error("所在地区无效");
        }
        if (member.getTopic()==null) {
            return Message.error("没有开通专栏");
        }
        if (member.getTopic().getTopicCard()==null) {
            return Message.error("没有开通会员卡");
        }

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("phone", Filter.Operator.eq,phone));

        List<Card> cards = cardService.findList(null,null,filters,null);

        if (cards.size()>0) {
            return Message.error("当前电话已办卡");
        }

        Receiver receiver = new Receiver();
        receiver.setAddress(address);
        receiver.setConsignee(consignee);
        receiver.setArea(areaService.find(areaId));
        receiver.setAreaName(receiver.getArea().getFullName());
        receiver.setPhone(phone);
        receiver.setIsDefault(isDefault);
        receiver.setZipCode("000000");
        if (level==null) {
            level = 0;
        }
        receiver.setLevel(level);
        receiver.setShop(null);

        cardService.createAndMember(receiver,member);

        ReceiverModel model = new ReceiverModel();
        model.bind(receiver);
        return Message.success(model,"添加成功");
    }

}