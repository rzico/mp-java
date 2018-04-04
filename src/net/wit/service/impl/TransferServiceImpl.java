package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.DepositDao;
import net.wit.dao.MemberDao;
import net.wit.plat.unspay.UnsPay;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.TransferDao;
import net.wit.entity.*;
import net.wit.service.TransferService;

/**
 * @ClassName: TransferDaoImpl
 * @author 降魔战队
 * @date 2017-10-17 19:48:19
 */
 
 
@Service("transferServiceImpl")
public class TransferServiceImpl extends BaseServiceImpl<Transfer, Long> implements TransferService {
	@Resource(name = "transferDaoImpl")
	private TransferDao transferDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "transferDaoImpl")
	public void setBaseDao(TransferDao transferDao) {
		super.setBaseDao(transferDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Transfer transfer) {
		super.save(transfer);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Transfer update(Transfer transfer) {
		return super.update(transfer);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Transfer update(Transfer transfer, String... ignoreProperties) {
		return super.update(transfer, ignoreProperties);
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
	public void delete(Transfer transfer) {
		super.delete(transfer);
	}

	public Page<Transfer> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return transferDao.findPage(beginDate,endDate,pageable);
	}
	public Transfer findBySn(String sn) {
		return transferDao.findBySn(sn);
	}

	@Transactional
	public synchronized Boolean submit(Transfer transfer) throws Exception {
		Member member = transfer.getMember();
		memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
		if (member.getBalance().compareTo(transfer.getAmount()) < 0) {
			throw new RuntimeException("账户余额不足");
		}
		try {
			transferDao.persist(transfer);
			member.setBalance(member.getBalance().subtract(transfer.getAmount()));
			memberDao.merge(member);
			memberDao.flush();
			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			deposit.setType(Deposit.Type.transfer);
			deposit.setMemo(transfer.getMemo());
			deposit.setMember(member);
			deposit.setCredit(BigDecimal.ZERO);
			deposit.setDebit(transfer.getAmount());
			deposit.setDeleted(false);
			deposit.setOperator("system");
			deposit.setTransfer(transfer);
			deposit.setSeller(member);
			depositDao.persist(deposit);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new RuntimeException("提交出错了");
		}
		if (transfer.getType().equals(Transfer.Type.bankcard)) {
			String result = UnsPay.submit(transfer);
			if ("0000".equals(result)) {
				transfer.setStatus(Transfer.Status.confirmed);
				transferDao.merge(transfer);
				return true;
			} else {
				if (!"3000".equals(result)) {
					logger.error(UnsPay.getErrMsg(result));
					throw new RuntimeException("提交银行失败");
				} else {
					return true;
				}
			}
		} else {
			throw new RuntimeException("暂不支持");
		}
	}

	@Transactional
	public synchronized Boolean transfer(Transfer transfer) throws Exception {
		try {
			transferDao.refresh(transfer, LockModeType.PESSIMISTIC_WRITE);
			if (transfer != null && transfer.getStatus().equals(Transfer.Status.waiting)) {
				String result = UnsPay.submit(transfer);
				if ("0000".equals(result)) {
					transfer.setStatus(Transfer.Status.confirmed);
					transferDao.merge(transfer);
					return true;
				} else {
					throw new RuntimeException(UnsPay.getErrMsg(result));
				}
			} else{
				throw new RuntimeException("已经提交了");
			}
		} catch (Exception e) {
			throw new RuntimeException("提交出错了");
		}
	}

	@Transactional
	public synchronized void handle(Transfer transfer) throws Exception {
		transferDao.refresh(transfer, LockModeType.PESSIMISTIC_WRITE);
		if (transfer != null && !transfer.getStatus().equals(Transfer.Status.success)) {
			transfer.setTransferDate(new Date());
			transfer.setStatus(Transfer.Status.success);
			transferDao.merge(transfer);
		}
	}

	@Transactional
	public synchronized void refunds(Transfer transfer) throws Exception {
		transferDao.refresh(transfer, LockModeType.PESSIMISTIC_WRITE);
		if (transfer != null && !transfer.getStatus().equals(Transfer.Status.failure)) {
			//开始退款
			Member member = transfer.getMember();
			memberDao.refresh(member,LockModeType.PESSIMISTIC_WRITE);
			member.setBalance(member.getBalance().add(transfer.getAmount()));
			memberDao.merge(member);
			memberDao.flush();
			Deposit deposit = new Deposit();
			deposit.setBalance(member.getBalance());
			deposit.setType(Deposit.Type.transfer);
			deposit.setMemo("提现失败，退回账户");
			deposit.setMember(member);
			deposit.setCredit(BigDecimal.ZERO);
			deposit.setDebit(BigDecimal.ZERO.subtract(transfer.getAmount()));
			deposit.setDeleted(false);
			deposit.setOperator("system");
			deposit.setTransfer(transfer);
			deposit.setSeller(member);
			depositDao.persist(deposit);
			transfer.setTransferDate(new Date());
			transfer.setStatus(Transfer.Status.failure);
			transferDao.merge(transfer);
		} else {
			throw new RuntimeException("重复提交");
		}
	}

}