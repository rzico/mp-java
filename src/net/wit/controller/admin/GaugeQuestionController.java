package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.controller.makey.model.GaugeQuestionOptionModel;
import net.wit.util.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Filters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.entity.BaseEntity.Save;
import net.wit.entity.GaugeQuestion;
import net.wit.service.GaugeQuestionService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: GaugeQuestionController
 * @author 降魔战队
 * @date 2018-2-12 21:4:40
 */
 
@Controller("adminGaugeQuestionController")
@RequestMapping("/admin/gaugeQuestion")
public class GaugeQuestionController extends BaseController {
	@Resource(name = "gaugeQuestionServiceImpl")
	private GaugeQuestionService gaugeQuestionService;
	
	@Resource(name = "gaugeServiceImpl")
	private GaugeService gaugeService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Long gaugeId,ModelMap model) {


		List<MapEntity> questionTypes = new ArrayList<>();
		questionTypes.add(new MapEntity("radioBox","单选题"));
		questionTypes.add(new MapEntity("memory","记忆题"));
		questionTypes.add(new MapEntity("sort","排序题"));
		model.addAttribute("questionTypes",questionTypes);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("text","文字"));
		types.add(new MapEntity("image","图片"));
		types.add(new MapEntity("formula","算式"));
		model.addAttribute("types",types);

		model.addAttribute("gaugeId",gaugeId);

		return "/admin/gaugeQuestion/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long gaugeId,ModelMap model) {

		List<MapEntity> questionTypes = new ArrayList<>();
		questionTypes.add(new MapEntity("radioBox","单选题"));
		questionTypes.add(new MapEntity("memory","记忆题"));
		questionTypes.add(new MapEntity("sort","排序题"));
		model.addAttribute("questionTypes",questionTypes);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("text","文字"));
		types.add(new MapEntity("image","图片"));
		types.add(new MapEntity("formula","算式"));
		model.addAttribute("types",types);

		model.addAttribute("gaugeId",gaugeId);

		return "/admin/gaugeQuestion/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(GaugeQuestion gaugeQuestion,String [] name,String [] image, BigDecimal [] score,Long gaugeId){
		GaugeQuestion entity = new GaugeQuestion();	

		entity.setCreateDate(gaugeQuestion.getCreateDate());

		entity.setModifyDate(gaugeQuestion.getModifyDate());

		entity.setOrders(gaugeQuestion.getOrders() == null ? 0 : gaugeQuestion.getOrders());

		List<Map<String,Object>> data = new ArrayList<>();
		for (int i=0;i<name.length;i++) {
			if (gaugeQuestion.getType().equals(GaugeQuestion.Type.text) && name[i]!=null && !"".equals(name[i])) {
				Map<String, Object> q = new HashMap<String, Object>();
				q.put("id", i);
				q.put("name", name[i]);
				q.put("image", image[i]);
				q.put("score", score[i]);
				data.add(q);
			}
			if (gaugeQuestion.getType().equals(GaugeQuestion.Type.image) && image[i]!=null && !"".equals(image[i])) {
				Map<String, Object> q = new HashMap<String, Object>();
				q.put("id", i);
				q.put("name", name[i]);
				q.put("image", image[i]);
				q.put("score", score[i]);
				data.add(q);
			}
		}

		entity.setContent(JsonUtils.toJson(data));

		entity.setTitle(gaugeQuestion.getTitle());

		entity.setQuestionType(gaugeQuestion.getQuestionType());

		entity.setType(gaugeQuestion.getType());

		entity.setStayTime(gaugeQuestion.getStayTime());

		entity.setStrLen(gaugeQuestion.getStrLen());

		entity.setType(gaugeQuestion.getType());

		entity.setGauge(gaugeService.find(gaugeId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gaugeQuestionService.save(entity);
            return Message.success(entity,"admin.save.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.save.error");
        }
	}


	/**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        try {
            gaugeQuestionService.delete(ids);
            return Message.success("admin.delete.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.delete.error");
        }
    }
	
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {


		List<MapEntity> questionTypes = new ArrayList<>();
		questionTypes.add(new MapEntity("radioBox","单选题"));
		questionTypes.add(new MapEntity("memory","记忆题"));
		questionTypes.add(new MapEntity("sort","排序题"));
		model.addAttribute("questionTypes",questionTypes);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("text","文字"));
		types.add(new MapEntity("image","图片"));
		types.add(new MapEntity("formula","算式"));
		model.addAttribute("types",types);

		GaugeQuestion question = gaugeQuestionService.find(id);
		model.addAttribute("data",question);
		List<GaugeQuestionOptionModel> opts = JsonUtils.toObject(question.getContent(),List.class);
//		List<Map<String,String>> data = new ArrayList<>();
//		for (int i=0;i<opts.size();i++) {
//				Map<String, String> q = new HashMap<String, String>();
//				q.put("name",  opts[i][1]);
//				q.put("image", opts[i][2]);
//				q.put("score", opts[i][3]);
//				data.add(q);
//		}
		model.addAttribute("options",opts);
		model.addAttribute("options_length",opts.size());
		model.addAttribute("gaugeId",question.getGauge().getId());

		return "/admin/gaugeQuestion/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(GaugeQuestion gaugeQuestion,String [] name,String [] image, BigDecimal [] score, Long gaugeId){
		GaugeQuestion entity = gaugeQuestionService.find(gaugeQuestion.getId());


		entity.setOrders(gaugeQuestion.getOrders() == null ? 0 : gaugeQuestion.getOrders());

		List<Map<String,Object>> data = new ArrayList<>();
		for (int i=0;i<name.length;i++) {
			if (gaugeQuestion.getType().equals(GaugeQuestion.Type.text) && name[i]!=null && !"".equals(name[i])) {
				Map<String, Object> q = new HashMap<String, Object>();
				q.put("id", i);
				q.put("name", name[i]);
				q.put("image", image[i]);
				q.put("score", score[i]);
				data.add(q);
			}
			if (gaugeQuestion.getType().equals(GaugeQuestion.Type.image) && image[i]!=null && !"".equals(image[i])) {
				Map<String, Object> q = new HashMap<String, Object>();
				q.put("id", i);
				q.put("name", name[i]);
				q.put("image", image[i]);
				q.put("score", score[i]);
				data.add(q);
			}
		}

		entity.setContent(JsonUtils.toJson(data));

		entity.setTitle(gaugeQuestion.getTitle());

		entity.setQuestionType(gaugeQuestion.getQuestionType());

		entity.setType(gaugeQuestion.getType());

		entity.setStayTime(gaugeQuestion.getStayTime());

		entity.setStrLen(gaugeQuestion.getStrLen());

		entity.setGauge(gaugeService.find(gaugeId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gaugeQuestionService.update(entity);
            return Message.success(entity,"admin.update.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.update.error");
        }
	}
	

	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Long gaugeId,Date beginDate, Date endDate, GaugeQuestion.Type type, Pageable pageable, ModelMap model) {

		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		Filter typeFilter = new Filter("gauge", Filter.Operator.eq, gaugeService.find(gaugeId));
		filters.add(typeFilter);


		Page<GaugeQuestion> page = gaugeQuestionService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * Gauge视图
	 */
	@RequestMapping(value = "/gaugeView", method = RequestMethod.GET)
	public String gaugeView(Long id, ModelMap model) {
		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("single","单常模"));
		types.add(new MapEntity("complex","多常模"));
		model.addAttribute("types",types);

		List<MapEntity> userTypes = new ArrayList<>();
		userTypes.add(new MapEntity("general","普通用户"));
		userTypes.add(new MapEntity("enterprise","企业用户"));
		userTypes.add(new MapEntity("school","学校用户"));
		model.addAttribute("userTypes",userTypes);

		model.addAttribute("products",productService.findAll());

		model.addAttribute("gauge",gaugeService.find(id));
		return "/admin/gaugeQuestion/view/gaugeView";
	}



}