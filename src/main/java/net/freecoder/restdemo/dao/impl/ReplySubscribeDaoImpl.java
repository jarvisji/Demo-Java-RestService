/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.List;

import net.freecoder.restdemo.dao.ReplySubscribeDao;
import net.freecoder.restdemo.model.WlsReplySubscribe;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class ReplySubscribeDaoImpl extends CommonDaoImpl<WlsReplySubscribe>
		implements ReplySubscribeDao {

	/**
	 * User only can setup one subscribe reply, so this method will delete
	 * original one before save.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String save(WlsReplySubscribe entity) {
		List<WlsReplySubscribe> entities = getListByWxAccountId(
				WlsReplySubscribe.class, entity.getWlsWxAccountId());
		for (WlsReplySubscribe existEntity : entities) {
			currentSession().delete(existEntity);
		}
		currentSession().flush();
		this.createTime(entity);
		return currentSession().save(entity).toString();
	}
}
