package net.wit.controller.weex.member;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.GameListModel;
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
     *  游戏列表
     */

    @RequestMapping(value = "/gameList")
    @ResponseBody
    public Message gameList(HttpServletRequest request,ModelMap model) {
        String resp = Crypto.gameList();
        Map<String,Object> data = JsonUtils.toObject(resp,Map.class);
        return Message.success(data,"获取成功");
    }
    /**
     *  游戏列表
     */

    @RequestMapping(value = "/list")
    @ResponseBody
    public Message list(HttpServletRequest request,ModelMap model){
        String resp = Crypto.gameList();
        JSONObject jsonObject = JSONObject.fromObject(resp);

        JSONObject sicbo = jsonObject.getJSONObject("Sicbo");

        JSONArray  sicboArr = sicbo.getJSONArray("tables");
        List<GameListModel> data = new ArrayList<>();
        for (int i=0;i<sicboArr.size();i++) {
           JSONObject tb = sicboArr.getJSONObject(i);
           GameListModel m = new GameListModel();
           m.setGame("Sicbo");
           m.setTable(tb.getString("table"));
           JSONArray mts = tb.getJSONArray("maintenance");
           m.setDealer("none");
           for (int j=0;j<mts.size();j++) {
               JSONObject mt = mts.getJSONObject(j);
               if (mt.getString("status").equals("1")) {
                   m.setDealer(mt.getString("division"));
               }
           }
            JSONArray ranges = tb.getJSONArray("ranges");
            String rng = "";
            for (int j=0;j<mts.size();j++) {
                JSONObject range = mts.getJSONObject(j);
                if (range.getString("status").equals("1")) {
                   rng = range.getString("min")+"-"+range.getString("max");
                   break;
                }
            }
            m.setRanges(rng);
            data.add(m);
        }



        JSONObject poker = jsonObject.getJSONObject("Poker");

        JSONArray  pokerArr = poker.getJSONArray("tables");
        for (int i=0;i<pokerArr.size();i++) {
            JSONObject tb = pokerArr.getJSONObject(i);
            GameListModel m = new GameListModel();
            m.setGame("Poker");
            m.setTable(tb.getString("table"));
            JSONArray mts = tb.getJSONArray("maintenance");
            m.setDealer("none");
            for (int j=0;j<mts.size();j++) {
                JSONObject mt = mts.getJSONObject(j);
                if (mt.getString("status").equals("1")) {
                    m.setDealer(mt.getString("division"));
                }
            }
            JSONArray ranges = tb.getJSONArray("ranges");
            String rng = "";
            for (int j=0;j<mts.size();j++) {
                JSONObject range = mts.getJSONObject(j);
                if (range.getString("status").equals("1")) {
                    rng = range.getString("min")+"-"+range.getString("max");
                    break;
                }
            }
            m.setRanges(rng);
            data.add(m);
        }



        JSONObject tiger = jsonObject.getJSONObject("Dragon-Tiger");

        JSONArray  tigerArr = tiger.getJSONArray("tables");
        for (int i=0;i<tigerArr.size();i++) {
            JSONObject tb = tigerArr.getJSONObject(i);
            GameListModel m = new GameListModel();
            m.setGame("Dragon-Tiger");
            m.setTable(tb.getString("table"));
            JSONArray mts = tb.getJSONArray("maintenance");
            m.setDealer("none");
            for (int j=0;j<mts.size();j++) {
                JSONObject mt = mts.getJSONObject(j);
                if (mt.getString("status").equals("1")) {
                    m.setDealer(mt.getString("division"));
                }
            }
            JSONArray ranges = tb.getJSONArray("ranges");
            String rng = "";
            for (int j=0;j<mts.size();j++) {
                JSONObject range = mts.getJSONObject(j);
                if (range.getString("status").equals("1")) {
                    rng = range.getString("min")+"-"+range.getString("max");
                    break;
                }
            }
            m.setRanges(rng);
            data.add(m);
        }



        JSONObject baccarat = jsonObject.getJSONObject("Baccarat");

        JSONArray  baccaratArr = baccarat.getJSONArray("tables");
        for (int i=0;i<baccaratArr.size();i++) {
            JSONObject tb = baccaratArr.getJSONObject(i);
            GameListModel m = new GameListModel();
            m.setGame("Baccarat");
            m.setTable(tb.getString("table"));
            JSONArray mts = tb.getJSONArray("maintenance");
            m.setDealer("none");
            for (int j=0;j<mts.size();j++) {
                JSONObject mt = mts.getJSONObject(j);
                if (mt.getString("status").equals("1")) {
                    m.setDealer(mt.getString("division"));
                }
            }
            JSONArray ranges = tb.getJSONArray("ranges");
            String rng = "";
            for (int j=0;j<mts.size();j++) {
                JSONObject range = mts.getJSONObject(j);
                if (range.getString("status").equals("1")) {
                    rng = range.getString("min")+"-"+range.getString("max");
                    break;
                }
            }
            m.setRanges(rng);
            data.add(m);
        }

        return Message.success(data,"获取成功");
    }

    /**
     *  获取游戏参数
     */

    @RequestMapping(value = "/view")
    @ResponseBody
    public Message view(String game,String table,String range, HttpServletRequest request,ModelMap model){
        String resp = Crypto.videoList();
        Map<String,Object> videoData = JsonUtils.toObject(resp,Map.class);
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        String sessionResp =  Crypto.getSession(request.getRemoteAddr(),member);

        Map<String,String> data = JsonUtils.toObject(sessionResp,Map.class);

        Map<String,String> video = (Map) videoData.get("list");
        if (game==null) {
            game = "Sicbo";
            table = "1";
            range = "5-100";
        }

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Map<String,String> params = new HashMap<>();
        params.put("url",bundle.getString("nihtan.url")+"/api/play.jhtml?token="+data.get("token")+"&game="+game+"&table="+table+"&range="+ URLEncoder.encode(range));
        params.put("video",video.get(game+"_"+table));
        return Message.success(params,"获取成功");

    }

    /**
     *  获取游戏参数
     */

    @RequestMapping(value = "/test")
    @ResponseBody
    public Message test(String game,String table,String range, HttpServletRequest request,ModelMap model){
        String resp = Crypto.videoList();
        Map<String,Object> videoData = JsonUtils.toObject(resp,Map.class);
        Member member = memberService.getCurrent();
        if (member==null) {
            return Message.error(Message.SESSION_INVAILD);
        }

        String sessionResp =  Crypto.getSession(request.getRemoteAddr(),member);

        Map<String,String> data = JsonUtils.toObject(sessionResp,Map.class);

        Map<String,String> video = (Map) videoData.get("list");
        if (game==null) {
            game = "Sicbo";
            table = "1";
            range = "5-100";
        }

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Map<String,String> params = new HashMap<>();
        params.put("url",bundle.getString("nihtan.url")+"/api/play.jhtml?token="+data.get("token")+"&game="+game+"&table="+table+"&range="+ URLEncoder.encode(range));
        params.put("video",video.get(game+"_"+table));
        return Message.success(params,"获取成功");

    }

}