package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.MessageModel;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @ClassName: MessageController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberMessageController")
@RequestMapping("/weex/member/message")
public class MessageController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    /**
     *  我的消息列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(String userId,Boolean readed,Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        Member sender = memberService.findByUsername(userId);
        List<Filter> filters = pageable.getFilters();
        filters.add(new Filter("receiver", Filter.Operator.eq,member));
        if (sender!=null) {
            filters.add(new Filter("member", Filter.Operator.eq,sender));
        }
        if (readed!=null) {
            filters.add(new Filter("readed", Filter.Operator.eq,readed));
        }
        Page<net.wit.entity.Message> page = messageService.findPage(null,null,pageable);
        for (net.wit.entity.Message message:page.getContent()) {
            if (!message.getReaded()) {
                message.setReaded(true);
                messageService.update(message);
            }
        }
        PageBlock model = PageBlock.bind(page);
        model.setData(MessageModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     *  会话列表
     */
    @RequestMapping(value = "/dialogue", method = RequestMethod.GET)
    @ResponseBody
    public Message dialogue(HttpServletRequest request){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("receiver", Filter.Operator.eq,member));
        filters.add(new Filter("readed", Filter.Operator.eq,false));
        filters.add(new Filter("deleted", Filter.Operator.eq,false));
        List<net.wit.entity.Message> ms = messageService.findList(null,null,filters,null);
        return Message.bind(MessageModel.bindDialogue(ms),request);
    }

}