package net.wit.controller.makey.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.GaugeAttributeModel;
import net.wit.controller.makey.model.GaugeListModel;
import net.wit.controller.makey.model.GaugeModel;
import net.wit.controller.makey.model.GaugeQuestionModel;
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
 * @ClassName: EvaluationController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("makeyEvaluationController")
@RequestMapping("/makey/evaluation")
public class EvaluationController extends BaseController {

    @Resource(name = "gaugeCategoryServiceImpl")
    private GaugeCategoryService gaugeCategoryService;

    @Resource(name = "gaugeServiceImpl")
    private GaugeService gaugeService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "evaluationServiceImpl")
    private EvaluationService evaluationService;

    /**
     * 创建订单
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public Message create(Long id,HttpServletRequest request){
        Gauge gauge = gaugeService.find(id);
        if (gauge==null) {
            return Message.error("无效量表编号");
        }

        Member member = memberService.getCurrent();
        Evaluation  eval =  new Evaluation();
        eval.setDeleted(false);
        eval.setEval(0L);
        eval.setSn(snService.generate(Sn.Type.order));
        eval.setEvalStatus(Evaluation.EvalStatus.waiting);
        eval.setGauge(gauge);
        eval.setMember(member);
        eval.setPrice(gauge.getPrice());
        eval.setTitle(gauge.getTitle());
        eval.setSubTitle(gauge.getSubTitle());
        eval.setTotal(new Long(gauge.getGaugeQuestions().size()));

        GaugeModel model =new GaugeModel();
        model.bind(gauge);
        return Message.bind(model,request);
    }


    /**
     *  用户详资
     */
    @RequestMapping(value = "/userAttributes", method = RequestMethod.GET)
    @ResponseBody
    public Message userAttributes(Long id,HttpServletRequest request){
        Gauge gauge = gaugeService.find(id);
        if (gauge==null) {
            return Message.error("无效量表编号");
        }
        return Message.bind(GaugeAttributeModel.bindList(gauge.getUserAttributes()),request);
    }

    /**
     *  常模修订
     */
    @RequestMapping(value = "/revisionAttributes", method = RequestMethod.GET)
    @ResponseBody
    public Message revisionAttributes(Long id,HttpServletRequest request){
        Gauge gauge = gaugeService.find(id);
        if (gauge==null) {
            return Message.error("无效量表编号");
        }
        return Message.bind(GaugeAttributeModel.bindList(gauge.getRevisionAttributes()),request);
    }


    /**
     *  获取题目
     */
    @RequestMapping(value = "/question", method = RequestMethod.GET)
    @ResponseBody
    public Message question(Long id,HttpServletRequest request){
        Gauge gauge = gaugeService.find(id);
        if (gauge==null) {
            return Message.error("无效量表编号");
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
        pageable.setFilters(filters);
        List<Tag> tags = tagService.findList(tagId);
        Page<Gauge> page = gaugeService.findPage(null,null,tags,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(GaugeListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}