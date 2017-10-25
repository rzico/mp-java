package net.wit.controller.mch;

import net.wit.Message;
import net.wit.Page;
import net.wit.PageBlock;
import net.wit.Pageable;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.OrderLog;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @ClassName: OrderLogController
 * @author 降魔战队
 * @date 2017-10-11 15:37:11
 */
 
@Controller("mchOrderLogController")
@RequestMapping("/mch/orderLog")
public class OrderLogController extends BaseController {
	@Resource(name = "orderLogServiceImpl")
	private OrderLogService orderLogService;
	
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("orderss",orderService.findAll());

		return "/mch/orderLog/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("orderss",orderService.findAll());

		return "/mch/orderLog/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(OrderLog orderLog, Long ordersId){
		OrderLog entity = new OrderLog();	

		entity.setCreateDate(orderLog.getCreateDate());

		entity.setModifyDate(orderLog.getModifyDate());

		entity.setContent(orderLog.getContent());

		entity.setOperator(orderLog.getOperator());

		entity.setType(orderLog.getType() == null ? OrderLog.Type.create : orderLog.getType());

		entity.setOrder(orderService.find(ordersId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            orderLogService.save(entity);
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
            orderLogService.delete(ids);
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

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("data",orderLogService.find(id));

		return "/mch/orderLog/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(OrderLog orderLog, Long ordersId){
		OrderLog entity = orderLogService.find(orderLog.getId());
		
		entity.setCreateDate(orderLog.getCreateDate());

		entity.setModifyDate(orderLog.getModifyDate());

		entity.setContent(orderLog.getContent());

		entity.setOperator(orderLog.getOperator());

		entity.setType(orderLog.getType() == null ? OrderLog.Type.create : orderLog.getType());

		entity.setOrder(orderService.find(ordersId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            orderLogService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Pageable pageable, ModelMap model) {	

		Page<OrderLog> page = orderLogService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 订单管理视图
	 */
	@RequestMapping(value = "/orderView", method = RequestMethod.GET)
	public String orderView(Long id, ModelMap model) {
		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("couponCodes",couponCodeService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		model.addAttribute("order",orderService.find(id));
		return "/mch/orderLog/view/orderView";
	}



}