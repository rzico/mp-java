package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.ueditor.MyJsonManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Filters;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.json.JSONObject;

import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Merchant;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: MerchantController
 * @author 降魔战队
 * @date 2018-1-10 16:2:46
 */
 
@Controller("adminMerchantController")
@RequestMapping("/admin/merchant")
public class MerchantController extends BaseController {
	@Resource(name = "merchantServiceImpl")
	private MerchantService merchantService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "bankcardServiceImpl")
	private BankcardService bankcardService;

	@Resource(name = "shopServiceImpl")
	private ShopService shopService;

	@Resource(name = "categoryServiceImpl")
	private CategoryService categoryService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("categorys",categoryService.findAll());

		return "/admin/merchant/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model, HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		JSONObject jsonObject = null;
		MyJsonManager myJsonManager = MyJsonManager.getInstance(rootPath+"WEB-INF/classes/","province.json");
		jsonObject = myJsonManager.getAllConfig();
		List<MapEntity> provinces = new ArrayList<>();
		JSONArray e = jsonObject.getJSONArray("province");
		for(int i=0;i < e.length();i++){
			provinces.add(new MapEntity(e.getJSONObject(i).getString("id"),e.getJSONObject(i).getString("name")));
		}
		model.addAttribute("provinces",provinces);

		MyJsonManager myJsonManagerCity = MyJsonManager.getInstance(rootPath+"WEB-INF/classes/","city.json");
		jsonObject = myJsonManagerCity.getAllConfig();
		List<MapEntity> citys = new ArrayList<>();
		JSONArray c = jsonObject.getJSONArray("city");
		for(int i=0;i < c.length();i++){
			citys.add(new MapEntity(c.getJSONObject(i).getString("id"),c.getJSONObject(i).getString("name")));
		}
		model.addAttribute("citys",citys);

		model.addAttribute("categorys",categoryService.findAll());

		return "/admin/merchant/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Merchant merchant, Long ownerId, Long enterpriseId){
		Merchant entity = new Merchant();	

		entity.setCreateDate(merchant.getCreateDate());

		entity.setModifyDate(merchant.getModifyDate());

		entity.setAddress(merchant.getAddress());

		entity.setBankName(merchant.getBankName());

		entity.setBranchBankName(merchant.getBranchBankName());

		entity.setBrokerage(new BigDecimal(0.38));

		entity.setCardCity(merchant.getCardCity());

		entity.setCardNo(merchant.getCardNo());

		entity.setCardProvince(merchant.getCardProvince());

		entity.setCity(merchant.getCity());

		entity.setEmail(merchant.getEmail());

		entity.setIdCard(merchant.getIdCard());

		entity.setIndustryType(merchant.getIndustryType());

		entity.setLicenseNo(merchant.getLicenseNo());

		entity.setMerchantName(merchant.getMerchantName());

		entity.setMerchantNo(merchant.getMerchantNo());

		entity.setPhone(merchant.getPhone());

		entity.setProvince(merchant.getProvince());

		entity.setScompany(merchant.getScompany());

		entity.setUserId(merchant.getUserId());

		if(enterpriseId != null){
			entity.setEnterprise(enterpriseService.find(enterpriseId));
		}

		if(ownerId != null){
			entity.setOwner(memberService.find(ownerId));
		}

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
			net.wit.plat.unspay.Merchant.addMerchant(entity);
			if (entity.getMerchantNo() != ""){
				merchantService.save(entity);
				return Message.success(entity,"admin.save.success");
			}else{
				merchantService.save(entity);
				return Message.success(entity,"验签不通过");
			}
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
            merchantService.delete(ids);
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
	public String edit(Long id, ModelMap model, HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		JSONObject jsonObject = null;
		MyJsonManager myJsonManager = MyJsonManager.getInstance(rootPath+"WEB-INF/classes/","province.json");
		jsonObject = myJsonManager.getAllConfig();
		List<MapEntity> provinces = new ArrayList<>();
		JSONArray e = jsonObject.getJSONArray("province");
		for(int i=0;i < e.length();i++){
			provinces.add(new MapEntity(e.getJSONObject(i).getString("id"),e.getJSONObject(i).getString("name")));
		}
		model.addAttribute("provinces",provinces);

		MyJsonManager myJsonManagerCity = MyJsonManager.getInstance(rootPath+"WEB-INF/classes/","city.json");
		jsonObject = myJsonManagerCity.getAllConfig();
		List<MapEntity> citys = new ArrayList<>();
		JSONArray c = jsonObject.getJSONArray("city");
		for(int i=0;i < c.length();i++){
			citys.add(new MapEntity(c.getJSONObject(i).getString("id"),c.getJSONObject(i).getString("name")));
		}
		model.addAttribute("citys",citys);

		model.addAttribute("categorys",categoryService.findAll());

		model.addAttribute("data",merchantService.find(id));

		return "/admin/merchant/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Merchant merchant, Long ownerId, Long enterpriseId){
		Merchant entity = merchantService.find(merchant.getId());
		
		//entity.setCreateDate(merchant.getCreateDate());

		//entity.setModifyDate(merchant.getModifyDate());

		entity.setAddress(merchant.getAddress());

		entity.setBankName(merchant.getBankName());

		entity.setBranchBankName(merchant.getBranchBankName());

		//entity.setBrokerage(new BigDecimal(0.38));

		entity.setCardCity(merchant.getCardCity());

		entity.setCardNo(merchant.getCardNo());

		entity.setCardProvince(merchant.getCardProvince());

		entity.setCity(merchant.getCity());

		entity.setEmail(merchant.getEmail());

		entity.setIdCard(merchant.getIdCard());

		entity.setIndustryType(merchant.getIndustryType());

		entity.setLicenseNo(merchant.getLicenseNo());

		entity.setMerchantName(merchant.getMerchantName());

		entity.setMerchantNo(merchant.getMerchantNo());

		entity.setPhone(merchant.getPhone());

		entity.setProvince(merchant.getProvince());

		entity.setScompany(merchant.getScompany());

		entity.setUserId(merchant.getUserId());

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setOwner(memberService.find(ownerId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
			net.wit.plat.unspay.Merchant.editMerchant(entity);
			if (entity.getMerchantNo() != ""){
				merchantService.update(entity);
				return Message.success(entity,"admin.save.success");
			}else{
				merchantService.update(entity);
				return Message.success(entity,"验签不通过");
			}
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
		Admin admin =adminService.getCurrent();
		Enterprise enterprise=admin.getEnterprise();
		if(enterprise==null){
			return Message.error("您还未绑定企业");
		}
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		//判断企业是否被删除
		if(enterprise.getDeleted()){
			Message.error("您的企业不存在");
		}
		//代理商
		if(enterprise.getType()== Enterprise.Type.shop){
			if(enterprise.getMember()!=null){
				Filter mediaTypeFilter = new Filter("owner", Filter.Operator.eq, enterprise.getMember());
				filters.add(mediaTypeFilter);
			}
			else{
				return Message.error("该商家未绑定");
			}
		}
		//个人代理商(無權限)
		//商家(無權限)

		Page<Merchant> page = merchantService.findPage(beginDate,endDate,pageable);
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

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/merchant/view/memberView";
	}


	/**
	 * 企业管理视图
	 */
	@RequestMapping(value = "/enterpriseView", method = RequestMethod.GET)
	public String enterpriseView(Long id, ModelMap model) {
		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("operate","运营商"));
		types.add(new MapEntity("agent","代理商"));
		model.addAttribute("types",types);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("enterprise",enterpriseService.find(id));
		return "/admin/merchant/view/enterpriseView";
	}

	/**
	 * 通过会员手机号调取会员信息
	 */
	@RequestMapping(value = "/getMemberInfo", method = RequestMethod.GET)
	@ResponseBody
	public Message getMemberInfo(String mobile){
		Map<String,String> memberInfo = new HashMap<String,String>();
		try {
			Member member = memberService.findByMobile(mobile);
			if(member != null){
				memberInfo.put("memberid",member.getId().toString());
				memberInfo.put("membername",member.getName() != ""?member.getName():member.getNickName());
				List<Filter> filters = new ArrayList<>();
				filters.add(new Filter("member", Filter.Operator.eq,member));
				List<Enterprise> enterprises = enterpriseService.findList(null,null,filters,null);
				if(enterprises != null) {
					memberInfo.put("enterpriseid", enterprises.get(0).getId().toString());
					memberInfo.put("enterprisename", enterprises.get(0).getName());
				}else{
					memberInfo.put("enterpriseid", "");
					memberInfo.put("enterprisename", "");
				}

				List<Bankcard> bankcards = bankcardService.findList(null,null,filters,null);
				if(bankcards != null) {
					memberInfo.put("identity", bankcards.get(0).getIdentity());
					memberInfo.put("province", bankcards.get(0).getProvince());
					memberInfo.put("city", bankcards.get(0).getCity());
					memberInfo.put("bankname", bankcards.get(0).getBankname());
					memberInfo.put("cardno", bankcards.get(0).getCardno());
					memberInfo.put("name", bankcards.get(0).getName());
				}else{
					memberInfo.put("identity", "");
					memberInfo.put("province", "");
					memberInfo.put("city", "");
					memberInfo.put("bankname", "");
					memberInfo.put("cardno", "");
					memberInfo.put("name", "");
				}

				filters.clear();
				filters.add(new Filter("owner",Filter.Operator.eq,member));
				List<Shop> shops = shopService.findList(null,null,filters,null);
				if(shops != null) {
					memberInfo.put("shopid", shops.get(0).getId().toString());
					memberInfo.put("shopname", shops.get(0).getName());
					memberInfo.put("area", shops.get(0).getArea().toString());
					memberInfo.put("license_code", shops.get(0).getLicenseCode());
					memberInfo.put("address", shops.get(0).getAddress());
				}else{
					memberInfo.put("shopid", "");
					memberInfo.put("shopname", "");
					memberInfo.put("area", "");
					memberInfo.put("license_code", "");
					memberInfo.put("address", "");
				}
				return Message.success(memberInfo,"admin.update.success");
			}
			return Message.error("admin.update.error");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.update.error");
		}
	}

}