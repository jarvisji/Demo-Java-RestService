/**
 * 
 */
package net.freecoder.emailengine.dao.impl;

import java.util.Date;
import java.util.Iterator;

import net.freecoder.emailengine.dao.EmailEngineDao;
import net.freecoder.emailengine.vo.EmailTask;
import net.freecoder.emailengine.vo.EmailTaskAttr;
import net.freecoder.emailengine.vo.EmailTaskHistory;
import net.freecoder.restdemo.util.DateTimeUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class EmailEngineDaoImpl implements EmailEngineDao {

	private static Logger logger = LoggerFactory
			.getLogger(EmailEngineDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public EmailTask getEmailTask(String id) {
		return (EmailTask) currentSession().get(EmailTask.class, id);
	}

	@Override
	public EmailTaskHistory getEmailTaskHistory(String id) {
		return (EmailTaskHistory) currentSession().get(EmailTaskHistory.class,
				id);
	}

	@Override
	public String save(EmailTask entity) {
		createTime(entity);
		return (String) currentSession().save(entity);
	}

	@Override
	public String save(EmailTaskHistory entity) {
		return (String) currentSession().save(entity);
	}

	@Override
	public void update(EmailTask entity) {
		currentSession().update(entity);

	}

	@Override
	public void update(EmailTaskHistory entity) {
		currentSession().update(entity);
	}

	@Override
	public void delete(EmailTask entity) {
		currentSession().delete(entity);

	}

	@Override
	public void delete(EmailTaskHistory entity) {
		currentSession().delete(entity);
	}

	private void createTime(EmailTask entity) {
		if (entity.getAppCreatedTime() == null) {
			entity.setAppCreatedTime(DateTimeUtil.getGMTDate());
		}
	}

}
