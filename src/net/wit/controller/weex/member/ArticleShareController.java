package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: ArticleShareController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberShareController")
@RequestMapping("/weex/member/share")
public class ArticleShareController extends BaseController {

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

    @Resource(name = "articleShareServiceImpl")
    private ArticleShareService articleShareService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "weixinUpServiceImpl")
    private WeixinUpService weixinUpService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     *   分享
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Message add(Long  articleId, ArticleShare.ShareType shareType, Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Article article = articleService.find(articleId);
        if (article==null) {
            return Message.error("无效文章 id");
        }
        ArticleShare share = new ArticleShare();
        share.setIp(request.getRemoteAddr());
        share.setMember(member);
        share.setArticle(article);
        share.setIsShow(true);
        share.setShareType(shareType);
        share.setAuthor(article.getMember());
        articleShareService.save(share);
        messageService.sharePushTo(share);
        return Message.success("分享成功");
   }


    /**
     *   分享公众号
     */
    @RequestMapping(value = "/platform",method = RequestMethod.GET)
    @ResponseBody
    public Message platform(Long[]  articleId, ArticleShare.ShareType shareType, Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        //找到该所绑定专栏的管理员
        Admin admin=adminService.findByMember(member);

        if(admin==null){
            return Message.error("没有点亮专栏");
        }

        if (admin.getEnterprise()==null) {
            return Message.error("店铺已打洋,请先启APP");
        }

        Enterprise enterprise = admin.getEnterprise();

        if(enterprise.getMember()==null){
            return Message.error("该专栏商家尚未入驻");
        }
        member=enterprise.getMember();

        Topic topic=member.getTopic();
        if(topic==null){
            return Message.error("该专栏无效");
        }
        //比较该专栏过期时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date overtime=topic.getExpire();
        Date nowtime=new Date();
        if(overtime.before(nowtime)){
            return Message.error("该专栏已到期");
        }

        //专栏公众号设置
        if(topic.getConfig()==null||topic.getConfig().getWxAppId().equals("")||topic.getConfig().getWxAppSerect().equals("")){
            return Message.error("您未绑定公众号");
        }

        List<Article> articles=articleService.findList(articleId);
        if(articles==null){
            return Message.error("文章ID无效");
        }

        if(weixinUpService.ArticleUpLoad(articleId,topic.getConfig().getWxAppId(),topic.getConfig().getWxAppSerect()).equals("success")){
            return Message.error("分享成功");
        }
        else{
            return Message.error("分享失败");
        }
    }

}