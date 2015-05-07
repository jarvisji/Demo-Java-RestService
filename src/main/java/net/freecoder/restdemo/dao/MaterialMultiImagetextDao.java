/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.model.WlsMaterialImagetext;
import net.freecoder.restdemo.model.WlsMaterialMultiImagetext;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * @author JiTing
 */
public interface MaterialMultiImagetextDao extends
		CommonDao<WlsMaterialMultiImagetext> {

	/**
	 * Get MultiImagetext material by id.
	 * 
	 * @param id
	 * @return
	 */
	WlsMaterialMultiImagetext get(String id);

	/**
	 * Get list of WlsMaterialMultiImagetext by WxAccountId.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	List<WlsMaterialMultiImagetext> getListByWxAccountId(String wxAccountId);

	/**
	 * @param criterion
	 * @return
	 */
	List<WlsMaterialMultiImagetext> find(Criterion criterion);

	/**
	 * @param criterion
	 * @param order
	 * @return
	 */
	List<WlsMaterialMultiImagetext> find(Criterion criterion, Order order);

}
