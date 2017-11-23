package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.FriendsModel;
import net.wit.controller.model.MemberListModel;
import net.wit.controller.model.UserModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: FriendsController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberFriendsController")
@RequestMapping("/weex/member/friends")
public class FriendsController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "friendsServiceImpl")
    private FriendsService friendsService;

    /**
     * 获取好友信息
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @ResponseBody
    public Message userInfo(String userId, HttpServletRequest request){
        Long id = Member.decodeUserId(userId);
        Member member = memberService.find(id);
        if (member==null) {
            return Message.error("无效用户号");
        }
        UserModel model =new UserModel();
        model.bind(member);
        return Message.bind(model,request);
    }

    /**
     *  好友列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Friends.Type type,Friends.Status status, Long timeStamp, Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = pageable.getFilters();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        if (type!=null) {
            filters.add(new Filter("type", Filter.Operator.eq,type));
        }
        if (status==null) {
            filters.add(new Filter("status", Filter.Operator.ne, Friends.Status.black));
        } else {
            filters.add(new Filter("status", Filter.Operator.eq, status));
        }

        if (timeStamp!=null) {
            filters.add(new Filter("modifyDate", Filter.Operator.le,new Date(timeStamp)));
        }
        pageable.setOrderDirection(Order.Direction.desc);
        pageable.setOrderProperty("modifyDate");
        Page<Friends> page = friendsService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(FriendsModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  搜索好友
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Message search(String keyword, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        String [] ms = keyword.split(",");
        List<MemberListModel> mds = new ArrayList<MemberListModel>();
        for (String m:ms) {
            Member friend = memberService.findByMobile(m);
            if (friend == null) {
                friend = memberService.findByUsername(m);
            }
            if (friend!=null) {
                MemberListModel md = new MemberListModel();
                md.bind(friend);
                mds.add(md);
            }
        }
        return Message.bind(mds,request);

    }

    /**
     *  添加好友
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Message add(Long friendId, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Member friend = memberService.find(friendId);
        if (friend==null) {
            return Message.error("无效好友");
        }

        Friends fds = friendsService.find(friend, member);
        if (fds==null) {
            fds = new Friends();
            fds.setFriend(member);
            fds.setMember(friend);
            fds.setStatus(Friends.Status.ask);
            fds.setType(Friends.Type.friend);
            friendsService.save(fds);
        } else {
            fds.setFriend(member);
            fds.setMember(friend);
            fds.setStatus(Friends.Status.ask);
            fds.setType(Friends.Type.friend);
            friendsService.save(fds);
        }
        messageService.addFriendPushTo(member,friend);
        return Message.success("添加成功");

    }

    /**
     *  同意好友
     */
    @RequestMapping(value = "/adopt", method = RequestMethod.POST)
    @ResponseBody
    public Message adopt(Long friendId, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Member friend = memberService.find(friendId);
        if (friend==null) {
            return Message.error("无效好友");
        }
        Friends fds = friendsService.find(member,friend);
        if (fds==null) {
            return Message.error("无效申请");
        }
        fds.setStatus(Friends.Status.adopt);
        friendsService.save(fds);
        Friends fds1 = friendsService.find(friend, member);
        if (fds1==null) {
            fds1 = new Friends();
            fds1.setFriend(member);
            fds1.setMember(friend);
            fds1.setStatus(Friends.Status.adopt);
            fds.setType(Friends.Type.friend);
            friendsService.save(fds1);
        } else {
            fds1.setStatus(Friends.Status.adopt);
            friendsService.save(fds1);
        }
        messageService.adoptFriendPushTo(member,friend);
        return Message.success("同意好友");
    }


    /**
     *  好友拉黑
     */
    @RequestMapping(value = "/black", method = RequestMethod.POST)
    @ResponseBody
    public Message black(Long friendId, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Member friend = memberService.find(friendId);
        if (friend==null) {
            return Message.error("无效好友");
        }
        Friends fds = friendsService.find(member,friend);
        if (fds!=null) {
            fds.setStatus(Friends.Status.black);
            friendsService.save(fds);
        }
        return Message.success("拉黑成功");
    }

}