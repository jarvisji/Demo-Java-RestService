/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.dao.SmsHistoryDao;
import net.freecoder.restdemo.model.WlsSmsHistory;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class SmsHistoryDaoImpl extends CommonDaoImpl<WlsSmsHistory> implements
		SmsHistoryDao {

	@Override
	public WlsSmsHistory get(String id) {
		return super.get(WlsSmsHistory.class, id);
	}

	@Override
	public List<WlsSmsHistory> getByWxAccountId(String wxAccountId) {
		String hql = "from WlsSmsHistory where wlsWxAccountId=:wlsWxAccountId";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("wlsWxAccountId", wxAccountId);
		return super.find(hql, params);
	}

	@Override
	public List<WlsSmsHistory> getByWxAccountId(String wxAccountId, Date start,
			Date end) {
		SimpleExpression eq = Restrictions.eq("wlsWxAccountId", wxAccountId);
		Criterion dateRange = Restrictions.between("appCreateTime", start, end);
		LogicalExpression and = Restrictions.and(eq, dateRange);
		Order desc = Order.desc("appCreateTime");
		List<WlsSmsHistory> list = super.find(WlsSmsHistory.class, and, desc);
		return list;
	}

	@Override
	public List<WlsSmsHistory> getByPhoneNumber(String phoneNumber) {
		String hql = "from WlsSmsHistory where phoneNumber like :phoneNumber";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("phoneNumber", "%" + phoneNumber + "%");
		return super.find(hql, params);
	}

	@Override
	public List<WlsSmsHistory> getByPhoneNumber(String phoneNumber, Date start,
			Date end) {
		SimpleExpression eq = Restrictions.eq("phoneNumber", phoneNumber);
		Criterion dateRange = Restrictions.between("appCreateTime", start, end);
		LogicalExpression and = Restrictions.and(eq, dateRange);
		Order desc = Order.desc("appCreateTime");
		List<WlsSmsHistory> list = super.find(WlsSmsHistory.class, and, desc);
		return list;
	}

	@Override
	public List<WlsSmsHistory> getByAccountPhone(String wxAccountId,
			String phoneNumber) {
		String hql = "from WlsSmsHistory where wlsWxAccountId=:wlsWxAccountId and phoneNumber like :phoneNumber";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("wlsWxAccountId", wxAccountId);
		params.put("phoneNumber", "%" + phoneNumber + "%");
		return super.find(hql, params);
	}

	@Override
	public List<WlsSmsHistory> getByAccountPhone(String wxAccountId,
			String phoneNumber, Date start, Date end) {
		SimpleExpression eqAccount = Restrictions.eq("wlsWxAccountId",
				wxAccountId);
		SimpleExpression eqPhone = Restrictions.eq("phoneNumber", phoneNumber);
		Criterion dateRange = Restrictions.between("appCreateTime", start, end);
		LogicalExpression and = Restrictions.and(eqAccount, eqPhone);
		LogicalExpression and2 = Restrictions.and(and, dateRange);
		Order desc = Order.desc("appCreateTime");
		List<WlsSmsHistory> list = super.find(WlsSmsHistory.class, and2, desc);
		return list;
	}

}
