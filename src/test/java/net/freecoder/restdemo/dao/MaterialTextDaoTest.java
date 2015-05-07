/**
 * 
 */
package net.freecoder.restdemo.dao;

/**
 * @author JiTing
 */
/**
 * 
 */
import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.dao.MaterialTextDao;
import net.freecoder.restdemo.dao.UserDao;
import net.freecoder.restdemo.dao.WxAccountDao;
import net.freecoder.restdemo.model.WlsMaterialText;
import net.freecoder.restdemo.model.WlsUser;
import net.freecoder.restdemo.model.WlsWxAccount;
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
public class MaterialTextDaoTest {

	private static String id = UUIDUtil.uuid8();
	private static String content = "test content of material text";

	@Autowired
	MaterialTextDao materialDao;
	@Autowired
	UserDao userDao;
	@Autowired
	WxAccountDao wxAccountDao;

	@Test
	@Order(value = 10)
	public void testCreate() {
		WlsUser wlsUser = new WlsUser(id, "pass", "test@freecoder.net");
		userDao.save(wlsUser);

		WlsWxAccount wlsWxAccount = new WlsWxAccount(id, "WxPubAccount_test",
				wlsUser.getId());
		wxAccountDao.save(wlsWxAccount);

		WlsMaterialText entity = new WlsMaterialText(id, content,
				wlsWxAccount.getId());
		materialDao.save(entity);
	}

	@Test
	@Order(value = 20)
	public void testGet() {
		WlsMaterialText text = materialDao.get(WlsMaterialText.class, id);
		Assert.assertEquals(content, text.getContent());
	}

	@Test
	@Order(value = 30)
	public void testUpdate() {
		String newContent = "new content";
		WlsMaterialText text = materialDao.get(WlsMaterialText.class, id);
		text.setContent(newContent);
		materialDao.update(text);
		WlsMaterialText text2 = materialDao.get(WlsMaterialText.class, id);
		Assert.assertEquals(newContent, text2.getContent());
	}

	@Test
	@Order(value = 40)
	public void testDelete() {
		WlsMaterialText text = materialDao.get(WlsMaterialText.class, id);
		materialDao.delete(text);
		text = materialDao.get(WlsMaterialText.class, id);
		Assert.assertNull(text);

		// user should still exists.
		WlsUser wlsUser = userDao.get(id);
		Assert.assertNotNull(wlsUser);
	}

}
