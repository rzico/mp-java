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

import net.wit.dao.*;
import net.wit.entity.summary.RebateSummary;
import net.wit.service.MessageService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;
import net.wit.service.RebateService;

/**
 * @ClassName: RebateDaoImpl
 * @author 降魔战队
 * @date 2018-4-24 20:57:53
 */
 
 
@Service("rebateServiceImpl")
public class RebateServiceImpl extends BaseServiceImpl<Rebate, Long> implements RebateService {
	@Resource(name = "rebateDaoImpl")
	private RebateDao rebateDao;

	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao;

	@Resource(name = "enterpriseDaoImpl")
	private EnterpriseDao enterpriseDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "rebateDaoImpl")
	public void setBaseDao(RebateDao rebateDao) {
		super.setBaseDao(rebateDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Rebate rebate) {
		super.save(rebate);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Rebate update(Rebate rebate) {
		return super.update(rebate);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Rebate update(Rebate rebate, String... ignoreProperties) {
		return super.update(rebate, ignoreProperties);
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
	public void delete(Rebate rebate) {
		super.delete(rebate);
	}

	public Page<Rebate> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return rebateDao.findPage(beginDate,endDate,pageable);
	}

	public Page<RebateSummary> sumPage(Date beginDate, Date endDate, Enterprise enterprise,Pageable pageable) {
		return rebateDao.sumPage(beginDate,endDate,enterprise,pageable);
	}
	public RebateSummary sum(Date beginDate, Date endDate, Enterprise enterprise, Member member) {
		return rebateDao.sum(beginDate,endDate,enterprise,member);
	}

	public void rebate(BigDecimal amount,Member buyer,Enterprise personal,Enterprise agent,Enterprise operate,Order order) throws Exception {
       if (personal!=null) {
		   Member member = personal.getMember();
		   BigDecimal rebate = amount.multiply(personal.getBrokerage()).multiply(new BigDecimal("0.01")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
		   if (rebate.compareTo(BigDecimal.ZERO)>0) {
			   memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
			   member.setBalance(member.getBalance().add(rebate));
			   memberDao.merge(member);
			   memberDao.flush();
			   Deposit d1 = new Deposit();
			   d1.setBalance(member.getBalance());
			   d1.setType(Deposit.Type.rebate);
			   d1.setMemo("代理奖励金");
			   d1.setMember(member);
			   d1.setCredit(rebate);
			   d1.setDebit(BigDecimal.ZERO);
			   d1.setDeleted(false);
			   d1.setOperator("system");
			   d1.setOrder(order);
			   if (order != null) {
				   d1.setSeller(order.getSeller());
			   }
			   depositDao.persist(d1);
			   messageService.depositPushTo(d1);

			   Rebate rb = new Rebate();
			   rb.setAmount(rebate);
			   rb.setDirect(rebate);
			   rb.setIndirect(BigDecimal.ZERO);
			   if (order!=null) {
			   	   rb.setAmount(order.getAmount());
			   } else {
				   rb.setAmount(BigDecimal.ZERO);
			   }
			   rb.setMember(buyer);
			   rb.setEnterprise(personal);
			   rebateDao.persist(rb);
		   }
	   }
		if (agent!=null) {
			Member member = agent.getMember();
			BigDecimal rebate = amount.multiply(agent.getBrokerage()).multiply(new BigDecimal("0.01")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			if (rebate.compareTo(BigDecimal.ZERO)>0) {
				memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
				member.setBalance(member.getBalance().add(rebate));
				memberDao.merge(member);
				memberDao.flush();
				Deposit d1 = new Deposit();
				d1.setBalance(member.getBalance());
				d1.setType(Deposit.Type.rebate);
				d1.setMemo("代理奖励金");
				d1.setMember(member);
				d1.setCredit(rebate);
				d1.setDebit(BigDecimal.ZERO);
				d1.setDeleted(false);
				d1.setOperator("system");
				d1.setOrder(order);
				if (order != null) {
					d1.setSeller(order.getSeller());
				}
				depositDao.persist(d1);
				messageService.depositPushTo(d1);

				Rebate rb = new Rebate();
				if (order!=null) {
					rb.setAmount(order.getAmount());
				} else {
					rb.setAmount(BigDecimal.ZERO);
				}
				if (personal!=null) {
					rb.setMember(personal.getMember());
					rb.setDirect(BigDecimal.ZERO);
					rb.setIndirect(rebate);
				} else {
					rb.setMember(buyer);
					rb.setDirect(rebate);
					rb.setIndirect(BigDecimal.ZERO);
				}
				rb.setEnterprise(agent);
				rebateDao.persist(rb);
			}
		}
		if (operate!=null) {
			Member member = operate.getMember();
			BigDecimal rebate = amount.multiply(operate.getBrokerage()).multiply(new BigDecimal("0.01")).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			if (rebate.compareTo(BigDecimal.ZERO)>0) {
				memberDao.refresh(member, LockModeType.PESSIMISTIC_WRITE);
				member.setBalance(member.getBalance().add(rebate));
				memberDao.merge(member);
				memberDao.flush();
				Deposit d1 = new Deposit();
				d1.setBalance(member.getBalance());
				d1.setType(Deposit.Type.rebate);
				d1.setMemo("代理奖励金");
				d1.setMember(member);
				d1.setCredit(rebate);
				d1.setDebit(BigDecimal.ZERO);
				d1.setDeleted(false);
				d1.setOperator("system");
				d1.setOrder(order);
				if (order != null) {
					d1.setSeller(order.getSeller());
				}
				depositDao.persist(d1);
				messageService.depositPushTo(d1);


				Rebate rb = new Rebate();
				if (order!=null) {
					rb.setAmount(order.getAmount());
				} else {
					rb.setAmount(BigDecimal.ZERO);
				}
				if (agent!=null) {
					rb.setMember(agent.getMember());
					rb.setDirect(BigDecimal.ZERO);
					rb.setIndirect(rebate);
				} else {
					rb.setMember(buyer);
					rb.setDirect(rebate);
					rb.setIndirect(BigDecimal.ZERO);
				}
				rb.setEnterprise(operate);
				rebateDao.persist(rb);

			}
		}
	}


	public void link(Member member,Member promoter) throws Exception {
		if (promoter!=null) {
			if (member.getPromoter()==null) {
				member.setPromoter(promoter);
			}
			Admin admin = adminDao.findByMember(promoter);
			if (admin!=null && admin.getEnterprise()!=null) {
				Enterprise enterprise = admin.getEnterprise();
				if (enterprise.getStatus().equals(Enterprise.Status.success)) {
					if (enterprise.getType().equals(Enterprise.Type.operate)) {
						member.setOperate(enterprise);
					} else
					if (enterprise.getType().equals(Enterprise.Type.agent)) {
						member.setAgent(enterprise);
					} else
					if (enterprise.getType().equals(Enterprise.Type.personal)) {
						member.setPersonal(enterprise);
					}
				}

				//
				Enterprise e1 = enterprise.getParent();
				if (e1!=null && !enterprise.getType().equals(Enterprise.Type.operate)) {
					if (e1.getStatus().equals(Enterprise.Status.success)) {
						if (e1.getType().equals(Enterprise.Type.operate)) {
							member.setOperate(e1);
						} else if (e1.getType().equals(Enterprise.Type.agent)) {
							member.setAgent(e1);
						} else if (e1.getType().equals(Enterprise.Type.personal)) {
							member.setPersonal(e1);
						}
					}

					//
					Enterprise e2 = e1.getParent();
					if (!e1.getType().equals(Enterprise.Type.operate) && e2!=null) {
						if (e2.getStatus().equals(Enterprise.Status.success)) {
							if (e2.getType().equals(Enterprise.Type.operate)) {
								member.setOperate(e2);
							} else if (e2.getType().equals(Enterprise.Type.agent)) {
								member.setAgent(e2);
							} else if (e2.getType().equals(Enterprise.Type.personal)) {
								member.setPersonal(e2);
							}
						}
					}
				}


			}
		}
		memberDao.merge(member);
	}

	public void link(Order order) throws Exception {
		Member promoter = order.getPromoter();
		if (promoter!=null) {
			Admin admin = adminDao.findByMember(promoter);
			if (admin!=null && admin.getEnterprise()!=null) {
				Enterprise enterprise = admin.getEnterprise();
				if (enterprise.getStatus().equals(Enterprise.Status.success)) {
					if (enterprise.getType().equals(Enterprise.Type.operate)) {
						order.setOperate(enterprise);
					} else
					if (enterprise.getType().equals(Enterprise.Type.agent)) {
						order.setAgent(enterprise);
					} else
					if (enterprise.getType().equals(Enterprise.Type.personal)) {
						order.setPersonal(enterprise);
					}
				}

				//
				Enterprise e1 = enterprise.getParent();
				if (e1!=null && !enterprise.getType().equals(Enterprise.Type.operate)) {
					if (e1.getStatus().equals(Enterprise.Status.success)) {
						if (e1.getType().equals(Enterprise.Type.operate)) {
							order.setOperate(e1);
						} else if (e1.getType().equals(Enterprise.Type.agent)) {
							order.setAgent(e1);
						} else if (e1.getType().equals(Enterprise.Type.personal)) {
							order.setPersonal(e1);
						}
					}

					//
					Enterprise e2 = e1.getParent();
					if (e2!=null && e1.getType().equals(Enterprise.Type.operate)) {
						if (e2.getStatus().equals(Enterprise.Status.success)) {
							if (e2.getType().equals(Enterprise.Type.operate)) {
								order.setOperate(e2);
							} else if (e2.getType().equals(Enterprise.Type.agent)) {
								order.setAgent(e2);
							} else if (e2.getType().equals(Enterprise.Type.personal)) {
								order.setPersonal(e2);
							}
						}
					}
				}


			}
		}
	}

}