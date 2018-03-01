package net.wit.controller.makey;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.*;
import net.wit.entity.Gauge;
import net.wit.entity.GaugeCategory;
import net.wit.entity.Organization;
import net.wit.entity.Tag;
import net.wit.service.GaugeCategoryService;
import net.wit.service.GaugeService;
import net.wit.service.OrganizationService;
import net.wit.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: OrganizationController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("makeyOrganizationController")
@RequestMapping("/makey/organization")
public class OrganizationController extends BaseController {

    @Resource(name = "organizationServiceImpl")
    private OrganizationService organizationService;

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Organization.Type type, Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (type!=null) {
            filters.add(new Filter("type", Filter.Operator.eq, type));
        }
        pageable.setFilters(filters);
        Page<Organization> page = organizationService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(OrganizationModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}