package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.ArticleModel;
import net.wit.controller.weex.model.ArticleOptionModel;
import net.wit.controller.weex.model.MemberModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleOptions;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberArticleController")
@RequestMapping("/weex/member/article")
public class ArticleController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    /**
     * 获取文章编辑信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        ArticleModel model =new ArticleModel();
        model.bind(article);
        return Message.success(model,"获取成功");
   }

    /**
     * 获取文章发布属性
     */
    @RequestMapping(value = "/option", method = RequestMethod.GET)
    @ResponseBody
    public Message option(Long id,HttpServletRequest request){
        Article article = articleService.find(id);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        ArticleOptionModel model =new ArticleOptionModel();
        model.bind(article);
        return Message.success(model,"获取成功");
    }

}