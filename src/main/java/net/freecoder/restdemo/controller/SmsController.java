/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.Calendar;
import java.util.Date;

import net.freecoder.restdemo.annotation.WlsModule;
import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.SystemModules;
import net.freecoder.restdemo.service.impl.SmsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author JiTing
 */
@Controller
@RequestMapping(value = "/sms")
@WlsModule(SystemModules.M_SHOP)
public class SmsController extends CommonController {

	@Autowired
	SmsService smsService;

	@RequestMapping(value = "/template/list", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	Result getTemplateList() {
		return new Result().setData(smsService.getTemplateList());
	}

	@RequestMapping(value = "/template/order/status/created/detail", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	Result getTemplateCreatedDetail() {
		Result result = new Result();
		result.setData("key", SmsService.SMS_ORDER_STATUS_CREATED_DETAIL);
		result.setData("value", smsService
				.getTemplate(SmsService.SMS_ORDER_STATUS_CREATED_DETAIL));
		return result;
	}

	@RequestMapping(value = "/template/order/status/created/simple", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	Result getTemplateCreatedSimple() {
		Result result = new Result();
		result.setData("key", SmsService.SMS_ORDER_STATUS_CREATED_SIMPLE);
		result.setData("value", smsService
				.getTemplate(SmsService.SMS_ORDER_STATUS_CREATED_SIMPLE));
		return result;
	}

	@RequestMapping(value = "/template/order/status/accepted", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	Result getTemplateAccepted() {
		Result result = new Result();
		result.setData("key", SmsService.SMS_ORDER_STATUS_ACCEPTED);
		result.setData("value",
				smsService.getTemplate(SmsService.SMS_ORDER_STATUS_ACCEPTED));
		return result;
	}

	@RequestMapping(value = "/template/order/status/denied", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	Result getTemplateDenied() {
		Result result = new Result();
		result.setData("key", SmsService.SMS_ORDER_STATUS_DENIED);
		result.setData("value",
				smsService.getTemplate(SmsService.SMS_ORDER_STATUS_DENIED));
		return result;
	}

	@RequestMapping(value = "/template/order/status/shipped/express", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	Result getTemplateShippedExpress() {
		Result result = new Result();
		result.setData("key", SmsService.SMS_ORDER_STATUS_SHIPPED_EXPRESS);
		result.setData("value", smsService
				.getTemplate(SmsService.SMS_ORDER_STATUS_SHIPPED_EXPRESS));
		return result;
	}

	@RequestMapping(value = "/template/order/status/shipped/shop", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	Result getTemplateShippedShop() {
		Result result = new Result();
		result.setData("key", SmsService.SMS_ORDER_STATUS_SHIPPED_SHOP);
		result.setData("value", smsService
				.getTemplate(SmsService.SMS_ORDER_STATUS_SHIPPED_SHOP));
		return result;
	}

	@RequestMapping(value = "/template/reg/verification/code", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	Result getTemplateVerficationCode() {
		Result result = new Result();
		result.setData("key", SmsService.SMS_REG_VERIFICATION_CODE);
		result.setData("value",
				smsService.getTemplate(SmsService.SMS_REG_VERIFICATION_CODE));
		return result;
	}

	@RequestMapping(value = "/count/account/{wxAccountId}/range/{timeRange}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	Result getSmsCountByAccount(@PathVariable String wxAccountId,
			@PathVariable String timeRange,
			@RequestParam("phone") String phoneNumber,
			@RequestParam("c") String cred) {
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			return new Result().setSuccess(false).setErrCode(
					ErrorCode.NO_PRIVILIEGE);
		}
		Date now = Calendar.getInstance().getTime();
		Date begin = getRangeBeginDate(now, timeRange);
		Date end = getRangeEndDate(now, timeRange);
		int countSms = smsService
				.countSms(wxAccountId, phoneNumber, begin, end);
		return new Result().setData("count", countSms);
	}
}
