package net.wit.controller.model;

import net.sf.json.JSONObject;
import net.wit.entity.Goods;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameListModel extends BaseModel implements Serializable {

    /** 名称 */
    private String game;
    /** 桌号 */
    private String table;
    /** 头像 */
    private String logo;
    /** 荷官 */
    private String dealer;
    /** 投注 */
    private String ranges;

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

    public List<GameListModel> bind(String game, String json) {
        List<GameListModel> games = new ArrayList<>();
        JSONObject oj = JSONObject.fromObject(json);
        if (oj.containsKey(game)) {

        }
    }

}