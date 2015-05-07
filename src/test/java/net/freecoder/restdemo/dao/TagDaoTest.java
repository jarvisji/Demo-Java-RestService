/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.dao.MaterialTextDao;
import net.freecoder.restdemo.dao.TagDao;
import net.freecoder.restdemo.model.MaterialType;
import net.freecoder.restdemo.model.WlsMaterialText;
import net.freecoder.restdemo.model.WlsTag;
import net.freecoder.restdemo.util.UUIDUtil;

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
public class TagDaoTest {

	private static String wxAccountId = UUIDUtil.uuid8();
	private static String materialId1 = UUIDUtil.uuid8();
	private static String materialId2 = UUIDUtil.uuid8();
	private static String tag = "test tag";

	@Autowired
	MaterialTextDao textDao;
	@Autowired
	TagDao tagDao;

	@Test
	public void test() {
		createMaterials();

		// create tag on material
		WlsTag wlsTag = new WlsTag();
		wlsTag.setTag(tag);
		wlsTag.setWlsWxAccountId(wxAccountId);
		tagDao.addTagOnMaterial(wlsTag, materialId1, MaterialType.TEXT);

		// check tag, and tagMaterial.
		List<WlsTag> tagsByAccount = tagDao.getTagsByAccount(wxAccountId);
		Assert.assertEquals(1, tagsByAccount.size());
		WlsTag dbTag = tagsByAccount.get(0);
		Assert.assertEquals(tag, dbTag.getTag());

		String[] materialIds = tagDao.getMaterialIdByTag(wxAccountId,
				dbTag.getId(), MaterialType.TEXT);
		Assert.assertEquals(1, materialIds.length);
		Assert.assertEquals(materialId1, materialIds[0]);

		// check tags by material
		List<WlsTag> tagsOfMaterial = tagDao.getTagsByMaterial(materialId1,
				MaterialType.TEXT);
		Assert.assertEquals(1, tagsOfMaterial.size());
		Assert.assertEquals(tag, tagsOfMaterial.get(0).getTag());

		// remove tag
		tagDao.removeTagFromMaterial(wlsTag.getId(), materialId1);

		// check tag, and tagMaterial.
		tagsByAccount = tagDao.getTagsByAccount(wxAccountId);
		Assert.assertEquals(0, tagsByAccount.size());

		materialIds = tagDao.getMaterialIdByTag(wxAccountId, dbTag.getId(),
				MaterialType.TEXT);
		Assert.assertEquals(0, materialIds.length);

	}

	private void createMaterials() {
		String textWithTag = "text with tag";
		String textWithoutTag = "text without tag";
		textDao.save(new WlsMaterialText(materialId2, textWithoutTag,
				wxAccountId));
		textDao.save(new WlsMaterialText(materialId1, textWithTag, wxAccountId));
	}
}
