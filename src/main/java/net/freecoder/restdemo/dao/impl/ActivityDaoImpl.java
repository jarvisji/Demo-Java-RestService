/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.constant.ActivityType;
import net.freecoder.restdemo.dao.ActivityDao;
import net.freecoder.restdemo.model.WlsActivity;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class ActivityDaoImpl extends CommonDaoImpl<WlsActivity> implements
		ActivityDao {

	@Override
	public WlsActivity getLastActivity(String initiator, ActivityType type) {
		String hql = "from WlsActivity where initiator=:initiator and type=:type order by appCreateTime desc";
		Map<String, String> params = new HashMap<String, String>();
		params.put("initiator", initiator);
		params.put("type", type.toString());
		List<WlsActivity> find = super.find(hql, params);
		WlsActivity ret = null;
		if (find.size() > 0) {
			ret = find.get(0);
		}
		return ret;
	}
}
