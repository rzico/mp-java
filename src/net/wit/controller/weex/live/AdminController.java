package net.wit.controller.weex.live;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.GiftModel;
import net.wit.entity.Live;
import net.wit.entity.LiveAdmin;
import net.wit.entity.LiveGift;
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
 * @ClassName: AdminController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexLiveAdminController")
@RequestMapping("/weex/live/admin")
public class AdminController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "liveAdminServiceImpl")
    private LiveAdminService liveAdminService;

    @Resource(name = "liveServiceImpl")
    private LiveService liveService;

    /**
     *   检查是否管理员
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message  view(Long liveId,Pageable pageable,HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Live live = liveService.find(liveId);
        Long rc = liveAdminService.count(new Filter("live", Filter.Operator.eq,live),new Filter("member", Filter.Operator.eq,member));
        return Message.success(rc>0,"success");
    }

    /**
     *   设成管理员
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Message add(Long id,Long liveId,Pageable pageable,HttpServletRequest request) {

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Live live = liveService.find(liveId);
        if (live==null) {
            return Message.error("房间号 id");
        }

        if (!live.getMember().equals(member)) {
            return Message.error("主播才能设置");
        }

        Member adminMember = memberService.find(id);

        Long rc = liveAdminService.count(new Filter("live", Filter.Operator.eq,live),new Filter("member", Filter.Operator.eq,adminMember));

        if (rc==0) {
            LiveAdmin admin = new LiveAdmin();
            admin.setLive(live);
            admin.setMember(member);
            liveAdminService.save(admin);
        }

        return Message.success("设置成功");

    }

    /**
     *   设成管理员
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Message delete(Long id,Long liveId,Pageable pageable,HttpServletRequest request) {

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Live live = liveService.find(liveId);
        if (live==null) {
            return Message.error("房间号 id");
        }

        if (!live.getMember().equals(member)) {
            return Message.error("主播才能设置");
        }
        Member adminMember = memberService.find(id);

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("live", Filter.Operator.eq,live));
        filters.add(new Filter("member", Filter.Operator.eq,adminMember));
        List<LiveAdmin> admins = liveAdminService.findList(null,null,filters,null);

        for (LiveAdmin admin:admins) {
            liveAdminService.delete(admin);
        }
        return Message.success("设置成功");

    }

}