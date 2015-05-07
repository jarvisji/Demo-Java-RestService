/**
 * 
 */
package net.freecoder.restdemo.common;

import net.freecoder.restdemo.service.impl.SmsService;
import net.freecoder.restdemo.util.UUIDUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Do not run this test frequently, this will burn out our SMS quota.
 * 
 * @author JiTing
 */
@RunWith(OrderedSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context-application.xml" })
public class SmsServiceTest {

	@Autowired
	SmsService smsService;

	@Test
	public void testSendSms() {
		String phoneNumber = "18655100548";
		String wxAccountId = UUIDUtil.uuid8();

		// smsService.sendRegVerificationCode(phoneNumber, UUIDUtil.uuid8(),
		// "10", wxAccountId);

		// smsService.sendOrderCreatedSimple(phoneNumber, "testOrderUrl",
		// wxAccountId);

		// smsService.sendOrderCreatedDetail(phoneNumber, "testOrderNo",
		// "可口可乐x1\n乐事薯条x2\n", 1, 5.00f, "jiting",
		// "12345678910", "Addr", wxAccountId);

		// smsService.sendOrderAccepted(phoneNumber, "orderNo", wxAccountId);

		// smsService.sendOrderDenied(phoneNumber, "orderNo", "shopTel",
		// wxAccountId);

		// smsService.sendOrderShippedShop("18656900836", "orderNo", "shopName",
		// wxAccountId);

		// smsService.sendOrderShippedExpress("15255183850", "orderNo",
		// "expressName", "expressNo", wxAccountId);

	}

}
