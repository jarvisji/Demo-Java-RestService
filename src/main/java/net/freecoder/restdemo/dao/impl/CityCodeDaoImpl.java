/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.List;

import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.dao.CityCodeDao;
import net.freecoder.restdemo.exception.ServiceDaoException;
import net.freecoder.restdemo.model.WlsCityCode;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class CityCodeDaoImpl extends CommonDaoImpl<WlsCityCode> implements
		CityCodeDao {

	@Override
	public List<WlsCityCode> getProvinces() {
		SimpleExpression like = Restrictions.like("cityCode", "0000",
				MatchMode.END);
		return super.find(WlsCityCode.class, like);
	}

	@Override
	public List<WlsCityCode> getCities(String provinceCode) {
		if (provinceCode == null || provinceCode.length() < 2) {
			throw new ServiceDaoException(ErrorCode.INVALID_PARAM.toString());
		}
		SimpleExpression start = Restrictions.like("cityCode",
				provinceCode.substring(0, 2), MatchMode.START);
		SimpleExpression end = Restrictions.like("cityCode", "00",
				MatchMode.END);
		Criterion exceptProvinceSelf = Restrictions.not(Restrictions.like(
				"cityCode", "0000", MatchMode.END));
		LogicalExpression and = Restrictions.and(Restrictions.and(start, end),
				exceptProvinceSelf);

		return super.find(WlsCityCode.class, and);
	}

	@Override
	public List<WlsCityCode> getAreas(String cityCode) {
		if (cityCode == null || cityCode.length() < 4) {
			throw new ServiceDaoException(ErrorCode.INVALID_PARAM.toString());
		}
		SimpleExpression start = Restrictions.like("cityCode",
				cityCode.substring(0, 4), MatchMode.START);
		Criterion exceptCitySelf = Restrictions.not(Restrictions.like(
				"cityCode", "00", MatchMode.END));
		LogicalExpression and = Restrictions.and(start, exceptCitySelf);
		return super.find(WlsCityCode.class, and);
	}

}
