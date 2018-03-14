package net.wit.controller.website;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleListModel;
import net.wit.controller.model.ArticleViewModel;
import net.wit.controller.model.GoodsListModel;
import net.wit.controller.model.NoticeModel;
import net.wit.entity.*;
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
 * @ClassName: NoticeController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("websiteNoticeController")
@RequestMapping("website/notice")
public class NoticeController extends BaseController {

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

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;

    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;

    @Resource(name = "memberFollowServiceImpl")
    private MemberFollowService memberFollowService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     *  文章列表,带分页
     *  会员 id
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long authorId,Pageable pageable, HttpServletRequest request){

         List<NoticeModel> data = new ArrayList<>();
         Member member = memberService.find(authorId);
         Admin admin = adminService.findByMember(member);
         if (admin!=null && admin.getEnterprise()!=null) {
             NoticeModel m = new NoticeModel();
             m.setType("tel");
             m.setTitle("欢迎咨询 "+admin.getEnterprise().getPhone());
             m.setUrl("tel://"+admin.getEnterprise().getPhone());
             data.add(m);
         } else {
             NoticeModel m = new NoticeModel();
             m.setType("none");
             m.setTitle("欢迎咨询光临！");
             data.add(m);
         }
         return Message.bind(data,request);
    }
}