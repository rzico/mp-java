package net.wit.controller.makey;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.GaugeCategoryModel;
import net.wit.entity.AgentCategory;
import net.wit.entity.Enterprise;
import net.wit.entity.GaugeCategory;
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
 * @ClassName: GaugeCategoryController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("makeyGaugeCategoryController")
@RequestMapping("/makey/gauge_category")
public class GaugeCategoryController extends BaseController {

    @Resource(name = "gaugeCategoryServiceImpl")
    private GaugeCategoryService gaugeCategoryService;

    @Resource(name = "agentCategoryServiceImpl")
    private AgentCategoryService agentCategoryService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long agent,Long xmid,HttpServletRequest request){
        if (xmid!=null) {
            agent = xmid;
        }
        if (agent==null) {
            List<GaugeCategory> categories = gaugeCategoryService.findAll();

            return Message.bind(GaugeCategoryModel.bindList(categories), request);
        } else {
            Enterprise enterprise = enterpriseService.find(agent);
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("enterprise", Filter.Operator.eq,enterprise));
            List<AgentCategory> categories = agentCategoryService.findList(null,null,filters,null);
            return Message.bind(GaugeCategoryModel.bindAgent(categories), request);
        }
    }

}