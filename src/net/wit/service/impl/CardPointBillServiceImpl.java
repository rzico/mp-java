package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.CardPointBillDao;
import net.wit.entity.CardPointBill;
import net.wit.service.CardPointBillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.util.Date;

/**
 * @ClassName: CardBillDaoImpl
 * @author 降魔战队
 * @date 2017-11-4 18:12:27
 */
 
 
@Service("cardPointBillServiceImpl")
public class CardPointBillServiceImpl extends BaseServiceImpl<CardPointBill, Long> implements CardPointBillService {
	@Resource(name = "cardPointBillDaoImpl")
	private CardPointBillDao cardPointBillDao;

	@Resource(name = "cardPointBillDaoImpl")
	public void setBaseDao(CardPointBillDao cardPointBillDao) {
		super.setBaseDao(cardPointBillDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(CardPointBill cardPointBill) {
		super.save(cardPointBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CardPointBill update(CardPointBill cardPointBill) {
		return super.update(cardPointBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CardPointBill update(CardPointBill cardPointBill, String... ignoreProperties) {
		return super.update(cardPointBill, ignoreProperties);
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
	public void delete(CardPointBill cardPointBill) {
		super.delete(cardPointBill);
	}

	public Page<CardPointBill> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return cardPointBillDao.findPage(beginDate,endDate,pageable);
	}

}