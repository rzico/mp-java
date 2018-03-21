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
import net.wit.entity.Card;
import net.wit.service.CardService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: CardController
 * @author 降魔战队
 * @date 2017-11-4 18:12:32
 */
 
@Controller("adminCardController")
@RequestMapping("/admin/card")
public class CardController extends BaseController {
	@Resource(name = "cardServiceImpl")
	private CardService cardService;
	
	@Resource(name = "shopServiceImpl")
	private ShopService shopService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("none","空卡"));
		statuss.add(new MapEntity("activate","已激活"));
		statuss.add(new MapEntity("loss","已挂失"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> vips = new ArrayList<>();
		vips.add(new MapEntity("vip1","vip1"));
		vips.add(new MapEntity("vip2","vip2"));
		vips.add(new MapEntity("vip3","vip3"));
		model.addAttribute("vips",vips);

		return "/admin/card/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/card/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Card card, Long shopId,Long ownerId){
		Card entity = new Card();	

		entity.setCreateDate(card.getCreateDate());

		entity.setModifyDate(card.getModifyDate());

		entity.setBalance(card.getBalance());

		entity.setCode(card.getCode());

		entity.setMobile(card.getMobile());

		entity.setName(card.getName());

		entity.setSign(card.getSign());

		entity.setUsedDate(card.getUsedDate());

		entity.setOwner(memberService.find(ownerId));

		entity.setShop(shopService.find(shopId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            cardService.save(entity);
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
            cardService.delete(ids);
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
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("none","空卡"));
		statuss.add(new MapEntity("activate","已激活"));
		statuss.add(new MapEntity("loss","已挂失"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("shops",shopService.findAll());

		model.addAttribute("data",cardService.find(id));

		return "/admin/card/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Card card, Long shopId, Long memberId){
		Card entity = cardService.find(card.getId());
		//entity.setCreateDate(card.getCreateDate());
		//entity.setModifyDate(card.getModifyDate());
		//entity.setBalance(card.getBalance());

		entity.setCode(card.getCode());
		entity.setStatus(card.getStatus());
		entity.setMobile(card.getMobile());

		entity.setName(card.getName());
		//entity.setSign(card.getSign());
		//entity.setUsedDate(card.getUsedDate());
		//entity.setOwner(memberService.find(memberId));
		entity.setShop(shopService.find(shopId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            cardService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Card.Status status, Card.VIP vip,String searchValue, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status != null){
			Filter statusFilter = new Filter("status",Filter.Operator.eq,status);
			filters.add(statusFilter);
		}

		if (vip != null){
			Filter vipFilter = new Filter("vip",Filter.Operator.eq,vip);
			filters.add(vipFilter);
		}
		Admin admin =adminService.getCurrent();
//		System.out.println("admin.ID:"+admin.getId());
		Enterprise enterprise=admin.getEnterprise();
//		System.out.println("enter.ID:"+enterprise.getId());

		if(enterprise==null){
			return Message.error("您还未绑定企业");
		}
		//判断企业是否被删除
		if(enterprise.getDeleted()){
			Message.error("您的企业不存在");
		}
		//代理商(無權限)
		//个人代理商(無權限)
		//商家
		if(enterprise.getType()== Enterprise.Type.shop){
			if(enterprise.getMember()!=null){
				Filter mediaTypeFilter = new Filter("owner", Filter.Operator.eq, enterprise.getMember());
//				System.out.println("admin.enter.member.ID:"+enterprise.getMember().getId());
				filters.add(mediaTypeFilter);
			}
			else{
				return Message.error("该商家未绑定");
			}
		}
		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("name", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<Card> page = cardService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * Shop视图
	 */
	@RequestMapping(value = "/shopView", method = RequestMethod.GET)
	public String shopView(Long id, ModelMap model) {


		model.addAttribute("shop",shopService.find(id));
		return "/admin/card/view/shopView";
	}



}