package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.MemberDao;
import net.wit.dao.PaymentDao;
import net.wit.dao.impl.ArticleDaoImpl;
import net.wit.plat.im.User;
import net.wit.service.MessageService;
import net.wit.service.SnService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.RedPackageDao;
import net.wit.entity.*;
import net.wit.service.RedPackageService;

/**
 * @ClassName: RedPackageDaoImpl
 * @author 降魔战队
 * @date 2018-7-6 10:38:17
 */
 
 
@Service("redPackageServiceImpl")
public class RedPackageServiceImpl extends BaseServiceImpl<RedPackage, Long> implements RedPackageService {
	@Resource(name = "redPackageDaoImpl")
	private RedPackageDao redPackageDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;
	@Resource(name = "articleDaoImpl")
	private ArticleDaoImpl articleDao;

	@Resource(name = "snServiceImpl")
	private SnService snService;


	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "redPackageDaoImpl")
	public void setBaseDao(RedPackageDao redPackageDao) {
		super.setBaseDao(redPackageDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(RedPackage redPackage) {
		super.save(redPackage);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public RedPackage update(RedPackage redPackage) {
		return super.update(redPackage);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public RedPackage update(RedPackage redPackage, String... ignoreProperties) {
		return super.update(redPackage, ignoreProperties);
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
	public void delete(RedPackage redPackage) {
		super.delete(redPackage);
	}

	public Page<RedPackage> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return redPackageDao.findPage(beginDate,endDate,pageable);
	}
	/**
	 * 获取随机红包方法， 该方法运行完需要更改数据库数据
	 * @return
	 */
	public double getRandomMoney(Article article) {
		ArticleRedPackage articleRedPackage = article.getArticleRedPackage();
        Long remainSize = articleRedPackage.getRemainSize();

        double remainMoney = articleRedPackage.getAmount().doubleValue();
        ArticleRedPackage.RedPackageType redPackageType = articleRedPackage.getRedPackageType();

        if(redPackageType == ArticleRedPackage.RedPackageType.AVE){//平均
			double getmoney = remainMoney / remainSize;
			articleRedPackage.setAmount(new BigDecimal(remainMoney - getmoney));
			articleRedPackage.setRemainSize(--remainSize);
			article.setArticleRedPackage(articleRedPackage);
			articleDao.merge(article);//更新红包金额

            return getmoney;
        }else{
            // remainSize 剩余的红包数量
            // remainMoney 剩余的钱
            if (remainSize == 1) {
                remainSize--;
				articleRedPackage.setAmount(new BigDecimal(0));
				articleRedPackage.setRemainSize(remainSize);
				article.setArticleRedPackage(articleRedPackage);
				articleDao.merge(article);//更新红包金额
                return (double) Math.round(remainMoney * 100) / 100;
            }else{
				Random r     = new Random();
				double min   = 0.01;
				double max   = remainMoney / remainSize * 2;
				double money = r.nextDouble() * max;
				money = money <= min ? 0.01: money;
				money = Math.floor(money * 100) / 100;
				remainSize--;
				remainMoney -= money;
				articleRedPackage.setAmount(new BigDecimal(remainMoney));
				articleRedPackage.setRemainSize(remainSize);
				article.setArticleRedPackage(articleRedPackage);
				articleDao.merge(article);//更新红包金额
				return money;
			}
        }
	}

	public synchronized double getRedPackage(RedPackage redPackage) {
		if(redPackage.getStatus().equals(RedPackage.Status.get)){//如果是领取红包的话
			//开始领红包操作
			//先判断有没有领取资格 1、红包有钱 2、 红包他没领过
			ArticleRedPackage articleRedPackage = redPackage.getArticle().getArticleRedPackage();
			boolean have1 = articleRedPackage.getRemainSize() > 0 && articleRedPackage.getAmount().doubleValue() > 0.0;
			List<RedPackage> redPackages = redPackageDao.findByRedPackage(redPackage);
			boolean have2 = redPackages == null || redPackages.size() == 0;

			boolean have3 = articleRedPackage.getIsPay();
			if(!have1){//已经领完
				return -2.0;
			}
			if(!have2){//表示已领取
				return 0.0;
			}
			if(have3){
				double getMoney = getRandomMoney(redPackage.getArticle());
				if(getMoney > 0.0){//说明领取成功了
					redPackage.setAmount(new BigDecimal(getMoney));
					redPackageDao.persist(redPackage);
					Member member = redPackage.getMember();
					member.setBalance(member.getBalance().add(new BigDecimal(getMoney)));
					memberDao.merge(member);
					//这里需要推送
					messageService.redPackagePushTo(redPackage);
					return getMoney;
				}
			}
		}

		return -1.0;//表示领取失败
	}

	public synchronized Payment sendRedPackage(RedPackage redPackage) {
		redPackageDao.persist(redPackage);
		String userName = "gm_"+String.valueOf(10200+Message.Type.account.ordinal());
		Member payee = memberDao.findByUsername(userName);
		if (payee==null) {
			payee = new Member();
			payee.setUsername(userName);
			payee.setNickName("账单提醒");
			payee.setLogo("http://cdn.rzico.com/weex/resources/images/"+userName+".png");
//			payee.setPoint(0L);
			payee.setBalance(BigDecimal.ZERO);
			payee.setIsEnabled(true);
			payee.setIsLocked(false);
			payee.setLoginFailureCount(0);
			payee.setRegisterIp("127.0.0.1");
			memberDao.persist(payee);
			User.userAttr(payee);
		}
		Payment payment = new Payment();
		payment.setMemo("发送红包");
		payment.setSn(snService.generate(Sn.Type.redpackage));
		payment.setMethod(Payment.Method.online);
		payment.setMember(redPackage.getMember());
		payment.setPayee(payee);
		payment.setAmount(redPackage.getAmount());
		payment.setType(Payment.Type.redpackage);
		payment.setAmount(redPackage.getAmount());
		payment.setRedPackage(redPackage);
		payment.setStatus(Payment.Status.waiting);
		paymentDao.persist(payment);
		redPackage.setPayment(payment);
		redPackageDao.merge(redPackage);
		return payment;
	}
}