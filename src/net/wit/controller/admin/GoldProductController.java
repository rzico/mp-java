package net.wit.controller.admin;

import java.util.Date;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.entity.GoldProduct;
import net.wit.service.GoldProductService;

import net.wit.*;


/**
 * @ClassName: GoldProductController
 * @author 降魔战队
 * @date 2018-3-25 14:59:8
 */
 
@Controller("adminGoldProductController")
@RequestMapping("/admin/goldProduct")
public class GoldProductController extends BaseController {
	@Resource(name = "goldProductServiceImpl")
	private GoldProductService goldProductService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {



		return "/admin/goldProduct/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/goldProduct/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(GoldProduct goldProduct){
		GoldProduct entity = new GoldProduct();

		entity.setCreateDate(goldProduct.getCreateDate());

		entity.setModifyDate(goldProduct.getModifyDate());

		entity.setGold(goldProduct.getGold() == null ? 0 : goldProduct.getGold());

		entity.setPrice(goldProduct.getPrice());

		entity.setTitle(goldProduct.getTitle());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            goldProductService.save(entity);
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
            goldProductService.delete(ids);
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



		model.addAttribute("data",goldProductService.find(id));

		return "/admin/goldProduct/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(GoldProduct goldProduct){
		GoldProduct entity = goldProductService.find(goldProduct.getId());
		
		entity.setCreateDate(goldProduct.getCreateDate());

		entity.setModifyDate(goldProduct.getModifyDate());

		entity.setGold(goldProduct.getGold() == null ? 0 : goldProduct.getGold());

		entity.setPrice(goldProduct.getPrice());

		entity.setTitle(goldProduct.getTitle());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            goldProductService.update(entity);
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

		Page<GoldProduct> page = goldProductService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}