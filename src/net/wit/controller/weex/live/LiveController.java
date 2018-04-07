package net.wit.controller.weex.live;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.LiveModel;
import net.wit.controller.model.LiveTapeModel;
import net.wit.controller.model.MemberModel;
import net.wit.entity.Live;
import net.wit.entity.LiveTape;
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
 * @ClassName: LiveController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexLiveController")
@RequestMapping("/weex/live")
public class LiveController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "liveServiceImpl")
    private LiveService liveService;

    @Resource(name = "liveTapeServiceImpl")
    private LiveTapeService liveTapeService;


    /**
     *   开通直播
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Message  create(String title,String frontcover,String location,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Live live = new Live();
        live.setMember(member);
        live.setTitle(title);
        live.setFrontcover(frontcover);
        live.setHeadpic(member.getLogo());
        live.setNickname(member.displayName());
        live.setGift(0L);
        live.setLikeCount(0L);
        live.setViewerCount(0L);
        live.setOnline("0");
        live.setStatus(Live.Status.waiting);
        live.setLocation(location);

        LiveModel model = new LiveModel();
        model.bind(live);
        return Message.success(model,"success");
   }


    /**
     *   开始直播
     */
    @RequestMapping(value = "/play", method = RequestMethod.POST)
    @ResponseBody
    public Message  play(Long id,String location,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Live live = liveService.find(id);
        if (live == null) {
            return Message.error("无效直播id");
        }


        String playUrl = "";
        String pushUrl = "";
        LiveTape liveTape = new LiveTape();
        liveTape.setLive(live);
        liveTape.setMember(member);
        liveTape.setFrontcover(live.getFrontcover());
        liveTape.setHeadpic(member.getLogo());
        liveTape.setNickname(member.displayName());
        liveTape.setGift(0L);
        liveTape.setLikeCount(0L);
        liveTape.setViewerCount(0L);
        liveTape.setLocation(live.getLocation());
        liveTape.setPlayUrl(playUrl);
        liveTape.setPushUrl(pushUrl);
        liveTape.setTitle(live.getTitle());
        liveTape.setLocation(location);
        liveTapeService.save(liveTape);

        live.setLiveTape(liveTape);
        live.setPushUrl(pushUrl);
        live.setPlayUrl(playUrl);
        live.setOnline("1");
        liveService.update(live);

        LiveTapeModel model = new LiveTapeModel();
        model.bind(liveTape);
        return Message.success(model,"success");
    }



    /**
     *   结束直播
     */
    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    @ResponseBody
    public Message  stop(Long id,String location,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Live live = liveService.find(id);
        if (live == null) {
            return Message.error("无效直播id");
        }
        LiveTape liveTape = live.getLiveTape();

        liveTape.setTitle(live.getTitle());
        liveTape.setLocation(location);
        liveTapeService.save(liveTape);

        live.setOnline("0");
        liveService.update(live);

        LiveTapeModel model = new LiveTapeModel();
        model.bind(liveTape);
        return Message.success(model,"success");
    }


    /**
     *   进入房间
     */
    @RequestMapping(value = "/into", method = RequestMethod.POST)
    @ResponseBody
    public Message  into(Long id,String location,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Live live = liveService.find(id);
        if (live == null) {
            return Message.error("无效直播id");
        }

        String playUrl = "";
        String pushUrl = "";
        LiveTape liveTape = new LiveTape();
        liveTape.setLive(live);
        liveTape.setMember(member);
        liveTape.setFrontcover(live.getFrontcover());
        liveTape.setHeadpic(member.getLogo());
        liveTape.setNickname(member.displayName());
        liveTape.setGift(0L);
        liveTape.setLikeCount(0L);
        liveTape.setViewerCount(0L);
        liveTape.setLocation(live.getLocation());
        liveTape.setPlayUrl(playUrl);
        liveTape.setPushUrl(pushUrl);
        liveTape.setTitle(live.getTitle());
        liveTape.setLocation(location);
        liveTapeService.save(liveTape);

        live.setLiveTape(liveTape);
        live.setPushUrl(pushUrl);
        live.setPlayUrl(playUrl);
        live.setOnline("1");
        liveService.update(live);

        LiveTapeModel model = new LiveTapeModel();
        model.bind(liveTape);
        return Message.success(model,"success");
    }



    /**
     *   退出房间
     */
    @RequestMapping(value = "/quit", method = RequestMethod.POST)
    @ResponseBody
    public Message  quit(Long id,String location,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Live live = liveService.find(id);
        if (live == null) {
            return Message.error("无效直播id");
        }
        LiveTape liveTape = live.getLiveTape();

        liveTape.setTitle(live.getTitle());
        liveTape.setLocation(location);
        liveTapeService.save(liveTape);

        live.setOnline("0");
        liveService.update(live);

        LiveTapeModel model = new LiveTapeModel();
        model.bind(liveTape);
        return Message.success(model,"success");
    }



}