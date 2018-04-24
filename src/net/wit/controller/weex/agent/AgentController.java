package net.wit.controller.weex.agent;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.NoticeModel;
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
 * @ClassName: AgentController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexAgentController")
@RequestMapping("/weex/agent")
public class AgentController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    /**
     *   获取公告
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable,HttpServletRequest request) {
        List<Filter> filters = pageable.getFilters();
        filters.add(new Filter("type", Filter.Operator.eq, Notice.Type.live));
//
//        Page<Notice> page = noticeService.findPage(null,null,pageable);
//        PageBlock model = PageBlock.bind(page);
//        model.setData(NoticeModel.bindList(page.getContent()));
        return Message.bind(null,request);
    }


}