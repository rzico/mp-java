package net.wit.controller.makey.member;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.*;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.*;
import net.wit.controller.model.PaymentModel;
import net.wit.entity.*;
import net.wit.entity.summary.EvaluationSummary;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "gaugeQuestionServiceImpl")
    private GaugeQuestionService gaugeQuestionService;

    @Resource(name = "memberAttributeServiceImpl")
    private MemberAttributeService memberAttributeService;

    @Resource(name = "evaluationServiceImpl")
    private EvaluationService evaluationService;

    @Resource(name = "evaluationAttributeServiceImpl")
    private EvaluationAttributeService evaluationAttributeService;

    /**
     * 判断量表是否存在
     */
    @RequestMapping(value = "/exists")
    @ResponseBody
    public Message exists(Long id,HttpServletRequest request) {
        Member member = memberService.getCurrent();
        Gauge gauge = gaugeService.find(id);
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("evalStatus", Filter.Operator.eq, Evaluation.EvalStatus.paid));
        filters.add(new Filter("gauge", Filter.Operator.eq, gauge));
        filters.add(new Filter("member", Filter.Operator.eq, member));
        List<Evaluation> data = evaluationService.findList(1,filters,null);
        EvaluationListModel model = new EvaluationListModel();
        if (data.size()>0) {
            model.bind(data.get(0));
        }
        return Message.bind(model,request);
    }

    /**
     * 创建订单
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    public Message create(Long id,Long xuid,HttpServletRequest request){
        Gauge gauge = gaugeService.find(id);
        if (gauge==null) {
            return Message.error("无效量表编号");
        }
        Member member = memberService.getCurrent();
        Evaluation  eval =  new Evaluation();
        eval.setDeleted(false);
        eval.setEval(0L);
        eval.setThumbnail(gauge.getThumbnail());
        eval.setSn(snService.generate(Sn.Type.order));
        eval.setEvalStatus(Evaluation.EvalStatus.waiting);
        eval.setGauge(gauge);
        eval.setMember(member);
        eval.setPrice(gauge.getPrice());
        eval.setRebate(BigDecimal.ZERO);
        eval.setTitle(gauge.getTitle());
        eval.setSubTitle(gauge.getSubTitle());
        eval.setTotal(new Long(gauge.getGaugeQuestions().size()));
        if (xuid!=null) {
            Member promoter = memberService.find(xuid);
            if (promoter!=null) {
                eval.setPromoter(promoter);
                Admin admin = adminService.findByMember(promoter);
                if (admin!=null && admin.getEnterprise().getStatus().equals(Enterprise.Status.success))
                {
                    eval.setRebate(eval.getPrice().multiply(gauge.getDistribution()).multiply(new BigDecimal("0.01")).setScale(3,BigDecimal.ROUND_HALF_DOWN));
                } else {
                    eval.setRebate(eval.getPrice().multiply(gauge.getBrokerage()).multiply(new BigDecimal("0.01")).setScale(3,BigDecimal.ROUND_HALF_DOWN));
                }
            }
        }
        Payment payment = evaluationService.create(eval);
        Map<String,Object> data = new HashMap<String,Object>();
        PaymentModel model = new PaymentModel();
        model.bind(payment);
        data.put("payment",model);
        EvaluationListModel evalm = new EvaluationListModel();
        evalm.bind(eval);
        data.put("evaluation",evalm);
        return Message.bind(data,request);

    }


    /**
     *  用户信息
     */
    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    @ResponseBody
    public Message userinfo(Long id,String attr1,String attr2,String attr3,String attr4,HttpServletRequest request){
        Evaluation evaluation = evaluationService.find(id);
        if (evaluation==null) {
            return Message.error("无效测评编号");
        }
        Member member = memberService.getCurrent();
        evaluation.setAttr1(attr1);
        evaluation.setAttr2(attr2);
        evaluation.setAttr3(attr3);
        evaluation.setAttr4(attr4);
        evaluationService.update(evaluation);
        return Message.success("保存成功");
    }

    /**
     *  常模修订
     */
    @RequestMapping(value = "/attributes")
    @ResponseBody
    public Message attributes(Long id,String body,HttpServletRequest request){
        Evaluation evaluation = evaluationService.find(id);
        if (evaluation==null) {
            return Message.error("无效测评编号");
        }
        Member member = memberService.getCurrent();
        JSONArray attrs = JSONArray.fromObject(body);

        for (int i=0;i<attrs.size();i++) {
            JSONObject attr = attrs.getJSONObject(i);
            MemberAttribute attribute = memberAttributeService.find(attr.getLong("id"));
            EvaluationAttribute eva = evaluationAttributeService.find(evaluation,attribute,EvaluationAttribute.Type.revision);
            if (eva==null) {
                eva = new EvaluationAttribute();
                eva.setEvaluation(evaluation);
                eva.setMember(member);
                eva.setMemberAttribute(attribute);
                eva.setName(attribute.getName());
                eva.setType(EvaluationAttribute.Type.revision);
                eva.setValue(attr.getString("value"));
                evaluationAttributeService.save(eva);
            } else {
                eva.setValue(attr.getString("value"));
                evaluationAttributeService.update(eva);
            }
        }
        return Message.success("保存成功");
    }


    /**
     *  提交答案
     */
    @RequestMapping(value = "/answer")
    @ResponseBody
    public Message question(Long id,String body, HttpServletRequest request){
        System.out.printf(body);

        Evaluation evaluation = evaluationService.find(id);
        if (evaluation==null) {
            return Message.error("无效测评编号");
        }
        Member member = memberService.getCurrent();

        String mBody = new String(Base64.decode(body));

        System.out.printf(mBody);


        JSONArray answers = JSONArray.fromObject(mBody);
        List<EvalAnswer> evals = new ArrayList<EvalAnswer>();

        for (int i=0;i<answers.size();i++) {
            JSONObject ar = answers.getJSONObject(i);
            GaugeQuestion question = gaugeQuestionService.find(ar.getLong("questionId"));
            if (question!=null) {
                EvalAnswer eas = new EvalAnswer();
                eas.setAnswer(ar.getLong("optionId"));
                eas.setContent(question.getContent());
                eas.setTitle(question.getTitle());
                eas.setEvaluation(evaluation);
                eas.setGauge(evaluation.getGauge());
                eas.setGaugeQuestion(question);
                eas.setMember(evaluation.getMember());
                eas.setScore(new BigDecimal(ar.getString("score")));
                evals.add(eas);
            }
        }
        evaluation.setEvalAnswers(evals);
        evaluationService.answer(evaluation);
        return Message.success("答题完毕");

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


    /**
     *  推广排序
     */
    @RequestMapping(value = "/promoter_list", method = RequestMethod.GET)
    @ResponseBody
    public Message promoterList(Long id,Pageable pageable, HttpServletRequest request){
        Gauge gauge = gaugeService.find(id);
        if (gauge==null) {
            return Message.error("无效量表编号");
        }

        List<EvaluationSummary> data = evaluationService.sumPromoter(gauge,null,null);
        return Message.bind(PromoterListModel.bindList(data),request);
    }

    /**
     *  我的推广
     */
    @RequestMapping(value = "/promoter", method = RequestMethod.GET)
    @ResponseBody
    public Message promoter(Pageable pageable, HttpServletRequest request){
        Member member = memberService.getCurrent();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("promoter", Filter.Operator.eq, member));
        filters.add(new Filter("evalStatus", Filter.Operator.eq, Evaluation.EvalStatus.completed));
        pageable.setFilters(filters);
        Page<Evaluation> page = evaluationService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(EvaluationListModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message view(Long id,HttpServletRequest request){
        Evaluation evaluation = evaluationService.find(id);
        if (evaluation==null) {
            return Message.error("无效测评编号");
        }
        EvaluationModel model =new EvaluationModel();
        model.bind(evaluation);
        return Message.bind(model,request);
    }

    /**
     * 报告
     */
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String report(Long id,HttpServletRequest request,ModelMap model){
        Evaluation evaluation = evaluationService.find(id);
        model.addAttribute("data",evaluation);
        return "/common/report";
    }


}