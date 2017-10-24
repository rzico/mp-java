package net.wit.controller.mch;

import net.wit.Message;
import net.wit.Page;
import net.wit.PageBlock;
import net.wit.Pageable;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Coupon;
import net.wit.service.CouponService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @ClassName: CouponController
 * @author 降魔战队
 * @date 2017-10-11 15:37:8
 */
 
@Controller("mchCouponController")
@RequestMapping("/mch/coupon")
public class CouponController extends BaseController {
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {



		return "/admin/coupon/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/coupon/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Coupon coupon){
		Coupon entity = new Coupon();	

		entity.setCreateDate(coupon.getCreateDate());

		entity.setModifyDate(coupon.getModifyDate());

		entity.setAmount(coupon.getAmount());

		entity.setBeginDate(coupon.getBeginDate());

		entity.setDeleted(coupon.getDeleted());

		entity.setEndDate(coupon.getEndDate());

		entity.setIntroduction(coupon.getIntroduction());

		entity.setIsEnabled(coupon.getIsEnabled());

		entity.setMinimumPrice(coupon.getMinimumPrice());

		entity.setName(coupon.getName());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            couponService.save(entity);
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
            couponService.delete(ids);
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



		model.addAttribute("data",couponService.find(id));

		return "/admin/coupon/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Coupon coupon){
		Coupon entity = couponService.find(coupon.getId());
		
		entity.setCreateDate(coupon.getCreateDate());

		entity.setModifyDate(coupon.getModifyDate());

		entity.setAmount(coupon.getAmount());

		entity.setBeginDate(coupon.getBeginDate());

		entity.setDeleted(coupon.getDeleted());

		entity.setEndDate(coupon.getEndDate());

		entity.setIntroduction(coupon.getIntroduction());

		entity.setIsEnabled(coupon.getIsEnabled());

		entity.setMinimumPrice(coupon.getMinimumPrice());

		entity.setName(coupon.getName());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            couponService.update(entity);
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

		Page<Coupon> page = couponService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}