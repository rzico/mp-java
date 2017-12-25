package net.wit.controller.mch;

import net.wit.*;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.service.OccupationService;
import net.wit.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: MemberController
 * @author 降魔战队
 * @date 2017-10-11 15:37:9
 */
 
@Controller("mchMemberController")
@RequestMapping("/mch/member")
public class MemberController extends BaseController {
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

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("occupations",occupationService.findAll());

		model.addAttribute("tags",tagService.findList(Tag.Type.member));

		return "/mch/member/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("occupations",occupationService.findAll());

		model.addAttribute("tags",tagService.findList(Tag.Type.member));

		return "/mch/member/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Member member, Long areaId, Long occupationId, Long [] tagIds){
		Member entity = new Member();	

//		entity.setAddress(member.getAddress());
//
		entity.setBirth(member.getBirth());

		entity.setGender(member.getGender());

		entity.setIsEnabled(member.getIsEnabled());

		entity.setIsLocked(member.getIsLocked());

		entity.setName(member.getName());

//		entity.setPhone(member.getPhone());
//
//		entity.setZipCode(member.getZipCode());
//
		entity.setArea(areaService.find(areaId));

		entity.setAutograph(member.getAutograph());

		entity.setLogo(member.getLogo());

		entity.setNickName(member.getNickName());

		entity.setOccupation(occupationService.find(occupationId));

		entity.setTags(tagService.findList(tagIds));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            memberService.save(entity);
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
            memberService.delete(ids);
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

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("occupations",occupationService.findAll());

		model.addAttribute("tags",tagService.findList(Tag.Type.member));

		model.addAttribute("data",memberService.find(id));

		return "/mch/member/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Member member, Long areaId, Long occupationId, Long [] tagIds){
		Member entity = memberService.find(member.getId());
		
		entity.setCreateDate(member.getCreateDate());

		entity.setModifyDate(member.getModifyDate());

//		entity.setAddress(member.getAddress());
//
//		entity.setAttributeValue0(member.getAttributeValue0());
//
//		entity.setAttributeValue1(member.getAttributeValue1());
//
//		entity.setAttributeValue2(member.getAttributeValue2());
//
//		entity.setAttributeValue3(member.getAttributeValue3());
//
//		entity.setAttributeValue4(member.getAttributeValue4());
//
//		entity.setAttributeValue5(member.getAttributeValue5());
//
//		entity.setAttributeValue6(member.getAttributeValue6());
//
//		entity.setAttributeValue7(member.getAttributeValue7());
//
//		entity.setAttributeValue8(member.getAttributeValue8());
//
		entity.setAttributeValue9(member.getAttributeValue9());

		entity.setBirth(member.getBirth());

//		entity.setEmail(member.getEmail());

		entity.setGender(member.getGender());

		entity.setIsEnabled(member.getIsEnabled());

		entity.setIsLocked(member.getIsLocked());

		entity.setLockedDate(member.getLockedDate());

		entity.setLoginDate(member.getLoginDate());

		entity.setLoginFailureCount(member.getLoginFailureCount() == null ? 0 : member.getLoginFailureCount());

		entity.setLoginIp(member.getLoginIp());

		entity.setName(member.getName());
//
//		entity.setPhone(member.getPhone());
//
//		entity.setPoint(member.getPoint() == null ? 0 : member.getPoint());

		entity.setUsername(member.getUsername());
//
//		entity.setZipCode(member.getZipCode());

		entity.setArea(areaService.find(areaId));

		entity.setAutograph(member.getAutograph());

		entity.setLogo(member.getLogo());

		entity.setNickName(member.getNickName());

		entity.setOccupation(occupationService.find(occupationId));

		entity.setTags(tagService.findList(tagIds));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            memberService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Member.Gender gender, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (gender!=null) {
			Filter genderFilter = new Filter("gender", Filter.Operator.eq, gender);
			filters.add(genderFilter);
		}

		Page<Member> page = memberService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/mch/member/view/areaView";
	}


	/**
	 * 职业管理视图
	 */
	@RequestMapping(value = "/occupationView", method = RequestMethod.GET)
	public String occupationView(Long id, ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("occupation",occupationService.find(id));
		return "/mch/member/view/occupationView";
	}



}