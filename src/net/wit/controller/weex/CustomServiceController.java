package net.wit.controller.weex;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCatalogModel;
import net.wit.controller.model.CustomServiceModel;
import net.wit.controller.model.TopicListModel;
import net.wit.controller.model.TopicViewModel;
import net.wit.entity.*;
import net.wit.plat.weixin.main.MenuManager;
import net.wit.service.*;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @ClassName: CustomServiceController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("weexCustomServiceController")
@RequestMapping("weex/custom_service")
public class CustomServiceController extends BaseController {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "smssendServiceImpl")
    private SmssendService smssendService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "articleCatalogServiceImpl")
    private ArticleCatalogService articleCatalogService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "topicServiceImpl")
    private TopicService topicService;

    @Resource(name = "memberFollowServiceImpl")
    private MemberFollowService memberFollowService;

    @Resource(name = "customServiceServiceImpl")
    private CustomServiceService customServiceService;

    /**
     *  专栏搜索
     *  keyword
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("online", Filter.Operator.eq,true));
        List<CustomService> data = customServiceService.findList(null,null,filters,null);
        return Message.bind(CustomServiceModel.bindList(data),request);
    }

}