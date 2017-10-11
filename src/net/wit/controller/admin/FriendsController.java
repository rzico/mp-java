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
import net.wit.entity.Friends;
import net.wit.service.FriendsService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: FriendsController
 * @author 降魔战队
 * @date 2017-10-11 15:37:9
 */
 
@Controller("adminFriendsController")
@RequestMapping("/admin/friends")
public class FriendsController extends BaseController {
	@Resource(name = "friendsServiceImpl")
	private FriendsService friendsService;
	
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

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("ask","申请"));
		statuss.add(new MapEntity("adopt","通过"));
		statuss.add(new MapEntity("black","拉黑"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("friends",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("friend","好友"));
		types.add(new MapEntity("customer","会员"));
		types.add(new MapEntity("vip1","一级代理"));
		types.add(new MapEntity("vip2","二级代理"));
		types.add(new MapEntity("vip3","三级代理"));
		model.addAttribute("types",types);

		return "/admin/friends/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("ask","申请"));
		statuss.add(new MapEntity("adopt","通过"));
		statuss.add(new MapEntity("black","拉黑"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("friends",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("friend","好友"));
		types.add(new MapEntity("customer","会员"));
		types.add(new MapEntity("vip1","一级代理"));
		types.add(new MapEntity("vip2","二级代理"));
		types.add(new MapEntity("vip3","三级代理"));
		model.addAttribute("types",types);

		return "/admin/friends/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Friends friends, Long friendId, Long memberId){
		Friends entity = new Friends();	

		entity.setCreateDate(friends.getCreateDate());

		entity.setModifyDate(friends.getModifyDate());

		entity.setStatus(friends.getStatus());

		entity.setFriend(memberService.find(friendId));

		entity.setMember(memberService.find(memberId));

		entity.setType(friends.getType());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            friendsService.save(entity);
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
            friendsService.delete(ids);
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
		statuss.add(new MapEntity("ask","申请"));
		statuss.add(new MapEntity("adopt","通过"));
		statuss.add(new MapEntity("black","拉黑"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("friends",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("friend","好友"));
		types.add(new MapEntity("customer","会员"));
		types.add(new MapEntity("vip1","一级代理"));
		types.add(new MapEntity("vip2","二级代理"));
		types.add(new MapEntity("vip3","三级代理"));
		model.addAttribute("types",types);

		model.addAttribute("data",friendsService.find(id));

		return "/admin/friends/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Friends friends, Long friendId, Long memberId){
		Friends entity = friendsService.find(friends.getId());
		
		entity.setCreateDate(friends.getCreateDate());

		entity.setModifyDate(friends.getModifyDate());

		entity.setStatus(friends.getStatus());

		entity.setFriend(memberService.find(friendId));

		entity.setMember(memberService.find(memberId));

		entity.setType(friends.getType());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            friendsService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Friends.Status status, Friends.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Friends> page = friendsService.findPage(beginDate,endDate,pageable);
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
		return "/admin/friends/view/memberView";
	}



}