package net.wit.controller.weex.live;

import net.wit.*;
import net.wit.Message;
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
import java.util.Date;
import java.util.List;


/**
 * @ClassName: GiftController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexLiveGiftController")
@RequestMapping("/weex/live/gift")
public class GiftController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "liveGiftServiceImpl")
    private LiveGiftService liveGiftService;

    @Resource(name = "liveGiftDataServiceImpl")
    private LiveGiftDataService liveGiftDataService;

    @Resource(name = "liveServiceImpl")
    private LiveService liveService;

    /**
     *   获取礼物
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message  list(Pageable pageable,HttpServletRequest request) {
        Page<LiveGift> page = liveGiftService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(GiftModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *   获取得到的礼物
     */
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    @ResponseBody
    public Message data(Long liveId,Pageable pageable,HttpServletRequest request) {
        List<Filter> filters = pageable.getFilters();
        Live live = liveService.find(liveId);
        filters.add(new Filter("live", Filter.Operator.eq,live));
        Page<LiveGiftData> page = liveGiftDataService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(GiftDataModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *   送礼物
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(Long id,Long liveId,Pageable pageable,HttpServletRequest request) {

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        LiveGift gift = liveGiftService.find(id);
        if (gift==null) {
            return Message.error("无效 id");
        }

        Live live = liveService.find(liveId);
        if (live==null) {
            return Message.error("房间号 id");
        }

        try {
            liveGiftService.add(gift,member,live);
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
        return Message.success(gift.getPrice(),"送成功");

    }

    /**
     *   点赞
     */
    @RequestMapping(value = "/laud", method = RequestMethod.POST)
    @ResponseBody
    public Message laud(Long liveId,Pageable pageable,HttpServletRequest request) {

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Live live = liveService.find(liveId);
        if (live==null) {
            return Message.error("房间号 id");
        }

        try {
            liveGiftService.laud(member,live);
        } catch (Exception e) {
            return Message.success(e.getMessage());
        }
        return Message.success("送成功");

    }


    /**
     *   发字幕
     */
    @RequestMapping(value = "/barrage")
    @ResponseBody
    public Message barrage(Long liveId,Pageable pageable,HttpServletRequest request) {

        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        Live live = liveService.find(liveId);
        if (live==null) {
            return Message.error("房间号 id");
        }

        try {
            liveGiftService.barrage(member,live);
        } catch (Exception e) {
            return Message.error(e.getMessage());
        }
        return Message.success(new BigDecimal("0.1"),"送成功");

    }

}