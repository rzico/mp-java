package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import com.chuanglan.sms.request.SmsSendRequest;
import com.chuanglan.sms.response.SmsSendResponse;
import com.chuanglan.sms.util.ChuangLanSmsUtil;
import net.sf.json.JSON;
import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.DepositDao;
import net.wit.dao.MemberDao;
import net.wit.plat.im.Push;
import net.wit.service.MessageService;
import net.wit.util.JsonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.SmssendDao;
import net.wit.entity.*;
import net.wit.service.SmssendService;

/**
 * @ClassName: SmssendDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("smssendServiceImpl")
public class SmssendServiceImpl extends BaseServiceImpl<Smssend, Long> implements SmssendService {
	@Resource(name = "smssendDaoImpl")
	private SmssendDao smssendDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "smssendDaoImpl")
	public void setBaseDao(SmssendDao smssendDao) {
		super.setBaseDao(smssendDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Smssend smssend) {
		super.save(smssend);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Smssend update(Smssend smssend) {
		return super.update(smssend);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Smssend update(Smssend smssend, String... ignoreProperties) {
		return super.update(smssend, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Smssend smssend) {
		super.delete(smssend);
	}

	/**
	 * 添加发送任务
	 */
	private void addTask(final String mobile,final String content) {
		try {
			taskExecutor.execute(new Runnable() {
				public void run() {
					ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
					String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
					String msg = "【" + bundle.getString("signature") + "】"+content;
					String phone = mobile;
					String report= "true";
					String r = "0000";
					try {
						SmsSendRequest smsSingleRequest = new SmsSendRequest(bundle.getString("softwareSerialNo"), bundle.getString("password"), msg, phone, report);
						String requestJson = JsonUtils.toJson(smsSingleRequest);
						String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
						SmsSendResponse smsSingleResponse = JsonUtils.toObject(response, SmsSendResponse.class);
						if (smsSingleResponse.getCode().equals("0")) {
							r = "0000";
						} else {
							r = "-"+smsSingleResponse.getErrorMsg();
						}
					} catch (Exception e) {
						r = "-9999";
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Page<Smssend> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return smssendDao.findPage(beginDate,endDate,pageable);
	}

	public String smsSend(Smssend smssend) {
		smssend.setFee(BigDecimal.ZERO);
		super.save(smssend);
		this.addTask(smssend.getMobile(),smssend.getContent());
		return "true";
	}

	public String send(Member member,String mobile,String content) {
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		if (member.getBalance().compareTo(new BigDecimal("0.1"))>=0) {
			Smssend smssend = new Smssend();
			smssend.setMobile(mobile);
			smssend.setContent(content);
			smssend.setMember(member);
			smssend.setFee(new BigDecimal("0.1"));
			super.save(smssend);
			member.setBalance(member.getBalance().subtract(smssend.getFee()));
			memberDao.merge(member);
			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			deposit.setType(Deposit.Type.smsSend);
			deposit.setMemo("手机短信");
			deposit.setMember(member);
			deposit.setCredit(BigDecimal.ZERO);
			deposit.setDebit(smssend.getFee());
			deposit.setDeleted(false);
			deposit.setOperator("system");
			depositDao.persist(deposit);

			this.addTask(smssend.getMobile(),smssend.getContent());
			messageService.depositPushTo(deposit);
			return "true";
		} else {
			return "false";
		}
	}

}