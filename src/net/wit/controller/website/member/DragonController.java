package net.wit.controller.website.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.DragonModel;
import net.wit.entity.Article;
import net.wit.entity.Dragon;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName: DragonController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteMemberDragonController")
@RequestMapping("/website/member/dragon")
public class DragonController extends BaseController {

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

    @Resource(name = "dragonServiceImpl")
    private DragonService dragonService;

     /**
     *   创建接龙
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public Message submit(Long articleId,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        Member member = memberService.getCurrent();
        Dragon dg = dragonService.find(article,article.getMember());
        if (dg==null) {
            return Message.error("接龙已经结束了");
        }
        Dragon dragon = dragonService.find(article,member);
        if (dragon==null) {
            dragon = new Dragon();
        }
        dragon.setArticle(article);
        dragon.setMember(member);
        dragon.setOwner(article.getMember());
        dragon.setStatus(Dragon.Status.normal);
        dragon.setTitle( dg.getTitle());
        dragon.setType( dg.getType());
        dragon.setDeleted(false);
        dragonService.save(dragon);
        DragonModel model = new DragonModel();
        model.bind(dragon);
        return Message.success(model,"发布成功");

    }

}