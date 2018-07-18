package net.wit.controller.makey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.controller.model.BaseModel;
import net.wit.controller.model.TagModel;
import net.wit.entity.Course;
import net.wit.entity.Music;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class MusicModel extends BaseModel implements Serializable {
    
    private Long id;
    /** 标题 */
     private String title;

    /** 缩例图 */
     private String thumbnail;

    /** 音乐文件 */
     private String musicFile;

    /** 阅读数 */
    private Long hits;

    /** 介绍 */
     private String content;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(String musicFile) {
        this.musicFile = musicFile;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void bind(Music music) {
        this.id = music.getId();
        this.title = music.getTitle();
        this.thumbnail = music.getThumbnail();
        this.hits = music.getHits();
        this.musicFile = music.getMusicFile();
    }

    public static List<MusicModel> bindList(List<Music> musics) {
        List<MusicModel> ms = new ArrayList<MusicModel>();
        for (Music music:musics) {
            MusicModel m = new MusicModel();
            m.bind(music);
            ms.add(m);
        }
        return ms;
    }

}