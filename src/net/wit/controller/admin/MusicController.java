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
	public Message save(Music music, Long enterpriseId){
		Music entity = new Music();	

		entity.setCreateDate(music.getCreateDate());

		entity.setModifyDate(music.getModifyDate());

		entity.setOrders(music.getOrders() == null ? 0 : music.getOrders());

		entity.setContent(music.getContent());

		entity.setHits(music.getHits() == null ? 0 : music.getHits());

		entity.setThumbnail(music.getThumbnail());

		entity.setTitle(music.getTitle());

		entity.setEnterprise(enterpriseService.find(enterpriseId));
		
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
		
		entity.setCreateDate(music.getCreateDate());

		entity.setModifyDate(music.getModifyDate());

		entity.setOrders(music.getOrders() == null ? 0 : music.getOrders());

		entity.setContent(music.getContent());

		entity.setHits(music.getHits() == null ? 0 : music.getHits());

		entity.setThumbnail(music.getThumbnail());

		entity.setTitle(music.getTitle());

		entity.setEnterprise(enterpriseService.find(enterpriseId));
		
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

		Page<Music> page = musicService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
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

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","待审核"));
		statuss.add(new MapEntity("success","已审核"));
		statuss.add(new MapEntity("failure","已关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("hosts",hostService.findAll());

		model.addAttribute("enterprise",enterpriseService.find(id));
		return "/admin/music/view/enterpriseView";
	}



}