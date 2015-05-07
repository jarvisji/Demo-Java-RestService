/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.lang.reflect.Field;
import java.util.Date;

import net.freecoder.restdemo.exception.ServiceException;
import net.freecoder.restdemo.util.DateTimeUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default implementation of CommonDao.
 * 
 * @author JiTing
 * @param <T>
 *            Any Hibernate entity class.
 */
public class BaseDaoImpl {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void updateTime(Object entity) {
		if (entity.getClass().getName()
				.startsWith("net.freecoder.restdemo.model.")) {
			Date currentTime = DateTimeUtil.getGMTDate();
			try {
				Field field = entity.getClass().getDeclaredField(
						"appCreateTime");
				if (field != null) {
					field.setAccessible(true);
					Object fieldValue = field.get(entity);
					if (fieldValue == null) {
						field.set(entity, currentTime);
					}
				}
				field = entity.getClass().getDeclaredField("appLastModifyTime");
				if (field != null) {
					field.setAccessible(true);
					field.set(entity, currentTime);
				}
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
	}

}
