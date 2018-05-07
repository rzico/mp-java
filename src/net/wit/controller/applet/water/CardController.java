package net.wit.controller.applet.water;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: CardController
 * @author 降魔战队  会员卡
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexWaterCardController")
@RequestMapping("/weex/water/card")
public class CardController extends BaseController {

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

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "cardBillServiceImpl")
    private CardBillService cardBillService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

    @Resource(name = "cardPointBillServiceImpl")
    private CardPointBillService cardPointBillService;

    // 自定义比较器：按书的价格排序
    static class AmountComparator implements Comparator {
        public int compare(Object object1, Object object2) {// 实现接口中的方法
            Map<String, Object> p1 = (Map<String, Object>) object1; // 强制转换
            Map<String, Object> p2 = (Map<String, Object>) object2;
            return new BigDecimal(p1.get("amount").toString()).compareTo(new BigDecimal(p2.get("amount").toString()));
        }
    }

    private BigDecimal calculate(Topic topic,BigDecimal amount) {
        if (topic.getTopicCard()==null) {
            return BigDecimal.ZERO;
        }

        if (topic.getTopicCard().getActivity()==null) {
            return BigDecimal.ZERO;
        }

        List<Map<String, Object>> activitys = JsonUtils.toObject(topic.getTopicCard().getActivity(),List.class);
        Collections.sort(activitys, new AmountComparator());

        Map<String, Object> curr = null;
        for (Map<String, Object> model:activitys) {
            if (new BigDecimal(model.get("amount").toString()).compareTo(amount)>0) {
                break;
            } else {
                curr = model;
            }
        }

        if (curr!=null) {
            return new BigDecimal(curr.get("present").toString());
        } else {
            return BigDecimal.ZERO;
        }

    }

    /**
     * 计算手续费
     */
    @RequestMapping(value = "calculate")
    @ResponseBody
    public Message calculateFee(Long id,BigDecimal amount,HttpServletRequest request){
        Card card = cardService.find(id);
        return Message.success(calculate( card.getOwner().getTopic(),amount),"success");
    }

    /**
     *  提交收款
     *  id amount
     */
    @RequestMapping(value = "/fill", method = RequestMethod.POST)
    @ResponseBody
    public Message fill(Long id,BigDecimal amount,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }
        if (!card.getStatus().equals(Card.Status.activate)) {
            return Message.error("会员卡没激活");
        }
        PayBill payBill = new PayBill();
        payBill.setType(PayBill.Type.card);
        payBill.setAmount(amount);
        BigDecimal present = calculate(card.getOwner().getTopic(),amount);
        payBill.setCardAmount(amount.add(present));
        payBill.setNoDiscount(BigDecimal.ZERO);
        payBill.setCouponCode(null);
        payBill.setCouponDiscount(BigDecimal.ZERO);
        payBill.setCardDiscount(BigDecimal.ZERO);
        payBill.setMethod(PayBill.Method.online);
        payBill.setStatus(PayBill.Status.none);
        payBill.setMember(member);
        payBill.setOwner(card.getOwner());
        payBill.setShop(null);
        payBill.setAdmin(null);
        payBill.setCard(card);
        Admin admin = adminService.findByMember(card.getOwner());
        payBill.setEnterprise(admin.getEnterprise());
        BigDecimal effective = payBill.getEffectiveAmount();
        payBill.setFee(admin.getEnterprise().calcFee(effective));
        try {
            if (amount.compareTo(BigDecimal.ZERO)<=0) {
                return Message.error("请输入充值金额");
            }
            Payment payment = payBillService.cardFill(payBill);
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("id",payBill.getId());
            data.put("sn",payment.getSn());
            return Message.success(data,"success");
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
    }

    /**
     *  获取设置活动
     */
    @RequestMapping(value = "/activity",method = RequestMethod.GET)
    @ResponseBody
    public Message activity(Long id,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Card card = cardService.find(id);
        if (card==null) {
            return Message.error("无效卡号");
        }

        Topic topic = card.getOwner().getTopic();
        if (topic==null) {
            return Message.error("没有开通专栏");
        }
        TopicCard topicCard = topic.getTopicCard();
        if (topicCard==null) {
            return Message.error("没有开通会员卡");
        }

        List<CardActivityModel> activitys = new ArrayList<CardActivityModel>();
        if (topicCard.getActivity()!=null) {
            activitys = JsonUtils.toObject(topicCard.getActivity(), List.class);
        }

        return Message.success(activitys,"获取成功");

    }


}