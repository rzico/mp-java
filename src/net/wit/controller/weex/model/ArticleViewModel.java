package net.wit.controller.weex.model;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//文章展示输出模板 H5等

public class ArticleViewModel implements Serializable {
    
    private Long id;
    /** 会员 */
    private MemberModel member;
    /** 作者 */
    private String author;
    /** 标题 */
    private String title;
    /** 标题图 */
    private String thumbnial;
    /** 创建时间 */
    private Date createDate;
    /** 背景音乐 */
    private String music;
    /** 链接 */
    private String url;
    /** 评论数 */
    private Long review;
    /** 阅读数 */
    private Long hits;
    /** 点赞数 */
    private Long laud;
    /** 内容 */
    private String content;

}