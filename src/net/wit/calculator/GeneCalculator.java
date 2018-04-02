package net.wit.calculator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONObject;
import net.wit.entity.*;
import net.wit.util.FreemarkerUtils;
import org.springframework.ui.ModelMap;
import org.tuckey.web.filters.urlrewrite.Run;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangsr on 2018/3/27.
 */
public class GeneCalculator implements Serializable {

    private Map<String,BigDecimal> genes = new HashMap<>();
    private Map<String,String> dimes = new HashMap<>();
    private List<GaugeResult> results = new ArrayList();
    private Evaluation evaluation;

    public Map<String, BigDecimal> getGenes() {
        return genes;
    }

    public void setGenes(Map<String, BigDecimal> genes) {
        this.genes = genes;
    }

    public Map<String, String> getDimes() {
        return dimes;
    }

    public void setDimes(Map<String, String> dimes) {
        this.dimes = dimes;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public List<GaugeResult> getResults() {
        return results;
    }

    public void setResults(List<GaugeResult> results) {
        this.results = results;
    }

    public BigDecimal score(GaugeQuestion question) {
        BigDecimal t = BigDecimal.ZERO;
        for (EvalAnswer answer:this.evaluation.getEvalAnswers()) {
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
        this.genes.put(gene.getName(),s);

        EvalGeneScore sc = new EvalGeneScore();
        sc.setEvaluation(this.evaluation);
        sc.setGauge(this.evaluation.getGauge());
        sc.setGaugeGene(gene);
        sc.setScore(s);
        sc.setMember(this.evaluation.getMember());
        sc.setName(gene.getName());
        this.evaluation.getEvalGeneScores().add(sc);

        net.sf.json.JSONArray attrs = net.sf.json.JSONArray.fromObject(gene.getAttribute());

        for (int i=0;i<attrs.size();i++) {
            JSONObject attr = attrs.getJSONObject(i);
            BigDecimal smin = new BigDecimal(attr.getDouble("smin"));
            BigDecimal smax = new BigDecimal(attr.getDouble("smax"));
            if (smin.compareTo(s)<=0 && smax.compareTo(s)>=0) {
                this.dimes.put(gene.getName(),attr.getString("sname"));
            }
        }

    }

    public void calcExpr(GaugeGene gene) throws Exception{
        ModelMap model = new ModelMap();
        for (String key : this.genes.keySet()) {
            model.addAttribute(key,this.genes.get("key"));
        }

        BigDecimal s = BigDecimal.ZERO;
        try {
            String expr = FreemarkerUtils.process(gene.getAttribute(),model);

            double result = Calculator.conversion(expr);
            s = new BigDecimal(result);
        } catch (Exception e) {
            throw new RuntimeException("表达式错误");
        }

        this.genes.put(gene.getName(),s);
    }

    //计算扩展变量
    public void calcExt() {
        //计算总数
        BigDecimal t = BigDecimal.ZERO;
        for (EvalAnswer as:this.evaluation.getEvalAnswers()) {
            t = t.add(as.getScore());
        }
        this.genes.put("total",t);
        //平均分
        this.genes.put("tavg",t.divide(new BigDecimal(this.getEvaluation().getTotal())).setScale(5,BigDecimal.ROUND_HALF_DOWN));
        //阳性项目数
        Long y = 0L;
        BigDecimal yd = BigDecimal.ZERO;
        for (EvalAnswer as:this.evaluation.getEvalAnswers()) {
            if (as.getScore().compareTo(new BigDecimal(2))>=0) {
                y = y+1L;
                yd = yd.add(as.getScore());
            };
        }
        this.genes.put("positive",new BigDecimal(y));
        //阳性项目数
        Long n = 0L;
        BigDecimal nd = BigDecimal.ZERO;
        for (EvalAnswer as:this.evaluation.getEvalAnswers()) {
            if (as.getScore().compareTo(new BigDecimal(2))<0) {
                n = n+1L;
                nd = as.getScore();
            };
        }
        this.genes.put("negative",new BigDecimal(n));
        //阳性项目均数
        if (y!=0L) {
            this.genes.put("pavg", yd.divide(new BigDecimal(y)).setScale(5, BigDecimal.ROUND_HALF_DOWN));
        } else {
            this.genes.put("pavg", BigDecimal.ZERO);
        }
        //因子平均分
        this.genes.put("savg", t.divide(new BigDecimal(this.getEvaluation().getGauge().getGaugeGenes().size())).setScale(5,BigDecimal.ROUND_HALF_DOWN));
        //所有用户的因子平均分
        this.genes.put("savg", t.divide(new BigDecimal(this.getEvaluation().getGauge().getGaugeGenes().size())).setScale(5,BigDecimal.ROUND_HALF_DOWN));
    }


    public Boolean calcResult(GaugeResult result) throws Exception{
        ModelMap model = new ModelMap();
        for (String key : this.genes.keySet()) {
            model.addAttribute(key,this.genes.get(key));
        }

        BigDecimal s = BigDecimal.ZERO;
        try {
            String expr = FreemarkerUtils.process(result.getAttribute(),model);

            double ret = Calculator.conversion(expr);
            s = new BigDecimal(ret);
        } catch (Exception e) {
            throw new RuntimeException("表达式错误");
        }

        return s.compareTo(BigDecimal.ZERO)>0;

    }

    public void calcAll(Evaluation evaluation) throws Exception {
        this.genes.clear();
        this.dimes.clear();
        this.evaluation = evaluation;
        for (GaugeGene gene:evaluation.getGauge().getGaugeGenes()) {
            if (gene.getRank().equals(GaugeGene.Rank.rank1)) {
                this.calc(gene);
            } else {
                this.calcExpr(gene);
            }
        }

        this.calcExt();
        for (GaugeResult result:evaluation.getGauge().getGaugeResults()) {
            if (this.calcResult(result)) {
                results.add(result);
            }
        }

    }

    public String getHtml() {
        ModelMap model = new ModelMap();
        for (String key : this.genes.keySet()) {
            model.addAttribute(key,this.genes.get("key"));
        }
        String s="";
        for (GaugeResult r:this.results) {
            try {
                String expr = FreemarkerUtils.process(r.getContent(),model);
                s = s+expr;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

}
