package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.entity.Feedback;
import net.wit.entity.Member;
import net.wit.service.FeedbackService;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.annotation.Resources;

/**
 * Created by Eric on 2018/3/29.
 */
@Controller("weexMemberFeedbackController")
@RequestMapping("/weex/member/feedback")
public class FeedbackController {

    @Resource(name = "feedbackServiceImpl")
    private FeedbackService feedbackService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @RequestMapping(value = "/add")
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
            return Message.success("反馈成功,请耐心等待客服解答");
        } catch (Exception e) {
            return Message.error("反馈失败,服务器资源跑路了");
        }
    }
}
