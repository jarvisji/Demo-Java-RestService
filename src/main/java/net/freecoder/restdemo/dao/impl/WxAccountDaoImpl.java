/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.io.Serializable;
import java.util.List;

import net.freecoder.restdemo.controller.WxAccountExtProp;
import net.freecoder.restdemo.dao.WxAccountDao;
import net.freecoder.restdemo.model.WlsWxAccount;
import net.freecoder.restdemo.model.WlsWxAccountExt;
import net.freecoder.restdemo.util.UUIDUtil;

import org.hibernate.criterion.Criterion;
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
public class WxAccountDaoImpl extends CommonDaoImpl<WlsWxAccount> implements
		WxAccountDao {

	@Override
	public WlsWxAccount get(String id) {
		return super.get(WlsWxAccount.class, id);
	}

	@Override
	public List<WlsWxAccount> getByUserId(String userId) {
		SimpleExpression eq = Restrictions.eq("wlsUserId", userId);
		return this.find(eq);
	}

	@Override
	public void delete(String id) {
		// TODOJ: should delete all of related data of account. site, menu,
		// reply, etc.
		super.delete(get(id));
	}

	@Override
	public List<WlsWxAccount> find(Criterion criterion) {
		return super.find(WlsWxAccount.class, criterion);
	}

	@Override
	public List<WlsWxAccount> find(Criterion criterion, Order order) {
		return super.find(WlsWxAccount.class, criterion, order);
	}

	@Override
	public List<WlsWxAccountExt> getAccountExt(String accountId) {
		SimpleExpression eq = Restrictions.eq("wlsWxAccountId", accountId);
		return currentSession().createCriteria(WlsWxAccountExt.class).add(eq)
				.list();
	}

	@Override
	public WlsWxAccountExt getAccountExtProp(String accountId,
			WxAccountExtProp propName) {
		SimpleExpression eq = Restrictions.eq("wlsWxAccountId", accountId);
		SimpleExpression eq2 = Restrictions.eq("propName", propName.toString());
		List list = currentSession().createCriteria(WlsWxAccountExt.class)
				.add(eq).add(eq2).list();
		if (list.size() > 0) {
			return (WlsWxAccountExt) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public String createWxAccountExt(WlsWxAccountExt entity) {
		Serializable id = currentSession().save(entity);
		return id.toString();
	}

	@Override
	public String resetWxAccountExt_EntryToken(String wxAccountId) {
		// delete old token
		SimpleExpression eq = Restrictions.eq("wlsWxAccountId", wxAccountId);
		SimpleExpression eq2 = Restrictions.eq("propName",
				WxAccountExtProp.ENTRY_TOKEN.toString());
		List<WlsWxAccountExt> entities = currentSession()
				.createCriteria(WlsWxAccountExt.class).add(eq).add(eq2).list();
		for (WlsWxAccountExt entity : entities) {
			currentSession().delete(entity);
		}

		// create new token
		WlsWxAccountExt entity = new WlsWxAccountExt(UUIDUtil.uuid8(),
				WxAccountExtProp.ENTRY_TOKEN.toString(), UUIDUtil.uuid8(),
				wxAccountId);
		this.createWxAccountExt(entity);
		return entity.getPropValue();
	}

}
