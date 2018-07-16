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

import net.wit.dao.MusicDao;
import net.wit.entity.*;
import net.wit.service.MusicService;

/**
 * @ClassName: MusicDaoImpl
 * @author 降魔战队
 * @date 2018-7-16 13:0:19
 */
 
 
@Service("musicServiceImpl")
public class MusicServiceImpl extends BaseServiceImpl<Music, Long> implements MusicService {
	@Resource(name = "musicDaoImpl")
	private MusicDao musicDao;

	@Resource(name = "musicDaoImpl")
	public void setBaseDao(MusicDao musicDao) {
		super.setBaseDao(musicDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Music music) {
		super.save(music);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Music update(Music music) {
		return super.update(music);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Music update(Music music, String... ignoreProperties) {
		return super.update(music, ignoreProperties);
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
	public void delete(Music music) {
		super.delete(music);
	}

	public Page<Music> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return musicDao.findPage(beginDate,endDate,pageable);
	}
}