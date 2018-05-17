package net.wit.controller.admin;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.model.AppletCodeConfig;
import net.wit.entity.*;
import net.wit.plat.weixin.pojo.AuthAccessToken;
import net.wit.plat.weixin.pojo.ComponentAccessToken;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.AdminService;
import net.wit.service.MemberService;
import net.wit.service.PluginConfigService;
import net.wit.service.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.applet.Applet;
import java.util.*;

/**
 * Created by Eric-Yang on 2018/5/8.
 */
@Controller("smallRangeController")
@RequestMapping("/admin/smallRange")
public class SmallRangeController extends BaseController {

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "topicServiceImpl")
    private TopicService topicService;

    @Resource(name="pluginConfigServiceImpl")
    private PluginConfigService pluginConfigService;

    /**
     * 主页
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "/admin/smallRange/list";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Date beginDate, Date endDate, Pageable pageable, ModelMap model, String searchValue) {

//        Admin admin= adminService.getCurrent();
//
//        Enterprise enterprise=admin.getEnterprise();
//        if(enterprise==null){
//            return Message.error("您还未绑定企业");
//        }
//        if(enterprise.getDeleted()){
//            return Message.error("您的企业不存在");
//        }
//
//        Member member=enterprise.getMember();
//
//        Topic topic= member.getTopic();
        List<Filter> filters = new ArrayList<Filter>();
        pageable.setFilters(filters);
        if (searchValue != null && !searchValue.equals("")) {
            filters.add(new Filter("name", Filter.Operator.like, "%" + searchValue + "%"));
        }
        Page<Topic> page = topicService.findPage(beginDate, endDate, pageable);
        return Message.success(PageBlock.bind(page), "admin.list.success");
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        List<MapEntity> types = new ArrayList<>();
        types.add(new MapEntity("outOfService","暂停服务"));
        types.add(new MapEntity("notUploaded","未上传"));
        types.add(new MapEntity("audit","待审核"));
        types.add(new MapEntity("online","已上线"));
        types.add(new MapEntity("passed","已通过"));
        model.addAttribute("types",types);
        model.addAttribute("data", topicService.find(id));
        return "/admin/smallRange/edit";
    }


    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(Topic topic, String appetAppId, String appetAppSerect, String version, TopicConfig.Estate estate){
        Topic entity = topicService.find(topic.getId());

        entity.setCreateDate(topic.getCreateDate());

        entity.setModifyDate(topic.getModifyDate());

        entity.setName(topic.getName());

        TopicConfig topicConfig=entity.getConfig();

        topicConfig.setAppetAppId(appetAppId);

        topicConfig.setAppetAppSerect(appetAppSerect);

        topicConfig.setVersion(version);

        topicConfig.setEstate(estate);

        entity.setConfig(topicConfig);

        if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            topicService.update(entity);
            return Message.success(entity,"admin.update.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.update.error");
        }
    }

    /**
     * 查询管理视图
     */
    @RequestMapping(value = "/searchView", method = RequestMethod.GET)
    public String searchView(Long id, ModelMap model) {
        Topic topic=topicService.find(id);
        model.addAttribute("name",topic.getName());
        model.addAttribute("appid",topic.getConfig().getAppetAppId());
        model.addAttribute("version",topic.getConfig().getVersion());
        String result=WeixinApi.getStatus(authToken(id));
        model.addAttribute("status",result);
        if(result.equals("0")){
            TopicConfig topicConfig=topic.getConfig();
            topicConfig.setEstate(TopicConfig.Estate.ISAUDITING);
            topic.setConfig(topicConfig);
            topicService.update(topic);
        }
        return "/admin/smallRange/view/statusView";
    }

    /**
     * 上传小程序
     * */
    @RequestMapping(value = "/upLoadView")
    public String upLoadView(Long id,ModelMap modelMap){
        modelMap.addAttribute("topic",topicService.find(id));
        return "/admin/smallRange/view/upLoad";
    }
    /**
     * 上传小程序
     * */
    @RequestMapping(value = "/upLoad",method = RequestMethod.POST)
    public Message upLoad(Long id,String version,String templateId,String userDesc){
        Topic topic=topicService.find(id);
        AppletCodeConfig appletCodeConfig=new AppletCodeConfig();
        appletCodeConfig.setAppid(topic.getConfig().getAppetAppId());
        appletCodeConfig.setName(topic.getName());
        appletCodeConfig.setMemberId(topic.getMember().getId());
        if(WeixinApi.commitAppletCode(authToken(id),templateId,version,userDesc,appletCodeConfig)){
            TopicConfig topicConfig=topic.getConfig();
            topicConfig.setVersion(version);
            topicConfig.setEstate(TopicConfig.Estate.AUDITING);
            topicConfig.setStateRemark(userDesc);
            topic.setConfig(topicConfig);
            topicService.update(topic);
            return Message.success("上传成功");
        }else {
            return Message.error("未知异常,请稍后重试");
        }
    }

    /**
     * 提交审核小程序
     * */
    @RequestMapping(value = "/commit",method = RequestMethod.POST)
    public Message commit(Long id){
        if(WeixinApi.pushAppletCode(authToken(id))){
            Topic topic=topicService.find(id);
            TopicConfig topicConfig=topic.getConfig();
            topicConfig.setEstate(TopicConfig.Estate.AUDITING);
            topic.setConfig(topicConfig);
            topicService.update(topic);
            return Message.success("提交成功");
        }else {
            return Message.error("未知异常,请稍后重试");
        }
    }

    /**
     * 发布小程序
     * */
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    public Message publish(Long id){
        if(WeixinApi.releaseAppletCode(authToken(id))){
            Topic topic=topicService.find(id);
            TopicConfig topicConfig=topic.getConfig();
            topicConfig.setEstate(TopicConfig.Estate.PASS);
            topic.setConfig(topicConfig);
            topicService.update(topic);
            return Message.success("提交成功");
        }else {
            return Message.error("未知异常,请稍后重试");
        }
    }

    /**
     * 小程序版本回退
     * */
    @RequestMapping(value = "/comeBack",method = RequestMethod.POST)
    public Message comeBack(Long id){
        if(WeixinApi.revertAppletCode(authToken(id))){
            return Message.success("回退成功");
        }else {
            return Message.error("未知异常,请稍后重试");
        }
    }

    /**
     * 设置是否可被搜索
     * */
    public boolean setAppletState(Long id,int status){
        if(WeixinApi.setAppletStatus(authToken(id),status).getErrcode().equals("0")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取authorizerToken
     * */
    public String authToken(Long id){
        //拿ticket
        PluginConfig pluginConfig = pluginConfigService.findByPluginId("verifyTicket");
        String verifyTicket = pluginConfig.getAttribute("verify_ticket");

        //拿第三方平台APPID SECRET
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String appid=bundle.getString("weixin.component.appid");
        String secret=bundle.getString("weixin.component.secret");

        //拿comtonken
        String componentToken=WeixinApi.getComponentToken(verifyTicket,appid,secret).getComponent_access_token();

        //拿refresh_token
        Topic topic=topicService.find(id);
        String refresh=topic.getConfig().getRefreshToken();
        String threeAppID=topic.getConfig().getAppetAppId();


        //拿authorizer_appid
//        String authorizerToken=WeixinApi.getAuthorizationCode(componentToken,appid,"关键字(code)").getAuthorizer_access_token();
        AuthAccessToken authorizer=WeixinApi.getRefreshAuthorizationCode(componentToken,threeAppID,refresh);

        topic.getConfig().setRefreshToken(authorizer.getAuthorizer_refresh_token());
        topic.getConfig().setTokenExpire(authorizer.getExpire());
        topicService.update(topic);

        return authorizer.getAuthorizer_access_token();
    }
}
