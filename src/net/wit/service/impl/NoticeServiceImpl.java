package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.NoticeDao;
import net.wit.entity.Notice;
import net.wit.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: NoticeDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("noticeServiceImpl")
public class NoticeServiceImpl extends BaseServiceImpl<Notice, Long> implements NoticeService {
	@Resource(name = "noticeDaoImpl")
	private NoticeDao noticeDao;

	@Resource(name = "noticeDaoImpl")
	public void setBaseDao(NoticeDao noticeDao) {
		super.setBaseDao(noticeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Notice notice) {
		super.save(notice);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Notice update(Notice notice) {
		return super.update(notice);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Notice update(Notice notice, String... ignoreProperties) {
		return super.update(notice, ignoreProperties);
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
	public void delete(Notice notice) {
		super.delete(notice);
	}

	public Page<Notice> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return noticeDao.findPage(beginDate,endDate,pageable);
	}
}