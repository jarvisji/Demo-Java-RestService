/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.freecoder.restdemo.dao.CommonDao;
import net.freecoder.restdemo.exception.ServiceException;
import net.freecoder.restdemo.util.DateTimeUtil;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO:J Update implementation of CommonDao interfaces to "protected".
 * 
 * @author JiTing
 */
@Repository
@Transactional
@SuppressWarnings("unchecked")
public class CommonDaoImpl<T> implements CommonDao<T> {
	private static Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public T get(Class<T> entityClass, String id) {
		return (T) currentSession().get(entityClass, id);
	}

	@Override
	public List<T> get(Class<T> entityClass, String[] ids) {
		List<T> retList = new ArrayList<T>();
		if (ids != null && ids.length > 0) {
			Criterion in = Restrictions.in("id", ids);
			retList = find(entityClass, in);
		}
		return retList;
	}

	@Override
	public String save(T entity) {
		createTime(entity);
		return currentSession().save(entity).toString();
	}

	@Override
	public void update(T entity) {
		updateTime(entity);
		currentSession().update(entity);
	}

	@Override
	public void saveOrUpdate(T entity) {
		updateTime(entity);
		currentSession().saveOrUpdate(entity);
	}

	@Override
	public void delete(T entity) {
		currentSession().delete(entity);
	}

	@Override
	public List<T> find(String hql) {
		return currentSession().createQuery(hql).list();
	}

	@Override
	public List<T> find(String hql, Map<String, String> params) {
		Query query = currentSession().createQuery(hql);
		for (Entry<String, String> param : params.entrySet()) {
			query.setString(param.getKey(), param.getValue());
		}
		return query.list();
	}

	@Override
	public List<T> find(Class<T> entityClass, Criterion criterion) {
		Order order = null;
		try {
			if (entityClass.getDeclaredField("appLastModifyTime") != null) {
				order = Order.desc("appLastModifyTime");
			}
		} catch (NoSuchFieldException e) {
			// nothing to do
		} catch (SecurityException e) {
			// nothing to do
		}
		return find(entityClass, criterion, order);
	}

	@Override
	public List<T> find(Class<T> entityClass, Criterion criterion, Order order) {
		Criteria criteria = currentSession().createCriteria(entityClass).add(
				criterion);
		if (order != null) {
			criteria.addOrder(order);
		}
		return criteria.list();
	}

	@Override
	public List<T> getListByWxAccountId(Class<T> entityClass, String wxAccountId) {
		SimpleExpression exp = Restrictions.eq("wlsWxAccountId", wxAccountId);
		return find(entityClass, exp);
	}

	protected void updateTime(Object entity) {
		if (entity.getClass().getName()
				.startsWith("net.freecoder.restdemo.model.")) {
			Date currentTime = DateTimeUtil.getGMTDate();
			try {
				Field field = entity.getClass().getDeclaredField(
						"appLastModifyTime");
				if (field != null) {
					field.setAccessible(true);
					field.set(entity, currentTime);
				}
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
	}

	/**
	 * Set "appCreateTime" and "appLastModifyTime". If "appCreateTime" already
	 * set, skip to change it. Always update "appLastModifyTime".
	 * 
	 * @param entity
	 */
	protected void createTime(Object entity) {
		// TODO:J avoid to change "createTime" and "lastModifyTime" by client
		// (outside of service).
		if (entity.getClass().getName()
				.startsWith("net.freecoder.restdemo.model.")) {
			Date currentTime = DateTimeUtil.getGMTDate();
			try {
				Field field = entity.getClass().getDeclaredField(
						"appCreateTime");
				if (field != null) {
					field.setAccessible(true);
					if (field.get(entity) == null) {
						field.set(entity, currentTime);
					}
				}
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			try {
				Field field = entity.getClass().getDeclaredField(
						"appLastModifyTime");
				if (field != null) {
					field.setAccessible(true);
					field.set(entity, currentTime);
				}
			} catch (Exception e) {
				logger.warn(
						"Set appLastModifyTime failed when creating entity: {}, message: {}",
						entity.getClass().getName(), e.getMessage());
				// e.printStackTrace();
			}
		}
	}

	@Override
	public int execute(String hql) {
		return currentSession().createQuery(hql).executeUpdate();
	}

	@Override
	public int execute(String hql, Map<String, String> params) {
		Query query = currentSession().createQuery(hql);
		for (Entry<String, String> param : params.entrySet()) {
			query.setString(param.getKey(), param.getValue());
		}
		return query.executeUpdate();
	}

}
