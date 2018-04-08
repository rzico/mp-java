package net.wit.controller;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleCategoryModel;
import net.wit.entity.ArticleCategory;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


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
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("status", Filter.Operator.eq, ArticleCategory.Status.enabled));
        List<ArticleCategory> categories = articleCategoryService.findList(null,null,filters,null);
        return Message.bind(ArticleCategoryModel.bindList(categories),request);
    }

}