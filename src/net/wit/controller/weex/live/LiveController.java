package net.wit.controller.weex.live;

import net.wit.*;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @ClassName: LiveController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexLiveController")
@RequestMapping("/weex/live")
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
    public Message check(Long memberId,Pageable pageable,HttpServletRequest request) {
        Member member = null;
        if (memberId!=null) {
            member = memberService.find(memberId);
        }
        if (member==null) {
            member = memberService.getCurrent();
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        List<Live> lives = liveService.findList(null,null,filters,null);
        Live live = null;
        if (lives.size()>0) {
            live = lives.get(0);
            LiveModel model = new LiveModel();
            model.bind(live);
            return Message.success(model,"已开通");
        }
        return Message.success("未开通");
    }

    /**
     *   直播间信息
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
    public Message list(Pageable pageable,HttpServletRequest request){
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
     *   开通直播
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public Message create(String title,String frontcover,String location,HttpServletRequest request){

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        List<Live> lives = liveService.findList(null,null,filters,null);

        Live live = null;
        if (lives.size()>0) {
            live = lives.get(0);
        } else {
            live = new Live();
            live.setMember(member);
            live.setGift(0L);
            live.setLikeCount(0L);
            live.setViewerCount(0L);
            live.setOnline("0");
            live.setStatus(Live.Status.waiting);
        }
        live.setTitle(title);
        live.setFrontcover(frontcover);
        live.setHeadpic(member.getLogo());
        live.setNickname(member.displayName());
        live.setLocation(location);
        liveService.save(live);

        if (member.getNickName()==null) {
            member.setNickName(title);
        }
        if (member.getLogo()==null) {
            member.setLogo(frontcover);
        }
        memberService.update(member);

        Topic topic =  member.getTopic();
        if (topic==null) {
            topic = new Topic();
            topic.setName(member.getNickName());
            topic.setBrokerage(new BigDecimal("0.6"));
            topic.setPaybill(new BigDecimal("0.4"));
            topic.setStatus(Topic.Status.waiting);
            topic.setHits(0L);
            topic.setMember(member);
            topic.setFee(new BigDecimal("1999"));
            topic.setLogo(member.getLogo());
            topic.setType(Topic.Type.personal);
            topic.setRanking(0L);
            TopicConfig config = topic.getConfig();
            if (config==null) {
                config = new TopicConfig();
                config.setUseCard(false);
                config.setUseCashier(false);
                config.setUseCoupon(false);
                config.setPromoterType(TopicConfig.PromoterType.any);
                config.setPattern(TopicConfig.Pattern.pattern1);
                config.setAmount(BigDecimal.ZERO);
            }
            topic.setConfig(config);
            Calendar calendar   =   new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(calendar.MONTH, 1);
            topic.setExpire(calendar.getTime());
            topic.setTemplate(templateService.findDefault(Template.Type.topic));
            topicService.create(topic);
            enterpriseService.create(topic);
        } else {
            enterpriseService.create(topic);
        }

        LiveModel model = new LiveModel();
        model.bind(live);
        return Message.success(model,"success");

   }


    /**
     *   开始直播
     */
    @RequestMapping(value = "/play")
    @ResponseBody
    public Message  play(Long id,String title,String frontcover,String location,Boolean record,HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Live live = liveService.find(id);
        if (live == null) {
            return Message.error("无效直播id");
        }

//        String string = "2018-04-30 23:59:59";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            sdf.parse(string);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
        Date tx = new Date();
        Long txTime = tx.getTime()/1000+86400;
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        String pushUrl = "rtmp://"+bundle.getString("live.bizid")+".livepush.myqcloud.com/live/"+bundle.getString("live.bizid")+"_"+String.valueOf(live.getId()+10201)+"?bizid="+bundle.getString("live.bizid")+"&"+getSafeUrl(bundle.getString("live.key"), bundle.getString("live.bizid")+"_"+String.valueOf(live.getId()+10201),txTime);

        if (record==null){
            record=false;
        }

        if (record) {
            pushUrl = pushUrl + "&record=mp4&record_interval=5400";
        }

        if (title!=null) {
            live.setTitle(title);
        }
        if (frontcover!=null) {
            live.setFrontcover(frontcover);
        }

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
        liveTape.setPushUrl(pushUrl);
        liveTape.setTitle(live.getTitle());
        liveTape.setLocation(location);
        liveTapeService.save(liveTape);

        live.setLiveTape(liveTape);
        live.setPushUrl(pushUrl);
        live.setOnline("1");
        liveService.update(live);

        LiveTapeModel model = new LiveTapeModel();
        model.bind(liveTape);

        model.setFans(new Long(member.getFans().size()));
        model.setFollow(new Long(member.getFollows().size()));
        model.setVip(member.getVip());
        return Message.success(model,"success");

    }



    /**
     *   结束直播
     */
    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    @ResponseBody
    public Message  stop(Long id,HttpServletRequest request){
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
        liveTape.setEndTime(new Date());
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

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        String playUrl = "rtmp://"+bundle.getString("live.bizid")+".liveplay.myqcloud.com/live/"+bundle.getString("live.bizid")+"_"+String.valueOf(live.getId()+10201);
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

        model.setFans(new Long(live.getMember().getFans().size()));
        model.setFollow(new Long(live.getMember().getFollows().size()));
        model.setVip(live.getMember().getVip());

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