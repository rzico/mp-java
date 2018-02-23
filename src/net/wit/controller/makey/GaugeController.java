package net.wit.controller.makey;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.GaugeCategoryModel;
import net.wit.controller.makey.model.GaugeListModel;
import net.wit.controller.makey.model.GaugeModel;
import net.wit.controller.model.ArticleListModel;
import net.wit.controller.model.ArticleViewModel;
import net.wit.entity.*;
import net.wit.service.GaugeCategoryService;
import net.wit.service.GaugeService;
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
 * @ClassName: GaugeController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("makeyGaugeController")
@RequestMapping("/makey/gauge")
public class GaugeController extends BaseController {

    @Resource(name = "gaugeCategoryServiceImpl")
    private GaugeCategoryService gaugeCategoryService;

    @Resource(name = "gaugeServiceImpl")
    private GaugeService gaugeService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;


    /**
     * 详情
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Gauge gauge = gaugeService.find(id);
        if (gauge==null) {
            return Message.error("无效量表编号");
        }
        GaugeModel model =new GaugeModel();
        model.bind(gauge);
        return Message.bind(model,request);
    }

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long gaugeCategoryId,Long tagId, Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (gaugeCategoryId!=null) {
            GaugeCategory category = gaugeCategoryService.find(gaugeCategoryId);
            filters.add(new Filter("gaugeCategory", Filter.Operator.eq, category));
        }
        pageable.setFilters(filters);
        List<Tag> tags = tagService.findList(tagId);
        Page<Gauge> page = gaugeService.findPage(null,null,tags,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(GaugeListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}