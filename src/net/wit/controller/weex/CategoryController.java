package net.wit.controller.weex;

import net.wit.CacheBlock;
import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.CategoryModel;
import net.wit.controller.weex.model.OccupationModel;
import net.wit.entity.Category;
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
 * @ClassName: CategoryController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("categoryController")
@RequestMapping("/category")
public class CategoryController extends BaseController {
    @Resource(name = "categoryServiceImpl")
    private CategoryService categoryService;

    /**
     *  分类列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(HttpServletRequest request){
        List<Category> categories = categoryService.findAll();
        return CacheBlock.bind(CategoryModel.bindList(categories),request);
    }

}