package net.wit.controller.makey;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.CourseModel;
import net.wit.controller.makey.model.MusicModel;
import net.wit.entity.Admin;
import net.wit.entity.Course;
import net.wit.entity.Member;
import net.wit.entity.Music;
import net.wit.service.AdminService;
import net.wit.service.CourseService;
import net.wit.service.MemberService;
import net.wit.service.MusicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: MusicController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("makeyMusicController")
@RequestMapping("/makey/music")
public class MusicController extends BaseController {

    @Resource(name = "musicServiceImpl")
    private MusicService musicService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    /**
     *  列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long xmid, Pageable pageable, HttpServletRequest request){
        List<Filter> filters = new ArrayList<Filter>();
        if (xmid!=null) {
                Member xmember = memberService.find(xmid);
                Admin admin = adminService.findByMember(xmember);
                filters.add(new Filter("enterprise", Filter.Operator.eq, admin.getEnterprise()));
        }
        pageable.setFilters(filters);
        Page<Music> page = musicService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(MusicModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }


    /**
     *  列表
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long id,HttpServletRequest request){
        Music music = musicService.find(id);

        MusicModel model = new MusicModel();
        model.bind(music);
        return Message.bind(model,request);
    }

}