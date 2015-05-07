/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.freecoder.restdemo.dao.WlsLogDao;
import net.freecoder.restdemo.model.WlsLog;

import org.hibernate.criterion.Projections;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * WlsLog only has operatorId but doesn't have operator email. We want show
 * operator email on UI, so we need left join WlsLog on WlsUser. However,
 * Hibernate doesn't support left join on two un-related entities, so here use
 * native SQL. Refer to:
 * http://stackoverflow.com/questions/9892008/hql-left-join
 * -of-un-related-entities
 * 
 * @author JiTing
 */
@Repository
@Transactional
public class WlsLogDaoImpl extends CommonDaoImpl<WlsLog> implements WlsLogDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getList() {
		String sql = "select l.*, u.email as email from wls_log l left join wls_user u on l.operator_id = u.id order by l.app_create_time desc";
		List<Object[]> list = currentSession().createSQLQuery(sql)
				.addEntity(WlsLog.class)
				.addScalar("email", StandardBasicTypes.STRING).list();
		return convertWlsLogResult(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getList(Date start, Date end) {
		String sql = "select l.*, u.email as email from wls_log l left join wls_user u on l.operator_id = u.id where l.app_create_time between ? and ? order by l.app_create_time desc";
		List<Object[]> list = currentSession().createSQLQuery(sql)
				.addEntity(WlsLog.class)
				.addScalar("email", StandardBasicTypes.STRING)
				.setTimestamp(0, start).setTimestamp(1, end).list();
		return convertWlsLogResult(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getList(long start, int pagesize) {
		String sql = "select l.*, u.email as email from wls_log l left join wls_user u on l.operator_id = u.id where l.app_create_time < ? order by l.app_create_time desc";
		List<Object[]> list = currentSession().createSQLQuery(sql)
				.addEntity(WlsLog.class)
				.addScalar("email", StandardBasicTypes.STRING)
				.setTimestamp(0, new Date(start)).setMaxResults(pagesize)
				.list();
		return convertWlsLogResult(list);
	}

	private List<Object> convertWlsLogResult(List<Object[]> list) {
		List<Object> ret = new ArrayList<Object>(list.size());
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> row = new HashMap<String, Object>(1);
			row.put("wlsLog", list.get(i)[0]);
			row.put("email", list.get(i)[1]);
			ret.add(row);
		}
		return ret;
	}

	@Override
	public int totalCount() {
		Object uniqueResult = currentSession().createCriteria(WlsLog.class)
				.setProjection(Projections.rowCount()).uniqueResult();
		return Integer.valueOf(uniqueResult.toString()).intValue();
	}

}
