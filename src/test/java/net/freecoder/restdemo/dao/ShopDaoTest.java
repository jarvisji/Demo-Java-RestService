/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.constant.ShopConfigPropPrefix;
import net.freecoder.restdemo.dao.ShopDao;
import net.freecoder.restdemo.exception.ServiceDaoForeignKeyConstraintException;
import net.freecoder.restdemo.model.WlsShopCategory;
import net.freecoder.restdemo.model.WlsShopConfig;
import net.freecoder.restdemo.model.WlsShopConfigId;
import net.freecoder.restdemo.model.WlsShopEntry;
import net.freecoder.restdemo.model.WlsShopItem;
import net.freecoder.restdemo.model.WlsShopOrder;
import net.freecoder.restdemo.model.WlsShopOrderItem;
import net.freecoder.restdemo.model.WlsShopOrderItemId;
import net.freecoder.restdemo.model.WlsShopPromotionPoints;
import net.freecoder.restdemo.util.DateTimeUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author JiTing
 */
@RunWith(OrderedSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context-application.xml" })
public class ShopDaoTest {

	private static String wxAccountId = UUIDUtil.uuid8();
	private static Date appCreateTime = DateTimeUtil.getGMTDate();

	@Autowired
	ShopDao shopDao;

	@Test
	@Order(value = 10)
	public void testShopEntry() {
		WlsShopEntry wlsShopEntry = new WlsShopEntry(wxAccountId, true,
				"testKeyword", "title", "coverUrl", appCreateTime);
		shopDao.save(wlsShopEntry);

		// get
		WlsShopEntry dbEntity = shopDao.get(wxAccountId);
		Assert.assertEquals("title", dbEntity.getTitle());

		// delte
		shopDao.delete(dbEntity);
		dbEntity = shopDao.get(wxAccountId);
		Assert.assertNull(dbEntity);
	}

	@Test
	@Order(value = 20)
	public void testShopConfig() {
		WlsShopConfigId wlsShopConfigId = new WlsShopConfigId("propName",
				wxAccountId);
		WlsShopConfig wlsShopConfig = new WlsShopConfig(wlsShopConfigId,
				"propValue", appCreateTime);
		shopDao.saveConfig(wlsShopConfig);

		WlsShopConfigId wlsShopConfigId2 = new WlsShopConfigId(
				ShopConfigPropPrefix.BASE.value() + "_propName", wxAccountId);
		WlsShopConfig wlsShopConfig2 = new WlsShopConfig(wlsShopConfigId2,
				"propValue", appCreateTime);
		shopDao.saveConfig(wlsShopConfig2);

		// get
		WlsShopConfig dbConfig = shopDao.getConfig("propName", wxAccountId);
		Assert.assertEquals("propValue", dbConfig.getPropValue());

		List<WlsShopConfig> configsWithoutPrefix = shopDao
				.getConfigs(wxAccountId);
		Assert.assertEquals(2, configsWithoutPrefix.size());

		List<WlsShopConfig> configsWithPrefix = shopDao.getConfigs(wxAccountId,
				ShopConfigPropPrefix.BASE);
		Assert.assertEquals(1, configsWithPrefix.size());

		// delete
		shopDao.deleteConfig("propName", wxAccountId);
		List<WlsShopConfig> configs = shopDao.getConfigs(wxAccountId);
		Assert.assertEquals(1, configs.size());
	}

	@Test
	@Order(value = 30)
	public void testShopCategoryAndItem() {
		String cid = UUIDUtil.uuid8();
		WlsShopCategory wlsShopCategory = new WlsShopCategory(cid,
				"categoryName", wxAccountId, appCreateTime);
		shopDao.saveCategory(wlsShopCategory);

		// get
		WlsShopCategory dbCategory = shopDao.getCategory(cid);
		Assert.assertEquals("categoryName", dbCategory.getName());
		List<WlsShopCategory> dbCategories = shopDao
				.getCategoriesByShop(wxAccountId);
		Assert.assertEquals(1, dbCategories.size());

		// create item in category
		String itemId = UUIDUtil.uuid8();
		WlsShopItem wlsShopItem = new WlsShopItem(itemId, cid, "name", "brief",
				new BigDecimal(1.20), 10, false, wxAccountId, appCreateTime);
		shopDao.saveItem(wlsShopItem);

		// get item
		WlsShopItem dbItem = shopDao.getItem(itemId);
		Assert.assertEquals("name", dbItem.getName());
		List<WlsShopItem> dbItems = shopDao.getItems(wxAccountId, cid);
		Assert.assertEquals(1, dbItems.size());

		// get items by criterion
		Map<String, Object> criterions = new HashMap<String, Object>(1);
		criterions.put("isInSell", true);
		List<WlsShopItem> items = shopDao.getItems(wxAccountId, criterions);
		Assert.assertEquals(0, items.size());

		// update
		wlsShopItem.setIsInSell(true);
		shopDao.saveItem(wlsShopItem);
		items = shopDao.getItems(wxAccountId, criterions);
		Assert.assertEquals(1, items.size());

		// delete category which has item will fail
		boolean caughtException = false;
		try {
			shopDao.deleteCategory(dbCategory);
		} catch (ServiceDaoForeignKeyConstraintException e) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);

		// delete item
		shopDao.deleteItem(dbItem);
		dbItems = shopDao.getItems(wxAccountId, cid);
		Assert.assertEquals(0, dbItems.size());

		// delete category
		shopDao.deleteCategory(dbCategory);
		dbCategories = shopDao.getCategoriesByShop(wxAccountId);
		Assert.assertEquals(0, dbCategories.size());
	}

	@Test
	@Order(value = 40)
	public void testShopOrderAndItem() {
		String orderId = UUIDUtil.uuid8();
		String itemId = UUIDUtil.uuid8();
		String itemId2 = UUIDUtil.uuid8();
		String vipId = UUIDUtil.uuid8();
		BigDecimal decimal = new BigDecimal(10.00);

		WlsShopOrder wlsShopOrder = new WlsShopOrder(orderId, "orderNo",
				(short) 1, decimal, vipId, "shippingAddress", "shippingName",
				"shippingPhone", "status", "payment", wxAccountId,
				appCreateTime);
		WlsShopOrderItemId wlsShopOrderItemId = new WlsShopOrderItemId(orderId,
				itemId);
		WlsShopOrderItem wlsShopOrderItem = new WlsShopOrderItem(
				wlsShopOrderItemId, wlsShopOrder, decimal, (short) 1, "source",
				appCreateTime);
		WlsShopOrderItemId wlsShopOrderItemId2 = new WlsShopOrderItemId(
				orderId, itemId2);
		WlsShopOrderItem wlsShopOrderItem2 = new WlsShopOrderItem(
				wlsShopOrderItemId2, wlsShopOrder, decimal, (short) 1,
				"source", appCreateTime);
		wlsShopOrder.getWlsShopOrderItems().add(wlsShopOrderItem);
		wlsShopOrder.getWlsShopOrderItems().add(wlsShopOrderItem2);
		shopDao.saveOrder(wlsShopOrder);

		// get order and item
		WlsShopOrder dbOrder = shopDao.getOrder(orderId);
		Assert.assertEquals("orderNo", dbOrder.getNo());
		Assert.assertEquals(2, dbOrder.getWlsShopOrderItems().size());
		List<WlsShopOrder> ordersByShop = shopDao.getOrdersByShop(wxAccountId);
		Assert.assertEquals(1, ordersByShop.size());
		List<WlsShopOrder> ordersByVip = shopDao.getOrdersByVip(vipId);
		Assert.assertEquals(1, ordersByVip.size());

		// delete item2, the remain item should be item1.
		shopDao.deleteOrderItem(wlsShopOrderItem2);
		WlsShopOrder newDbOrder = shopDao.getOrder(orderId);
		Assert.assertEquals(1, newDbOrder.getWlsShopOrderItems().size());
		Object[] arrayItems = newDbOrder.getWlsShopOrderItems().toArray();
		Assert.assertEquals(itemId, ((WlsShopOrderItem) arrayItems[0]).getId()
				.getItemId());

		// delete order
		shopDao.deleteOrder(newDbOrder);
		ordersByShop = shopDao.getOrdersByShop(wxAccountId);
		Assert.assertEquals(0, ordersByShop.size());
		ordersByVip = shopDao.getOrdersByVip(vipId);
		Assert.assertEquals(0, ordersByVip.size());
	}

	@Test
	@Order(value = 50)
	public void testShopPromotionPoints() {
		String ppId = UUIDUtil.uuid8();
		WlsShopPromotionPoints wlsShopPromotionPoints = new WlsShopPromotionPoints(
				ppId, (short) 10, wxAccountId, appCreateTime);
		shopDao.savePromotionPoints(wlsShopPromotionPoints);

		// get
		WlsShopPromotionPoints dbPromotionPoints = shopDao
				.getPromotionPoints(ppId);
		Assert.assertNotNull(dbPromotionPoints);

		boolean isFilterEnabled = true;
		List<WlsShopPromotionPoints> promotionPointsByShop = shopDao
				.getPromotionPointsByShop(wxAccountId, isFilterEnabled);
		Assert.assertEquals(0, promotionPointsByShop.size());

		isFilterEnabled = false;
		promotionPointsByShop = shopDao.getPromotionPointsByShop(wxAccountId,
				isFilterEnabled);
		Assert.assertEquals(1, promotionPointsByShop.size());

		// update enabled flag.
		dbPromotionPoints.setIsEnable(true);
		shopDao.savePromotionPoints(dbPromotionPoints);
		promotionPointsByShop = shopDao.getPromotionPointsByShop(wxAccountId,
				isFilterEnabled);
		Assert.assertEquals(1, promotionPointsByShop.size());

		isFilterEnabled = false;
		promotionPointsByShop = shopDao.getPromotionPointsByShop(wxAccountId,
				isFilterEnabled);
		Assert.assertEquals(1, promotionPointsByShop.size());

		// delete
		shopDao.deletePromotionPoints(dbPromotionPoints);
		promotionPointsByShop = shopDao.getPromotionPointsByShop(wxAccountId,
				false);
		Assert.assertEquals(0, promotionPointsByShop.size());

	}

}
