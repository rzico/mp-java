package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.entity.Feedback;
import net.wit.entity.Member;
import net.wit.service.FeedbackService;
import net.wit.service.MemberService;
import net.wit.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.annotation.Resources;

/**
 * Created by Eric on 2018/3/29.
 */
@Controller("weexMemberFeedbackController")
@RequestMapping("/weex/member/feedback")
public class FeedbackController extends BaseController{

    @Resource(name = "feedbackServiceImpl")
    private FeedbackService feedbackService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Message add(String content,String[] imgs) {
        Member member = memberService.getCurrent();
        Feedback entity = new Feedback();
        entity.setMember(member);
        entity.setSolve(false);
        entity.setContent(content);
        int i = imgs.length;
        if (--i > -1) {
            entity.setProblemPictrue1(imgs[i]);
        }
        if (--i > -1) {
            entity.setProblemPictrue2(imgs[i]);
        }
        if (--i > -1) {
            entity.setProblemPictrue3(imgs[i]);
        }
        if (--i > -1) {
            entity.setProblemPictrue4(imgs[i]);
        }
        if (--i > -1) {
            entity.setProblemPictrue5(imgs[i]);
        }
        try {
            feedbackService.save(entity);
        } catch (Exception e) {
            return Message.error("反馈失败,服务器资源跑路了");
        }
        net.wit.entity.Message message=new net.wit.entity.Message();
        message.setContent("已收到您的反馈，请耐心等待结果！");

        message.setType(net.wit.entity.Message.Type.message);

        message.setDeleted(false);

        message.setReceiver(member);

        message.setReaded(false);

        message.setTitle("问题反馈回复");

        message.setThumbnial("http://cdn.rzico.com/weex/resources/images/gm_10202.png");

        message.setMember(null);

        message.setSender(null);

        if (!isValid(message)) {
            return Message.error("问题已收到,消息推送失败");
        }
        try {
            messageService.pushTo(message);
            return Message.success("消息推送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return net.wit.Message.error("admin.save.error");
        }
    }
}
