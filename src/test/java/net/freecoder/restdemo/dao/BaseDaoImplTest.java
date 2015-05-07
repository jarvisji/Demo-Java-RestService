/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.Date;

import net.freecoder.restdemo.dao.impl.BaseDaoImpl;
import net.freecoder.restdemo.model.WlsUser;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author JiTing
 */
public class BaseDaoImplTest extends BaseDaoImpl {

	@Test
	public void testUpdateTime() throws Exception {
		WlsUser user = new WlsUser();
		this.updateTime(user);
		Assert.assertNotNull(user.getAppCreateTime());
		Assert.assertNotNull(user.getAppLastModifyTime());

		Thread.sleep(1);

		Date savedCreateTime = user.getAppCreateTime();
		this.updateTime(user);
		Assert.assertEquals(savedCreateTime, user.getAppCreateTime());
		Assert.assertTrue(user.getAppLastModifyTime().after(savedCreateTime));
	}

	/**
	 * updateTime method only applicable on entities in
	 * net.freecoder.restdemo.model package. For other objects, the method
	 * should change nothing.
	 */
	@Test
	public void testUpdateTime4NonModel() {
		// how to test?
	}

}
