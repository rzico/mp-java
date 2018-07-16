package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Music;

/**
 * @ClassName: MusicService
 * @author 降魔战队
 * @date 2018-7-16 13:0:19
 */

public interface MusicService extends BaseService<Music, Long> {
	Page<Music> findPage(Date beginDate, Date endDate, Pageable pageable);
}