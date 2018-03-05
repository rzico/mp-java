package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GameList;
import net.wit.entity.Member;

import java.util.Date;

/**
 * @ClassName: GameService
 * @author 降魔战队
 * @date 2018-2-3 21:28:27
 */

public interface GameListService extends BaseService<GameList, Long> {
	Page<GameList> findPage(Date beginDate, Date endDate, Pageable pageable);

	public GameList find(GameList.Type type, String game, String tableNo,String ranges);
}