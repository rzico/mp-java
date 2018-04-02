package net.wit.controller.weex.member;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.GameListModel;
import net.wit.controller.model.OccupationModel;
import net.wit.entity.GameList;
import net.wit.entity.Member;
import net.wit.entity.Occupation;
import net.wit.plat.nihtan.Crypto;
import net.wit.service.GameListService;
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
import java.text.SimpleDateFormat;
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

    @Resource(name = "gameListServiceImpl")
    private GameListService gameListService;

    private String utc2time(String utcStr) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUtcDate = null;
        try {
            gpsUtcDate = utcFormater.parse(utcStr);
        } catch (Exception e) {
            return "未知";
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("HH:mm");
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUtcDate.getTime())+"开放";
        return localTime;
    }

    /**
     *  游戏列表
     */

    @RequestMapping(value = "/gameList")
    @ResponseBody
    public Message gameList() {
        String resp = Crypto.gameList();
//        System.out.println("========="+resp);

        JSONObject jsonObject = JSONObject.fromObject(resp);

        JSONObject sicbo = jsonObject.getJSONObject("Sicbo");

        JSONArray  sicboArr = sicbo.getJSONArray("tables");
        List<GameListModel> data = new ArrayList<>();
        for (int i=0;i<sicboArr.size();i++) {
            JSONObject tb = sicboArr.getJSONObject(i);
            String status = "0";
            String memo = "正常";


            JSONArray maintenance = tb.getJSONArray("maintenance");
            for (int j = 0; j < maintenance.size(); j++) {
                JSONObject mt = maintenance.getJSONObject(j);
                if (mt.getString("status").equals("1")) {
                    status = "1";
                    memo = utc2time(mt.getString("end_time"));
                }
            }
            JSONArray ranges = tb.getJSONArray("ranges");
            String rng = "";
            for (int j = 0; j < ranges.size(); j++) {
                GameListModel m = new GameListModel();
                m.setGame("Sicbo");
                m.setStatus(status);
                m.setMemo(memo);
                m.setTable(tb.getString("table"));
                if (tb.containsKey("type")) {
                    m.setType(tb.getString("type"));
                } else {
                    m.setType("n");
                }
                m.setDealer("none");
                if (j==0) {
                  m.setVip("vip1");
                } else
                if (j==1) {
                    m.setVip("vip2");
                } else
                if (j==2) {
                    m.setVip("vip3");
                } else {
                    m.setVip("vip1");
                }
                JSONObject range = ranges.getJSONObject(j);
                rng = range.getString("min") + "-" + range.getString("max");
                m.setRanges(rng);
                data.add(m);


            }
        }



        JSONObject poker = jsonObject.getJSONObject("Poker");

        JSONArray  pokerArr = poker.getJSONArray("tables");
        for (int i=0;i<pokerArr.size();i++) {
            JSONObject tb = pokerArr.getJSONObject(i);

            String status = "0";
            String memo = "正常";
            JSONArray maintenance = tb.getJSONObject("maintenance").getJSONArray("maintenance");
            for (int j = 0; j < maintenance.size(); j++) {
                JSONArray info = maintenance.getJSONObject(j).getJSONArray("info");
                for (int r = 0; r < info.size(); r++) {
                    JSONObject mt = info.getJSONObject(r);
                    if (mt.getString("status").equals("1")) {
                        status = "1";
                        memo = utc2time(mt.getString("end_time"));
                    }
                }

            }

            JSONArray ranges = tb.getJSONArray("ranges");
            String rng = "";
            for (int j = 0; j < ranges.size(); j++) {
                JSONObject range = ranges.getJSONObject(j);
                rng = range.getString("min") + "-" + range.getString("max");
                GameListModel m = new GameListModel();
                m.setGame("Poker");
                m.setStatus(status);
                m.setMemo(memo);
                m.setTable(tb.getString("table"));
                if (tb.containsKey("type")) {
                    m.setType(tb.getString("type"));
                } else {
                    m.setType("n");
                }
                m.setDealer("none");
                m.setRanges(rng);
                if (j==0) {
                    m.setVip("vip1");
                } else
                if (j==1) {
                    m.setVip("vip2");
                } else
                if (j==2) {
                    m.setVip("vip3");
                } else {
                    m.setVip("vip1");
                }
                data.add(m);
            }
        }

        JSONObject tiger = jsonObject.getJSONObject("Dragon-Tiger");

        JSONArray  tigerArr = tiger.getJSONArray("tables");
        for (int i=0;i<tigerArr.size();i++) {
            JSONObject tb = tigerArr.getJSONObject(i);

            String status = "0";
            String memo = "正常";


            JSONArray maintenance = tb.getJSONArray("maintenance");
            for (int j = 0; j < maintenance.size(); j++) {
                JSONObject mt = maintenance.getJSONObject(j);
                if (mt.getString("status").equals("1")) {
                    memo = utc2time(mt.getString("end_time"));
                    status = "1";
                }
            }
             JSONArray ranges = tb.getJSONArray("ranges");
            String rng = "";
            for (int j = 0; j < ranges.size(); j++) {
                JSONObject range = ranges.getJSONObject(j);
                GameListModel m = new GameListModel();
                m.setGame("Dragon-Tiger");
                m.setStatus(status);
                m.setMemo(memo);

                m.setTable(tb.getString("table"));
                if (tb.containsKey("type")) {
                    m.setType(tb.getString("type"));
                } else {
                    m.setType("n");
                }
                m.setDealer("none");
                if (j==0) {
                    m.setVip("vip1");
                } else
                if (j==1) {
                    m.setVip("vip2");
                } else
                if (j==2) {
                    m.setVip("vip3");
                } else {
                    m.setVip("vip1");
                }
                rng = range.getString("min") + "-" + range.getString("max");
                m.setRanges(rng);
                data.add(m);
            }
        }



        JSONObject baccarat = jsonObject.getJSONObject("Baccarat");

        JSONArray  baccaratArr = baccarat.getJSONArray("tables");
        for (int i=0;i<baccaratArr.size();i++) {
            JSONObject tb = baccaratArr.getJSONObject(i);


            String status = "0";
            String memo = "正常";
            JSONArray maintenance = tb.getJSONObject("maintenance").getJSONArray("maintenance");
            for (int j = 0; j < maintenance.size(); j++) {
                JSONArray info = maintenance.getJSONObject(j).getJSONArray("info");
                for (int r = 0; r < info.size(); r++) {
                    JSONObject mt = info.getJSONObject(r);
                    if (mt.getString("status").equals("1")) {
                        memo = utc2time(mt.getString("end_time"));
                        status = "1";
                    }
                }

            }

            JSONArray ranges = tb.getJSONArray("ranges");
            String rng = "";
            for (int j = 0; j < ranges.size(); j++) {
                JSONObject range = ranges.getJSONObject(j);
                GameListModel m = new GameListModel();
                m.setGame("Baccarat");
                m.setStatus(status);
                m.setMemo(memo);
                m.setTable(tb.getString("table"));
                if (tb.containsKey("type")) {
                    m.setType(tb.getString("type"));
                } else {
                    m.setType("n");
                }
                m.setDealer("none");
                rng = range.getString("min") + "-" + range.getString("max");
                m.setRanges(rng);
                if (j==0) {
                    m.setVip("vip1");
                } else
                if (j==1) {
                    m.setVip("vip2");
                } else
                if (j==2) {
                    m.setVip("vip3");
                } else {
                    m.setVip("vip1");
                }
                data.add(m);
            }
        }

        for (GameListModel m:data) {
            GameList gameList = gameListService.find(GameList.Type.nihtan,m.getGame(),m.getTable(),m.getRanges());
            if (gameList!=null) {
                gameList.setActive(m.getStatus());
                gameList.setMemo(m.getMemo());
                gameListService.update(gameList);
            }

        }
        Map<String,Object> data11 = JsonUtils.toObject(resp,Map.class);
        return Message.success(data11,"获取成功");

    }

    /**
     *  游戏列表
     */

    @RequestMapping(value = "/list")
    @ResponseBody
    public Message list(HttpServletRequest request,ModelMap model){
        gameList();
        List<Filter> filters = new ArrayList<Filter>();

        filters.add(new Filter("status", Filter.Operator.eq, GameList.Status.enabled));
        filters.add(new Filter("type", Filter.Operator.eq, GameList.Type.nihtan));
        List<GameList> gl = gameListService.findList(null,null,filters,null);

        return Message.success(GameListModel.bindList(gl),"获取成功");
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

        gameList();
        GameList gameList = gameListService.find(GameList.Type.nihtan,game,table,range);
        if (gameList==null) {
            return Message.error("游戏没开通");
        }
        if (member.getVip().compareTo(Member.VIP.valueOf(gameList.getVip()))<0) {
            return Message.error(gameList.getVip()+"级才能进入");
        }
        if ("1".equals(gameList.getActive())) {
            return Message.error("休息中..");
        }

//        System.out.println(video);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Map<String,String> params = new HashMap<>();
        params.put("url",bundle.getString("nihtan.url")+"/api/play.jhtml?nihtan="+URLEncoder.encode(data.get("token"))+"&game="+game+"&table="+table+"&range="+ URLEncoder.encode(range));
        String key = game.replace("-","_")+"_"+table;
        if (video.containsKey(key)) {
            params.put("video", video.get(key));
        } else {
            params.put("video", "");
            return Message.error("没有获取视频数据");
        }
//        System.out.println(params);
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
        params.put("url",bundle.getString("nihtan.url")+"/api/play.jhtml?nihtan="+URLEncoder.encode(data.get("token"))+"&game="+game+"&table="+table+"&range="+ URLEncoder.encode(range));
        params.put("video",video.get(game+"_"+table));
        return Message.success(params,"获取成功");

    }

}