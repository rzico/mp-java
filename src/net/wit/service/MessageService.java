package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

/**
 * @ClassName: MessageService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface MessageService extends BaseService<Message, Long> {
	Page<Message> findPage(Date beginDate,Date endDate, Pageable pageable);
	public Boolean pushTo(Message message);
	//活动专栏
	public Boolean topicPushTo(Topic topic);
	//账单提醒
	public Boolean depositPushTo(Deposit deposit);
	//赞赏提醒
	public Boolean rewardPushTo(ArticleReward reward);
	//分享提醒
	public Boolean sharePushTo(ArticleShare share);
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
	//同意好友
	public Boolean adoptFriendPushTo(Member member,Member friend);
}