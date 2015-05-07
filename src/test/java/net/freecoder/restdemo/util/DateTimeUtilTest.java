/**
 * 
 */
package net.freecoder.restdemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.freecoder.restdemo.util.DateTimeUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author JiTing
 */
public class DateTimeUtilTest {

	@Test
	public void testGetTodayRange() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss:SSS");
		Date date = sdf.parse("2014-5-13 14:02:25:123");
		String expDayBegin = "2014-5-13 00:00:00:000";
		String expDayEnd = "2014-5-13 23:59:59:999";

		String actBegin = sdf.format(DateTimeUtil.getDayBegin(date));
		String actEnd = sdf.format(DateTimeUtil.getDayEnd(date));

		Assert.assertEquals(expDayBegin, actBegin);
		Assert.assertEquals(expDayEnd, actEnd);
	}

	@Test
	public void testGetWeekRange() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss:SSS");
		Date date = sdf.parse("2014-5-13 14:02:25:123");
		String expBegin = "2014-5-11 00:00:00:000";
		String expEnd = "2014-5-17 23:59:59:999";

		String actBegin = sdf.format(DateTimeUtil.getWeekBegin(date));
		String actEnd = sdf.format(DateTimeUtil.getWeekEnd(date));

		Assert.assertEquals(expBegin, actBegin);
		Assert.assertEquals(expEnd, actEnd);
	}

	@Test
	public void testGetMonthRange() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss:SSS");
		Date date = sdf.parse("2014-5-13 14:02:25:123");
		String expBegin = "2014-5-01 00:00:00:000";
		String expEnd = "2014-5-31 23:59:59:999";

		String actBegin = sdf.format(DateTimeUtil.getMonthBegin(date));
		String actEnd = sdf.format(DateTimeUtil.getMonthEnd(date));

		Assert.assertEquals(expBegin, actBegin);
		Assert.assertEquals(expEnd, actEnd);
	}
}
