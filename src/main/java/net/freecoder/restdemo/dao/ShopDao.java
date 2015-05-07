/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.constant.ShopConfigPropPrefix;
import net.freecoder.restdemo.model.WlsShopCategory;
import net.freecoder.restdemo.model.WlsShopConfig;
import net.freecoder.restdemo.model.WlsShopEntry;
import net.freecoder.restdemo.model.WlsShopItem;
import net.freecoder.restdemo.model.WlsShopOrder;
import net.freecoder.restdemo.model.WlsShopOrderItem;
import net.freecoder.restdemo.model.WlsShopPromotionPoints;
import net.freecoder.restdemo.model.WlsShopVip;
import net.freecoder.restdemo.model.WlsShopVipId;

/**
 * @author JiTing
 */
public interface ShopDao extends CommonDao<WlsShopEntry> {

	/**
	 * Get WlsShopEntry by id, we reuse wxAccoountId as shop identity by design.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	WlsShopEntry get(String wxAccountId);

	void delete(String wxAccountId);

	/*************************************************************************************************************
	 * WlsShopConfig
	 *************************************************************************************************************/
	void saveConfig(WlsShopConfig config);

	void saveConfigs(WlsShopConfig[] configs);

	List<WlsShopConfig> getConfigs(String wxAccountId);

	List<WlsShopConfig> getConfigs(String wxAccountId,
			ShopConfigPropPrefix prefix);

	WlsShopConfig getConfig(String propName, String wxAccountId);

	void deleteConfig(String propName, String wxAccountId);

	/*************************************************************************************************************
	 * WlsShopCategory
	 *************************************************************************************************************/
	void saveCategory(WlsShopCategory category);

	WlsShopCategory getCategory(String id);

	List<WlsShopCategory> getCategoriesByShop(String wxAccountId);

	void deleteCategory(WlsShopCategory category);

	/*************************************************************************************************************
	 * WlsShopItem
	 *************************************************************************************************************/
	void saveItem(WlsShopItem item);

	WlsShopItem getItem(String itemId);

	List<WlsShopItem> getItems(String wxAccountId);

	List<WlsShopItem> getItems(String wxAccountId, String categoryId);

	/**
	 * Get items by wxAccountId and other criterions.
	 * 
	 * @param wxAccountId
	 * @param criterions
	 *            Map<String, Object>
	 * @return
	 */
	List<WlsShopItem> getItems(String wxAccountId,
			Map<String, Object> criterions);

	void deleteItem(WlsShopItem item);

	/*************************************************************************************************************
	 * WlsShopOrder
	 *************************************************************************************************************/
	/**
	 * Save or update order, at mean time, update WlsShopVip.
	 * 
	 * @param order
	 */
	void saveOrder(WlsShopOrder order);

	WlsShopOrder getOrder(String orderId);

	List<WlsShopOrder> getOrdersByShop(String wxAccountId);

	List<WlsShopOrder> getOrdersByShop(String wxAccountId, Date start, Date end);

	/**
	 * Get latest orders of shop (order by create time desc), only return given
	 * number of pagesize.
	 * 
	 * @param wxAccountId
	 * @param start
	 * @param pagesize
	 * @return
	 */
	List<WlsShopOrder> getOrdersByShop(String wxAccountId, long start,
			int pagesize);

	List<WlsShopOrder> getOrdersByVip(String vipId);

	List<WlsShopOrder> getOrdersByVip(String vipId, Date start, Date end);

	List<WlsShopOrder> getOrdersByVip(String vipId, long start, int pagesize);

	List<WlsShopOrder> getOrdersByShopAndVip(String wxAccountId, String vipId);

	List<WlsShopOrder> getOrdersByShopAndVip(String wxAccountId, String vipId,
			Date start, Date end);

	List<WlsShopOrder> getOrdersByShopAndVip(String wxAccountId, String vipId,
			long start, int pagesize);

	int countOrdersByShop(String wxAccountId);

	int countOrdersByVip(String vipId);

	int countOrdersByShopAndVip(String wxAccountId, String vipId);

	void deleteOrder(WlsShopOrder order);

	/**
	 * Order item may be created and updated with order, however we need delete
	 * it individually.
	 */
	void deleteOrderItem(WlsShopOrderItem orderItem);

	/*************************************************************************************************************
	 * WlsShopVip
	 *************************************************************************************************************/
	WlsShopVip getShopVip(WlsShopVipId id);

	List<WlsShopVip> getShopVips(String wxAccountId);

	void saveShopVip(WlsShopVip entity);

	void deleteShopVip(WlsShopVip entity);

	/*************************************************************************************************************
	 * WlsShopVip
	 *************************************************************************************************************/
	void savePromotionPoints(WlsShopPromotionPoints entity);

	WlsShopPromotionPoints getPromotionPoints(String entityId);

	List<WlsShopPromotionPoints> getPromotionPointsByShop(String wxAccountId,
			boolean isFilterEnabled);

	void deletePromotionPoints(WlsShopPromotionPoints entity);
}
