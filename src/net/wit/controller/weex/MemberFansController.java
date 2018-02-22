package net.wit.controller.weex;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.MemberFollowModel;
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
 * @ClassName: WeexFansController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexFansController")
@RequestMapping("/weex/fans")
public class MemberFansController extends BaseController {

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
     *   关注我的
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long id,Pageable pageable, HttpServletRequest request){
        Member member = memberService.find(id);
        if (member==null) {
            return Message.error("无效 id");
        }
        Member self = memberService.getCurrent();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("follow", Filter.Operator.eq,member));
        pageable.setFilters(filters);
        Page<MemberFollow> page = memberFollowService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        List<MemberFollowModel> follows = MemberFollowModel.bindFans(page.getContent());
        if (self!=null) {
            for (MemberFollowModel followModel : follows) {
                Member follow = memberService.find(followModel.getId());
                if (self.equals(follow)) {
                    followModel.setFollowed(true);
                } else {
                    MemberFollow memberFollow = memberFollowService.find(self, follow);
                    followModel.setFollowed(memberFollow!=null);
                }
                MemberFollow memberFollow = memberFollowService.find(follow, self);
                followModel.setFollow(memberFollow!=null);
            }
        }
        model.setData(follows);
        return Message.bind(model,request);
   }

}