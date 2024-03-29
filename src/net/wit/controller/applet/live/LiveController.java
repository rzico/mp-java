package net.wit.controller.applet.live;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.LiveListModel;
import net.wit.controller.model.LiveModel;
import net.wit.controller.model.LiveTapeModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * @ClassName: LiveController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletLiveController")
@RequestMapping("/applet/live")
public class LiveController extends BaseController {
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

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

    @Resource(name = "liveDataServiceImpl")
    private LiveDataService liveDataService;

    @Resource(name = "memberFollowServiceImpl")
    private MemberFollowService memberFollowService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

    @Resource(name = "topicServiceImpl")
    private TopicService topicService;

    @Resource(name = "topicCardServiceImpl")
    private TopicCardService topicCardService;

    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    /*
			     * KEY+ stream_id + txTime
			     */
    private static String getSafeUrl(String key, String streamId, long txTime) {
        String input = new StringBuilder().
                append(key).
                append(streamId).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
                new StringBuilder().
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(txTime).toUpperCase()).
                        toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

    /**
     *   检查是否开通直播
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @ResponseBody
    public Message  check(Long memberId,Pageable pageable,HttpServletRequest request) {
        Member member = null;
        if (memberId!=null) {
            member = memberService.find(memberId);
        }
        if (member==null) {
            memberService.getCurrent();
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        List<Live> lives = liveService.findList(null,null,filters,null);
        Live live = null;
        if (lives.size()>0) {
            live = lives.get(0);
            if (live.getStatus().equals(Live.Status.success)) {
                return Message.success(true,"success");
            }
        }
        return Message.success(false,"success");
    }

    /**
     *   用户信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message  view(Long id,Pageable pageable,HttpServletRequest request) {
        Live live = liveService.find(id);
        LiveModel model = new LiveModel();
        model.bind(live);
        return Message.bind(model,request);
    }

    /**
     *   获取直播间
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message  list(Pageable pageable,HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("online", Filter.Operator.eq,"1"));

        pageable.setFilters(filters);
        pageable.setOrderDirection(Order.Direction.desc);
        Page<Live> page = liveService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(LiveModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *   进入房间
     */
    @RequestMapping(value = "/into", method = RequestMethod.POST)
    @ResponseBody
    public Message  into(Long id,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Live live = liveService.find(id);
        if (live == null) {
            return Message.error("无效直播id");
        }

        Date tx = new Date();
        Long txTime = tx.getTime()+300L;

        String playUrl = "rtmp://22303.liveplay.myqcloud.com/live/22303_"+String.valueOf(live.getId()+10201);
//        String playUrl = "rtmp://22303.liveplay.myqcloud.com/live/22303_"+String.valueOf(live.getId()+10201)+"_550"+getSafeUrl("429c000ffc0009387260daa9504003ba", "22303_"+String.valueOf(live.getId()+10201)+"_550",txTime);
//        String hlsPlayUrl = "rtmp://22303.liveplay.myqcloud.com/live/22303_"+String.valueOf(live.getId()+10201)+"_550.m3u8";

        LiveData liveData = new LiveData();

        liveData.setLive(live);
        liveData.setLiveTape(live.getLiveTape());
        liveData.setMember(member);
        liveData.setFrontcover(live.getFrontcover());
        liveData.setHeadpic(member.getLogo());
        liveData.setNickname(member.displayName());

        liveData.setLocation(live.getLocation());
        liveData.setPlayUrl(playUrl);
        liveDataService.save(liveData);

        live.setViewerCount(live.getViewerCount()+1);
        live.setPlayUrl(playUrl);
        liveService.update(live);

        LiveTape liveTape = live.getLiveTape();
        liveTape.setPlayUrl(playUrl);
        liveTape.setViewerCount(liveTape.getViewerCount()+1);
        liveTapeService.update(liveTape);

        MemberFollow memberFollow=memberFollowService.find(member,live.getMember());

        LiveTapeModel model=new LiveTapeModel();
        model.bind(liveTape);
        if (memberFollow==null){
            model.setFollow(false);
        }else {
            model.setFollow(true);
        }
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

        live.setViewerCount(live.getViewerCount()-1);
        liveService.update(live);

        liveTape.setViewerCount(liveTape.getViewerCount()-1);
        liveTapeService.update(liveTape);

        return Message.success("success");
    }

    /**
     *  热点查询列表
     *  会员 id
     */
    @RequestMapping(value = "/slide", method = RequestMethod.GET)
    @ResponseBody
    public Message slide(Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("online", Filter.Operator.eq,"1"));

        pageable.setFilters(filters);
        pageable.setOrderDirection(Order.Direction.desc);
        Page<Live> page = liveService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(LiveListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}