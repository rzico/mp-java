package net.wit.controller.admin;

import java.math.BigDecimal;
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
import net.wit.entity.Enterprise;
import net.wit.service.EnterpriseService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;

/**
 * @ClassName: EnterpriseController
 * @author 降魔战队
 * @date 2017-10-11 15:37:9
 */
 
@Controller("adminEnterpriseController")
@RequestMapping("/admin/enterprise")
public class EnterpriseController extends BaseController {

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("operate","代理商"));
		types.add(new MapEntity("agent","合作商"));
		types.add(new MapEntity("personal","推广员"));
		types.add(new MapEntity("shop","商户"));
		model.addAttribute("types",types);

//		model.addAttribute("areas",areaService.findAll());

		return "/admin/enterprise/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("operate","代理商"));
		types.add(new MapEntity("agent","合作商"));
		types.add(new MapEntity("personal","推广员"));
		types.add(new MapEntity("shop","商户"));
		model.addAttribute("types",types);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","待审核"));
		statuss.add(new MapEntity("success","已审核"));
		statuss.add(new MapEntity("failure","已关闭"));
		model.addAttribute("statuss",statuss);

		return "/admin/enterprise/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(String name,String logo,BigDecimal brokerage,BigDecimal transfer,String type,String status, Long areaId,Long memberId){
		Member member = memberService.find(memberId);
		if (member==null) {
			return Message.error("请正确输入会员");
		}
		ArrayList<Filter> filters = new ArrayList<>();
		Filter memberFilter = new Filter("member", Filter.Operator.eq, member);
		filters.add(memberFilter);

		List<Enterprise> ens = enterpriseService.findList(null,null,filters,null);
		if (ens.size()>0) {
			return Message.error("已经存在企业了");
		}

		Enterprise entity = new Enterprise();
		//entity.setCreateDate(enterprise.getCreateDate());
		//entity.setModifyDate(enterprise.getModifyDate());
		entity.setName(name);

		entity.setPhone(member.getMobile());

		entity.setLogo(logo);

		entity.setDeleted(false);

		entity.setCreditLine(BigDecimal.ZERO);

		entity.setBrokerage(brokerage);

		entity.setTransfer(transfer);

		entity.setType(Enterprise.Type.operate);

		entity.setStatus(Enterprise.Status.success);

		entity.setArea(areaService.find(areaId));

		entity.setMember(member);

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
		    Enterprise enterprise =	enterpriseService.addCreate(entity,member);

			if (member!=null) {
				Topic topic = member.getTopic();
				if (topic==null) {
					topicService.create(topic);
				}
				if (topic != null) {
					topic.setName(enterprise.getName());
					if (enterprise.getStatus().equals(Enterprise.Status.success)) {
						topic.setStatus(Topic.Status.success);
					} else if (enterprise.getStatus().equals(Enterprise.Status.failure)) {
						topic.setStatus(Topic.Status.failure);
					} else {
						topic.setStatus(Topic.Status.waiting);
					}
					topicService.update(topic);
				}
			}

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
            enterpriseService.delete(ids);
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
		types.add(new MapEntity("operate","代理商"));
		types.add(new MapEntity("agent","合作商"));
		types.add(new MapEntity("personal","推广员"));
 		types.add(new MapEntity("shop","商户"));
		model.addAttribute("types",types);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","待审核"));
		statuss.add(new MapEntity("success","已审核"));
		statuss.add(new MapEntity("failure","已关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("data",enterpriseService.find(id));

		return "/admin/enterprise/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Enterprise enterprise, Long areaId){
		Enterprise entity = enterpriseService.find(enterprise.getId());
		//entity.setCreateDate(enterprise.getCreateDate());
		//entity.setModifyDate(enterprise.getModifyDate());
		entity.setLogo(enterprise.getLogo());
		entity.setName(enterprise.getName());

		entity.setPhone(enterprise.getPhone());
		entity.setLinkman(enterprise.getLinkman());

		entity.setTransfer(enterprise.getTransfer());

		entity.setBrokerage(enterprise.getBrokerage());

		entity.setStatus(enterprise.getStatus());

		entity.setArea(areaService.find(areaId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            enterpriseService.update(entity);

            Member member = enterprise.getMember();
            if (member!=null) {
				Topic topic = member.getTopic();
				if (topic==null) {
					topicService.create(topic);
				}
				if (topic != null) {
					topic.setName(enterprise.getName());
					if (enterprise.getStatus().equals(Enterprise.Status.success)) {
						topic.setStatus(Topic.Status.success);
					} else if (enterprise.getStatus().equals(Enterprise.Status.failure)) {
						topic.setStatus(Topic.Status.failure);
					} else {
						topic.setStatus(Topic.Status.waiting);
					}
					topicService.update(topic);
				}
			}
            return Message.success(entity,"admin.update.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.update.error");
        }

	}


	/**
	 * 申请手动转账
	 */
	@RequestMapping(value = "/creditLine", method = RequestMethod.GET)
	public String manualTransfer(ModelMap model,Long id) {
		model.addAttribute("data",enterpriseService.find(id));

		return "/admin/enterprise/view/creditline";
	}

	/**
	 * 授信
	 */
	@RequestMapping(value = "/creditLine", method = RequestMethod.POST)
	@ResponseBody
	public Message creditLine(Long id,BigDecimal amount){
		Enterprise entity = enterpriseService.find(id);
		try {
			enterpriseService.creditLine(entity,amount,adminService.getCurrent());
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
	public Message list(Date beginDate, Date endDate, Enterprise.Type type,Enterprise.Status status,String searchValue, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}
//		Admin admin =adminService.getCurrent();
//		Enterprise enterprise=admin.getEnterprise();
//
//		if(enterprise==null){
//			return Message.error("您还未绑定企业");
//		}
		//判断企业是否被删除
//		if(enterprise.getDeleted()){
//			Message.error("您的企业不存在");
//		}
		//代理商
		//个人代理商(無權限)
		//商家(無權限)
//		if(enterprise.getType()== Enterprise.Type.agent){
//			if(enterprise.getMember()!=null){
//				Filter mediaTypeFilter = new Filter("area", Filter.Operator.eq, enterprise.getArea());
//				filters.add(mediaTypeFilter);
//			}
//			else{
//				return Message.error("该商家未绑定");
//			}
//		}
		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("name", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<Enterprise> page = enterpriseService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {
		model.addAttribute("area",areaService.find(id));

		return "/admin/enterprise/view/areaView";
	}



	/**
	 * 通过会员手机号调取会员信息
	 */
	@RequestMapping(value = "/getMemberInfo", method = RequestMethod.GET)
	@ResponseBody
	public Message getMemberInfo(String phone){
		try {
			Member member = memberService.findByMobile(phone);
			if(member != null){
				List<MapEntity> memberinfo = new ArrayList<>();
				memberinfo.add(new MapEntity("name",member.getName()));
				memberinfo.add(new MapEntity("mobile",member.getMobile()));
				memberinfo.add(new MapEntity("email",member.getUsername()));
				memberinfo.add(new MapEntity("id",member.getId().toString()));
				return Message.success(memberinfo,"admin.update.success");
			}else{
				return Message.error("admin.update.error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.update.error");
		}
	}

}