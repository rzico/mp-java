package net.wit.controller.makey;

import javafx.scene.shape.QuadCurve;
import net.sf.json.JSONObject;
import net.wit.*;
import net.wit.Message;
import net.wit.calculator.GeneCalculator;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.*;
import net.wit.controller.model.ArticleListModel;
import net.wit.controller.model.ArticleViewModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.MD5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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

    @Resource(name = "gaugeRelationServiceImpl")
    private GaugeRelationService gaugeRelationService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "enterpriseServiceImpl")
    private EnterpriseService enterpriseService;

    /**
     *  详情
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
     *  常模修订
     */
    @RequestMapping(value = "/attributes", method = RequestMethod.GET)
    @ResponseBody
    public Message revisionAttributes(Long id,HttpServletRequest request){
        Gauge gauge = gaugeService.find(id);
        if (gauge==null) {
            return Message.error("无效量表编号");
        }
        return Message.bind(GaugeAttributeModel.bindList(gauge.getAttributes()),request);
    }

    /**
     *  获取题目
     */
    @RequestMapping(value = "/question", method = RequestMethod.GET)
    @ResponseBody
    public Message question(Long id,Long agent,Long timeStamp,String sign,HttpServletRequest request){
        Gauge gauge = gaugeService.find(id);
        if (gauge==null) {
            return Message.error("无效量表编号");
        }
        Member member = memberService.getCurrent();
        if (member==null) {
            Enterprise enterprise = enterpriseService.find(agent);
            if (!MD5Utils.getMD5Str(String.valueOf(id)+ String.valueOf(agent) + String.valueOf(timeStamp)+MD5Utils.getMD5Str(enterprise.getId().toString()+"rzico.com")).equals(sign)) {
                return Message.error("无效签名");
            }
            member = enterprise.getMember();
        }

        return Message.bind(GaugeQuestionModel.bindList(gauge.getGaugeQuestions()),request);
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
        filters.add(new Filter("status", Filter.Operator.eq, Gauge.Status.enabled));
        pageable.setFilters(filters);
        List<Tag> tags = tagService.findList(tagId);
        Page<Gauge> page = gaugeService.findPage(null,null,tags,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(GaugeListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }


    /**
     *  列表
     */
    @RequestMapping(value = "/relation", method = RequestMethod.GET)
    @ResponseBody
    public Message relation(Long gaugeId,Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (gaugeId!=null) {
            Gauge gauge = gaugeService.find(gaugeId);
            filters.add(new Filter("gauge", Filter.Operator.eq, gauge));
        }
        pageable.setFilters(filters);
        Page<GaugeRelation> page = gaugeRelationService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(GaugeListModel.bindRelation(page.getContent()));
        return Message.bind(model,request);
    }
}