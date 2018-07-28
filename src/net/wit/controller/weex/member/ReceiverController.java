package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.OrderListModel;
import net.wit.controller.model.ReceiverModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


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

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long tagIds, String type,String keyword, Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Admin admin = adminService.findByMember(member);

//        List<Order> orders = new ArrayList<Order>();
//        orders.add(new Order("isDefault", Order.Direction.desc));
        Page<Receiver> page = null;
        List<Filter> filters = pageable.getFilters();
        if (keyword!=null) {
            pageable.setSearchValue(keyword);
        }

//        if ("query".equals(type)) {
            page = receiverService.findPage(null,null,admin.getEnterprise().getMember(),pageable);
//        } else {
//            filters.add(new Filter("member", Filter.Operator.eq,member));
//            page = receiverService.findPage(null,null,pageable);
//        }
        PageBlock model = PageBlock.bind(page);
        model.setData(ReceiverModel.bindList(page.getContent()));
        return Message.bind(model,request);
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
        Receiver receiver = null;
        if (member!=null) {
            receiver = member.defaultReceiver();
        }

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
    public Message add(Long memberId,Long areaId,String address,String consignee,String phone,Boolean isDefault,Integer level,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        if (memberId!=null) {
            member = memberService.find(memberId);
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
//        receiver.setMember(member);
        if (level!=null) {
            receiver.setLevel(level);
        }

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
     *   判断用户是否存在
     */
    @RequestMapping(value = "exist")
    @ResponseBody
    public Message exist(String phone,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通店铺");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }

        Member owner = admin.getEnterprise().getMember();

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("mobile", Filter.Operator.eq,phone));
        if (!"3".equals(bundle.getString("weex"))) {
            filters.add(new Filter("owner",Filter.Operator.eq,owner));
        }

        List<Card> cards = cardService.findList(null,null,filters,null);

        if (cards.size()>0) {
            return Message.bind(true,request);
        } else {
            return Message.bind(false,request);
        }

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

        Admin admin = adminService.findByMember(member);
        if (admin==null) {
            return Message.error("没有开通");
        }
        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }

        Member owner = admin.getEnterprise().getMember();

        if (owner.getTopic()==null) {
            return Message.error("没有开通专栏");
        }
        if (owner.getTopic().getTopicCard()==null) {
            return Message.error("没有开通会员卡");
        }

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("mobile", Filter.Operator.eq,phone));
        if (!"3".equals(bundle.getString("weex"))) {
            filters.add(new Filter("owner",Filter.Operator.eq,owner));
        }

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
        receiver.setShop(admin.getShop());

        cardService.createAndMember(receiver,owner);

        ReceiverModel model = new ReceiverModel();
        model.bind(receiver);
        return Message.success(model,"添加成功");
    }

}