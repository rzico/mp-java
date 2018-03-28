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
 * @date 2017-10-11 15:37:10
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

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

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

		return "/admin/message/add";
	}

	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public net.wit.Message save(net.wit.entity.Message message,String mobile){
		net.wit.entity.Message entity = null;
		String [] ms = mobile.split(",");
		String s="";
		int w=0;
		for (String m:ms) {
			Member member = memberService.findByMobile(m);
			if (member!=null) {
				w = w + 1;
                entity =  new net.wit.entity.Message();

				entity.setContent(message.getContent());

				entity.setReaded(false);

				entity.setTitle(message.getTitle());

				entity.setType(message.getType());

				entity.setMember(null);

				entity.setReceiver(member);

				entity.setThumbnial(message.getThumbnial());

				entity.setDeleted(false);

				if (!isValid(entity)) {
					return net.wit.Message.error("admin.data.valid");
				}
				try {
					messageService.pushTo(entity);
				} catch (Exception e) {
					e.printStackTrace();
					return net.wit.Message.error("admin.save.error");
				}
			} else {
				s = s + ","+m;
			}
		}
		if (w==ms.length) {
			return net.wit.Message.success(entity, "发送成功");
		}
		if (w==0) {
			return net.wit.Message.error("发送失败");
		}
		return net.wit.Message.error(s+",部份发送失败");
	}

	/**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
	net.wit.Message delete(Long[] ids) {
        try {
            messageService.delete(ids);
            return net.wit.Message.success("admin.delete.success");
        } catch (Exception e) {
            e.printStackTrace();
            return net.wit.Message.error("admin.delete.error");
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

		model.addAttribute("data",messageService.find(id));

		return "/admin/message/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public net.wit.Message update(net.wit.entity.Message message, Long memberId, Long receiverId){
		net.wit.entity.Message entity = messageService.find(message.getId());
		
		entity.setCreateDate(message.getCreateDate());

		entity.setModifyDate(message.getModifyDate());

		entity.setContent(message.getContent());

		entity.setReaded(message.getReaded());

		entity.setThumbnial(message.getThumbnial());

		entity.setTitle(message.getTitle());

		entity.setType(message.getType());

		entity.setMember(memberService.find(memberId));

		entity.setReceiver(memberService.find(receiverId));

		entity.setDeleted(message.getDeleted());
		
		if (!isValid(entity)) {
            return net.wit.Message.error("admin.data.valid");
        }
        try {
            messageService.update(entity);
            return net.wit.Message.success(entity,"admin.update.success");
        } catch (Exception e) {
            e.printStackTrace();
            return net.wit.Message.error("admin.update.error");
        }
	}
	

	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public net.wit.Message list(Date beginDate, Date endDate, net.wit.entity.Message.Type type, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<net.wit.entity.Message> page = messageService.findPage(beginDate,endDate,pageable);
		return net.wit.Message.success(PageBlock.bind(page), "admin.list.success");
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
		return "/admin/message/view/memberView";
	}



}