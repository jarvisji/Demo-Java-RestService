/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.Date;
import java.util.List;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.dao.SmsHistoryDao;
import net.freecoder.restdemo.model.WlsSmsHistory;
import net.freecoder.restdemo.util.UUIDUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author JiTing
 */
@RunWith(OrderedSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context-application.xml" })
public class SmsHistoryDaoTest {

	private static String uuid = UUIDUtil.uuid8();
	private static String wxAccountId = UUIDUtil.uuid8();

	@Autowired
	SmsHistoryDao smsDao;

	@Test
	public void testSmsHistoryDao() {
		String phoneNumber = "1234567890";
		String content = "English + 中文  test message.";
		Date dateRangeStart = new Date();
		WlsSmsHistory wlsSmsHistory = new WlsSmsHistory(uuid, phoneNumber,
				content, wxAccountId);
		smsDao.save(wlsSmsHistory);

		// get
		WlsSmsHistory dbEntity = smsDao.get(uuid);
		Assert.assertNotNull(dbEntity);
		Assert.assertNotNull(dbEntity.getAppCreateTime());
		Assert.assertNotNull(dbEntity.getAppLastModifyTime());
		Assert.assertEquals(phoneNumber, dbEntity.getPhoneNumber());

		// get by account id
		List<WlsSmsHistory> byWxAccountId = smsDao
				.getByWxAccountId(wxAccountId);
		Assert.assertEquals(1, byWxAccountId.size());
		Assert.assertEquals(content, byWxAccountId.get(0).getContent());

		// get by phone number
		List<WlsSmsHistory> byPhoneNumber = smsDao
				.getByPhoneNumber(phoneNumber);
		Assert.assertEquals(1, byPhoneNumber.size());

		// get by account and time range
		Date dateRangeEnd = new Date();
		System.out.println("Date range: " + dateRangeStart.toString() + " - "
				+ dateRangeEnd.toString());
		List<WlsSmsHistory> byWxAccountId2 = smsDao.getByWxAccountId(
				wxAccountId, dateRangeStart, dateRangeEnd);
		Assert.assertEquals(1, byWxAccountId2.size());

		// get by phone number and time range.
		List<WlsSmsHistory> byPhoneNumber2 = smsDao.getByPhoneNumber(
				phoneNumber, dateRangeStart, dateRangeEnd);
		Assert.assertEquals(1, byPhoneNumber2.size());

		List<WlsSmsHistory> byWxAccountPhone = smsDao.getByAccountPhone(
				wxAccountId, phoneNumber, dateRangeStart, dateRangeEnd);
		Assert.assertEquals(1, byWxAccountPhone.size());
	}
}
