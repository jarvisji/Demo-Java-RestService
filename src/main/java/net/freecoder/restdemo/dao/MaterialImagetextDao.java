/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.model.WlsMaterialImagetext;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * @author JiTing
 */
public interface MaterialImagetextDao extends CommonDao<WlsMaterialImagetext> {

	WlsMaterialImagetext get(String id);

	List<WlsMaterialImagetext> getListByWxAccountId(String wxAccountId);

	List<WlsMaterialImagetext> find(Criterion criterion);

	List<WlsMaterialImagetext> find(Criterion criterion, Order order);

	void delete(String id);
}
