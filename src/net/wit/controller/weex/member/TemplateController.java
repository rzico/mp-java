package net.wit.controller.weex.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.ArticleModel;
import net.wit.controller.weex.model.ArticleOptionModel;
import net.wit.controller.weex.model.TemplateModel;
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
 * @ClassName: ArticleController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexMemberTemplateController")
@RequestMapping("/weex/member/template")
public class TemplateController extends BaseController {

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

    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

     /**
     * 获取页面模版
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Message list(Template.Type type,HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("type", Filter.Operator.eq,type));
        List<Template> templates = templateService.findList(null,filters,null);
        return Message.success(TemplateModel.bindList(templates),"发布成功");
    }

}