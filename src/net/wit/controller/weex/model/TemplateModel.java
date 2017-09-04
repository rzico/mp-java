package net.wit.controller.weex.model;

import net.wit.entity.Article;

import java.io.Serializable;
import java.util.Date;

//文章展示输出模板 H5等

public class TemplateModel implements Serializable {
    /** 媒体类型 */
    private Article.MediaType mediaType;
    /** 内容 */
    private String content;
    /** 链接 */
    private String url;
}