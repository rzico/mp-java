package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.BarrelModel;
import net.wit.controller.model.BarrelStockModel;
import net.wit.entity.Barrel;
import net.wit.entity.BarrelStock;
import net.wit.entity.Card;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
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
 * @ClassName: BarrelStockController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberBarrelStockController")
@RequestMapping("/weex/member/barrel_stock")
public class BarrelStockController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "barrelStockServiceImpl")
    private BarrelStockService barrelStockService;

    @Resource(name = "cardServiceImpl")
    private CardService cardService;

    /**
     *  获取列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long cardId,HttpServletRequest request){
        Card card = cardService.find(cardId);
        if (card==null) {
            return Message.error("cardId 无效");
        }

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("card",Filter.Operator.eq,card));

        List<BarrelStock> bs = barrelStockService.findList(null,null,filters,null);

        Integer stock = 0;
        for (BarrelStock b:bs) {
            stock = stock + b.getStock();
        }
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("stock",stock);
        data.put("pledge",BigDecimal.ZERO);
        data.put("data", BarrelStockModel.bindList(bs));
        return Message.bind(data,request);

    }

}