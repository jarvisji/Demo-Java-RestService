/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.constant.WxMessageType;
import net.freecoder.restdemo.dao.WxMessageDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Get stats data for weixin messages.
 * 
 * @author JiTing
 */
@Controller
@RequestMapping("/wxstats")
public class WxStatsController extends CommonController {

	private final Logger logger = LoggerFactory
			.getLogger(WxStatsController.class);

	@Autowired
	WxMessageDao wxMsgDao;

	/**
	 * Get subscribe count in special time range.
	 * 
	 * @param timeRange
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/event/subscribe/{timeRange}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	Result getSubscribeCount(@PathVariable String timeRange,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		logger.debug(
				"Get count of subscription of account id is '{}', time range in the '{}' of today.",
				wxAccountId, timeRange);
		long count = 0;
		Date now = Calendar.getInstance().getTime();
		Date begin = getRangeBeginDate(now, timeRange);
		Date end = getRangeEndDate(now, timeRange);
		String distinctProp = "fromUsername";
		if (begin != null && end != null) {
			count = wxMsgDao.getCount(begin, end,
					WxMessageType.EVENT_SUBSCRIBE, wxAccountId, distinctProp);
		} else {
			logger.debug("Time range is not supported: {}, return count is 0.",
					timeRange);
		}
		long totalCount = wxMsgDao.getCount(WxMessageType.EVENT_SUBSCRIBE,
				wxAccountId, distinctProp);
		Result result = new Result();
		result.setData("type", WxMessageType.EVENT_SUBSCRIBE.toString());
		result.setCount(count, totalCount);
		return result;
	}

	/**
	 * Get unsubscribe count in special time range.
	 * 
	 * @param timeRange
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/event/unsubscribe/{timeRange}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	Result getUnSubscribeCount(@PathVariable String timeRange,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		logger.debug(
				"Get count of unsubscription of account id is '{}', time range in the '{}' of today.",
				wxAccountId, timeRange);
		long count = 0;
		Date now = Calendar.getInstance().getTime();
		Date begin = getRangeBeginDate(now, timeRange);
		Date end = getRangeEndDate(now, timeRange);
		String distinctProp = "fromUsername";
		if (begin != null && end != null) {
			count = wxMsgDao.getCount(begin, end,
					WxMessageType.EVENT_UNSUBSCRIBE, wxAccountId, distinctProp);
		} else {
			logger.debug("Time range is not supported: {}, return count is 0.",
					timeRange);
		}
		long totalCount = wxMsgDao.getCount(WxMessageType.EVENT_UNSUBSCRIBE,
				wxAccountId, distinctProp);
		Result result = new Result();
		result.setData("type", WxMessageType.EVENT_UNSUBSCRIBE.toString());
		result.setCount(count, totalCount);
		return result;
	}

	/**
	 * Get message count in special time range.
	 * 
	 * @param timeRange
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/message/text/{timeRange}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	Result getTextMessageCount(@PathVariable String timeRange,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		logger.debug(
				"Get count of text message of account id '{}', time range in the '{}' of today.",
				wxAccountId, timeRange);
		long count = 0;
		Date now = Calendar.getInstance().getTime();
		Date begin = getRangeBeginDate(now, timeRange);
		Date end = getRangeEndDate(now, timeRange);
		if (begin != null && end != null) {
			count = wxMsgDao.getCount(begin, end, WxMessageType.TEXT,
					wxAccountId);
		} else {
			logger.debug("Time range is not supported: {}, return count is 0.",
					timeRange);
		}
		long totalCount = wxMsgDao.getCount(WxMessageType.TEXT, wxAccountId);
		Result result = new Result();
		result.setData("type", WxMessageType.TEXT.toString());
		result.setCount(count, totalCount);
		return result;
	}

	/**
	 * Frequently used data of special weixin account, this is a quick interface
	 * for account index page.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/account/{timeRange}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	Result getAccountStats(@PathVariable String timeRange,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		logger.debug(
				"Get status of account id '{}', time range in the '{}' of today.",
				wxAccountId, timeRange);

		Date now = Calendar.getInstance().getTime();
		Date begin = getRangeBeginDate(now, timeRange);
		Date end = getRangeEndDate(now, timeRange);
		String distinctProp = "fromUsername";

		long textCount = 0;
		long subscribeCount = 0;
		long unsubscribeCount = 0;
		if (begin != null && end != null) {
			textCount = wxMsgDao.getCount(begin, end, WxMessageType.TEXT,
					wxAccountId);
			subscribeCount = wxMsgDao.getCount(begin, end,
					WxMessageType.EVENT_SUBSCRIBE, wxAccountId, distinctProp);
			unsubscribeCount = wxMsgDao.getCount(begin, end,
					WxMessageType.EVENT_UNSUBSCRIBE, wxAccountId, distinctProp);
		} else {
			logger.debug("Time range is not supported: {}, return count is 0.",
					timeRange);
		}
		Result result = new Result();
		result.setData("textCount", textCount);
		result.setData("subscribeCount", subscribeCount);
		result.setData("unsubscribeCount", unsubscribeCount);

		long totalCount = wxMsgDao.getCount(WxMessageType.TEXT, wxAccountId);
		result.setData("textTotalCount", totalCount);

		totalCount = wxMsgDao.getCount(WxMessageType.EVENT_SUBSCRIBE,
				wxAccountId, distinctProp);
		result.setData("subscribeTotalCount", totalCount);

		totalCount = wxMsgDao.getCount(WxMessageType.EVENT_UNSUBSCRIBE,
				wxAccountId, distinctProp);
		result.setData("unsubscribeTotalCount", totalCount);

		return result;
	}
}
