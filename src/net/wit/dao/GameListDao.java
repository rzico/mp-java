package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GameList;
import net.wit.entity.Member;

import java.util.Date;


/**
 * @ClassName: GameListDao
 * @author 降魔战队
 * @date 2018-2-3 21:28:23
 */
 

public interface GameListDao extends BaseDao<GameList, Long> {
	/**
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Game>
	 * @Title：findPage
	 * @Description：标准代码
	 */
	Page<GameList> findPage(Date beginDate, Date endDate, Pageable pageable);

	public GameList find(GameList.Type type, String game, String tableNo,String ranges);
}