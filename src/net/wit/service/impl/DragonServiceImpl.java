package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DragonDao;
import net.wit.entity.Article;
import net.wit.entity.Dragon;
import net.wit.entity.Member;
import net.wit.service.DragonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: DragonServiceImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("dragonServiceImpl")
public class DragonServiceImpl extends BaseServiceImpl<Dragon, Long> implements DragonService {
	@Resource(name = "dragonDaoImpl")
	private DragonDao dragonDao;

	@Resource(name = "dragonDaoImpl")
	public void setBaseDao(DragonDao dragonDao) {
		super.setBaseDao(dragonDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Dragon dragon) {
		super.save(dragon);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Dragon update(Dragon dragon) {
		return super.update(dragon);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Dragon update(Dragon dragon, String... ignoreProperties) {
		return super.update(dragon, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		Dragon dragon = dragonDao.find(id);
		dragon.setDeleted(true);
		super.update(dragon);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			this.delete(id);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Dragon dragon) {
		dragon.setDeleted(true);
		super.update(dragon);
	}

	public Page<Dragon> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return dragonDao.findPage(beginDate,endDate,pageable);
	}

	public Dragon find(Article article, Member member) {
		return dragonDao.find(article,member);
	}

}