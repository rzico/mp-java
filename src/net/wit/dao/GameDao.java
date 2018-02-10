package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Game;
import net.wit.entity.Member;


/**
 * @ClassName: GameDao
 * @author 降魔战队
 * @date 2018-2-3 21:28:23
 */
 

public interface GameDao extends BaseDao<Game, Long> {
	/**
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Game>
	 * @Title：findPage
	 * @Description：标准代码
	 */
	Page<Game> findPage(Date beginDate, Date endDate, Pageable pageable);

	public Game find(Member member, String game, String tableNo, String roundNo);
}