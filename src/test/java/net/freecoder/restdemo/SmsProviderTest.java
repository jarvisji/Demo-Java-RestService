/**
 * 
 */
package net.freecoder.restdemo;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.service.SmsProvider;

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
public class SmsProviderTest {
	@Autowired
	SmsProvider smsProvider;

	@Test
	public void testCountSmsPieces() {
		Assert.assertEquals(1, smsProvider.countPieces(70));
		Assert.assertEquals(2, smsProvider.countPieces(71));
		Assert.assertEquals(2, smsProvider.countPieces(124));
		Assert.assertEquals(3, smsProvider.countPieces(125));
		Assert.assertEquals(3, smsProvider.countPieces(140));
		Assert.assertEquals(3, smsProvider.countPieces(186));
		Assert.assertEquals(4, smsProvider.countPieces(187));
		Assert.assertEquals(4, smsProvider.countPieces(248));
		Assert.assertEquals(5, smsProvider.countPieces(249));
	}

}
