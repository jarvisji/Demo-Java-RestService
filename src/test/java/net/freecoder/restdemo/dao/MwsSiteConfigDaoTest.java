/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.dao.MwsDao;
import net.freecoder.restdemo.model.WlsMwsSiteConfig;
import net.freecoder.restdemo.model.WlsMwsSiteConfigId;
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
public class MwsSiteConfigDaoTest {

	@Autowired
	MwsDao mwsDao;

	private static String wxAccountId = UUIDUtil.uuid8();

	@Test
	public void testCRUD() {
		String propName = "testProp";
		String propValue = String.valueOf(System.currentTimeMillis());

		WlsMwsSiteConfigId id = new WlsMwsSiteConfigId(propName, wxAccountId);
		WlsMwsSiteConfig configItem = new WlsMwsSiteConfig(id, propValue);
		WlsMwsSiteConfigId configId = mwsDao.saveConfig(configItem);

		// get
		WlsMwsSiteConfig dbItem = mwsDao.get(id);
		Assert.assertEquals(configId, id);

		// update
		String newValue = "new";
		dbItem.setPropValue(newValue);
		mwsDao.update(dbItem);

		// get by wxAccountId
		List<WlsMwsSiteConfig> configList = mwsDao
				.getListByWxAccountId(wxAccountId);
		Assert.assertEquals(1, configList.size());
		Assert.assertEquals(id, configList.get(0).getId());
		Assert.assertEquals(newValue, configList.get(0).getPropValue());

		// delete
		mwsDao.delete(dbItem);
		WlsMwsSiteConfig deletedDbItem = mwsDao.get(id);
		Assert.assertNull(deletedDbItem);
	}

}
