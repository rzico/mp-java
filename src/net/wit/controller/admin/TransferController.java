package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.plat.unspay.UnsPay;
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
import net.wit.entity.Transfer;
import net.wit.service.TransferService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: TransferController
 * @author 降魔战队
 * @date 2017-10-17 21:43:40
 */
 
@Controller("adminTransferController")
@RequestMapping("/admin/transfer")
public class TransferController extends BaseController {
	@Resource(name = "transferServiceImpl")
	private TransferService transferService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","等待审核"));
		statuss.add(new MapEntity("confirmed","审核提交"));
		statuss.add(new MapEntity("success","提现成功"));
		statuss.add(new MapEntity("failure","提现失败"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("weixin","微信钱包"));
		types.add(new MapEntity("alipay","支付宝"));
		types.add(new MapEntity("bankcard","银行卡"));
		model.addAttribute("types",types);

		return "/admin/transfer/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","等待审核"));
		statuss.add(new MapEntity("confirmed","审核提交"));
		statuss.add(new MapEntity("success","提现成功"));
		statuss.add(new MapEntity("failure","提现失败"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("weixin","微信钱包"));
		types.add(new MapEntity("alipay","支付宝"));
		types.add(new MapEntity("bankcard","银行卡"));
		model.addAttribute("types",types);

		return "/admin/transfer/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Transfer transfer,Long memberId){
		Transfer entity = new Transfer();	

		entity.setCreateDate(transfer.getCreateDate());

		entity.setModifyDate(transfer.getModifyDate());

		entity.setAmount(transfer.getAmount());

		entity.setMemo(transfer.getMemo());

		entity.setOperator(transfer.getOperator());

		entity.setSn(transfer.getSn());

		entity.setStatus(transfer.getStatus());

		entity.setType(transfer.getType());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            transferService.save(entity);
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
            transferService.delete(ids);
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
		statuss.add(new MapEntity("waiting","等待审核"));
		statuss.add(new MapEntity("confirmed","审核提交"));
		statuss.add(new MapEntity("success","提现成功"));
		statuss.add(new MapEntity("failure","提现失败"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("weixin","微信钱包"));
		types.add(new MapEntity("alipay","支付宝"));
		types.add(new MapEntity("bankcard","银行卡"));
		model.addAttribute("types",types);

		model.addAttribute("data",transferService.find(id));

		return "/admin/transfer/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Transfer transfer,Long memberId){
		Transfer entity = transferService.find(transfer.getId());
        try {
			if (entity.getStatus().equals(Transfer.Status.waiting)) {
				if (transferService.transfer(entity)) {
					return Message.success(entity,"提交成功");
				} else {
					return Message.error("提交失败");
				}
			} else {
			    String resp = UnsPay.query(entity.getSn());
			    if ("00".equals(resp)) {
					transferService.handle(entity);
					return Message.success(entity,"提现成功");
				} else
				if ("20".equals(resp)) {
					transferService.refunds(entity);
					return Message.success(entity,"提现失败,款项退回账号");
				} else
				if ("10".equals(resp)) {
					return Message.success(entity,"正在处理中");
				} else {
					return Message.error("查询失败");
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
	}
	

	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Date beginDate, Date endDate, Transfer.Status status, Transfer.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Transfer> page = transferService.findPage(beginDate,endDate,pageable);
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
		return "/admin/transfer/view/memberView";
	}

	/**
	 * 申请手动转账
	 */
	@RequestMapping(value = "/manualTransfer", method = RequestMethod.GET)
	public String manualTransfer(ModelMap model,Long id) {
		model.addAttribute("data",transferService.find(id));

		return "/admin/transfer/view/manualTransfer";
	}

	/**
	 * 提交手动转账
	 */
	@RequestMapping(value = "/manualTransferSave", method = RequestMethod.POST)
	@ResponseBody
	public Message manualTransferSave(String voucher, BigDecimal amount, Long Id){
		if("".equals(voucher)){
			return Message.error("凭证号不能为空!");
		}

		Transfer entity = transferService.find(Id);
		if(amount.compareTo(entity.getAmount()) != 0){
			return Message.error("汇款金额不正确,请重新填写!");
		}
		Member member = memberService.getCurrent();
		entity.setVoucher(voucher);
		entity.setStatus(Transfer.Status.success);
		if(member == null){
			entity.setOperator("系统操作员");
		}else{
			entity.setOperator(member.getName());
		}

		try {
			transferService.update(entity);
			return Message.success(entity,"正在处理中");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error(e.getMessage());
		}
	}

}