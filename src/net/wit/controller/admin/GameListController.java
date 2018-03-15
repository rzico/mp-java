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
import net.wit.entity.GameList;
import net.wit.service.GameListService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: GameListController
 * @author 降魔战队
 * @date 2018-3-15 15:35:39
 */
 
@Controller("adminGameListController")
@RequestMapping("/admin/gameList")
public class GameListController extends BaseController {
	@Resource(name = "gameListServiceImpl")
	private GameListService gameListService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {


		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开通"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("nihtan","真人游戏"));
		types.add(new MapEntity("kage","电子游戏"));
		model.addAttribute("types",types);


		return "/admin/gameList/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {




		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开通"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("nihtan","真人游戏"));
		types.add(new MapEntity("kage","电子游戏"));
		model.addAttribute("types",types);

		return "/admin/gameList/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(GameList gameList){
		GameList entity = new GameList();

		entity.setCreateDate(gameList.getCreateDate());

		entity.setModifyDate(gameList.getModifyDate());

		entity.setOrders(gameList.getOrders() == null ? 0 : gameList.getOrders());

		entity.setGame(gameList.getGame());

		entity.setLogo(gameList.getLogo());

		entity.setMemo(gameList.getMemo());

		entity.setName(gameList.getName());

		entity.setRanges(gameList.getRanges());

		entity.setStatus(gameList.getStatus() == null ? GameList.Status.enabled : gameList.getStatus());

		entity.setTableNo(gameList.getTableNo());

		entity.setType(gameList.getType() == null ? GameList.Type.nihtan : gameList.getType());

		entity.setVip(gameList.getVip());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gameListService.save(entity);
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
            gameListService.delete(ids);
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
		statuss.add(new MapEntity("enabled","开通"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("nihtan","真人游戏"));
		types.add(new MapEntity("kage","电子游戏"));
		model.addAttribute("types",types);


		model.addAttribute("data",gameListService.find(id));

		return "/admin/gameList/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(GameList gameList){
		GameList entity = gameListService.find(gameList.getId());

		entity.setOrders(gameList.getOrders() == null ? 0 : gameList.getOrders());

		entity.setGame(gameList.getGame());

		entity.setLogo(gameList.getLogo());

		entity.setMemo(gameList.getMemo());

		entity.setName(gameList.getName());

		entity.setRanges(gameList.getRanges());

		entity.setStatus(gameList.getStatus() == null ? GameList.Status.enabled : gameList.getStatus());

		entity.setTableNo(gameList.getTableNo());

		entity.setVip(gameList.getVip());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            gameListService.update(entity);
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
	public Message list(Date beginDate, Date endDate,GameList.Type type,GameList.Status status, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter authorityFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(authorityFilter);
		}
		if (status!=null) {
			Filter mediaTypeFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(mediaTypeFilter);
		}

		Page<GameList> page = gameListService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}