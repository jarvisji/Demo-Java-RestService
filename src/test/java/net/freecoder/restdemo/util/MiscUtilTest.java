/**
 * 
 */
package net.freecoder.restdemo.util;

import net.freecoder.restdemo.model.WlsMaterialAudio;
import net.freecoder.restdemo.util.MiscUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author JiTing
 */
public class MiscUtilTest {

	@Test
	public void testIsNullOrEmpty() {
		Object oNull = null;
		Assert.assertTrue(MiscUtil.isNullOrEmpty(oNull));
		Assert.assertTrue(!MiscUtil.isNotNullAndNotEmpty(oNull));

		String sNull = null;
		Assert.assertTrue(MiscUtil.isNullOrEmpty(sNull));
		Assert.assertTrue(!MiscUtil.isNotNullAndNotEmpty(sNull));

		String sEmpty = "";
		Assert.assertTrue(MiscUtil.isNullOrEmpty(sEmpty));
		Assert.assertTrue(!MiscUtil.isNotNullAndNotEmpty(sEmpty));

		Object oNotNull = new WlsMaterialAudio();
		Assert.assertTrue(!MiscUtil.isNullOrEmpty(oNotNull));
		Assert.assertTrue(MiscUtil.isNotNullAndNotEmpty(oNotNull));

		String sNotNull = new String("test");
		Assert.assertTrue(!MiscUtil.isNullOrEmpty(sNotNull));
		Assert.assertTrue(MiscUtil.isNotNullAndNotEmpty(oNotNull));
	}

}
