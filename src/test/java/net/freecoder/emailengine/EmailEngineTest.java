/**
 * 
 */
package net.freecoder.emailengine;

import java.util.HashMap;
import java.util.Map;

import net.freecoder.emailengine.EmailEngine;
import net.freecoder.emailengine.EmailService;
import net.freecoder.emailengine.EmailTemplateEnum;
import net.freecoder.emailengine.vo.EmailTask;
import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.util.DateTimeUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author JiTing
 */
@RunWith(OrderedSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context-application.xml" })
public class EmailEngineTest {

	@Autowired
	EmailEngine emailEngine;

	@Test
	public void empty() {
		// this test has no real cases currently.
	}

	public void test() {
		EmailService emailService = emailEngine.getEmailService();
		EmailTask emailTask = emailService.createEmailTask(
				EmailTemplateEnum.EMAIL_REGISTER_CONFIRM, "test@freecoder.com");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regConfirmUrl", "regConfirmUrl");
		params.put("wlWebsiteUrl", "wlWebSiteUrl");
		params.put("currentDate", DateTimeUtil.getCSTDateString());

		emailService.sendEmail(emailTask, params);
	}

}
