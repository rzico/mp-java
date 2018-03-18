package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.MemberDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.ReceiverDao;
import net.wit.entity.*;
import net.wit.service.ReceiverService;

/**
 * @ClassName: ReceiverDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("receiverServiceImpl")
public class ReceiverServiceImpl extends BaseServiceImpl<Receiver, Long> implements ReceiverService {
	@Resource(name = "receiverDaoImpl")
	private ReceiverDao receiverDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "receiverDaoImpl")
	public void setBaseDao(ReceiverDao receiverDao) {
		super.setBaseDao(receiverDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Receiver receiver) {
		if (receiver.getIsDefault()) {
			for (Receiver r:receiver.getMember().getReceivers()) {
				r.setIsDefault(false);
				receiverDao.merge(r);
			}
		}
		Member member = receiver.getMember();
		if (member!=null) {
			if (member.getName()==null) {
				member.setName(receiver.getConsignee());
			};
			if (member.getMobile()==null && receiver.getPhone().startsWith("1")) {
				if (memberDao.findByMobile(receiver.getPhone())==null) {
					member.setMobile(receiver.getPhone());
				}
			}
		}
		memberDao.merge(member);
		super.save(receiver);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Receiver update(Receiver receiver) {
		if (receiver.getIsDefault()) {
			for (Receiver r:receiver.getMember().getReceivers()) {
				if (!r.equals(receiver)) {
					r.setIsDefault(false);
					receiverDao.merge(r);
				}
			}
		}
		Member member = receiver.getMember();
		if (member!=null) {
			if (member.getName()==null) {
				member.setName(receiver.getConsignee());
			};
			if (member.getMobile()==null && receiver.getPhone().startsWith("1")) {
				if (memberDao.findByMobile(receiver.getPhone())==null) {
					member.setMobile(receiver.getPhone());
				}
			}
		}
		memberDao.merge(member);
		return super.update(receiver);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Receiver update(Receiver receiver, String... ignoreProperties) {
		return super.update(receiver, ignoreProperties);
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
	public void delete(Receiver receiver) {
		super.delete(receiver);
	}

	public Page<Receiver> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return receiverDao.findPage(beginDate,endDate,pageable);
	}
}