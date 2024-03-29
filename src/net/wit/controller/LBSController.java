/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.entity.Area;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Controller - 地理信息控制
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("LBSController")
@RequestMapping("/lbs")
public class LBSController extends BaseController {
    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static final double pi = 3.14159265358979324;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    //百度转高德
    public static Location bd_decrypt(Location location)
    {   double bd_lat = location.getLat();
        double bd_lon = location.getLng();
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        Location gd = new Location();
        gd.setLat(z * Math.sin(theta));
        gd.setLng(z * Math.cos(theta));
        return gd;
    }

    //高德转百度
    public static Location bd_encrypt(Location location)
    {
        double gg_lat = location.getLat();
        double gg_lon = location.getLng();
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        Location bd = new Location();
        bd.setLat(z * Math.sin(theta) + 0.006);
        bd.setLng(z * Math.cos(theta) + 0.0065);
        return bd;
    }

    /**
     * 经纬度获取城市
     *
     * @param lat 纬度
     * @param lng 经度
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Message get(double lat, double lng, HttpServletRequest request) {
        String result = "";
        Location gd = new Location();
        gd.setLat(lat);
        gd.setLng(lng);
        Location bd = bd_encrypt(gd);
        @SuppressWarnings("resource")
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet("http://api.map.baidu.com/geocoder/v2/?coordtype=gcj02ll&location="+String.valueOf(bd.getLat())+","+String.valueOf(bd.getLng())+"&output=json&ak=l1dGeE1f95ZFQOc6jjC5mejIjE3U7GwM" );
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        JSONObject jsonObject=JSONObject.fromObject(result);
        String adcode = null;
        if(Objects.equals(jsonObject.getString("status"), "0")){
            adcode = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("adcode");
        }

        if (adcode!=null) {
            Area area = areaService.find(adcode);
            if (area!=null) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", area.getId());
                data.put("name", area.getName());
                return Message.success(data, "执行成功");
            } else {
                return Message.error("定位失败");
            }
        } else {
            return Message.error("定位失败");
        }

    }

}