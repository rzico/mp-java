package net.wit.controller;

import net.wit.controller.admin.BaseController;
import net.wit.plat.nihtan.Crypto;
import net.wit.util.JsonUtils;
import net.wit.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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

    /**
     *  进入游戏同步金额
     */
    @RequestMapping(value = "/check")
    public String check(HttpServletRequest request,ModelMap model){
        String json = WebUtils.getBodyParams(request);
        System.out.println(json);
        if (json!=null && !json.equals("")) {
            Map<String,String> params = JsonUtils.toObject(json,Map.class);
            model.addAttribute("notifyMessage",10000000);
        } else {
            model.addAttribute("notifyMessage",10000000);
        }
        return "common/notify";
    }

    /**
     *  下注时会通知我们
     */
    @RequestMapping(value = "/transaction")
    public String transaction(HttpServletRequest request,ModelMap model){
        String json = WebUtils.getBodyParams(request);
        System.out.println(json);
        Map<String,String> data = new HashMap<>();
        if (json!=null && !json.equals("")) {
            Map<String,String> params = JsonUtils.toObject(json,Map.class);
            data.put("code","200");
            data.put("status","ok");
            data.put("credits",params.get("amount"));
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
    public String history(HttpServletRequest request,ModelMap model){
        String json = WebUtils.getBodyParams(request);
        System.out.println(json);
        Map<String,String> data = new HashMap<>();
        if (json!=null && !json.equals("")) {
            Map<String,String> params = JsonUtils.toObject(json,Map.class);
            data.put("code","200");
            data.put("status","ok");
            data.put("credits","100");
        } else {
            data.put("code","500");
            data.put("status","error");
            data.put("credits","0");
        }
        model.addAttribute("notifyMessage",JsonUtils.toJson(data));
        return "common/notify";
    }

}