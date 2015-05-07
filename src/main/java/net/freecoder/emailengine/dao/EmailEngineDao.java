package net.freecoder.emailengine.dao;

import net.freecoder.emailengine.vo.EmailTask;
import net.freecoder.emailengine.vo.EmailTaskHistory;

/**
 * 
 * @author JiTing
 */
public interface EmailEngineDao {
	/**
	 * Get EmailTask entity by id.
	 * 
	 * @param id
	 * @return
	 */
	EmailTask getEmailTask(String id);

	/**
	 * Get EmailTaskHistory entity by id.
	 * 
	 * @param id
	 * @return
	 */
	EmailTaskHistory getEmailTaskHistory(String id);

	/**
	 * Save EmailTask entity to persistence data store.
	 * 
	 * @param entity
	 *            Entity object.
	 */
	String save(EmailTask entity);

	/**
	 * Save EmailTaskHistory entity to persistence data store.
	 * 
	 * @param entity
	 * @return
	 */
	String save(EmailTaskHistory entity);

	/**
	 * Update EmailTask entity.
	 * 
	 * @param entity
	 */
	void update(EmailTask entity);

	/**
	 * Update EmailTaskHistory entity.
	 * 
	 * @param entity
	 */
	void update(EmailTaskHistory entity);

	/**
	 * Delete EmailTask entity from persistence data store.
	 * 
	 * @param entity
	 *            Entity object.
	 */
	void delete(EmailTask entity);

	/**
	 * Delete EmailTaskHistory entity from persistence data store.
	 * 
	 * @param entity
	 */
	void delete(EmailTaskHistory entity);

}
