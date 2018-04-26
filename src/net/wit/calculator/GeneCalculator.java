package net.wit.calculator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.entity.*;
import net.wit.util.FreemarkerUtils;
import net.wit.util.JsonUtils;
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

    private Map<String,Object> genes = new HashMap<>();
    private Map<String,String> dimes = new HashMap<>();
    private List<GaugeResult> results = new ArrayList();
    private Evaluation evaluation;

    private String N2A(Long a) {
        if (a.equals(1L)) {
            return "A";
        } else
        if (a.equals(2L)) {
            return "B";
        } else
        if (a.equals(3L)) {
            return "C";
        } else
        if (a.equals(4L)) {
            return "D";
        } else
        if (a.equals(5L)) {
            return "E";
        } else
        if (a.equals(6L)) {
            return "F";
        } else
        if (a.equals(7L)) {
            return "G";
        } else {
            throw new RuntimeException("error");
        }
    }

    private Long A2N(String a) {
        if (a.equals("A")) {
            return 1L;
        } else
        if (a.equals("B")) {
            return 2L;
        } else
        if (a.equals("C")) {
            return 3L;
        } else
        if (a.equals("D")) {
            return 4L;
        } else
        if (a.equals("E")) {
            return 5L;
        } else
        if (a.equals("F")) {
            return 6L;
        } else
        if (a.equals("G")) {
            return 7L;
        } else {
            throw new RuntimeException("error");
        }
    }

    public Map<String, Object> getGenes() {
        return genes;
    }

    public void setGenes(Map<String, Object> genes) {
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

    public void P1A_calc(GaugeGene gene) {
        Long a = 0L;
        for (GaugeQuestion question:gene.getQuestions()) {
            for (EvalAnswer answer:this.evaluation.getEvalAnswers()) {
                if (answer.getGaugeQuestion().equals(question) && answer.getAnswer().equals(1L)) {
                    a = a+1L;
                }
            }
        }
        this.genes.put(gene.getName()+"A",a);

        Long b = 0L;
        for (GaugeQuestion question:gene.getQuestions()) {
            for (EvalAnswer answer:this.evaluation.getEvalAnswers()) {
                if (answer.getGaugeQuestion().equals(question) && answer.getAnswer().equals(2L)) {
                    b = b+1L;
                }
            }
        }
        this.genes.put(gene.getName()+"B",b);

        Long c = 0L;
        for (GaugeQuestion question:gene.getQuestions()) {
            for (EvalAnswer answer:this.evaluation.getEvalAnswers()) {
                if (answer.getGaugeQuestion().equals(question) && answer.getAnswer().equals(3L)) {
                    c = c+1L;
                }
            }
        }
        this.genes.put(gene.getName()+"C",c);

        Long d = 0L;
        for (GaugeQuestion question:gene.getQuestions()) {
            for (EvalAnswer answer:this.evaluation.getEvalAnswers()) {
                if (answer.getGaugeQuestion().equals(question) && answer.getAnswer().equals(4L)) {
                    d = d+1L;
                }
            }
        }
        this.genes.put(gene.getName()+"D",d);

        Long e = 0L;
        for (GaugeQuestion question:gene.getQuestions()) {
            for (EvalAnswer answer:this.evaluation.getEvalAnswers()) {
                if (answer.getGaugeQuestion().equals(question) && answer.getAnswer().equals(5L)) {
                    e = e+1L;
                }
            }
        }
        this.genes.put(gene.getName()+"E",e);

        Long f = 0L;
        for (GaugeQuestion question:gene.getQuestions()) {
            for (EvalAnswer answer:this.evaluation.getEvalAnswers()) {
                if (answer.getGaugeQuestion().equals(question) && answer.getAnswer().equals(6L)) {
                    f = f+1L;
                }
            }
        }
        this.genes.put(gene.getName()+"F",f);


        Long g = 0L;
        for (GaugeQuestion question:gene.getQuestions()) {
            for (EvalAnswer answer:this.evaluation.getEvalAnswers()) {
                if (answer.getGaugeQuestion().equals(question) && answer.getAnswer().equals(7L)) {
                    g = g+1L;
                }
            }
        }
        this.genes.put(gene.getName()+"G",g);
    }
    public void Q3_calc() {
        int i=0;
        for (GaugeQuestion question:this.getEvaluation().getGauge().getGaugeQuestions()) {
            i=i+1;
            for (EvalAnswer answer:this.evaluation.getEvalAnswers()) {
                if (answer.getGaugeQuestion().equals(question)) {
                    this.genes.put("Q"+i,N2A(answer.getAnswer()));
                }
            }
        }
    }
    public void calc(GaugeGene gene) {

        BigDecimal s = BigDecimal.ZERO;
        for (GaugeQuestion question:gene.getQuestions()) {
            s = s.add(score(question));
        }
        this.genes.put(gene.getName(),s);

        //因子平均分
        if (gene.getQuestions().size()>0) {
            this.genes.put(gene.getName() + "avg", s.divide(new BigDecimal(gene.getQuestions().size()), 5, BigDecimal.ROUND_HALF_DOWN));
        } else {
            this.genes.put(gene.getName() + "avg", BigDecimal.ZERO);
        }

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
            model.addAttribute(key,this.genes.get(key));
        }

        BigDecimal s = BigDecimal.ZERO;
        try {
            String expr = FreemarkerUtils.process(gene.getAttribute(),model);
            double result = Calculator.conversion(expr);
            s = new BigDecimal(result);
        } catch (Exception e) {
            throw new RuntimeException("因子表达式错误:"+gene.getAttribute());
        }

        this.genes.put(gene.getName(),s);
    }

    //计算扩展变量
    public void calcExt() {
        //用时秒数
        this.genes.put("seconds",evaluation.getSeconds());
        //计算总数
        BigDecimal t = BigDecimal.ZERO;
        for (EvalAnswer as:this.evaluation.getEvalAnswers()) {
            t = t.add(as.getScore());
        }
        this.genes.put("total",t);
        //平均分
        BigDecimal tavg = t.divide(new BigDecimal(this.getEvaluation().getTotal()),5,BigDecimal.ROUND_HALF_DOWN);
        this.genes.put("tavg",tavg);
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
            this.genes.put("pavg", yd.divide(new BigDecimal(y),5,BigDecimal.ROUND_HALF_DOWN));
        } else {
            this.genes.put("pavg", BigDecimal.ZERO);
        }
        //因子平均分
        this.genes.put("savg", t.divide(new BigDecimal(this.getEvaluation().getGauge().getGaugeGenes().size()),5,BigDecimal.ROUND_HALF_DOWN));

        //所有用户的因子平均分
        this.genes.put("stavg", this.evaluation.getGauge().getTavg());
        //所有用户的因子标准分
        this.genes.put("devi", this.evaluation.getGauge().getDevi());
        //指定因子选 A 的题数
        for (GaugeGene ge:this.evaluation.getGauge().getGaugeGenes()) {
            this.P1A_calc(ge);
        }
        //获取指定题的题案
        this.Q3_calc();

    }


    public Boolean calcResult(GaugeResult result) throws Exception{
        ModelMap model = new ModelMap();
        for (String key : this.genes.keySet()) {
            model.addAttribute(key,this.genes.get(key));
        }


        BigDecimal s = BigDecimal.ZERO;
        try {
            String expr = FreemarkerUtils.process(result.getAttribute(),model);
            double ret = Calculator.conversion(" "+expr+" ");
            s = new BigDecimal(ret);
        } catch (Exception e) {
            throw new RuntimeException("结果表达式错误:"+result.getAttribute());
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
        this.calcInvaid(evaluation);
        for (GaugeResult result:evaluation.getGauge().getGaugeResults()) {
            if (this.calcResult(result)) {
                results.add(result);
            }
        }

    }


    public void calcInvaid(Evaluation evaluation) throws Exception {

        Long total = 0L;
        Long eq = 0L;
        String correct = "";
        List<Map<String,Long>> data = new ArrayList<>();
        if (evaluation.getGauge().getDetect()!=null) {
            JSONObject jsonObject = JSONObject.fromObject(evaluation.getGauge().getDetect());
            correct = jsonObject.getString("correct");
            JSONArray ar = jsonObject.getJSONArray("detect");
            for (int i=0;i<ar.size();i++) {
                total= total+1;
                JSONObject jb = ar.getJSONObject(i);
                Map<String,Long> d = new HashMap<>();
                String A = this.genes.get("Q"+jb.getString("A")).toString();
                String B = this.genes.get("Q"+jb.getString("B")).toString();
                if (A.equals(B)) {
                    eq = eq + 1L;
                }
            }
        }

        if (total>0) {
            this.genes.put("PASSED", eq * 100 / total);
        } else {
            this.genes.put("PASSED", 100);
        }

        ModelMap model = new ModelMap();
        for (String key : this.genes.keySet()) {
            model.addAttribute(key,this.genes.get(key));
        }

        if (!"".equals(correct)) {
            try {
                String expr = FreemarkerUtils.process(correct, model);
                double ret = Calculator.conversion(expr);
                if (ret<=0) {
                    throw new RuntimeException("无效答券");
                }
            } catch (Exception e) {
                throw new RuntimeException("测慌表达式错误:"+correct);
            }
        }
    }

    public String getHtml() {
        System.out.printf(JsonUtils.toJson(this.genes));
        ModelMap model = new ModelMap();
        for (String key : this.genes.keySet()) {
            model.addAttribute(key,this.genes.get(key));
        }
        String s="";
        for (GaugeResult r:this.results) {
            try {
                String text = r.getContent();
                String expr = FreemarkerUtils.process(text,model);
                s = s+expr;
            } catch (Exception e) {
                throw new RuntimeException("结果页面出错:id="+r.getId());
            }
        }
        return s;
    }

    public String getVars() {
        return JsonUtils.toJson(this.genes);
    }

}
