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
import net.wit.entity.Bankcard;
import net.wit.service.BankcardService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: BankcardController
 * @author 降魔战队
 * @date 2017-10-20 17:56:10
 */
 
@Controller("adminBankcardController")
@RequestMapping("/admin/bankcard")
public class BankcardController extends BaseController {
	@Resource(name = "bankcardServiceImpl")
	private BankcardService bankcardService;
	
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



		return "/admin/bankcard/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/bankcard/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Bankcard bankcard, Long memberId){
		Bankcard entity = new Bankcard();	

		entity.setCreateDate(bankcard.getCreateDate());

		entity.setModifyDate(bankcard.getModifyDate());

		entity.setBankimage(bankcard.getBankimage());

		entity.setBankname(bankcard.getBankname());

		entity.setBanknum(bankcard.getBanknum());

		entity.setCardname(bankcard.getCardname());

		entity.setCardno(bankcard.getCardno());

		entity.setCardtype(bankcard.getCardtype());

		entity.setCity(bankcard.getCity());

		entity.setIdentity(bankcard.getIdentity());

		entity.setMobile(bankcard.getMobile());

		entity.setName(bankcard.getName());

		entity.setProvince(bankcard.getProvince());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            bankcardService.save(entity);
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
            bankcardService.delete(ids);
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



		model.addAttribute("data",bankcardService.find(id));

		return "/admin/bankcard/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Bankcard bankcard, Long memberId){
		Bankcard entity = bankcardService.find(bankcard.getId());
		
		entity.setCreateDate(bankcard.getCreateDate());

		entity.setModifyDate(bankcard.getModifyDate());

		entity.setBankimage(bankcard.getBankimage());

		entity.setBankname(bankcard.getBankname());

		entity.setBanknum(bankcard.getBanknum());

		entity.setCardname(bankcard.getCardname());

		entity.setCardno(bankcard.getCardno());

		entity.setCardtype(bankcard.getCardtype());

		entity.setCity(bankcard.getCity());

		entity.setIdentity(bankcard.getIdentity());

		entity.setMobile(bankcard.getMobile());

		entity.setName(bankcard.getName());

		entity.setProvince(bankcard.getProvince());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            bankcardService.update(entity);
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

		Page<Bankcard> page = bankcardService.findPage(beginDate,endDate,pageable);
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
		return "/admin/bankcard/view/memberView";
	}



}