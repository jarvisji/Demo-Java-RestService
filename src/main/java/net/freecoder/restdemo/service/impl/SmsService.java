package net.freecoder.restdemo.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;

import net.freecoder.restdemo.dao.SmsHistoryDao;
import net.freecoder.restdemo.exception.ServiceException;
import net.freecoder.restdemo.model.WlsSmsHistory;
import net.freecoder.restdemo.service.SmsProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service for SMS.
 * 
 * @author JiTing
 * 
 */
@Service
public class SmsService {

	private static Logger logger = LoggerFactory.getLogger(SmsService.class);

	@Autowired
	private Properties smsTemplates;
	@Autowired
	private SmsProvider smsProvider;
	@Autowired
	private SmsHistoryDao smsHistoryDao;

	public static final String SMS_REG_VERIFICATION_CODE = "reg.verification.code";
	public static final String SMS_ORDER_STATUS_CREATED_DETAIL = "order.status.created.detail";
	public static final String SMS_ORDER_STATUS_CREATED_SIMPLE = "order.status.created.simple";
	public static final String SMS_ORDER_STATUS_ACCEPTED = "order.status.accepted";
	public static final String SMS_ORDER_STATUS_DENIED = "order.status.denied";
	public static final String SMS_ORDER_STATUS_SHIPPED_SHOP = "order.status.shipped.shop";
	public static final String SMS_ORDER_STATUS_SHIPPED_EXPRESS = "order.status.shipped.express";

	public void sendRegVerificationCode(String phoneNumber, String code,
			String expireInMinites, String wxAccountId) {
		String template = getTemplate(SMS_REG_VERIFICATION_CODE);
		String content = MessageFormat.format(template, code, expireInMinites);
		send(phoneNumber, content, wxAccountId);
	}

	public void sendOrderCreatedDetail(String phoneNumber, String orderNo,
			String goods, int pieces, BigDecimal totalPrice, String consignee,
			String tel, String addr, String wxAccountId) {
		String template = getTemplate(SMS_ORDER_STATUS_CREATED_DETAIL);
		String content = MessageFormat.format(template, orderNo, goods, pieces,
				totalPrice, consignee, tel, addr);
		send(phoneNumber, content, wxAccountId);
	}

	public void sendOrderCreatedSimple(String phoneNumber, String orderUrl,
			String wxAccountId) {
		String template = getTemplate(SMS_ORDER_STATUS_CREATED_SIMPLE);
		String content = MessageFormat.format(template, orderUrl);
		send(phoneNumber, content, wxAccountId);
	}

	public void sendOrderAccepted(String phoneNumber, String orderNo,
			String wxAccountId) {
		String template = getTemplate(SMS_ORDER_STATUS_ACCEPTED);
		String content = MessageFormat.format(template, orderNo);
		send(phoneNumber, content, wxAccountId);
	}

	public void sendOrderDenied(String phoneNumber, String orderNo,
			String shopTel, String wxAccountId) {
		String template = getTemplate(SMS_ORDER_STATUS_DENIED);
		String content = MessageFormat.format(template, orderNo, shopTel);
		send(phoneNumber, content, wxAccountId);
	}

	public void sendOrderShippedShop(String phoneNumber, String orderNo,
			String shopName, String wxAccountId) {
		String template = getTemplate(SMS_ORDER_STATUS_SHIPPED_SHOP);
		String content = MessageFormat.format(template, orderNo, shopName);
		send(phoneNumber, content, wxAccountId);
	}

	public void sendOrderShippedExpress(String phoneNumber, String orderNo,
			String expressName, String expressNo, String wxAccountId) {
		String template = getTemplate(SMS_ORDER_STATUS_SHIPPED_EXPRESS);
		String content = MessageFormat.format(template, orderNo, expressName,
				expressNo);
		send(phoneNumber, content, wxAccountId);
	}

	public String getTemplate(String templateName) {
		String template = smsTemplates.getProperty(templateName);
		if (template == null) {
			throw new ServiceException("Not defined SMS tempalte: "
					+ templateName);
		}
		return template;
	}

	public Properties getTemplateList() {
		return smsTemplates;
	}

	public int countSms(String wxAccountId) {
		return smsHistoryDao.getByWxAccountId(wxAccountId).size();
	}

	public int countSms(String wxAccountId, String phoneNumber) {
		return smsHistoryDao.getByAccountPhone(wxAccountId, phoneNumber).size();
	}

	public int countSms(String wxAccountId, String phoneNumber, Date start,
			Date end) {
		return smsHistoryDao.getByAccountPhone(wxAccountId, phoneNumber, start,
				end).size();
	}

	private void send(String phoneNumber, String content, String wxAccountId) {
		long start = System.currentTimeMillis();
		WlsSmsHistory smsHistory = smsProvider.send(phoneNumber, content);
		logger.debug("Send message '{}' to '{}'.", content, phoneNumber);
		logger.debug("Response: {}, returns in {} milliseconds.",
				smsHistory.getProxyResponse(), System.currentTimeMillis()
						- start);

		smsHistory.setWlsWxAccountId(wxAccountId);
		smsHistory.setSmsCount(smsProvider.countPieces(content.length()));
		smsHistoryDao.save(smsHistory);
		// check response after save history, otherwise, will lost history if
		// exception throws.
		smsProvider.checkResponse(smsHistory.getProxyResponse());
	}
}
