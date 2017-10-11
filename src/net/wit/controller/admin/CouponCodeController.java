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
import net.wit.entity.CouponCode;
import net.wit.service.CouponCodeService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: CouponCodeController
 * @author 降魔战队
 * @date 2017-10-11 15:37:8
 */
 
@Controller("adminCouponCodeController")
@RequestMapping("/admin/couponCode")
public class CouponCodeController extends BaseController {
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("coupons",couponService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/couponCode/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("coupons",couponService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/couponCode/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(CouponCode couponCode, Long couponId, Long memberId){
		CouponCode entity = new CouponCode();	

		entity.setCreateDate(couponCode.getCreateDate());

		entity.setModifyDate(couponCode.getModifyDate());

		entity.setCode(couponCode.getCode());

		entity.setIsUsed(couponCode.getIsUsed());

		entity.setUsedDate(couponCode.getUsedDate());

		entity.setCoupon(couponService.find(couponId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            couponCodeService.save(entity);
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
            couponCodeService.delete(ids);
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

		model.addAttribute("coupons",couponService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("data",couponCodeService.find(id));

		return "/admin/couponCode/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(CouponCode couponCode, Long couponId, Long memberId){
		CouponCode entity = couponCodeService.find(couponCode.getId());
		
		entity.setCreateDate(couponCode.getCreateDate());

		entity.setModifyDate(couponCode.getModifyDate());

		entity.setCode(couponCode.getCode());

		entity.setIsUsed(couponCode.getIsUsed());

		entity.setUsedDate(couponCode.getUsedDate());

		entity.setCoupon(couponService.find(couponId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            couponCodeService.update(entity);
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

		Page<CouponCode> page = couponCodeService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 优惠券视图
	 */
	@RequestMapping(value = "/couponView", method = RequestMethod.GET)
	public String couponView(Long id, ModelMap model) {


		model.addAttribute("coupon",couponService.find(id));
		return "/admin/couponCode/view/couponView";
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
		return "/admin/couponCode/view/memberView";
	}



}