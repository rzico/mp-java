package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.*;
import net.wit.Filter.Operator;

import net.wit.dao.MemberDao;
import net.wit.entity.Message;
import net.wit.plat.im.Push;
import net.wit.plat.im.User;
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

	@Transactional
	public Boolean pushTo(Message message) {
		try {
			String userName = "gm_"+String.valueOf(10200+message.getType().ordinal());
			Member sender = memberDao.findByUsername(userName);
			if (sender==null) {
				sender = new Member();
				sender.setUsername(userName);
				sender.setNickName(message.getTitle());
				sender.setLogo("http://cdn.rzico.com/weex/resources/images/"+userName+".png");
				sender.setPoint(0L);
				sender.setBalance(BigDecimal.ZERO);
				sender.setIsEnabled(true);
				sender.setIsLocked(false);
				sender.setLoginFailureCount(0);
				sender.setRegisterIp("127.0.0.1");
				memberDao.persist(sender);
			}
			User.userAttr(sender);
			message.setMember(sender);
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
	public Boolean depositPushTo(Deposit deposit) {
        Message msg = new Message();
        msg.setReaded(false);
        msg.setReceiver(deposit.getMember());
        msg.setType(Message.Type.account);
		msg.setThumbnial("http://cdn.rzico.com/weex/resources/images/account.png");
		msg.setTitle("账单提醒");
		if (Deposit.Type.cashier.equals(deposit.getType())) {
			BigDecimal amount = deposit.getCredit().subtract(deposit.getDebit());
			if (amount.compareTo(BigDecimal.ZERO)>0) {
				java.text.DecimalFormat   df   =  new   java.text.DecimalFormat("#.00");
				msg.setContent(deposit.getMemo()+",到账:"+df.format(amount)+"元");
			} else {
				java.text.DecimalFormat   df   =  new   java.text.DecimalFormat("#.00");
				msg.setContent(deposit.getMemo()+",退款:"+df.format(BigDecimal.ZERO.subtract(amount))+"元");
			}
		} else {
			BigDecimal amount = deposit.getCredit().subtract(deposit.getDebit());
			if (amount.compareTo(BigDecimal.ZERO)>0) {
				java.text.DecimalFormat   df   =  new   java.text.DecimalFormat("#.00");
				msg.setContent("账户收入:"+df.format(amount)+"元,来源:"+deposit.getMemo());
			} else {
				java.text.DecimalFormat   df   =  new   java.text.DecimalFormat("#.00");
				msg.setContent("账户支出:"+df.format(BigDecimal.ZERO.subtract(amount))+"元,用途:"+deposit.getMemo());
			}
		}
		return pushTo(msg);
	}
}