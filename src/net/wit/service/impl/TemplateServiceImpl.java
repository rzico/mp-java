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

import net.wit.dao.TemplateDao;
import net.wit.entity.*;
import net.wit.service.TemplateService;

/**
 * @ClassName: TemplateDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("templateServiceImpl")
public class TemplateServiceImpl extends BaseServiceImpl<Template, Long> implements TemplateService {
	@Resource(name = "templateDaoImpl")
	private TemplateDao templateDao;

	@Resource(name = "templateDaoImpl")
	public void setBaseDao(TemplateDao templateDao) {
		super.setBaseDao(templateDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Template template) {
		super.save(template);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Template update(Template template) {
		return super.update(template);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Template update(Template template, String... ignoreProperties) {
		return super.update(template, ignoreProperties);
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
	public void delete(Template template) {
		super.delete(template);
	}

	public Page<Template> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return templateDao.findPage(beginDate,endDate,pageable);
	}
}