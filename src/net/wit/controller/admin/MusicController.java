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
import net.wit.entity.Music;
import net.wit.service.MusicService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: MusicController
 * @author 降魔战队
 * @date 2018-7-16 13:0:41
 */
 
@Controller("adminMusicController")
@RequestMapping("/admin/music")
public class MusicController extends BaseController {
	@Resource(name = "musicServiceImpl")
	private MusicService musicService;
	
	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "hostServiceImpl")
	private HostService hostService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;


	@Resource(name = "adminServiceImpl")
	private AdminService adminService;


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("enterprises",enterpriseService.findAll());

		return "/admin/music/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("enterprises",enterpriseService.findAll());

		return "/admin/music/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Music music){
		Admin admin = adminService.getCurrent();
		Music entity = new Music();

		entity.setOrders(music.getOrders() == null ? 0 : music.getOrders());

		entity.setContent(music.getContent());

		entity.setHits(0L);

		entity.setMusicFile(music.getMusicFile());

		entity.setThumbnail(music.getThumbnail());

		entity.setTitle(music.getTitle());

		entity.setEnterprise(admin.getEnterprise());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            musicService.save(entity);
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
            musicService.delete(ids);
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

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("data",musicService.find(id));

		return "/admin/music/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Music music, Long enterpriseId){
		Music entity = musicService.find(music.getId());
		
		entity.setOrders(music.getOrders() == null ? 0 : music.getOrders());

		entity.setContent(music.getContent());

		entity.setMusicFile(music.getMusicFile());

		entity.setThumbnail(music.getThumbnail());

		entity.setTitle(music.getTitle());

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            musicService.update(entity);
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
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		Admin admin =adminService.getCurrent();

		//非超级管理员都只能管本企业用户
		if (!admin.isManager()) {
			filters.add(new Filter("enterprise", Filter.Operator.eq, admin.getEnterprise()));
		}

		Page<Music> page = musicService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}

}