/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * Common CRUD operations for all Daos. Other Daos should consider to extend
 * CommonDao.
 * 
 * @author JiTing
 * @param <T>
 *            Any Hibernate entity class.
 */
public interface CommonDao<T> {

	/**
	 * Get entity by id.
	 * 
	 * @param id
	 *            Entity id.
	 * @param entityClass
	 *            Entity Class.
	 * @return Entity object.
	 */
	T get(Class<T> entityClass, String id);

	/**
	 * Get entities by an array of ids.
	 * 
	 * @param entityClass
	 * @param ids
	 * @return
	 */
	List<T> get(Class<T> entityClass, String[] ids);

	/**
	 * Save entity to persistence data store.
	 * 
	 * @param entity
	 *            Entity object.
	 */
	String save(T entity);

	/**
	 * Update entity.
	 * 
	 * @param entity
	 */
	void update(T entity);

	/**
	 * Save or update entity.
	 * 
	 * @param entity
	 */
	void saveOrUpdate(T entity);

	/**
	 * Delete entity from persistence data store.
	 * 
	 * @param entity
	 *            Entity object.
	 */
	void delete(T entity);

	/**
	 * Query entity list by HQL. HQL could be constructed by
	 * {@link org.hibernate.criterion.Criterion} interface.
	 * 
	 * @param hql
	 *            HQL string.
	 * @return Entity list.
	 */
	List<T> find(String hql);

	List<T> find(String hql, Map<String, String> params);

	/**
	 * Query entity list by special criterion.
	 * <p>
	 * This interface implemented the default order by "appLastModifyTime" desc.
	 * </p>
	 * 
	 * @param entityClass
	 * @param criterion
	 * @return entity list.
	 */
	List<T> find(Class<T> entityClass, Criterion criterion);

	/**
	 * Query entity list by special criterion and order.
	 * 
	 * @param entityClass
	 * @param criterion
	 * @param order
	 * @return entity list.
	 */
	List<T> find(Class<T> entityClass, Criterion criterion, Order order);

	/**
	 * Get entity list by WxAccountId.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	List<T> getListByWxAccountId(Class<T> entityClass, String wxAccountId);

	/**
	 * Execute HQL
	 * 
	 * @param hql
	 * @return Number of records were updated or deleted.
	 */
	int execute(String hql);

	int execute(String hql, Map<String, String> params);
}
