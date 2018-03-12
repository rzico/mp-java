package net.wit.controller.usered;

import net.wit.MapEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2018/3/12.
 */
@Controller("useredArticleController")
@RequestMapping("/usered/article")
public class ArticleController {

    /**
     * 主页
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {

        List<MapEntity> authoritys = new ArrayList<>();
        authoritys.add(new MapEntity("isPublic","公开"));
        authoritys.add(new MapEntity("isShare","不公开"));
        authoritys.add(new MapEntity("isEncrypt","加密"));
        authoritys.add(new MapEntity("isPrivate","私秘"));
        model.addAttribute("authoritys",authoritys);

        List<MapEntity> mediaTypes = new ArrayList<>();
        mediaTypes.add(new MapEntity("html","文本"));
        mediaTypes.add(new MapEntity("image","图文"));
        mediaTypes.add(new MapEntity("audio","音频"));
        mediaTypes.add(new MapEntity("video","视频"));
        model.addAttribute("mediaTypes",mediaTypes);

        return "/usered/article/index";
    }
}
