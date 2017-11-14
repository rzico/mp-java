package net.wit.controller.website.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleReviewModel;
import net.wit.controller.model.CardModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReview;
import net.wit.entity.Card;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName: CardController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteMemberCardController")
@RequestMapping("/website/member/card")
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

     /**
     *  激活会员卡
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(String mobile,String name,Long cardId,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Card card = cardService.find(cardId);
        if (card==null) {
            return Message.error("无效卡号");
        }
        card.setMobile(mobile);
        card.setName(name);
        card.setStatus(Card.Status.activate);
        card.setVip(Card.VIP.vip1);
        cardService.activate(card,member);
        CardModel model = new CardModel();
        model.bind(card);
        return Message.success(model,"激活成功");
    }

    /**
     *   获取会员卡
     */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
    @ResponseBody
    public Message view(Long cardId,HttpServletRequest request){
        Card card = cardService.find(cardId);
        if (card==null) {
            return Message.error("无效卡号");
        }
        CardModel model = new CardModel();
        model.bind(card);
        return Message.success(model,"激活成功");
    }
}