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
import net.wit.entity.Smssend;
import net.wit.service.SmssendService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: SmssendController
 * @author 降魔战队
 * @date 2017-10-11 15:37:15
 */
 
@Controller("adminSmssendController")
@RequestMapping("/admin/smssend")
public class SmssendController extends BaseController {
	@Resource(name = "smssendServiceImpl")
	private SmssendService smssendService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待"));
		statuss.add(new MapEntity("send","成功"));
		statuss.add(new MapEntity("Error","失败"));
		model.addAttribute("statuss",statuss);

		return "/admin/smssend/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待"));
		statuss.add(new MapEntity("send","成功"));
		statuss.add(new MapEntity("Error","失败"));
		model.addAttribute("statuss",statuss);

		return "/admin/smssend/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Smssend smssend){
		Smssend entity = new Smssend();	

		entity.setContent(smssend.getContent());

		entity.setFee(new BigDecimal(0.1));

		entity.setMobile(smssend.getMobile());

		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            smssendService.smsSend(entity);
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
            smssendService.delete(ids);
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
		statuss.add(new MapEntity("wait","等待"));
		statuss.add(new MapEntity("send","成功"));
		statuss.add(new MapEntity("Error","失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("data",smssendService.find(id));

		return "/admin/smssend/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Smssend smssend){
		Smssend entity = smssendService.find(smssend.getId());

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            smssendService.update(entity);
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
	public Message list(Date beginDate, Date endDate,Pageable pageable,String searchValue, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("mobile", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<Smssend> page = smssendService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}