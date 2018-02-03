package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Game;

/**
 * @ClassName: GameService
 * @author 降魔战队
 * @date 2018-2-3 21:28:27
 */

public interface GameService extends BaseService<Game, Long> {
	Page<Game> findPage(Date beginDate, Date endDate, Pageable pageable);

	public void sumbit(Game game) throws Exception;

	public void history(Game game) throws Exception;

	public Game find(String game,String tableNo,String roundNo);
}