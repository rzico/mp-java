package net.wit.controller.admin;

import net.wit.*;
import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.AdminService;
import net.wit.service.MemberService;
import net.wit.service.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}
