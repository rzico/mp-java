package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.util.JSONUtils;
import net.wit.*;
import net.wit.Filter.Operator;

import net.wit.controller.model.ArticleListModel;
import net.wit.controller.model.DepositModel;
import net.wit.dao.MemberDao;
import net.wit.entity.Message;
import net.wit.plat.im.Push;
import net.wit.plat.im.User;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.MessageDao;
import net.wit.entity.*;
import net.wit.service.MessageService;

/**
 * @ClassName: MessageDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("messageServiceImpl")
public class MessageServiceImpl extends BaseServiceImpl<Message, Long> implements MessageService {
	@Resource(name = "messageDaoImpl")
	private MessageDao messageDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "messageDaoImpl")
	public void setBaseDao(MessageDao messageDao) {
		super.setBaseDao(messageDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Message message) {
		super.save(message);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Message update(Message message) {
		return super.update(message);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Message update(Message message, String... ignoreProperties) {
		return super.update(message, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		Message m = super.find(id);
		m.setDeleted(true);
		super.update(m);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			Message m = super.find(id);
			m.setDeleted(true);
			super.update(m);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Message message) {
		message.setDeleted(true);
		super.update(message);
	}

	public Member GMInit(Message.Type type) {
		String userName = "gm_"+String.valueOf(10200+type.ordinal());
		Member sender = memberDao.findByUsername(userName);
		if (sender==null) {
			sender = new Member();
			sender.setUsername(userName);
			String title = "";
			if (type.equals(Message.Type.account)) {
				title = "账单提醒";
			} else
			if (type.equals(Message.Type.addfriend)) {
				title = "添加好友";
			} else
			if (type.equals(Message.Type.adoptfriend)) {
				title = "同意好友";
			} else
			if (type.equals(Message.Type.favorite)) {
				title = "收款提醒";
			} else
			if (type.equals(Message.Type.follow)) {
				title = "关注提醒";
			} else
			if (type.equals(Message.Type.gmchat)) {
				title = "系统客服";
			} else
			if (type.equals(Message.Type.laud)) {
				title = "点赞提醒";
			} else
			if (type.equals(Message.Type.message)) {
				title = "系统消息";
			} else
			if (type.equals(Message.Type.order)) {
				title = "订单提醒";
			} else
			if (type.equals(Message.Type.review)) {
				title = "评价提醒";
			} else
			if (type.equals(Message.Type.reward)) {
				title = "点赞提醒";
			} else
			if (type.equals(Message.Type.share)) {
				title = "文章分享";
			} else
			{
				title = "系统消息";
			}
			sender.setNickName( title);
			sender.setLogo("http://cdn.rzico.com/weex/resources/images/"+userName+".png");
			sender.setPoint(0L);
			sender.setBalance(BigDecimal.ZERO);
			sender.setIsEnabled(true);
			sender.setIsLocked(false);
			sender.setLoginFailureCount(0);
			sender.setRegisterIp("127.0.0.1");
			memberDao.persist(sender);
			User.userAttr(sender);
			return sender;
		} else {
			User.userAttr(sender);
			return sender;
		}
	}

	@Transactional
	public Boolean pushTo(Message message) {
		try {
			Member sender = GMInit(message.getType());
			if (message.getThumbnial()==null) {
				message.setThumbnial(sender.getLogo());
			}
			message.setReaded(false);
			message.setDeleted(false);
			message.setSender(sender);
			if (message.getMember()==null) {
				message.setMember(sender);
			}
			super.save(message);
			Push.impush(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Page<Message> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return messageDao.findPage(beginDate,endDate,pageable);
	}

	//赞赏提醒
	public Boolean rewardPushTo(ArticleReward reward) {
		Message msg = new Message();
		msg.setReceiver(reward.getAuthor());
		msg.setMember(reward.getMember());
		msg.setType(Message.Type.reward);
		msg.setThumbnial(reward.getMember().getLogo());
		msg.setTitle("【"+reward.getMember().getNickName()+"】赞赏了你");
		msg.setContent(""+reward.getMember().getNickName()+"赞赏你:"+reward.getAmount()+"元,文章:"+reward.getArticle().getTitle());
		ArticleListModel ext = new ArticleListModel();
		ext.bind(reward.getArticle());
		msg.setExt(JsonUtils.toJson(ext));
		return pushTo(msg);
	}

	//收藏提醒
	public Boolean favoritePushTo(ArticleFavorite favorite) {
		Message msg = new Message();
		msg.setMember(favorite.getMember());
		msg.setReceiver(favorite.getArticle().getMember());
		msg.setType(Message.Type.favorite);
		msg.setThumbnial(favorite.getMember().getLogo());
		msg.setTitle("【"+favorite.getMember().getNickName()+"】收藏了您的文章");
		msg.setContent("【"+favorite.getMember().getNickName()+"】收藏了您的文章:"+favorite.getArticle().getTitle());
		ArticleListModel ext = new ArticleListModel();
		ext.bind(favorite.getArticle());
		msg.setExt(JsonUtils.toJson(ext));
		return pushTo(msg);
	}

	//分享提醒
	public Boolean sharePushTo(ArticleShare share) {
		Setting setting = SettingUtils.get();
		Message msg = new Message();
		msg.setMember(share.getMember());
		msg.setReceiver(share.getArticle().getMember());
		msg.setType(Message.Type.share);
		msg.setThumbnial(share.getMember().getLogo());
		String shareDescr = "";
		if (share.getShareType().equals(ArticleShare.ShareType.appMessage)) {
			shareDescr = "微信好友";
		} else
		if (share.getShareType().equals(ArticleShare.ShareType.timeline)) {
			shareDescr = "微信朋友圈";
		} else
		if (share.getShareType().equals(ArticleShare.ShareType.shareQQ)) {
			shareDescr = "QQ好友";
		} else
		if (share.getShareType().equals(ArticleShare.ShareType.shareQZone)) {
			shareDescr = "QQ空间";
		} else {
			shareDescr = setting.getSiteName()+"好友";
		}
		msg.setTitle("【"+share.getMember().getNickName()+"】分享你的文章至"+shareDescr);
		msg.setContent("【"+share.getMember().getNickName()+"】分享你的文章:"+share.getArticle().getTitle());
		ArticleListModel ext = new ArticleListModel();
		ext.bind(share.getArticle());
		msg.setExt(JsonUtils.toJson(ext));
		return pushTo(msg);
	}

	//关注提醒
	public Boolean followPushTo(MemberFollow follow) {
		Message msg = new Message();
		msg.setMember(follow.getMember());
		msg.setReceiver(follow.getFollow());
		msg.setType(Message.Type.follow);
		msg.setThumbnial(follow.getMember().getLogo());
		msg.setTitle(follow.getMember().getNickName());
		msg.setContent("【"+follow.getMember().getNickName()+"】关注了您，将订阅您的动态");
		return pushTo(msg);
	}

	//点赞提醒
	public Boolean laudPushTo(ArticleLaud laud) {
		Message msg = new Message();
		msg.setMember(laud.getMember());
		msg.setReceiver(laud.getArticle().getMember());
		msg.setType(Message.Type.laud);
		msg.setThumbnial(laud.getMember().getLogo());
		msg.setTitle("【"+laud.getMember().getNickName()+"】点赞了您的文章");
		msg.setContent("【"+laud.getMember().getNickName()+"】点赞了您的文章:"+laud.getArticle().getTitle());
		return pushTo(msg);
	}

	//评论提醒
	public Boolean reviewPushTo(ArticleReview review) {
		Message msg = new Message();
		msg.setMember(review.getMember());
		msg.setReceiver(review.getArticle().getMember());
		msg.setType(Message.Type.favorite);
		msg.setThumbnial(review.getMember().getLogo());
		msg.setTitle("【"+review.getMember().getNickName()+"】评论了您的文章");
		msg.setContent("【"+review.getMember().getNickName()+"】评论了您的文章:"+review.getContent());
		return pushTo(msg);
	}

	//添加好友
	public Boolean addFriendPushTo(Member member,Member friend) {
		Message msg = new Message();
		msg.setMember(member);
		msg.setReceiver(friend);
		msg.setType(Message.Type.addfriend);
		msg.setThumbnial(member.getLogo());
		msg.setTitle(member.getNickName());
		msg.setContent("【"+member.getNickName()+"】申请成为你的好友。");
		msg.setExt("friend.add");
		return pushTo(msg);
	}

	//同意好友
	public Boolean adoptFriendPushTo(Member member,Member friend) {
		Message msg = new Message();
		msg.setMember(member);
		msg.setReceiver(friend);
		msg.setType(Message.Type.adoptfriend);
		msg.setThumbnial(member.getLogo());
		msg.setTitle(member.getNickName());
		msg.setContent("【"+member.getNickName()+"】同意成为你的好友。");
		msg.setExt("friend.adopt");
		return pushTo(msg);
	}

	//活动专栏
	public Boolean topicPushTo(Topic topic) {
		Message msg = new Message();
		msg.setMember(topic.getMember());
		msg.setReceiver(topic.getMember());
		msg.setType(Message.Type.message);
		msg.setTitle("活动专栏");
		msg.setContent("【"+topic.getMember().getNickName()+"】感谢您点亮专栏，您已拥有VIP特权。");
		return pushTo(msg);
	}

	public Boolean depositPushTo(Deposit deposit) {
        Message msg = new Message();
        msg.setReceiver(deposit.getMember());
        msg.setType(Message.Type.account);
        if (deposit.getType().equals(Deposit.Type.cashier)) {
			msg.setMember(deposit.getPayment().getMember());
			msg.setThumbnial(msg.getMember().getLogo());
			msg.setTitle("线下收单");
		} else
		if (deposit.getType().equals(Deposit.Type.recharge)) {
			msg.setTitle("钱包充值");
		} else
		if (deposit.getType().equals(Deposit.Type.transfer)) {
			msg.setTitle("提现支出");
		} else
		if (deposit.getType().equals(Deposit.Type.payment)) {
			msg.setMember(deposit.getPayment().getPayee());
			msg.setThumbnial(msg.getMember().getLogo());
			msg.setTitle("消费支出");
		} else
		if (deposit.getType().equals(Deposit.Type.refunds)) {
			msg.setMember(deposit.getPayment().getPayee());
			msg.setThumbnial(msg.getMember().getLogo());
			msg.setTitle("消费退款");
		} else
		if (deposit.getType().equals(Deposit.Type.product)) {
			msg.setTitle("资金结算");
		} else
		if (deposit.getType().equals(Deposit.Type.reward)) {
			msg.setMember(deposit.getPayment().getMember());
			msg.setThumbnial(msg.getMember().getLogo());
			msg.setTitle("赞赏收入");
		} else
		if (deposit.getType().equals(Deposit.Type.rebate)) {
			msg.setTitle("奖励收入");
		} else {
			msg.setTitle("账单提醒");
		}
		BigDecimal amount = deposit.getCredit().subtract(deposit.getDebit());
		if (amount.compareTo(BigDecimal.ZERO)>0) {
			java.text.DecimalFormat   df   =  new   java.text.DecimalFormat("#.00");
			msg.setContent("账户收入:"+df.format(amount)+"元,来源:"+deposit.getMemo());
		} else {
			java.text.DecimalFormat   df   =  new   java.text.DecimalFormat("#.00");
			msg.setContent("账户支出:"+df.format(BigDecimal.ZERO.subtract(amount))+"元,用途:"+deposit.getMemo());
		}
		DepositModel ext = new DepositModel();
		ext.bind(deposit);
		msg.setExt(JsonUtils.toJson(ext));
		return pushTo(msg);
	}
}