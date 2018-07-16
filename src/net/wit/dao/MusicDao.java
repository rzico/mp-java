package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Music;


/**
 * @ClassName: MusicDao
 * @author 降魔战队
 * @date 2018-7-16 13:0:11
 */
 

public interface MusicDao extends BaseDao<Music, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Music>
	 */
	Page<Music> findPage(Date beginDate, Date endDate, Pageable pageable);
}