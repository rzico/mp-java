package net.wit.entity.eqxiuentity;

import java.util.List;

/**
 * Created by Eric on 2018/2/24.
 */
public class Properties {

//    private PageScroll pageScroll;
//    private List<String> triggerGroup;
    private String thumbSrc;
    private boolean scratch;
    private String src;
    private ImgStyle imgStyle;
    private int initType;
    private String maskSrc;
    private String originSrc;
    private List<Anim> anim;
    private GSensor GSensor;
    private Var var;
    public void setScratch(boolean scratch) {
        this.scratch = scratch;
    }
    public boolean getScratch() {
        return scratch;
    }

    public void setSrc(String src) {
        this.src = src;
    }
    public String getSrc() {
        return src;
    }

    public void setImgStyle(ImgStyle imgStyle) {
        this.imgStyle = imgStyle;
    }
    public ImgStyle getImgStyle() {
        return imgStyle;
    }

    public void setInitType(int initType) {
        this.initType = initType;
    }
    public int getInitType() {
        return initType;
    }

    public void setMaskSrc(String maskSrc) {
        this.maskSrc = maskSrc;
    }
    public String getMaskSrc() {
        return maskSrc;
    }

    public void setOriginSrc(String originSrc) {
        this.originSrc = originSrc;
    }
    public String getOriginSrc() {
        return originSrc;
    }

    public void setAnim(List<Anim> anim) {
        this.anim = anim;
    }
    public List<Anim> getAnim() {
        return anim;
    }

    public void setGSensor(GSensor GSensor) {
        this.GSensor = GSensor;
    }
    public GSensor getGSensor() {
        return GSensor;
    }

    public void setVar(Var var) {
        this.var = var;
    }
    public Var getVar() {
        return var;
    }
//    public void setPageScroll(PageScroll pageScroll) {
//        this.pageScroll = pageScroll;
//    }
//    public PageScroll getPageScroll() {
//        return pageScroll;
//    }
//
//    public void setTriggerGroup(List<String> triggerGroup) {
//        this.triggerGroup = triggerGroup;
//    }
//    public List<String> getTriggerGroup() {
//        return triggerGroup;
//    }

    public void setThumbSrc(String thumbSrc) {
        this.thumbSrc = thumbSrc;
    }
    public String getThumbSrc() {
        return thumbSrc;
    }
}

