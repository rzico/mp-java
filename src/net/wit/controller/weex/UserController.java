package net.wit.controller.weex;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleReviewModel;
import net.wit.controller.model.MemberViewModel;
import net.wit.controller.model.UserModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleReview;
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
 * @ClassName: UserController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexUserController")
@RequestMapping("/weex/user")
public class UserController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "memberFollowServiceImpl")
    private MemberFollowService memberFollowService;

    /**
     *  用户信息
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long id, Pageable pageable, HttpServletRequest request){
        Member member = memberService.find(id);
        Member that=memberService.getCurrent();
        if (member==null) {
            return Message.error("无效用户 id");
        }
        UserModel model = new UserModel();
        model.bind(member);
        MemberFollow memberFollow = memberFollowService.find(that,member);
        if(memberFollow==null){
            model.setIsfollow(false);
        }else {
            model.setIsfollow(true);
        }
        return Message.bind(model,request);
   }

}