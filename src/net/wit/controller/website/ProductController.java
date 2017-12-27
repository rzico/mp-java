package net.wit.controller.website;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.entity.Admin;
import net.wit.entity.Article;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * @ClassName: ProductController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteProductController")
@RequestMapping("/website/product")
public class ProductController extends BaseController {

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

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     *  商品信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message  view(Long articleId,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        Map<String,String> data = new HashMap<>();
        Member member = article.getMember();
        Admin admin = adminService.findByMember(member);
        if (member.getTopic()!=null && member.getTopic().getConfig().getUseCard() && admin!=null && admin.getShop()!=null) {
            data.put("useCard","true");
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            Long c = 100000000+admin.getShop().getId();
            String url = "http://"+bundle.getString("weixin.url")+"/card?code="+"86"+String.valueOf(c);
            data.put("url",url);
        } else {
            data.put("useCard","false");
            data.put("url","");
        }
        return Message.bind(data,request);
    }

}