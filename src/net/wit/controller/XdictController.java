package net.wit.controller;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.CategoryModel;
import net.wit.controller.model.XDictModel;
import net.wit.entity.Category;
import net.wit.entity.Xdict;
import net.wit.service.CategoryService;
import net.wit.service.XdictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @ClassName: XdictController
 * @author 降魔战队  行业分类
 * @date 2017-9-14 19:42:9
 */
 
@Controller("xdictController")
@RequestMapping("/xdict")
public class XdictController extends BaseController {
    @Resource(name = "xdictServiceImpl")
    private XdictService xdictService;

    /**
     *  数据字典
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(HttpServletRequest request){
        List<Xdict> xdicts = xdictService.findAll();
        return Message.bind(XDictModel.bindList(xdicts),request);
    }

}