package net.wit.controller.weex.live;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.GiftModel;
import net.wit.controller.model.NoticeModel;
import net.wit.entity.Live;
import net.wit.entity.LiveGift;
import net.wit.entity.Member;
import net.wit.entity.Notice;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @ClassName: NoticeController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexLiveNoticeController")
@RequestMapping("/weex/live/notice")
public class NoticeController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "noticeServiceImpl")
    private NoticeService noticeService;

    @Resource(name = "liveServiceImpl")
    private LiveService liveService;

    /**
     *   获取公告
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable,HttpServletRequest request) {
        List<Filter> filters = pageable.getFilters();
        filters.add(new Filter("type", Filter.Operator.eq, Notice.Type.live));

        Page<Notice> page = noticeService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(NoticeModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }


}