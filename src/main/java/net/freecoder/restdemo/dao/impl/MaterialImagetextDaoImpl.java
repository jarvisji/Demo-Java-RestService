/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.List;

import net.freecoder.restdemo.dao.MaterialImagetextDao;
import net.freecoder.restdemo.model.WlsMaterialImagetext;
import net.freecoder.restdemo.model.WlsMaterialMultiImagetext;
import net.freecoder.restdemo.util.MiscUtil;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class MaterialImagetextDaoImpl extends
		CommonDaoImpl<WlsMaterialImagetext> implements MaterialImagetextDao {

	@Override
	public WlsMaterialImagetext get(String id) {
		return super.get(WlsMaterialImagetext.class, id);
	}

	@Override
	public List<WlsMaterialImagetext> find(Criterion criterion) {
		Criterion null1 = Restrictions.isNull("wlsMaterialMultiImagetext");
		LogicalExpression and = Restrictions.and(criterion, null1);
		return super.find(WlsMaterialImagetext.class, and);
	}

	@Override
	public List<WlsMaterialImagetext> find(Criterion criterion, Order order) {
		Criterion null1 = Restrictions.isNull("wlsMaterialMultiImagetext");
		LogicalExpression and = Restrictions.and(criterion, null1);
		return super.find(WlsMaterialImagetext.class, and, order);
	}

	@Override
	public void delete(String id) {
		super.delete(get(id));
	}

	@Override
	public List<WlsMaterialImagetext> getListByWxAccountId(String wxAccountId) {
		SimpleExpression eqAccountId = Restrictions.eq("wlsWxAccountId",
				wxAccountId);
		Criterion null1 = Restrictions.isNull("wlsMaterialMultiImagetext");
		LogicalExpression exp = Restrictions.and(eqAccountId, null1);
		Order order = Order.desc("appLastModifyTime");
		return find(exp, order);
	}

	@Override
	public String save(WlsMaterialImagetext entity) {
		return super.save(entity);
	}

	@Override
	public void update(WlsMaterialImagetext entity) {
		super.update(entity);
	}

}
