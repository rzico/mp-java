package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: MessageService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface MessageService extends BaseService<Message, Long> {
	Page<Message> findPage(Date beginDate,Date endDate, Pageable pageable);
	public Member GMInit(Message.Type type);
	public Boolean pushTo(Message message);
	//活动专栏
	public Boolean topicPushTo(Topic topic);
	//账单提醒
	public Boolean depositPushTo(Deposit deposit);
	//赞赏提醒
	public Boolean rewardPushTo(ArticleReward reward);
	//分享提醒
	public Boolean sharePushTo(ArticleShare share);
	//发布提醒
	public Boolean publishPushTo(Article article,Member receiver);
	//收藏提醒
	public Boolean favoritePushTo(ArticleFavorite favorite);
	//关注提醒
	public Boolean followPushTo(MemberFollow follow);
	//点赞提醒
	public Boolean laudPushTo(ArticleLaud laud);
	//评论提醒
	public Boolean reviewPushTo(ArticleReview review);
	//添加好友
	public Boolean addFriendPushTo(Member member,Member friend);
	//发展成员提醒
	public Boolean addPromoterPushTo(Member member,Member promoter);
	//同意好友
	public Boolean adoptFriendPushTo(Member member,Member friend);
	public void login(Member member,HttpServletRequest request);
	public Boolean payBillPushTo(PayBill payBill);
	//订单提醒
	public Boolean orderMemberPushTo(OrderLog orderLog);

	//订单提醒
	public Boolean orderSellerPushTo(OrderLog orderLog);

	//送货提醒
	public Boolean shippingPushTo(Shipping shipping,OrderLog orderLog);

	//送货提醒
	public Boolean shippingAdminPushTo(Shipping shipping,OrderLog orderLog);

}