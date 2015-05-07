/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.Date;
import java.util.List;

import net.freecoder.restdemo.constant.WxMessageType;
import net.freecoder.restdemo.dao.WxMessageDao;
import net.freecoder.restdemo.model.WlsWxMessage;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class WxMessageDaoImpl extends CommonDaoImpl<WlsWxMessage> implements
		WxMessageDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<WlsWxMessage> getList(WxMessageType msgType, String wxAccountId) {
		Order desc = Order.desc("createTime");
		List<WlsWxMessage> list = getCriteria(msgType, wxAccountId).addOrder(
				desc).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WlsWxMessage> getList(Date start, Date end,
			WxMessageType msgType, String wxAccountId) {
		Criterion dateRange = Restrictions.between("createTime", start, end);
		Order desc = Order.desc("createTime");
		List<WlsWxMessage> list = getCriteria(msgType, wxAccountId)
				.add(dateRange).addOrder(desc).list();
		return list;
	}

	@Override
	public Long getCount(WxMessageType msgType, String wxAccountId) {
		return getCount(msgType, wxAccountId, null);
	}

	@Override
	public Long getCount(WxMessageType msgType, String wxAccountId,
			String distinctPropName) {
		Criteria criteria = getCriteria(msgType, wxAccountId);
		if (distinctPropName != null) {
			criteria.setProjection(Projections.countDistinct(distinctPropName));
		} else {
			criteria.setProjection(Projections.rowCount());
		}
		return (Long) criteria.uniqueResult();
	}

	@Override
	public Long getCount(Date start, Date end, WxMessageType msgType,
			String wxAccountId) {
		return getCount(start, end, msgType, wxAccountId, null);
	}

	@Override
	public Long getCount(Date start, Date end, WxMessageType msgType,
			String wxAccountId, String distinctPropName) {
		Criterion dateRange = Restrictions.between("createTime", start, end);
		Criteria criteria = getCriteria(msgType, wxAccountId).add(dateRange);
		if (distinctPropName != null) {
			criteria.setProjection(Projections.countDistinct(distinctPropName));
		} else {
			criteria.setProjection(Projections.rowCount());
		}
		return (Long) criteria.uniqueResult();
	}

	private Criteria getCriteria(WxMessageType msgType, String wxAccountId) {
		SimpleExpression eqMsgType = Restrictions.eq("msgType", msgType.name());
		SimpleExpression eqAccount = Restrictions.eq("wlsWxAccountId",
				wxAccountId);
		return currentSession().createCriteria(WlsWxMessage.class)
				.add(eqAccount).add(eqMsgType);
	}
}
