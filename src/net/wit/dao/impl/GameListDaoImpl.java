package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.GameListDao;
import net.wit.entity.Game;
import net.wit.entity.GameList;
import net.wit.entity.Member;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;


/**
 * @ClassName: GameDaoImpl
 * @author 降魔战队
 * @date 2018-2-3 21:28:23
 */
 

@Repository("gameListDaoImpl")
public class GameListDaoImpl extends BaseDaoImpl<GameList, Long> implements GameListDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Game>
	 */
	public Page<GameList> findPage(Date beginDate, Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GameList> criteriaQuery = criteriaBuilder.createQuery(GameList.class);
		Root<GameList> root = criteriaQuery.from(GameList.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.conjunction();
		if (beginDate!=null) {
			Date b = DateUtils.truncate(beginDate,Calendar.DATE);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), b));
		}
		if (endDate!=null) {
			Date e = DateUtils.truncate(endDate,Calendar.DATE);
			e =DateUtils.addDays(e,1);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date> get("createDate"), e));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}

	public GameList find(GameList.Type type, String game, String tableNo,String ranges) {
		String jpql = "select gameList from GameList gameList where gameList.type=:type and gameList.ranges=:ranges and gameList.game = :game and gameList.tableNo=:tableNo";
		try {
			return entityManager.createQuery(jpql, GameList.class).setFlushMode(FlushModeType.COMMIT)
					.setParameter("type", type)
					.setParameter("game", game)
					.setParameter("tableNo", tableNo)
					.setParameter("ranges", ranges)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}