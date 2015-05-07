/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.math.BigDecimal;
import java.util.List;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.constant.VipPointsSource;
import net.freecoder.restdemo.dao.ShopDao;
import net.freecoder.restdemo.dao.VipDao;
import net.freecoder.restdemo.model.WlsShopOrder;
import net.freecoder.restdemo.model.WlsShopVip;
import net.freecoder.restdemo.model.WlsShopVipId;
import net.freecoder.restdemo.model.WlsVip;
import net.freecoder.restdemo.model.WlsVipAddress;
import net.freecoder.restdemo.model.WlsVipAddressId;
import net.freecoder.restdemo.model.WlsVipPointsHistory;
import net.freecoder.restdemo.model.WlsVipPointsHistoryId;
import net.freecoder.restdemo.model.WlsVipStatistics;
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
public class VipDaoTest {

	static String vipId = UUIDUtil.uuid8();
	static String mobilePhone = "13905511234";
	static String password = "testpass";

	@Autowired
	VipDao vipDao;
	@Autowired
	ShopDao shopDao;

	@Test
	@Order(value = 10)
	public void testCreateVip() {
		WlsVip wlsVip = new WlsVip(vipId, mobilePhone, password,
				DateTimeUtil.getGMTDate());
		vipDao.save(wlsVip);

		WlsVip dbVip = vipDao.get(vipId);
		Assert.assertEquals(mobilePhone, dbVip.getMobilePhone());
		Assert.assertNotNull(dbVip.getAppCreateTime());
		Assert.assertNotNull(dbVip.getAppLastModifyTime());

		// login
		WlsVip vipByPhone = vipDao.loginByPhone(mobilePhone, password);
		Assert.assertEquals(vipId, vipByPhone.getId());
	}

	@Test
	@Order(value = 15)
	public void testGetVipByShop() {
		short itemTotalCount = 1;
		BigDecimal itemTotalPrice = new BigDecimal(12.50);
		String wlsWxAccountId = UUIDUtil.uuid8();

		// The uppon case already created a vip, now create an order, and entity
		// of WlsShopVip. So we can test get vip
		// by shop.
		WlsShopOrder wlsShopOrder = new WlsShopOrder(UUIDUtil.uuid8(),
				"testOrderNo", itemTotalCount, itemTotalPrice, vipId,
				"testAddress", "testName", "testPhone", "testStatus",
				"testPayment", wlsWxAccountId, DateTimeUtil.getGMTDate());
		shopDao.saveOrder(wlsShopOrder);

		WlsShopVipId wlsShopVipId = new WlsShopVipId(vipId, wlsWxAccountId);
		WlsShopVip wlsShopVip = new WlsShopVip(wlsShopVipId,
				DateTimeUtil.getGMTDate());
		shopDao.saveShopVip(wlsShopVip);

		List<WlsVip> vipList = vipDao.getByShop(wlsWxAccountId);
		Assert.assertEquals(1, vipList.size());
		Assert.assertEquals(mobilePhone, vipList.get(0).getMobilePhone());
	}

	@Test
	@Order(value = 20)
	public void testVipAddress() {
		WlsVipAddressId wlsVipAddressId = new WlsVipAddressId(UUIDUtil.uuid8(),
				vipId);
		WlsVipAddress wlsVipAddress = new WlsVipAddress(wlsVipAddressId,
				"test", "testaddress", "testphone", true,
				DateTimeUtil.getGMTDate());
		vipDao.saveAddresss(wlsVipAddress);

		// get list
		List<WlsVipAddress> addresses = vipDao.getAddresses(vipId);
		Assert.assertEquals(1, addresses.size());
		Assert.assertEquals("test", addresses.get(0).getName());

		// get single entity
		String addressId = addresses.get(0).getId().getId();
		WlsVipAddress address = vipDao.getAddress(vipId, addressId);
		Assert.assertEquals("test", address.getName());

		// update
		wlsVipAddress.setName("updatedName");
		vipDao.saveAddresss(wlsVipAddress);
		addresses = vipDao.getAddresses(vipId);
		Assert.assertEquals(1, addresses.size());
		Assert.assertEquals("updatedName", addresses.get(0).getName());

		// delete
		vipDao.deleteAddress(addresses.get(0));
		addresses = vipDao.getAddresses(vipId);
		Assert.assertEquals(0, addresses.size());
	}

	@Test
	@Order(value = 30)
	public void testVipPointsHistory() {
		String testSourceId = UUIDUtil.uuid8();
		WlsVipPointsHistoryId wlsVipPointsHistoryId = new WlsVipPointsHistoryId(
				UUIDUtil.uuid8(), vipId);
		WlsVipPointsHistory wlsVipPointsHistory = new WlsVipPointsHistory(
				wlsVipPointsHistoryId, (short) 1,
				VipPointsSource.ORDER_PRICE.toString(), testSourceId,
				DateTimeUtil.getGMTDate());
		vipDao.savePointsHistory(wlsVipPointsHistory);

		// get list
		List<WlsVipPointsHistory> pointsHistories = vipDao
				.getPointsHistories(vipId);
		Assert.assertEquals(1, pointsHistories.size());
		Assert.assertEquals(testSourceId, pointsHistories.get(0).getSourceId());

		// get single entity
		String phId = pointsHistories.get(0).getId().getId();
		WlsVipPointsHistory pointsHistory = vipDao
				.getPointsHistory(vipId, phId);
		Assert.assertEquals(testSourceId, pointsHistory.getSourceId());

		// delete
		vipDao.deletePointsHistory(pointsHistories.get(0));
		pointsHistories = vipDao.getPointsHistories(vipId);
		Assert.assertEquals(0, pointsHistories.size());
	}

	@Test
	@Order(value = 40)
	public void testVipStatistics() {
		WlsVipStatistics wlsVipStatistics = new WlsVipStatistics(vipId, 1,
				DateTimeUtil.getGMTDate());
		vipDao.saveStatistics(wlsVipStatistics);

		// get
		WlsVipStatistics statistics = vipDao.getStatistics(vipId);
		Assert.assertEquals(1, statistics.getTotalPoints());

		// update
		vipDao.adjustStatTotalPoints(vipId, 3);
		statistics = vipDao.getStatistics(vipId);
		Assert.assertEquals(4, statistics.getTotalPoints());

		vipDao.adjustStatTotalPoints(vipId, -2);
		statistics = vipDao.getStatistics(vipId);
		Assert.assertEquals(2, statistics.getTotalPoints());

		// delete
		vipDao.deleteStatistics(statistics);
		statistics = vipDao.getStatistics(vipId);
		Assert.assertNull(statistics);
	}

	@Test
	@Order(value = 999)
	public void testDeleteVip() {
		vipDao.delete(vipId);
		WlsVip dbVip = vipDao.get(vipId);
		Assert.assertNull(dbVip);
	}
}
