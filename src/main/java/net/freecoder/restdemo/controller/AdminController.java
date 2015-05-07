/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.UserType;
import net.freecoder.restdemo.dao.WlsLogDao;
import net.freecoder.restdemo.util.DateTimeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author JiTing
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends CommonController {

	final static Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Autowired
	WlsLogDao wlsLogDao;

	@RequestMapping(value = "/wlslog/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getWlslogs(@RequestParam("c") String cred,
			@RequestParam(value = "start", required = false) String strStart,
			@RequestParam(value = "end", required = false) String strEnd,
			@RequestParam(value = "before", required = false) String strBefore,
			@RequestParam(value = "count", required = false) Integer pageSize) {

		UserType userType = cred2UserType(cred);
		if (!(UserType.ADM.equals(userType) || UserType.CSR.equals(userType))) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}

		Date start = null, end = null;
		long before = 0;
		if (strStart != null && strEnd != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			try {
				start = DateTimeUtil.getDayBegin(simpleDateFormat
						.parse(strStart));
				end = DateTimeUtil.getDayEnd(simpleDateFormat.parse(strEnd));
			} catch (ParseException e) {
				return new Result().setErrCode(ErrorCode.INVALID_PARAM)
						.setErrMsg("Invalid date string");
			}
		} else if (strBefore != null && pageSize != null) {
			try {
				before = Long.valueOf(strBefore);
			} catch (NumberFormatException e) {
				return new Result().setErrCode(ErrorCode.INVALID_PARAM)
						.setErrMsg("Invalid timestamp string");
			}
			if (pageSize.intValue() < 1) {
				return new Result().setErrCode(ErrorCode.INVALID_PARAM)
						.setErrMsg("Invalid count");
			}
		}

		List<Object> logs = null;
		Result result = new Result();
		if (start != null && end != null) {
			logger.debug("getWlslogs(), start={}, end={}", start, end);
			logs = wlsLogDao.getList(start, end);
			result.setDataTotalCount(wlsLogDao.totalCount());
		} else if (before > 0 && pageSize != null) {
			logger.debug("getWlslogs(), before={}, count={}", before, pageSize);
			logs = wlsLogDao.getList(before, pageSize.intValue());
			result.setDataTotalCount(wlsLogDao.totalCount());
		} else {
			logger.warn("getWlslogs(), get all logs, this should be avoid.");
			logs = wlsLogDao.getList();
		}
		return result.setEntity(logs);
	}
}
