package net.wit.controller.weex.member;

import net.wit.*;
import net.wit.controller.admin.BaseController;
import net.wit.controller.model.ArticleFormDataModel;
import net.wit.controller.model.ArticleFormModel;
import net.wit.controller.model.ArticleModel;
import net.wit.entity.Article;
import net.wit.entity.ArticleForm;
import net.wit.entity.Member;
import net.wit.service.ArticleFormService;
import net.wit.service.ArticleService;
import net.wit.service.MemberService;
import net.wit.service.MessageService;
import net.wit.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller("weexMemberArticleFormController")
@RequestMapping("/weex/member/article/form")
public class ArticleFormController extends BaseController {
    @Resource(name = "articleFormServiceImpl")
    private ArticleFormService articleFormService;

    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;


    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Message submit(String body, Long articleId, HttpServletRequest request) {
        try {
            Member sender = memberService.getCurrent();
            Article article = articleService.find(articleId);
            Member receiver = article.getMember();
            ArticleFormDataModel data = JsonUtils.toObject(body,ArticleFormDataModel.class);

            if(sender != null && receiver != null && article != null){
                ArticleForm articleForm = new ArticleForm();
                articleForm.setArticle(article);
                articleForm.setSender(sender);
                articleForm.setReceiver(receiver);
                articleForm.setData(JsonUtils.toJson(data));
                articleFormService.save(articleForm);
                messageService.formPushTo(articleForm);
                return Message.success("提交成功");
            }else {
                return Message.error("提交失败");
            }
        }catch (Exception e){
            return Message.error("提交失败");
        }
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Message list(Long articleId,Pageable pageable, HttpServletRequest request){
        Article article = articleService.find(articleId);
        if(article != null && article.getForm() != null && !article.getForm().equalsIgnoreCase("")){
            List<Filter> filters = new ArrayList<Filter>();
            filters.add(new Filter("article", Filter.Operator.eq,article));
            pageable.setFilters(filters);
            Page<ArticleForm> page = articleFormService.findPage(null,null,pageable);
            PageBlock model = PageBlock.bind(page);
            model.setData(ArticleFormModel.bindList(page.getContent()));

            return Message.bind(model,request);

        }else {
            return  Message.error("无效文章ID");
        }
    }



}
