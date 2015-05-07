/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.dao.MaterialImagetextDao;
import net.freecoder.restdemo.dao.MaterialMultiImagetextDao;
import net.freecoder.restdemo.model.WlsMaterialImagetext;
import net.freecoder.restdemo.model.WlsMaterialMultiImagetext;
import net.freecoder.restdemo.util.UUIDUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author JiTing
 */
@RunWith(OrderedSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context-application.xml" })
public class MaterialMultiImagetextDaoTest {

	@Autowired
	MaterialMultiImagetextDao mmitDao;
	@Autowired
	MaterialImagetextDao mitDao;

	private static String title = "TestMultiImageText";
	private static String title2 = "TestMultiImageText1";
	private static String summary = "TestMultiImageTextSummary";
	private static String coverUrl = "http://localhost";
	private static String wxAccountId = UUIDUtil.uuid8();

	@Test
	@Order(value = 10)
	public void testMitBaseFunctions() {
		// create flush new item will create mit also.
		String sitId = UUIDUtil.uuid8();
		String sitId2 = UUIDUtil.uuid8();
		WlsMaterialImagetext sit = new WlsMaterialImagetext(sitId, title,
				summary, coverUrl, wxAccountId);
		WlsMaterialImagetext sit2 = new WlsMaterialImagetext(sitId2, title2,
				summary, coverUrl, wxAccountId);

		WlsMaterialMultiImagetext mit = new WlsMaterialMultiImagetext(
				UUIDUtil.uuid8(), wxAccountId);
		mit.addWlsMaterialImagetext(sit);
		mit.addWlsMaterialImagetext(sit2);
		String savedId = mmitDao.save(mit);

		// get
		WlsMaterialMultiImagetext dbMit = mmitDao.get(savedId);
		Assert.assertEquals(wxAccountId, dbMit.getWlsWxAccountId());
		Assert.assertNotNull(dbMit.getAppCreateTime());
		Assert.assertNotNull(dbMit.getAppLastModifyTime());

		WlsMaterialImagetext[] arrayMit = new WlsMaterialImagetext[dbMit
				.getWlsMaterialImagetexts().size()];
		dbMit.getWlsMaterialImagetexts().toArray(arrayMit);
		Assert.assertEquals(2, arrayMit.length);
		// The order of set is not fixed, so we only assert the ids are exist.
		Assert.assertTrue(sitId == arrayMit[1].getId()
				|| sitId == arrayMit[0].getId());
		Assert.assertTrue(sitId2 == arrayMit[1].getId()
				|| sitId2 == arrayMit[0].getId());

		/** verify delete */
		mmitDao.delete(dbMit);
		WlsMaterialMultiImagetext deletedMit = mmitDao.get(mit.getId());
		Assert.assertNull(deletedMit);
		WlsMaterialImagetext deletedSit = mitDao.get(sitId2);
		Assert.assertNull(deletedSit);

	}

	@Test
	public void testSitCRUD() throws Exception {
		System.out.println(UUIDUtil.uuid8());
		System.out.println(UUIDUtil.uuid8());
		System.out.println(UUIDUtil.uuid8());
		WlsMaterialImagetext sit = new WlsMaterialImagetext(UUIDUtil.uuid8(),
				title, summary, coverUrl, wxAccountId);
		String savedSitId = mitDao.save(sit);

		// get
		WlsMaterialImagetext dbSit = mitDao.get(savedSitId);
		Assert.assertNotNull(dbSit);
		Assert.assertNotNull(dbSit.getAppCreateTime());
		Assert.assertNotNull(dbSit.getAppLastModifyTime());
		Assert.assertEquals(title, dbSit.getTitle());

		// update
		String newTitle = "new title for sit";
		dbSit.setTitle(newTitle);
		mitDao.update(dbSit);

		WlsMaterialImagetext updatedDbSit = mitDao.get(savedSitId);
		Assert.assertEquals(newTitle, updatedDbSit.getTitle());

		// delete
		mitDao.delete(savedSitId);
		WlsMaterialImagetext deletedSit = mitDao.get(savedSitId);
		Assert.assertNull(deletedSit);

	}
}
