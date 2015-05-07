/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.List;

import net.freecoder.restdemo.dao.MaterialMultiImagetextDao;
import net.freecoder.restdemo.model.WlsMaterialMultiImagetext;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class MaterialMultiImagetextDaoImpl extends
		CommonDaoImpl<WlsMaterialMultiImagetext> implements
		MaterialMultiImagetextDao {

	public WlsMaterialMultiImagetext get(String id) {
		return super.get(WlsMaterialMultiImagetext.class, id);
	}

	@Override
	public List<WlsMaterialMultiImagetext> getListByWxAccountId(
			String wxAccountId) {
		return super.getListByWxAccountId(WlsMaterialMultiImagetext.class,
				wxAccountId);
	}

	@Override
	public List<WlsMaterialMultiImagetext> find(Criterion criterion) {
		return super.find(WlsMaterialMultiImagetext.class, criterion);
	}

	@Override
	public List<WlsMaterialMultiImagetext> find(Criterion criterion, Order order) {
		return super.find(WlsMaterialMultiImagetext.class, criterion, order);
	}
}
