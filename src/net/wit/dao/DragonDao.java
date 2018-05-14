package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Article;
import net.wit.entity.Dragon;
import net.wit.entity.Member;

import java.util.Date;


/**
 * @ClassName: DragonDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

public interface DragonDao extends BaseDao<Dragon, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Vip>
	 */
	Page<Dragon> findPage(Date beginDate, Date endDate, Pageable pageable);


	Dragon find(Article article, Member member);

}