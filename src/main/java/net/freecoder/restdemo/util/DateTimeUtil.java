package net.freecoder.restdemo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Some utility methods for date and time.
 * 
 * @author JiTing
 */
public final class DateTimeUtil {

	private DateTimeUtil() {

	}

	/**
	 * Get string value of current datetime of GMT.
	 * 
	 * @return String. Date time string in format yyyy-MM-dd HH:mm:ss.
	 */
	public static String getGMTDateTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar now = Calendar.getInstance();
		String dateTimeStr = sdf.format(now.getTime());
		return dateTimeStr;
	}

	/**
	 * Get string value of current date of GMT.
	 * 
	 * @return String. Date string in format yyyy-MM-dd.
	 */
	public static String getCSTDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("CST"));
		String dateStr = sdf.format(Calendar.getInstance().getTime());
		return dateStr;
	}

	/**
	 * Get Date value of GMT.
	 * 
	 * @return Date. Current date.
	 */
	public static Date getGMTDate() {
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		return now.getTime();
	}

	/**
	 * Convert Date object to string in special format: "yyyy-MM-dd HH:mm:ss".
	 * 
	 * @param date
	 *            Date object to convert.
	 * @return String
	 */
	public static String convertDate2String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * Get begin time of special date.
	 * 
	 * @return yyyy-MM-dd 00:00:00:000
	 */
	public static Date getDayBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Get end time of special date.
	 * 
	 * @return yyyy-MM-dd 23:59:59:999
	 */
	public static Date getDayEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * Get begin time of the first day in week of special date.
	 * 
	 * @return yyyy-MM-{date of the first day in week} 00:00:00:000
	 */
	public static Date getWeekBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDayBegin(date));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return cal.getTime();
	}

	/**
	 * Get end time of the last day in week of special date.
	 * 
	 * @return yyyy-MM-{date of the last day in week} 00:00:00:000
	 */
	public static Date getWeekEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDayEnd(date));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		return cal.getTime();
	}

	/**
	 * Get begin time of the first day in month of special date.
	 * 
	 * @return yyyy-MM-01 00:00:00:000
	 */
	public static Date getMonthBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDayBegin(date));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	/**
	 * Get end time of the last day in month of special date.
	 * 
	 * @return yyyy-MM-{date of last day in month} 00:00:00:000
	 */
	public static Date getMonthEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDayEnd(date));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}
}
