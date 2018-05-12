package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleReviewModel;
import net.wit.controller.model.DragonModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReview;
import net.wit.entity.Dragon;
import net.wit.entity.Member;
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
 * @ClassName: DragonController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberDragonController")
@RequestMapping("/weex/member/dragon")
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
    public Message submit(Long articleId,String title,Dragon.Type type,HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        Member member = memberService.getCurrent();
        Long rc = dragonService.count(new Filter("article", Filter.Operator.eq, article),new Filter("member", Filter.Operator.eq, member),new Filter("status", Filter.Operator.eq, Dragon.Status.normal));
        if (rc>0) {
            return Message.error("你还有没完成的接龙");
        }
        Dragon dragon = new Dragon();
        dragon.setArticle(article);
        dragon.setMember(member);
        dragon.setOwner(article.getMember());
        dragon.setStatus(Dragon.Status.normal);
        dragon.setTitle(title);
        dragon.setType(type);
        dragonService.save(dragon);
//        messageService.reviewPushTo(review);
        DragonModel model = new DragonModel();
        model.bind(dragon);
        return Message.success(model,"发布成功");

    }

    /**
     *  删除评论
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Message delete(Long id,HttpServletRequest request){
        Dragon dragon = dragonService.find(id);
        if (dragon==null) {
            return Message.error("无效评论编号");
        }
        dragonService.delete(id);
        return Message.success("删除成功");

    }

    /**
     *  关闭接龙
     */
    @RequestMapping(value = "/ended")
    @ResponseBody
    public Message ended(Long id,HttpServletRequest request){
        Dragon dragon = dragonService.find(id);
        if (dragon==null) {
            return Message.error("无效评论编号");
        }
        dragon.setStatus(Dragon.Status.closed);
        dragonService.update(dragon);
        return Message.success("关闭接龙");
    }

    /**
     *  我的接龙
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        Page<Dragon> page = dragonService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(DragonModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}