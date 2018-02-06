package net.wit.controller;

import net.wit.controller.admin.BaseController;
import net.wit.entity.Game;
import net.wit.entity.Member;
import net.wit.plat.nihtan.Crypto;
import net.wit.service.CategoryService;
import net.wit.service.GameService;
import net.wit.service.MemberService;
import net.wit.util.JsonUtils;
import net.wit.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    /**
     *  进入游戏同步金额
     */
    @RequestMapping(value = "/check")
    public String check(HttpServletRequest request,ModelMap model){
        System.out.println("check");
        String json = WebUtils.getBodyParams(request);
        System.out.println(json);
        if (json!=null && !json.equals("")) {
            Map<String,String> params = JsonUtils.toObject(json,Map.class);
            Member member = memberService.findByUsername(params.get("user_id"));
            if (member!=null) {
                model.addAttribute("notifyMessage",member.getBalance());
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
        System.out.println("transaction");
        System.out.println(json);
        Map<String,String> data = new HashMap<>();
//        if (hash!=null && json!=null && !json.equals("") && hash.equals(Crypto.encrypt(Crypto.key,json))) {
            Map<String,String> params = JsonUtils.toObject(json,Map.class);
            Member member = memberService.findByUsername(params.get("user_id"));
            if (member!=null) {
                Game game = new Game();
                game.setGame(params.get("game"));
                game.setTableNo(params.get("table"));
                game.setRoundNo(params.get("round_no"));
                game.setGame(params.get("round_no"));
                game.setDebit(new BigDecimal(params.get("amount")));
                game.setCredit(BigDecimal.ZERO);
                game.setMember(member);
                game.setStatus(Game.Status.transaction);
                game.setMemo(params.get("range"));
                try {
                    gameService.sumbit(game);
                    data.put("code","200");
                    data.put("status","ok");
                    data.put("credits",params.get("amount"));
                } catch (Exception e) {
                    data.put("code","500");
                    data.put("status",e.getMessage());
                    data.put("credits","0");
                }
            } else {
                data.put("code","500");
                data.put("status","error");
                data.put("credits","0");
            }
//        } else {
//            data.put("code","500");
//            data.put("status","error");
//            data.put("credits","0");
//        }
        model.addAttribute("notifyMessage",JsonUtils.toJson(data));
        System.out.println(JsonUtils.toJson(data));
        return "common/notify";
    }

    /**
     *  分类列表
     */
    @RequestMapping(value = "/history")
    public String history(String hash,HttpServletRequest request,ModelMap model){
        System.out.println("history");
        String json = WebUtils.getBodyParams(request);
        System.out.println(json);
        Map<String,String> data = new HashMap<>();
//        if (hash!=null && json!=null && !json.equals("") && hash.equals(Crypto.encrypt(Crypto.key,json))) {
//            Map<String,String> params = JsonUtils.toObject(json,Map.class);
//            Member member = memberService.findByUsername(params.get("user_id"));
//            if (member!=null) {
//                Game game = gameService.find(params.get("game"),params.get("table"),params.get("round_no"));
//                game.setCredit(game.getDebit());
//
//                try {
//                    gameService.history(game);
//                    data.put("code","200");
//                    data.put("status","ok");
//                    data.put("credits",params.get("amount"));
//                } catch (Exception e) {
//                    data.put("code","500");
//                    data.put("status",e.getMessage());
//                    data.put("credits","0");
//                }
//            } else {
//                data.put("code","500");
//                data.put("status","error");
//                data.put("credits","0");
//            }
//        } else {
//            data.put("code","500");
//            data.put("status","error");
//            data.put("credits","0");
//        }
                    data.put("code","200");
                    data.put("status","ok");
                    data.put("credits","0");
        model.addAttribute("notifyMessage",JsonUtils.toJson(data));
//        System.out.println(JsonUtils.toJson(data));
        return "common/notify";
    }

}