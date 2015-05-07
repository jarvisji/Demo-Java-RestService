/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.List;

import net.freecoder.restdemo.dao.ReplyAutoDao;
import net.freecoder.restdemo.model.WlsReplyAuto;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class ReplyAutoDaoImpl extends CommonDaoImpl<WlsReplyAuto> implements
		ReplyAutoDao {

	/**
	 * User only can setup one auto reply, so this method will delete original
	 * one before save.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String save(WlsReplyAuto entity) {
		// TODO:J validate entity data before read it's value. The same to other
		// daos.
		List<WlsReplyAuto> entities = getListByWxAccountId(WlsReplyAuto.class,
				entity.getWlsWxAccountId());
		for (WlsReplyAuto existEntity : entities) {
			currentSession().delete(existEntity);
		}
		currentSession().flush();
		this.createTime(entity);
		return currentSession().save(entity).toString();
	}
}
