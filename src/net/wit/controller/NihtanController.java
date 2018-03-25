package net.wit.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Principal;
import net.wit.controller.admin.BaseController;
import net.wit.entity.Cart;
import net.wit.entity.Game;
import net.wit.entity.Member;
import net.wit.plat.im.User;
import net.wit.plat.nihtan.Crypto;
import net.wit.plat.nihtan.Kaga;
import net.wit.service.CategoryService;
import net.wit.service.GameService;
import net.wit.service.MemberService;
import net.wit.service.RedisService;
import net.wit.util.JsonUtils;
import net.wit.util.WebUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @ClassName: NihtanController
 * @author 降魔战队  职业分类
 * @date 2017-9-14 19:42:9
 */
 
@Controller("nihtanController")
@RequestMapping("/api")
public class NihtanController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "gameServiceImpl")
    private GameService gameService;

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    /**
     *  进入游戏
     */

    @RequestMapping(value = "/play")
    public String play(String game,String table,String range,String nihtan,HttpServletRequest request,ModelMap model){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        model.addAttribute("requestUrl",bundle.getString("nihtan.host"));
        model.addAttribute("requestMethod","post");
        model.addAttribute("requestCharset","utf-8");

        if (nihtan==null) {
            Member member = memberService.find(43L);
            String sessionResp = Crypto.getSession(request.getRemoteAddr(), member);
            Map<String, String> data = JsonUtils.toObject(sessionResp, Map.class);
            nihtan =data.get("token");
            game = "Sicbo";
            table = "1";
            range = "5-100";
         }

        Map<String,String> parameterMap = new HashMap<>();

        parameterMap.put("token",nihtan);
        parameterMap.put("mobile","1");
        parameterMap.put("g",game);
        parameterMap.put("t",table);
        parameterMap.put("r",range);
        parameterMap.put("m","0");
        model.addAttribute("parameterMap",parameterMap);
        return "common/play";

    }


    /**
     *  进入游戏
     */

    @RequestMapping(value = "/kaga")
    public String kaga(String data,String hash,HttpServletRequest request,ModelMap model){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        try {
            model.addAttribute("data", new String(Base64.decode(data),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("hash",hash);
        model.addAttribute("url", Kaga.sessionURL);
        return "common/kaga";

    }

    /**
     *  进入游戏同步金额
     */
    @RequestMapping(value = "/check")
    public String check(HttpServletRequest request,ModelMap model){
//        System.out.println("check");
        String json = WebUtils.getBodyParams(request);
//        System.out.println(json);
        if (json!=null && !json.equals("")) {
            JSONObject jsonObject = JSONObject.fromObject(json);
            Member member = memberService.findByUsername(jsonObject.getString("user_id"));
            if (member!=null) {
                model.addAttribute("notifyMessage",member.getPoint());
            } else {
                model.addAttribute("notifyMessage",0);
            }
        } else {
            model.addAttribute("notifyMessage",0);
        }
        return "common/notify";
    }

    /**
     *  下注时会通知我们
     */
    @RequestMapping(value = "/transaction")
    public String transaction(String hash,HttpServletRequest request,ModelMap model){
        String json = WebUtils.getBodyParams(request);
//        System.out.println("transaction");
//        System.out.println(json);
        Map<String,String> data = new HashMap<>();
//        if (hash!=null && json!=null && !json.equals("") && hash.equals(Crypto.encrypt(Crypto.key,json))) {
            JSONObject jsonObject = JSONObject.fromObject(json);
            Member member = memberService.findByUsername(jsonObject.getString("user_id"));
            if (member!=null) {
                Game game = new Game();
                game.setGame(jsonObject.getString("game"));
                game.setTableNo(jsonObject.getString("table"));
                game.setRoundNo("0");
                if (jsonObject.containsKey("round_no")) {
                    game.setRoundNo(jsonObject.getString("round_no"));
                }
                if (jsonObject.containsKey("round_id")) {
                    game.setRoundNo(jsonObject.getString("round_id"));
                }
                game.setDebit(new Long(jsonObject.getString("amount")));
                game.setCredit(0L);
                game.setMember(member);
                game.setStatus(Game.Status.transaction);
                game.setMemo(game.getGame());
                try {
                    gameService.sumbit(game);
                    data.put("code","200");
                    data.put("status","ok");
                    data.put("credits",jsonObject.getString("amount"));
                } catch (Exception e) {
                    e.printStackTrace();
                    data.put("code","500");
                    data.put("status",e.getMessage());
                    data.put("credits","0");
                }
            } else {
                data.put("code","500");
                data.put("status","error");
                data.put("credits","0");
            }
        model.addAttribute("notifyMessage",JsonUtils.toJson(data));
        return "common/notify";
    }

    /**
     *  分类列表
     */
    @RequestMapping(value = "/history")
    public String history(String hash,HttpServletRequest request,ModelMap model){
        String json = WebUtils.getBodyParams(request);
        Map<String,String> data = new HashMap<>();
        JSONObject jsonObject = JSONObject.fromObject(json);
        String game = jsonObject.getString("game");
        String table = jsonObject.getString("table");
        String round_no = "0";
        if (jsonObject.containsKey("round_no")) {
            round_no = jsonObject.getString("round_no");
        }
        if (jsonObject.containsKey("round_id")) {
            round_no = jsonObject.getString("round_id");
        }
        JSONArray datas = jsonObject.getJSONArray("data");
        for (int i = 0 ; i<datas.size(); i++) {
            JSONObject user = datas.getJSONObject(i);
            String user_id = user.getString("user_id");
            Long win_money = new Long(user.getString("total_win"));
            Member member = memberService.findByUsername(user_id);
            if (member!=null) {
                Game gameData = gameService.find(member,game,table,round_no);
                if (gameData!=null) {
                    gameData.setCredit(win_money);
                    try {
                        gameService.history(gameData);
                        data.put("code", "200");
                        data.put("status", "ok");
                        data.put("credits", user.getString("total_win"));
                    } catch (Exception e) {
                        data.put("code", "500");
                        data.put("status", e.getMessage());
                        data.put("credits", "0");
                    }
                } else {
                    data.put("code", "200");
                    data.put("status", "ok1");
                    data.put("credits", "0");
                }
            } else {
                data.put("code", "200");
                data.put("status", "ok2");
                data.put("credits", "0");
            }
        }
        model.addAttribute("notifyMessage",JsonUtils.toJson(data));
        return "common/notify";
    }

}