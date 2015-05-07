package net.freecoder.restdemo.dao.impl;

import java.util.List;

import net.freecoder.restdemo.dao.UserDao;
import net.freecoder.restdemo.model.WlsUser;
import net.freecoder.restdemo.model.WlsUserPay;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDaoImpl extends CommonDaoImpl<WlsUser> implements UserDao {

	public WlsUser get(String id) {
		return super.get(WlsUser.class, id);
	}

	public void delete(String id) {
		super.delete(get(id));
	}

	@Override
	public List<WlsUser> find(Criterion criterion) {
		return super.find(WlsUser.class, criterion);
	}

	@Override
	public List<WlsUser> find(Criterion criterion, Order order) {
		return super.find(WlsUser.class, criterion, order);
	}

	@Override
	public void updatePassword(String id, String newPass) {
		String hql = "update WlsUser u set u.password=? where u.id=?";
		currentSession().createQuery(hql).setString(0, newPass)
				.setString(1, id).executeUpdate();
	}

	@Override
	public WlsUserPay getLastUserPay(String userId) {
		SimpleExpression eqUserId = Restrictions.eq("userid", userId);
		Order desc = Order.desc("appLastModifyTime");
		List<WlsUserPay> find = currentSession()
				.createCriteria(WlsUserPay.class).add(eqUserId).addOrder(desc)
				.setMaxResults(1).list();
		if (find.size() > 0) {
			return find.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void updateUserPay(WlsUserPay userPay) {
		super.updateTime(userPay);
		currentSession().update(userPay);
	}

	@Override
	public void createUserPay(WlsUserPay userPay) {
		super.createTime(userPay);
		currentSession().save(userPay);
	}

	@Override
	public List<WlsUserPay> getUserPays(String userId) {
		SimpleExpression eqUserId = Restrictions.eq("userid", userId);
		Order desc = Order.desc("appLastModifyTime");
		List<WlsUserPay> find = currentSession()
				.createCriteria(WlsUserPay.class).add(eqUserId).addOrder(desc)
				.list();
		return find;
	}

}
