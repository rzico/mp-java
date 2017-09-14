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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.TagDao;
import net.wit.entity.*;
import net.wit.service.TagService;

/**
 * @ClassName: TagDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("tagServiceImpl")
public class TagServiceImpl extends BaseServiceImpl<Tag, Long> implements TagService {
	@Resource(name = "tagDaoImpl")
	private TagDao tagDao;

	@Resource(name = "tagDaoImpl")
	public void setBaseDao(TagDao tagDao) {
		super.setBaseDao(tagDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Tag tag) {
		super.save(tag);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Tag update(Tag tag) {
		return super.update(tag);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Tag update(Tag tag, String... ignoreProperties) {
		return super.update(tag, ignoreProperties);
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
	public void delete(Tag tag) {
		super.delete(tag);
	}

	public Page<Tag> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return tagDao.findPage(beginDate,endDate,pageable);
	}
}