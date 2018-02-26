package net.wit.controller.makey.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.*;
import net.wit.controller.model.PaymentModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.JsonUtils;
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
 
@Controller("makeyMemberEvaluationController")
@RequestMapping("/makey/member/evaluation")
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

    @Resource(name = "memberAttributeServiceImpl")
    private MemberAttributeService memberAttributeService;

    @Resource(name = "evaluationServiceImpl")
    private EvaluationService evaluationService;

    @Resource(name = "evaluationAttributeServiceImpl")
    private EvaluationAttributeService evaluationAttributeService;

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
        Payment payment = evaluationService.create(eval);
        PaymentModel model = new PaymentModel();
        model.bind(payment);
        return Message.bind(model,request);
    }


    /**
     *  用户信息采集
     */
    @RequestMapping(value = "/userAttributes", method = RequestMethod.POST)
    @ResponseBody
    public Message userAttributes(Long id,String body,HttpServletRequest request){
        Evaluation evaluation = evaluationService.find(id);
        if (evaluation==null) {
            return Message.error("无效测评编号");
        }
        Member member = memberService.getCurrent();
        List<GaugeAttributeModel> attrs = new ArrayList<GaugeAttributeModel>();
        attrs = JsonUtils.toObject(body,List.class);
        for (GaugeAttributeModel attr:attrs) {
            MemberAttribute attribute = memberAttributeService.find(attr.getId());
           EvaluationAttribute eva = evaluationAttributeService.find(evaluation,attribute,EvaluationAttribute.Type.user);
           if (eva==null) {
               eva = new EvaluationAttribute();
               eva.setEvaluation(evaluation);
               eva.setMember(member);
               eva.setMemberAttribute(attribute);
               eva.setName(attribute.getName());
               eva.setType(EvaluationAttribute.Type.user);
               eva.setValue(attr.getValue());
               evaluationAttributeService.save(eva);
           } else {
               eva.setValue(attr.getValue());
               evaluationAttributeService.update(eva);
           }
        }
        return Message.success("保存成功");
    }

    /**
     *  常模修订
     */
    @RequestMapping(value = "/revisionAttributes", method = RequestMethod.GET)
    @ResponseBody
    public Message revisionAttributes(Long id,String body,HttpServletRequest request){
        Evaluation evaluation = evaluationService.find(id);
        if (evaluation==null) {
            return Message.error("无效测评编号");
        }
        Member member = memberService.getCurrent();
        List<GaugeAttributeModel> attrs = new ArrayList<GaugeAttributeModel>();
        attrs = JsonUtils.toObject(body,List.class);
        for (GaugeAttributeModel attr:attrs) {
            MemberAttribute attribute = memberAttributeService.find(attr.getId());
            EvaluationAttribute eva = evaluationAttributeService.find(evaluation,attribute,EvaluationAttribute.Type.revision);
            if (eva==null) {
                eva = new EvaluationAttribute();
                eva.setEvaluation(evaluation);
                eva.setMember(member);
                eva.setMemberAttribute(attribute);
                eva.setName(attribute.getName());
                eva.setType(EvaluationAttribute.Type.revision);
                eva.setValue(attr.getValue());
                evaluationAttributeService.save(eva);
            } else {
                eva.setValue(attr.getValue());
                evaluationAttributeService.update(eva);
            }
        }
        return Message.success("保存成功");
    }


    /**
     *  提交答案
     */
    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    @ResponseBody
    public Message question(Long id,String body,HttpServletRequest request){
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
    public Message list(Evaluation.EvalStatus status,Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (status!=null) {
            filters.add(new Filter("evalStatus", Filter.Operator.eq, status));
        }
        pageable.setFilters(filters);
        Page<Evaluation> page = evaluationService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(EvaluationListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

}