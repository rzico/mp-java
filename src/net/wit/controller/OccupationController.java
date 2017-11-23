package net.wit.controller;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.OccupationModel;
import net.wit.entity.Occupation;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @ClassName: OccupationController
 * @author 降魔战队  职业分类
 * @date 2017-9-14 19:42:9
 */
 
@Controller("occupationController")
@RequestMapping("/occupation")
public class OccupationController extends BaseController {

    @Resource(name = "occupationServiceImpl")
    private OccupationService occupationService;

    /**
     *  分类列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(HttpServletRequest request){
        List<Occupation> occupations = occupationService.findAll();
        return Message.bind(OccupationModel.bindList(occupations),request);
    }

}