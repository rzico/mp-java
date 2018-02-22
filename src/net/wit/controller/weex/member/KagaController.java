package net.wit.controller.weex.member;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.GameListModel;
import net.wit.entity.Member;
import net.wit.plat.nihtan.Crypto;
import net.wit.plat.nihtan.Kaga;
import net.wit.service.MemberService;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;


/**
 * @ClassName: KagaController
 * @author 降魔战队  职业分类
 * @date 2017-9-14 19:42:9
 */
 
@Controller("kagaMemberController")
@RequestMapping("/weex/member/kaga")
public class KagaController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    /**
     *  游戏列表
     */

    @RequestMapping(value = "/gameList")
    @ResponseBody
    public Message gameList(HttpServletRequest request,ModelMap model) {
        String resp = Kaga.gameList();
        Map<String,Object> data = JsonUtils.toObject(resp,Map.class);
        return Message.success(data,"获取成功");
    }
    /**
     *  游戏列表
     */

    @RequestMapping(value = "/list")
    @ResponseBody
    public Message list(HttpServletRequest request,ModelMap model){
        String resp = Kaga.gameList();
        System.out.println(resp);
        JSONObject jsonObject = JSONObject.fromObject(resp);

        JSONArray  games = jsonObject.getJSONArray("games");
        List<GameListModel> data = new ArrayList<>();
        for (int i=0;i<games.size();i++) {
           JSONObject tb = games.getJSONObject(i);
           GameListModel m = new GameListModel();
           m.setGame(tb.getString("gameId"));
           m.setLogo(tb.getString("iconURLPrefix"));
           data.add(m);
        }
        return Message.success(data,"获取成功");
    }

    /**
     *  获取游戏参数
     */

    @RequestMapping(value = "/view")
    @ResponseBody
    public Message view(String game,HttpServletRequest request,ModelMap model){
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        String resp =  Kaga.getSession(game,request.getRemoteAddr(),member);

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Map<String,String> params = new HashMap<>();
        params.put("url",resp);
        params.put("video","");
        System.out.println(params);
        return Message.success(params,"获取成功");

    }

}