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
import net.wit.service.MessageService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: MessageController
 * @author 降魔战队
 * @date 2017-9-14 19:42:14
 */
 
@Controller("adminMessageController")
@RequestMapping("/admin/message")
public class MessageController extends BaseController {
	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("order","订单提醒"));
		types.add(new MapEntity("account","账单提醒"));
		types.add(new MapEntity("message","系统消息"));
		types.add(new MapEntity("review","评论回复"));
		types.add(new MapEntity("laud","点赞提醒"));
		types.add(new MapEntity("follow","关注提醒"));
		types.add(new MapEntity("favorite","收藏提醒"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("receivers",memberService.findAll());

		return "/admin/message/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("order","订单提醒"));
		types.add(new MapEntity("account","账单提醒"));
		types.add(new MapEntity("message","系统消息"));
		types.add(new MapEntity("review","评论回复"));
		types.add(new MapEntity("laud","点赞提醒"));
		types.add(new MapEntity("follow","关注提醒"));
		types.add(new MapEntity("favorite","收藏提醒"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("receivers",memberService.findAll());

		return "/admin/message/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(net.wit.entity.Message message, Long receiverId, Long memberId){
		net.wit.entity.Message entity = new net.wit.entity.Message();

		entity.setCreateDate(message.getCreateDate());

		entity.setModifyDate(message.getModifyDate());

		entity.setContent(message.getContent());

		entity.setDeleted(false);

		entity.setReaded(message.getReaded());

		entity.setThumbnial(message.getThumbnial());

		entity.setTitle(message.getTitle());

		entity.setType(message.getType());

		entity.setMember(memberService.find(memberId));

		entity.setReceiver(memberService.find(receiverId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            messageService.save(entity);
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
            messageService.delete(ids);
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

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("order","订单提醒"));
		types.add(new MapEntity("account","账单提醒"));
		types.add(new MapEntity("message","系统消息"));
		types.add(new MapEntity("review","评论回复"));
		types.add(new MapEntity("laud","点赞提醒"));
		types.add(new MapEntity("follow","关注提醒"));
		types.add(new MapEntity("favorite","收藏提醒"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("receivers",memberService.findAll());

		model.addAttribute("data",messageService.find(id));

		return "/admin/message/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(net.wit.entity.Message message, Long receiverId, Long memberId){
		net.wit.entity.Message entity = messageService.find(message.getId());
		
		entity.setCreateDate(message.getCreateDate());

		entity.setModifyDate(message.getModifyDate());

		entity.setContent(message.getContent());

		entity.setDeleted(false);

		entity.setReaded(message.getReaded());

		entity.setThumbnial(message.getThumbnial());

		entity.setTitle(message.getTitle());

		entity.setType(message.getType());

		entity.setMember(memberService.find(memberId));

		entity.setReceiver(memberService.find(receiverId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            messageService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Message.Type type, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<net.wit.entity.Message> page = messageService.findPage(beginDate,endDate,pageable);
		return Message.success(PageModel.bind(page), "admin.list.success");
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

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/message/view/memberView";
	}



}