package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DepositDao;
import net.wit.dao.GameDao;
import net.wit.dao.GameListDao;
import net.wit.dao.MemberDao;
import net.wit.entity.Deposit;
import net.wit.entity.Game;
import net.wit.entity.GameList;
import net.wit.entity.Member;
import net.wit.service.GameListService;
import net.wit.service.GameService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: GameListServiceImpl
 * @author 降魔战队
 * @date 2018-2-3 21:28:27
 */
 
 
@Service("gameListServiceImpl")
public class GameListServiceImpl extends BaseServiceImpl<GameList, Long> implements GameListService {

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "gameListDaoImpl")
	private GameListDao gameListDao;

	@Resource(name = "gameListDaoImpl")
	public void setBaseDao(GameListDao gameListDao) {
		super.setBaseDao(gameListDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GameList game) {
		super.save(game);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GameList update(GameList game) {
		return super.update(game);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GameList update(GameList game, String... ignoreProperties) {
		return super.update(game, ignoreProperties);
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
	public void delete(GameList game) {
		super.delete(game);
	}

	public Page<GameList> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return gameListDao.findPage(beginDate,endDate,pageable);
	}

	public GameList find(GameList.Type type, String game, String tableNo,String ranges) {
		return gameListDao.find(type,game,tableNo,ranges);
	}

}