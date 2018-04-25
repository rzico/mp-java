package net.wit.controller.applet;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.MemberModel;
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
 * @ClassName: weexCircleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletCircleController")
@RequestMapping("/applet/circle")
public class CircleController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "memberFollowServiceImpl")
    private  MemberFollowService memberFollowService;

    /**
     *   我关注的人
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long id,Pageable pageable, HttpServletRequest request){
        Member member = memberService.find(id);
        if (member==null) {
            return Message.error("无效id");
        }
        Member self = memberService.getCurrent();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        Page<MemberFollow> page = memberFollowService.findPage(null,null,pageable);
        List<MemberModel> data = new ArrayList<>();
        for (MemberFollow follow : page.getContent()) {
            MemberModel m = new MemberModel();
            m.bind(follow.getFollow());
            data.add(m);
        }
        return Message.bind(data,request);
   }

}