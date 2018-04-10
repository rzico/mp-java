package net.wit.controller.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

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
import net.wit.entity.OrderRanking;
import net.wit.service.OrderRankingService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: OrderRankingController
 * @author 降魔战队
 * @date 2018-4-5 18:22:49
 */
 
@Controller("adminOrderRankingController")
@RequestMapping("/admin/orderRanking")
public class OrderRankingController extends BaseController {
	@Resource(name = "orderRankingServiceImpl")
	private OrderRankingService orderRankingService;
	
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("owners",memberService.findAll());

		return "/admin/orderRanking/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("owners",memberService.findAll());

		return "/admin/orderRanking/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(OrderRanking orderRanking, Long goodsId, Long ownerId, Long memberId){
		OrderRanking entity = new OrderRanking();	

		entity.setCreateDate(orderRanking.getCreateDate());

		entity.setModifyDate(orderRanking.getModifyDate());

		entity.setOrders(orderRanking.getOrders() == null ? 0 : orderRanking.getOrders());

		entity.setAmount(orderRanking.getAmount());

		entity.setName(orderRanking.getName());

		entity.setPoint(orderRanking.getPoint() == null ? 0 : orderRanking.getPoint());

		entity.setSpec(orderRanking.getSpec());

		entity.setGoods(goodsService.find(goodsId));

		entity.setMember(memberService.find(memberId));

		entity.setOwner(memberService.find(ownerId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            orderRankingService.save(entity);
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
            orderRankingService.delete(ids);
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

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("owners",memberService.findAll());

		model.addAttribute("data",orderRankingService.find(id));

		return "/admin/orderRanking/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(OrderRanking orderRanking, Long goodsId, Long ownerId, Long memberId){
		OrderRanking entity = orderRankingService.find(orderRanking.getId());
		
		entity.setCreateDate(orderRanking.getCreateDate());

		entity.setModifyDate(orderRanking.getModifyDate());

		entity.setOrders(orderRanking.getOrders() == null ? 0 : orderRanking.getOrders());

		entity.setAmount(orderRanking.getAmount());

		entity.setName(orderRanking.getName());

		entity.setPoint(orderRanking.getPoint() == null ? 0 : orderRanking.getPoint());

		entity.setSpec(orderRanking.getSpec());

		entity.setGoods(goodsService.find(goodsId));

		entity.setMember(memberService.find(memberId));

		entity.setOwner(memberService.find(ownerId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            orderRankingService.update(entity);
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

		Page<OrderRanking> page = orderRankingService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 商品管理视图
	 */
	@RequestMapping(value = "/goodsView", method = RequestMethod.GET)
	public String goodsView(Long id, ModelMap model) {


		model.addAttribute("goods",goodsService.find(id));
		return "/admin/orderRanking/view/goodsView";
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

		model.addAttribute("topics",topicService.findAll());

		List<MapEntity> vips = new ArrayList<>();
		vips.add(new MapEntity("vip1","vip1"));
		vips.add(new MapEntity("vip2","vip2"));
		vips.add(new MapEntity("vip3","vip3"));
		model.addAttribute("vips",vips);

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/orderRanking/view/memberView";
	}



}