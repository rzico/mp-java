package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.AdminModel;
import net.wit.controller.model.RoleModel;
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
 * @ClassName: RoleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberRoleController")
@RequestMapping("/weex/member/role")
public class RoleController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;


    /**
     *  角色列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("isSystem", Filter.Operator.eq,true));
        List<Role> data = roleService.findList(null,null,filters,null);
        return Message.bind(RoleModel.bindList(data),request);
    }

}