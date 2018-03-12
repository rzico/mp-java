package net.wit.controller.applet.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.entity.Member;
import net.wit.entity.MemberFollow;
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
 * @ClassName: WeexMemberFavoriteController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberFollowController")
@RequestMapping("/applet/member/follow")
public class MemberFollowController extends BaseController {

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

    @Resource(name = "memberFollowServiceImpl")
    private MemberFollowService memberFollowService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    /**
     *   关注
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Message add(Long authorId,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Member author = memberService.find(authorId);
        if (author==null) {
            return Message.error("作者id无效");
        }
        MemberFollow follow = new MemberFollow();
        follow.setIp(request.getRemoteAddr());
        follow.setMember(member);
        follow.setFollow(author);
        memberFollowService.save(follow);
        messageService.followPushTo(follow);
        return Message.success("关注成功");
   }


    /**
     *   取消
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long  authorId,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Member author = memberService.find(authorId);
        if (author==null) {
            return Message.error("作者id无效");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        filters.add(new Filter("follow", Filter.Operator.eq,author));
        List<MemberFollow> data = memberFollowService.findList(null,null,filters,null);
        for (MemberFollow follow:data) {
            memberFollowService.delete(follow);
        }
        return Message.success("取消成功");
    }

}