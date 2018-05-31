package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.XdictDao;
import net.wit.entity.Xdict;
import net.wit.service.XdictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: XdictDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("xdictServiceImpl")
public class XdictServiceImpl extends BaseServiceImpl<Xdict, Long> implements XdictService {
	@Resource(name = "xdictDaoImpl")
	private XdictDao xdictDao;

	@Resource(name = "xdictDaoImpl")
	public void setBaseDao(XdictDao xdictDao) {
		super.setBaseDao(xdictDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Xdict xdict) {
		super.save(xdict);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Xdict update(Xdict xdict) {
		return super.update(xdict);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Xdict update(Xdict xdict, String... ignoreProperties) {
		return super.update(xdict, ignoreProperties);
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
	public void delete(Xdict xdict) {
		super.delete(xdict);
	}

	public Page<Xdict> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return xdictDao.findPage(beginDate,endDate,pageable);
	}
}