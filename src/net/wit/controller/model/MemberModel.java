package net.wit.controller.model;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MemberModel extends BaseModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String username;
    /** 昵称 */
    private String nickName;
    /** 收藏 */
    private int favorite;
    /** 余额 */
    private BigDecimal balance;
    /** 粉丝 */
    private int fans;
    /** 关注 */
    private int follow;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 卡包 */
    private int coupon;
    /** 订单 */
    private int order;
    /** 奖金 */
    private BigDecimal rebate;

    /** 标签 */
    private List<TagModel> tags = new ArrayList<TagModel>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.autograph = member.getAutograph();
        this.fans = member.getFans().size();
        this.favorite = member.getFavorites().size();
        this.follow = member.getFollows().size();
        this.username = member.getUsername();
        this.nickName = member.displayName();
        this.logo = member.getLogo();
        this.tags = TagModel.bindList(member.getTags());
        this.balance = member.getBalance().setScale(2,BigDecimal.ROUND_HALF_DOWN);
        this.order = member.getOrders().size();
        int c=0;
        for (CouponCode couponCode:member.getCouponCodes()) {
            if (couponCode.getEnabled()) {
                c=c+1;
            }
        }
        this.coupon = c;
        this.rebate = BigDecimal.ZERO;
    }
}