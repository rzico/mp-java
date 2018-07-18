package net.wit.controller.makey;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.makey.model.CourseModel;
import net.wit.entity.Admin;
import net.wit.entity.Course;
import net.wit.entity.Member;
import net.wit.service.AdminService;
import net.wit.service.CourseService;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: CourseController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("makeyCourseController")
@RequestMapping("/makey/course")
public class MusicController extends BaseController {

    @Resource(name = "courseServiceImpl")
    private CourseService courseService;
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
        Page<Course> page = courseService.findPage(null,null,pageable);
        PageBlock model = PageBlock.bind(page);
        model.setData(CourseModel.bindList(page.getContent()));
        return Message.bind(model,request);
    }


    /**
     *  列表
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long id,HttpServletRequest request){
        Course course = courseService.find(id);

        CourseModel model = new CourseModel();
        model.bind(course);
        return Message.bind(model,request);
    }

}