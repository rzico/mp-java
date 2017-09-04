package net.wit.controller.weex.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章展示输出模板 H5等

public class ArticleEditModel implements Serializable {

    private Long id;
    /** 标题 */
    private String title;
    /** 标题图 */
    private String thumbnial;
    /** 背景音乐 */
    private String music;
    /** 内容 */
    private List<TemplateModel> templates = new ArrayList<TemplateModel>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnial() {
        return thumbnial;
    }

    public void setThumbnial(String thumbnial) {
        this.thumbnial = thumbnial;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public List<TemplateModel> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateModel> templates) {
        this.templates = templates;
    }
}