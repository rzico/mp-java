package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.Member;
import net.wit.entity.Transfer;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.GameDao;
import net.wit.entity.Game;


/**
 * @ClassName: GameDaoImpl
 * @author 降魔战队
 * @date 2018-2-3 21:28:23
 */
 

@Repository("gameDaoImpl")
public class GameDaoImpl extends BaseDaoImpl<Game, Long> implements GameDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Game>
	 */
	public Page<Game> findPage(Date beginDate, Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Game> criteriaQuery = criteriaBuilder.createQuery(Game.class);
		Root<Game> root = criteriaQuery.from(Game.class);
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

	public Game find(Member member, String game, String tableNo, String roundNo) {
		String jpql = "select game from Game game where game.member=:member and game.game = :game and game.tableNo=:tableNo and game.roundNo=:roundNo";
		try {
			return entityManager.createQuery(jpql, Game.class).setFlushMode(FlushModeType.COMMIT)
					.setParameter("member", member)
					.setParameter("game", game)
					.setParameter("tableNo", tableNo)
					.setParameter("roundNo", roundNo)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}