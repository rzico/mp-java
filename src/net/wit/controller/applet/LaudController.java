package net.wit.controller.applet;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleLaudModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleLaud;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: WeexLaudController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletLaudController")
@RequestMapping("/applet/laud")
public class LaudController extends BaseController {

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

    @Resource(name = "articleLaudServiceImpl")
    private ArticleLaudService articleLaudService;

    /**
     *  评论列表,带分页
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long articleId, Pageable pageable, HttpServletRequest request){
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("article", Filter.Operator.eq,article));
        pageable.setFilters(filters);
        Page<ArticleLaud> page = articleLaudService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(ArticleLaudModel.bindList(page.getContent()));
        return Message.bind(model,request);
   }


    /**
     *  点赞情况
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long articleId, Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章编号");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("article", Filter.Operator.eq,article));
        long count= articleLaudService.count(new Filter("article", Filter.Operator.eq,article));
        boolean laud = false;
        if (member!=null) {
            java.util.List<Filter> laudfilters = new ArrayList<Filter>();
            laudfilters.add(new Filter("member", Filter.Operator.eq, member));
            laudfilters.add(new Filter("article", Filter.Operator.eq, article));
            List<ArticleLaud> lauds = articleLaudService.findList(null, null, laudfilters, null);
            laud = (lauds.size() > 0);
        }

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("laud",laud);
        data.put("count",count);
        return Message.bind(data,request);
    }

}