package net.wit.controller.weex;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.weex.model.ArticleCatalogModel;
import net.wit.controller.weex.model.ArticleCategoryModel;
import net.wit.controller.weex.model.MessageModel;
import net.wit.entity.ArticleCategory;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: ArticleCategoryController
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
@Controller("articleCategoryController")
@RequestMapping("/article_category")
public class ArticleCategoryController extends BaseController {

    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;

    /**
     *  分类列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long tagIds,HttpServletRequest request){
        List<ArticleCategory> categories = articleCategoryService.findAll();
        return CacheBlock.bind(ArticleCategoryModel.bindList(categories),request);
    }

}