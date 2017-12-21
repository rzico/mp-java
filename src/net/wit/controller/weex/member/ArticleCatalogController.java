package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCatalogModel;
import net.wit.entity.ArticleCatalog;
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
 * @ClassName: ArticleCatalogController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberArticleCatalogController")
@RequestMapping("/weex/member/article_catalog")
public class ArticleCatalogController extends BaseController {

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

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;

    /**
     *  文集列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long tagIds,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        List<ArticleCatalog> categories = articleCatalogService.findList(null,null,filters,null);

        return Message.bind(ArticleCatalogModel.bindList(categories),request);
    }


    /**
     *  文集排序
     */
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    @ResponseBody
    public Message sort(Long[] ids,HttpServletRequest request){
        int i=0;
        for (Long id:ids) {
            ArticleCatalog catalog = articleCatalogService.find(id);
            i=i+1;
            catalog.setOrders(i);
            articleCatalogService.update(catalog);
        }
        return Message.success("success");
    }

    /**
     *  添加文集
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Message add(String name,Integer orders,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ArticleCatalog catalog = new ArticleCatalog();
        catalog.setOrders(orders);
        catalog.setName(name);
        catalog.setStatus(ArticleCatalog.Status.enabled);
        catalog.setMember(member);
        articleCatalogService.save(catalog);

        ArticleCatalogModel model = new ArticleCatalogModel();
        model.bind(catalog);
        return Message.success(model,"添加成功");
    }

    /**
     *  修改文集
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Long id,String name,Integer orders,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        ArticleCatalog catalog = articleCatalogService.find(id);
        if (catalog==null) {
            return Message.error("无效文集id");
        }
        catalog.setOrders(orders);
        catalog.setName(name);
        catalog.setStatus(ArticleCatalog.Status.enabled);
        catalog.setMember(member);
        articleCatalogService.save(catalog);
        ArticleCatalogModel model = new ArticleCatalogModel();
        model.bind(catalog);
        return Message.success(model,"添加成功");
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
        ArticleCatalog catalog = articleCatalogService.find(id);
        if (catalog==null) {
            return Message.error("无效文集id");
        }
        if (catalog.getArticles().size()>0) {
            return Message.error("有文章不能删");
        }

        articleCatalogService.delete(id);
        return Message.success("删除成功");
    }
}