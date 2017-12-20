package net.wit.controller.applet;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleListModel;
import net.wit.entity.ArticleFavorite;
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
 * @ClassName: WeexFavoriteController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletFavoriteController")
@RequestMapping("/applet/favorite")
public class ArticleFavoriteController extends BaseController {

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

    @Resource(name = "articleFavoriteServiceImpl")
    private ArticleFavoriteService articleFavoriteService;

    /**
     *   我收藏的文章
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long id,Pageable pageable, HttpServletRequest request){
        Member member = memberService.find(id);
        if (member==null) {
            return Message.error("无效 id");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        Page<ArticleFavorite> page = articleFavoriteService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleListModel.bindFavorite(page.getContent()));
        return Message.bind(model,request);
   }

}