/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.model.MaterialType;
import net.freecoder.restdemo.model.ReferenceType;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author JiTing
 */
public class EnumTest {

	@Test
	public void testKeywordRefType() {
		String value = ReferenceType.MATERIAL.value();
		Assert.assertEquals("material", value);

		ReferenceType kwrt = ReferenceType.parseValue("material");
		Assert.assertEquals(kwrt, ReferenceType.MATERIAL);
	}

	@Test
	public void testMaterialType() {
		Assert.assertEquals("material_text", MaterialType.TEXT.value());
		Assert.assertEquals(MaterialType.AUDIO,
				MaterialType.parseValue("material_audio"));
		Assert.assertEquals(MaterialType.SIT, MaterialType.parseValue("sit"));
	}
}
