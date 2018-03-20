package net.wit.controller.weex;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.TemplateModel;
import net.wit.entity.Tag;
import net.wit.entity.Template;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: TemplateController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("templateController")
@RequestMapping("/template")
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

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

     /**
     * 获取页面模版
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(HttpServletRequest request){
        List<Template> templates = templateService.findList(Template.Type.topic);
        return Message.bind(TemplateModel.bindList(templates, Template.Type.topic),request);
    }

}