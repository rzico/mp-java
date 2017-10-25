package net.wit.controller.mch;

import net.wit.*;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Returns;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: ReturnsController
 * @author 降魔战队
 * @date 2017-10-11 15:37:14
 */
 
@Controller("mchReturnsController")
@RequestMapping("/mch/returns")
public class ReturnsController extends BaseController {
	@Resource(name = "returnsServiceImpl")
	private ReturnsService returnsService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		return "/admin/returns/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		return "/admin/returns/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Returns returns, Long memberId, Long sellerId, Long ordersId){
		Returns entity = new Returns();	

		entity.setCreateDate(returns.getCreateDate());

		entity.setModifyDate(returns.getModifyDate());

		entity.setAddress(returns.getAddress());

		entity.setAreaName(returns.getAreaName());

		entity.setDeliveryCorp(returns.getDeliveryCorp());

		entity.setFreight(returns.getFreight());

		entity.setMemo(returns.getMemo());

		entity.setOperator(returns.getOperator());

		entity.setPhone(returns.getPhone());

		entity.setShipper(returns.getShipper());

		entity.setShippingMethod(returns.getShippingMethod());

		entity.setSn(returns.getSn());

		entity.setTrackingNo(returns.getTrackingNo());

		entity.setZipCode(returns.getZipCode());

		entity.setMember(memberService.find(memberId));

		entity.setOrder(orderService.find(ordersId));

		entity.setSeller(memberService.find(sellerId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            returnsService.save(entity);
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
            returnsService.delete(ids);
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

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		model.addAttribute("data",returnsService.find(id));

		return "/admin/returns/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Returns returns, Long memberId, Long sellerId, Long ordersId){
		Returns entity = returnsService.find(returns.getId());
		
		entity.setCreateDate(returns.getCreateDate());

		entity.setModifyDate(returns.getModifyDate());

		entity.setAddress(returns.getAddress());

		entity.setAreaName(returns.getAreaName());

		entity.setDeliveryCorp(returns.getDeliveryCorp());

		entity.setFreight(returns.getFreight());

		entity.setMemo(returns.getMemo());

		entity.setOperator(returns.getOperator());

		entity.setPhone(returns.getPhone());

		entity.setShipper(returns.getShipper());

		entity.setShippingMethod(returns.getShippingMethod());

		entity.setSn(returns.getSn());

		entity.setTrackingNo(returns.getTrackingNo());

		entity.setZipCode(returns.getZipCode());

		entity.setMember(memberService.find(memberId));

		entity.setOrder(orderService.find(ordersId));

		entity.setSeller(memberService.find(sellerId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            returnsService.update(entity);
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

		Page<Returns> page = returnsService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 会员管理视图
	 */
	@RequestMapping(value = "/memberView", method = RequestMethod.GET)
	public String memberView(Long id, ModelMap model) {
		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("occupations",occupationService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/returns/view/memberView";
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
		return "/admin/returns/view/orderView";
	}



}