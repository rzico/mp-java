package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.BarrelModel;
import net.wit.controller.model.GuideModel;
import net.wit.entity.*;
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
 * @ClassName: BarrelController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberBarrelController")
@RequestMapping("/weex/member/barrel")
public class BarrelController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "shopServiceImpl")
    private ShopService shopService;

    @Resource(name = "bankcardServiceImpl")
    private BankcardService bankcardService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "barrelServiceImpl")
    private BarrelService barrelService;

    /**
     *  获取包装列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(HttpServletRequest request){

        List<Barrel> barrels = barrelService.findAll();

        return Message.bind(BarrelModel.bindList(barrels),request);

    }


}