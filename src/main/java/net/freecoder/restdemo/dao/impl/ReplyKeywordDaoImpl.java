/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.List;

import net.freecoder.restdemo.dao.ReplyKeywordDao;
import net.freecoder.restdemo.model.ReferenceType;
import net.freecoder.restdemo.model.WlsReplyKeyword;

import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class ReplyKeywordDaoImpl extends CommonDaoImpl<WlsReplyKeyword>
		implements ReplyKeywordDao {

	static private Logger logger = LoggerFactory
			.getLogger(ReplyAutoDaoImpl.class);

	@Override
	public List<WlsReplyKeyword> getListByWxAccountIdAndRefType(
			String wxAccountId, ReferenceType refType) {
		SimpleExpression like = Restrictions.like("refType",
				refType.toString(), MatchMode.START);
		SimpleExpression exp = Restrictions.eq("wlsWxAccountId", wxAccountId);
		LogicalExpression and = Restrictions.and(exp, like);
		Order order = Order.desc("appLastModifyTime");
		return find(WlsReplyKeyword.class, and, order);
	}

	/**
	 * This method will delete original replies before save.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String save(WlsReplyKeyword entity) {
		WlsReplyKeyword[] arrEntity = new WlsReplyKeyword[] { entity };
		return this.saveAll(entity.getWlsWxAccountId(), arrEntity)[0];
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String[] saveAll(String wxAccountId, WlsReplyKeyword[] entities) {
		if (entities == null || entities.length == 0) {
			logger.debug("Skip saving blank array of entities.");
			return null;
		}

		String groupName = entities[0].getKeywordGroup();
		deleteByGroup(wxAccountId, groupName);
		currentSession().flush();

		// create new replies.
		String[] retId = new String[entities.length];
		for (int i = 0; i < entities.length; i++) {
			this.createTime(entities[i]);
			retId[i] = currentSession().save(entities[i]).toString();
		}
		return retId;
	}

	@Override
	public List<WlsReplyKeyword> getByKeyword(String wxAccountId, String keyword) {
		SimpleExpression eq = Restrictions.eq("keyword", keyword);
		SimpleExpression eq2 = Restrictions.eq("wlsWxAccountId", wxAccountId);
		LogicalExpression and = Restrictions.and(eq, eq2);
		return find(WlsReplyKeyword.class, and);
	}

	@Override
	public void deleteByGroup(String wxAccountId, String groupName) {
		String hql = "delete from WlsReplyKeyword as kw where kw.keywordGroup = ? and kw.wlsWxAccountId = ?";
		currentSession().createQuery(hql).setString(0, groupName)
				.setString(1, wxAccountId).executeUpdate();
	}

}
