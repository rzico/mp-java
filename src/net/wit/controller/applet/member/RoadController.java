package net.wit.controller.applet.member;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CouponCodeModel;
import net.wit.controller.model.RoadModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: RoadController
 * @author 降魔战队  优惠券
 * @date 2017-9-14 19:42:9
 */
 
@Controller("appletMemberRoadController")
@RequestMapping("/applet/member/road")
public class RoadController extends BaseController {

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

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "roadServiceImpl")
    private RoadService roadService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

     /**
     * 领取优惠券
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Message list(Long areaId, double lat, double lng, Pageable pageable, HttpServletRequest request){
        Area area = areaService.find(areaId);

        Page<Road> page = roadService.findPage(null,null,pageable);

        return Message.bind(RoadModel.bindList(page.getContent()),request);
    }

}