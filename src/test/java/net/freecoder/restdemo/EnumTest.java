/**
 * 
 */
package net.freecoder.restdemo;

import net.freecoder.restdemo.constant.UserStatusEnum;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author JiTing
 */
public class EnumTest {

	@Test
	public void testUserStatusEnum() {
		Assert.assertEquals(UserStatusEnum.NORMAL_STATUS_ACTIVATED,
				UserStatusEnum.parseValue("1"));
		Assert.assertEquals(UserStatusEnum.PENDING_STATUS_INACTIVE,
				UserStatusEnum.parseValue("2"));
		Assert.assertEquals(UserStatusEnum.PENDING_STATUS_AUDIT,
				UserStatusEnum.parseValue("3"));
		Assert.assertEquals(UserStatusEnum.PENDING_STATUS_VERIFICATION,
				UserStatusEnum.parseValue("10000"));
		Assert.assertEquals(UserStatusEnum.PENDING_STATUS_VERIFICATION,
				UserStatusEnum.parseValue("32767"));
	}

}
