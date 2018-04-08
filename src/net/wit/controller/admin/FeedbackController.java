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
import net.wit.entity.Feedback;
import net.wit.service.FeedbackService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: FeedbackController
 * @author 降魔战队
 * @date 2018-3-26 15:13:15
 */
 
@Controller("adminFeedbackController")
@RequestMapping("/admin/feedback")
public class FeedbackController extends BaseController {
	@Resource(name = "feedbackServiceImpl")
	private FeedbackService feedbackService;
	
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

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

//		model.addAttribute("members",memberService.findAll());

		return "/admin/feedback/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("members",memberService.findAll());

		return "/admin/feedback/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Feedback feedback, Long memberId){
		Feedback entity = new Feedback();	

		entity.setCreateDate(feedback.getCreateDate());

		entity.setModifyDate(feedback.getModifyDate());

		entity.setContent(feedback.getContent());

		entity.setSolve(feedback.getSolve());

		entity.setProblemPictrue1(feedback.getProblemPictrue1());

		entity.setProblemPictrue2(feedback.getProblemPictrue2());

		entity.setProblemPictrue3(feedback.getProblemPictrue3());

		entity.setProblemPictrue4(feedback.getProblemPictrue4());

		entity.setProblemPictrue5(feedback.getProblemPictrue5());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            feedbackService.save(entity);
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
            feedbackService.delete(ids);
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

		model.addAttribute("data",feedbackService.find(id));

		return "/admin/feedback/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Long Id,String recontent, Long memberId){

		Feedback entity=feedbackService.find(Id);

		net.wit.entity.Message message=new net.wit.entity.Message();

		message.setContent(recontent);

		message.setType(net.wit.entity.Message.Type.message);

		message.setDeleted(false);

		message.setReceiver(entity.getMember());

		message.setReaded(false);

		message.setTitle("问题反馈回复");

		message.setThumbnial("http://cdn.rzico.com/weex/resources/images/gm_10202.png");

		message.setMember(null);

		message.setSender(null);
		
		if (!isValid(message)) {
            return Message.error("admin.data.valid");
        }
		try {
			messageService.pushTo(message);
			entity.setMessage(message);
			entity.setSolve(true);
		} catch (Exception e) {
			e.printStackTrace();
			return net.wit.Message.error("admin.save.error");
		}
        try {
            feedbackService.update(entity);
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
	public Message list(Date beginDate, Date endDate,String searchValue, Pageable pageable, ModelMap model) {

		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (searchValue!=null) {
			Filter authorityFilter = new Filter("content", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(authorityFilter);
		}
		Page<Feedback> page = feedbackService.findPage(beginDate,endDate,pageable);
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

		model.addAttribute("topics",topicService.findAll());

		List<MapEntity> vips = new ArrayList<>();
		vips.add(new MapEntity("vip1","vip1"));
		vips.add(new MapEntity("vip2","vip2"));
		vips.add(new MapEntity("vip3","vip3"));
		model.addAttribute("vips",vips);

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/feedback/view/memberView";
	}

	/**
	 * 会员管理视图
	 */
	@RequestMapping(value = "/feedbackView", method = RequestMethod.GET)
	public String feedbackView(Long id, ModelMap model) {
		Feedback feedback =feedbackService.find(id);
		MapEntity member=feedback.getMapMember();
		MapEntity message=feedback.getMapMessage();
		model.addAttribute("feedback",feedback);
		model.addAttribute("member",member);
		model.addAttribute("message",message);
		return "/admin/feedback/view/feedbackView";
	}



}