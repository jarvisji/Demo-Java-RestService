/**
 * 
 */
package net.freecoder.common.query.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.freecoder.common.query.Query;
import net.freecoder.restdemo.dao.CommonDao;
import net.freecoder.restdemo.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implements Query interface for all query types.
 * 
 * @author JiTing
 * @param <T>
 * @param <U>
 */
public class CommonQueryImpl<T extends Query<?, ?>, U> implements Query<T, U> {

	private static Logger logger = LoggerFactory
			.getLogger(CommonQueryImpl.class);
	@Autowired
	private CommonDao<U> commonDao;
	private String orderBy;
	private String orderByColumn;
	/** Store where criteria which equal to the values. */
	private List<QueryCriteria> whereCriteria = new ArrayList<QueryCriteria>();
	private String entityName;

	/**
	 * Add where criteria to query instance.
	 * 
	 * @param qc
	 *            QueryCriteria.
	 */
	protected void where(QueryCriteria qc) {
		this.whereCriteria.add(qc);
	}

	/**
	 * Set entity name of HQL.
	 * 
	 * @param entityName
	 *            Object name of Hibernate mapping.
	 */
	protected void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Return query string that this query will construct.
	 * 
	 * @return Query string.
	 */
	public String getQueryString() {
		return constructQueryStr();
	}

	@Override
	public T asc() {
		return direction(Direction.ASCENDING);
	}

	@Override
	public T desc() {
		return direction(Direction.DESCENDING);
	}

	@Override
	public long count() {
		checkQueryOk();
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public U singleResult() {
		checkQueryOk();
		String hql = constructQueryStr();
		List<U> results = commonDao.find(hql);
		if (results.size() == 1) {
			return results.get(0);
		} else if (results.size() > 1) {
			throw new ServiceException("Query return " + results.size()
					+ " results instead of max 1");
		}
		return null;
	}

	@Override
	public List<U> list() {
		checkQueryOk();
		String hql = constructQueryStr();
		List<U> results = commonDao.find(hql);
		return results;
	}

	@Override
	public List<U> listPage(final int firstResult, final int maxResults) {
		checkQueryOk();
		throw new ServiceException("not implemented yet.");
		// final String hql = constructQueryStr();
		// commonDao.find(hql);
		// @SuppressWarnings("unchecked")
		// List<U> result = commonDao.find(new HibernateCallback<List<U>>() {
		// @Override
		// public List<U> doInHibernate(Session session) throws SQLException {
		// org.hibernate.Query query = session.createQuery(hql);
		// query.setFirstResult(firstResult);
		// query.setMaxResults(maxResults);
		// return query.list();
		// }
		// });
		// return result;
	}

	private String constructQueryStr() {
		checkQueryOk();
		if (entityName == null || entityName.isEmpty()) {
			throw new ServiceException("Invalid query: entity name is blank.");
		}
		if (whereCriteria.size() == 0) {
			throw new ServiceException("Invalid query: where clause is blank.");
		}
		StringBuffer queryStr = new StringBuffer("from ").append(entityName)
				.append(" where ");
		// construct where clause.
		Iterator<QueryCriteria> iterator = whereCriteria.iterator();
		while (iterator.hasNext()) {
			QueryCriteria next = iterator.next();
			queryStr.append(next.toString());
			if (iterator.hasNext()) {
				queryStr.append(" and ");
			}
		}
		// construct order by clause.
		if (orderBy != null) {
			queryStr.append(" order by ").append(orderBy);
		}
		logger.debug("Query string: {}", queryStr.toString());
		return queryStr.toString();
	}

	/**
	 * Add order by ceritia.
	 * 
	 * @param column
	 *            Order by column name.
	 * @return Query instance itself.
	 */
	@SuppressWarnings("unchecked")
	public T orderBy(String column) {
		this.orderByColumn = column;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	private T direction(Direction direction) {
		if (orderByColumn == null) {
			throw new ServiceException(
					"You should call any of the orderBy methods first before specifying a direction");
		}
		addOrder(orderByColumn, direction.getName());
		orderByColumn = null;
		return (T) this;
	}

	private void addOrder(String column, String sortOrder) {
		if (orderBy == null) {
			orderBy = "";
		} else {
			orderBy = orderBy + ", ";
		}
		orderBy = orderBy + column + " " + sortOrder;
	}

	private void checkQueryOk() {
		if (orderByColumn != null) {
			throw new ServiceException(
					"Invalid query: call asc() or desc() after using orderByXX()");
		}
	}

}
