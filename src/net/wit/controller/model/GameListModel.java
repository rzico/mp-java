package net.wit.controller.model;

import net.sf.json.JSONObject;
import net.wit.entity.*;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameListModel extends BaseModel implements Serializable {
    /** 名称 */
    private String game;
    /** 类型 */
    private String type;
    /** 星级 */
    private String vip;
    /** 桌号 */
    private String table;
    /** 头像 */
    private String logo;
    /** 荷官 */
    private String dealer;
    /** 投注 */
    private String ranges;
    /** 说明 */
    private String memo;

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getRanges() {
        return ranges;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void bind(GameList game) {
        this.setLogo(game.getLogo());
        this.setGame(game.getGame());
        this.setRanges(game.getRanges());
        this.setTable(game.getTableNo());
        this.setDealer(game.getName());
        this.setType(game.getType().name());
        this.setVip(game.getVip());
        this.memo = game.getMemo();
    }

    public static List<GameListModel> bindList(List<GameList> games) {
        List<GameListModel> gs = new ArrayList<>();
        for (GameList game:games) {
           GameListModel md = new GameListModel();
           md.bind(game);
           gs.add(md);
        }
        return gs;
    }

}