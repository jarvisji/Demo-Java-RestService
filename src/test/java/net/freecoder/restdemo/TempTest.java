package net.freecoder.restdemo;

import net.freecoder.restdemo.util.UUIDUtil;

import org.junit.Test;

/**
 * @author JiTing
 */
public class TempTest {

	@Test
	public void test() throws Exception {

		for (int i = 0; i < 10; i++) {
			System.out.println(UUIDUtil.uuid8());
		}

	}

}
