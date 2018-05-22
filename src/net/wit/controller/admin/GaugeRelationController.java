package net.wit.controller.admin;

import net.wit.*;
import net.wit.controller.makey.model.GaugeGeneAttributeModel;
import net.wit.entity.Gauge;
import net.wit.entity.GaugeGene;
import net.wit.entity.GaugeRelation;
import net.wit.service.*;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


/**
 * @ClassName: GaugeRelationController
 * @author 降魔战队
 * @date 2018-2-12 21:4:40
 */
 
@Controller("adminGaugeRelationController")
@RequestMapping("/admin/gaugeRelation")
public class GaugeRelationController extends BaseController {


	@Resource(name = "gaugeRelationServiceImpl")
	private GaugeRelationService gaugeRelationService;
	
	@Resource(name = "gaugeServiceImpl")
	private GaugeService gaugeService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Long gaugeId,ModelMap model) {

		model.addAttribute("gaugeId",gaugeId);
		return "/admin/gaugeRelation/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long gaugeId,ModelMap model) {
		model.addAttribute("gaugeId",gaugeId);

		return "/admin/gaugeRelation/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Long gaugeId,Long relationId,Integer orders){
		GaugeRelation entity = new GaugeRelation();

        Gauge relation = gaugeService.find(relationId);
        if (relation==null) {
        	return Message.error("无效量表");
		}
		entity.setRelation(relation);
		entity.setGauge(gaugeService.find(gaugeId));
		entity.setOrders(orders);

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gaugeRelationService.save(entity);
            return Message.success(entity,"admin.save.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.save.error");
        }
	}

	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Long gaugeId,Date beginDate, Date endDate, Pageable pageable, ModelMap model) {

		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		Filter typeFilter = new Filter("gauge", Filter.Operator.eq, gaugeService.find(gaugeId));
		filters.add(typeFilter);

		Page<GaugeRelation> page = gaugeRelationService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}


	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		try {
			gaugeRelationService.delete(ids);
			return Message.success("admin.delete.success");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.delete.error");
		}
	}


}