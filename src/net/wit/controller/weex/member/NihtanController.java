package net.wit.controller.weex.member;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.OccupationModel;
import net.wit.entity.Member;
import net.wit.entity.Occupation;
import net.wit.plat.nihtan.Crypto;
import net.wit.service.MemberService;
import net.wit.service.OccupationService;
import net.wit.util.JsonUtils;
import net.wit.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;


/**
 * @ClassName: NihtanController
 * @author 降魔战队  职业分类
 * @date 2017-9-14 19:42:9
 */
 
@Controller("nihtanMemberController")
@RequestMapping("/weex/member/nihtan")
public class NihtanController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    /**
     *  进入游戏
     */

    @RequestMapping(value = "/play")
    public String play(String game,String table,String range,HttpServletRequest request,ModelMap model){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        Member member = memberService.getCurrent();
        model.addAttribute("requestUrl",bundle.getString("nihtan.host"));
        model.addAttribute("requestMethod","post");
        model.addAttribute("requestCharset","utf-8");


        Map<String,String> parameterMap = new HashMap<>();

        String resp =  Crypto.getSession(request.getRemoteAddr());

        Map<String,String> data = JsonUtils.toObject(resp,Map.class);
        if (game==null) {
            game = "Baccarat";
            table = "1";
            range = "100-2000";
        }

        parameterMap.put("token",data.get("token"));
        parameterMap.put("mobile","1");
        parameterMap.put("g",game);
        parameterMap.put("t",table);
        parameterMap.put("r",range);
        parameterMap.put("m","1");
        model.addAttribute("parameterMap",parameterMap);
        return "common/play";

    }

    /**
     *  游戏列表
     */

    @RequestMapping(value = "/list")
    @ResponseBody
    public Message list(HttpServletRequest request,ModelMap model){
        String resp = Crypto.gameList();
        Map<String,Object> data = JsonUtils.toObject(resp,Map.class);


        return Message.success(data,"获取成功");
    }


    /**
     *  获取游戏参数
     */

    @RequestMapping(value = "/view")
    @ResponseBody
    public Message view(String game,String table,String range, HttpServletRequest request,ModelMap model){
        String resp = Crypto.videoList();
        Map<String,Object> data = JsonUtils.toObject(resp,Map.class);

        Map<String,String> video = (Map) data.get("list");
        if (game==null) {
            game = "Baccarat";
            table = "1";
            range = "100-2000";
        }

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Map<String,String> params = new HashMap<>();
        params.put("url",bundle.getString("nihtan.url")+"/nihtan/weex/member/nihtan/play.jhtml?game="+game+"&table="+table+"&range="+ URLEncoder.encode(range));
        params.put("video",video.get(game+"_"+table));
        return Message.success(params,"获取成功");

    }

}