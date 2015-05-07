/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.constant.WxMessageType;
import net.freecoder.restdemo.dao.WxMessageDao;
import net.freecoder.restdemo.model.WlsWxMessage;
import net.freecoder.restdemo.util.DateTimeUtil;
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
public class WxMessageDaoTest {

	@Autowired
	WxMessageDao msgDao;

	@Test
	public void test() throws ParseException {
		String fromUsername = "from";
		String toUsername = "to";
		String wxAccountId = UUIDUtil.uuid8();
		String msgId = String.valueOf(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss:SSS");
		Date aDate = sdf.parse("2014-5-13 14:02:25:123");
		Date dateInWeek = sdf.parse("2014-5-12 14:02:25:123");
		Date dateInMonth = sdf.parse("2014-5-01 14:02:25:123");

		// create messages, type is EVENT_SUBSCRIBE.
		WlsWxMessage subMsg = new WlsWxMessage(UUIDUtil.uuid8(), fromUsername,
				toUsername, WxMessageType.EVENT_SUBSCRIBE.toString(), aDate,
				wxAccountId);
		msgDao.save(subMsg);

		// create 1 message, date is another day in week, type is
		// EVENT_UNSUBSCRIBE.
		WlsWxMessage weekMsg = new WlsWxMessage(UUIDUtil.uuid8(), fromUsername,
				toUsername, WxMessageType.EVENT_SUBSCRIBE.toString(),
				dateInWeek, wxAccountId);
		msgDao.save(weekMsg);

		// create 1 message, date is another day in month, type is
		// EVENT_UNSUBSCRIBE.
		WlsWxMessage monthMsg = new WlsWxMessage(UUIDUtil.uuid8(),
				fromUsername, toUsername,
				WxMessageType.EVENT_SUBSCRIBE.toString(), dateInMonth,
				wxAccountId);
		msgDao.save(monthMsg);

		// create 1 message, date is another day in month, type is
		// EVENT_UNSUBSCRIBE.
		WlsWxMessage textMsg = new WlsWxMessage(UUIDUtil.uuid8(), fromUsername,
				toUsername, WxMessageType.TEXT.toString(), aDate, wxAccountId);
		msgDao.save(textMsg);

		// create 1 message, another account.
		String anotherAccountId = UUIDUtil.uuid8();
		WlsWxMessage anotherMsg = new WlsWxMessage(UUIDUtil.uuid8(),
				fromUsername, toUsername, WxMessageType.TEXT.toString(), aDate,
				anotherAccountId);
		msgDao.save(anotherMsg);

		// verify.
		long count = msgDao.getCount(DateTimeUtil.getDayBegin(aDate),
				DateTimeUtil.getDayEnd(aDate), WxMessageType.EVENT_SUBSCRIBE,
				wxAccountId);
		Assert.assertEquals(1L, count);

		count = msgDao.getCount(DateTimeUtil.getWeekBegin(aDate),
				DateTimeUtil.getWeekEnd(aDate), WxMessageType.EVENT_SUBSCRIBE,
				wxAccountId);
		Assert.assertEquals(2L, count);

		count = msgDao.getCount(DateTimeUtil.getMonthBegin(aDate),
				DateTimeUtil.getMonthEnd(aDate), WxMessageType.EVENT_SUBSCRIBE,
				wxAccountId);
		Assert.assertEquals(3L, count);

		count = msgDao.getCount(WxMessageType.EVENT_SUBSCRIBE, wxAccountId);
		Assert.assertEquals(3L, count);

		count = msgDao.getCount(DateTimeUtil.getMonthBegin(aDate),
				DateTimeUtil.getMonthEnd(aDate), WxMessageType.TEXT,
				anotherAccountId);
		Assert.assertEquals(1L, count);

		// verify with distinct.
		count = msgDao.getCount(DateTimeUtil.getMonthBegin(aDate),
				DateTimeUtil.getMonthEnd(aDate), WxMessageType.EVENT_SUBSCRIBE,
				wxAccountId, "fromUsername");
		Assert.assertEquals(1L, count);

		count = msgDao.getCount(WxMessageType.EVENT_SUBSCRIBE, wxAccountId,
				"fromUsername");
		Assert.assertEquals(1L, count);
	}

}
