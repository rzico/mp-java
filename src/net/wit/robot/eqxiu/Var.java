package net.wit.robot.eqxiu;

/**
 * Created by Eric on 2018/2/24.
 */
public class Var {

    private boolean isUsed;
    private String page;
    private String comp;
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    public boolean getIsUsed() {
        return isUsed;
    }

    public void setPage(String page) {
        this.page = page;
    }
    public String getPage() {
        return page;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }
    public String getComp() {
        return comp;
    }
}
