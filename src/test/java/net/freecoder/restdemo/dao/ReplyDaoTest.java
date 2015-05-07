/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.dao.ReplyAutoDao;
import net.freecoder.restdemo.dao.ReplyKeywordDao;
import net.freecoder.restdemo.dao.ReplySubscribeDao;
import net.freecoder.restdemo.model.MaterialType;
import net.freecoder.restdemo.model.ReferenceType;
import net.freecoder.restdemo.model.WlsReplyAuto;
import net.freecoder.restdemo.model.WlsReplyKeyword;
import net.freecoder.restdemo.util.UUIDUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;

@RunWith(OrderedSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context-application.xml" })
public class ReplyDaoTest {

	@Autowired
	ReplyAutoDao mAutoDao;
	@Autowired
	ReplyKeywordDao mKwDao;
	@Autowired
	ReplySubscribeDao mSubDao;

	private static String materialId = UUIDUtil.uuid8();
	private static String wxAccountId = UUIDUtil.uuid8();

	@Test
	@Order(value = 10)
	public void testReplyBaseFunctions() throws Exception {
		WlsReplyAuto entity = new WlsReplyAuto();
		entity.setId(UUIDUtil.uuid8());
		entity.setMaterialId(materialId);
		entity.setWlsWxAccountId(wxAccountId);
		entity.setMaterialType(MaterialType.TEXT.toString());
		String entityId = mAutoDao.save(entity);

		// get and verify
		WlsReplyAuto dbEntity = mAutoDao.get(WlsReplyAuto.class, entityId);
		Assert.assertEquals(materialId, dbEntity.getMaterialId());
		Assert.assertEquals(wxAccountId, dbEntity.getWlsWxAccountId());
		Assert.assertEquals(MaterialType.TEXT.toString(),
				dbEntity.getMaterialType());
		Assert.assertNotNull(dbEntity.getAppCreateTime());
		Assert.assertNotNull(dbEntity.getAppLastModifyTime());

		// update
		// Thread.sleep(100);
		entity.setMaterialType(MaterialType.AUDIO.toString());
		mAutoDao.update(entity);
		// varify
		dbEntity = mAutoDao.get(WlsReplyAuto.class, entityId);
		Assert.assertEquals(materialId, dbEntity.getMaterialId());
		Assert.assertEquals(wxAccountId, dbEntity.getWlsWxAccountId());
		Assert.assertEquals(MaterialType.AUDIO.toString(),
				dbEntity.getMaterialType());
		Assert.assertNotNull(dbEntity.getAppCreateTime());
		Assert.assertNotNull(dbEntity.getAppLastModifyTime());
		Assert.assertTrue(dbEntity.getAppLastModifyTime().getTime() > dbEntity
				.getAppCreateTime().getTime());

		// get by wxAccountid
		List<WlsReplyAuto> entities = mAutoDao.getListByWxAccountId(
				WlsReplyAuto.class, wxAccountId);
		Assert.assertTrue(entities.size() == 1);
		Assert.assertEquals(entityId, entities.get(0).getId());

		// delete
		mAutoDao.delete(dbEntity);
		dbEntity = mAutoDao.get(WlsReplyAuto.class, entityId);
		Assert.assertNull(dbEntity);
	}

	/**
	 * The case:
	 * 
	 * <pre>
	 * There are two keyword replis in DB:
	 * 1. Keyword is "helloText" refType is "MATERIAL_TEXT";
	 * 2. Keyword is "helloAudio" refType is "MATERIAL_AUDIO".
	 * 
	 * Now create new keywords to overwite all of the two:
	 * 1. keyword is "helloMit" refType is "MATERIAL_MIT".
	 * 2. keyword is "helloKeyword" refType is "MATERIAL_MIT".
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testKeywordInterfaces() throws Exception {
		String oldKwId1 = UUIDUtil.uuid8();
		String oldKwId2 = UUIDUtil.uuid8();
		String newKwId1 = UUIDUtil.uuid8();
		String newKwId2 = UUIDUtil.uuid8();
		String oldKword1 = "helloText";
		String oldKword2 = "helloAudio";
		String newKword1 = "helloMit";
		String newKword2 = "helloKeyword";
		String oldRefType1 = ReferenceType.MATERIAL + "_" + MaterialType.TEXT;
		String oldRefType2 = ReferenceType.MATERIAL + "_" + MaterialType.AUDIO;
		String newRefType = ReferenceType.MATERIAL + "_" + MaterialType.MIT;
		String kwGroup = String.valueOf(System.currentTimeMillis());

		// create some keyword replies first.
		WlsReplyKeyword kw1 = new WlsReplyKeyword();
		kw1.setId(oldKwId1);
		kw1.setKeyword(oldKword1);
		kw1.setRefId(materialId);
		kw1.setRefType(oldRefType1);
		kw1.setKeywordGroup(kwGroup);
		kw1.setWlsWxAccountId(wxAccountId);

		WlsReplyKeyword kw2 = new WlsReplyKeyword();
		kw2.setId(oldKwId2);
		kw2.setKeyword(oldKword2);
		kw2.setRefId(materialId);
		kw2.setRefType(oldRefType2);
		kw2.setKeywordGroup(kwGroup);
		kw2.setWlsWxAccountId(wxAccountId);

		WlsReplyKeyword[] entities = new WlsReplyKeyword[] { kw1, kw2 };
		mKwDao.saveAll(wxAccountId, entities);

		// verify db records.
		// get data by refType.
		List<WlsReplyKeyword> dbEntities = mKwDao
				.getListByWxAccountIdAndRefType(wxAccountId,
						ReferenceType.MATERIAL);
		Assert.assertEquals(2, dbEntities.size());
		// get data by id.
		WlsReplyKeyword dbRecord1 = mKwDao.get(WlsReplyKeyword.class, oldKwId1);
		Assert.assertEquals(oldKword1, dbRecord1.getKeyword());
		// get data by keyword.
		dbEntities = mKwDao.getByKeyword(wxAccountId, oldKword2);
		Assert.assertEquals(1, dbEntities.size());
		Assert.assertEquals(oldKwId2, dbEntities.get(0).getId());

		// over write new data.
		WlsReplyKeyword newkw1 = new WlsReplyKeyword();
		newkw1.setId(newKwId1);
		newkw1.setKeyword(newKword1);
		newkw1.setRefId(materialId);
		newkw1.setRefType(newRefType);
		newkw1.setKeywordGroup(kwGroup);
		newkw1.setWlsWxAccountId(wxAccountId);

		WlsReplyKeyword newkw2 = new WlsReplyKeyword();
		newkw2.setId(newKwId2);
		newkw2.setKeyword(newKword2);
		newkw2.setRefId(materialId);
		newkw2.setRefType(newRefType);
		newkw2.setKeywordGroup(kwGroup);
		newkw2.setWlsWxAccountId(wxAccountId);

		WlsReplyKeyword[] newEntities = new WlsReplyKeyword[] { newkw1, newkw2 };
		String[] savedIds = mKwDao.saveAll(wxAccountId, newEntities);

		// verify new data by returned ids.
		WlsReplyKeyword dbEntity = mKwDao.get(WlsReplyKeyword.class,
				savedIds[0]);
		Assert.assertEquals(newKword1, dbEntity.getKeyword());

		dbEntity = mKwDao.get(WlsReplyKeyword.class, savedIds[1]);
		Assert.assertEquals(newKword2, dbEntity.getKeyword());
		// and there are only two records
		dbEntities = mKwDao.getListByWxAccountIdAndRefType(wxAccountId,
				ReferenceType.MATERIAL);
		Assert.assertEquals(2, dbEntities.size());
	}
}