package net.wit.calculator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import net.sf.json.JSONObject;
import net.wit.entity.EvalAnswer;
import net.wit.entity.Evaluation;
import net.wit.entity.GaugeGene;
import net.wit.entity.GaugeQuestion;
import net.wit.util.FreemarkerUtils;
import org.springframework.ui.ModelMap;
import org.tuckey.web.filters.urlrewrite.Run;

import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangsr on 2018/3/27.
 */
public class GeneCalculator implements Serializable {

    private Map<String,BigDecimal> genes = new HashMap<>();
    private Map<String,String> dimes = new HashMap<>();
    private Evaluation evaluation;


    public BigDecimal score(GaugeQuestion question) {
        BigDecimal t = BigDecimal.ZERO;
        for (EvalAnswer answer:evaluation.getEvalAnswers()) {
            if (answer.getGaugeQuestion().equals(question)) {
                t = t.add(answer.getScore());
                break;
            }
        }
        return t;
    }

    public void calc(GaugeGene gene) {
        BigDecimal s = BigDecimal.ZERO;
        for (GaugeQuestion question:gene.getQuestions()) {
            s = s.add(score(question));
        }
        genes.put(gene.getName(),s);

        net.sf.json.JSONArray attrs = net.sf.json.JSONArray.fromObject(gene.getAttribute());
        for (int i=0;i<attrs.size();i++) {
            JSONObject attr = attrs.getJSONObject(i);
            BigDecimal smin = new BigDecimal(attr.getDouble("smin"));
            BigDecimal smax = new BigDecimal(attr.getDouble("smax"));
            if (smin.compareTo(s)<=0 && smax.compareTo(s)>=0) {
                dimes.put(gene.getName(),attr.getString("name"));
            }
        }

    }

    public void calcExpr(GaugeGene gene) throws Exception{
        ModelMap model = new ModelMap();
        for (String key : genes.keySet()) {
            model.addAttribute(key,genes.get("key"));
        }

        BigDecimal s = BigDecimal.ZERO;
        try {
            String expr = FreemarkerUtils.process(gene.getAttribute(),model);

            double result = Calculator.conversion(expr);
            s = new BigDecimal(result);
        } catch (Exception e) {
            throw new RuntimeException("表达式错误");
        }

        genes.put(gene.getName(),s);
    }

    public void calcAll(Evaluation evaluation) throws Exception {
        this.evaluation = evaluation;
        for (GaugeGene gene:evaluation.getGauge().getGaugeGenes()) {
            if (gene.getRank().equals(GaugeGene.Rank.rank1)) {
                calc(gene);
            } else {
                calcExpr(gene);
            }
        }
    }


}
