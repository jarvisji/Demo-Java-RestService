/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.dao.UserDao;
import net.freecoder.restdemo.model.WlsUser;
import net.freecoder.restdemo.util.SecurityUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

/**
 * @author JiTing
 */
@RunWith(OrderedSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context-application.xml" })
public class UserDaoTest {

	private static String id = UUIDUtil.uuid8();
	private String displayName = "UserDaoTest" + id;
	private String email = displayName + "@wxservice.com";
	private String mobilePhone = "12345678910";
	private String password = "pass";

	@Autowired
	UserDao userDao;

	@Test
	@Order(value = 10)
	public void testCreate() {
		WlsUser entity = new WlsUser(id, SecurityUtil.sha1Encode(password),
				email);
		entity.setUsername(displayName);
		userDao.save(entity);
	}

	@Test
	@Order(value = 20)
	public void testGet() {
		WlsUser dbUser = userDao.get(id);
		Assert.notNull(dbUser);
		Assert.isTrue(displayName.equals(dbUser.getUsername()));
		Assert.notNull(dbUser.getAppCreateTime());
		Assert.notNull(dbUser.getAppLastModifyTime());
		Assert.isTrue(dbUser.getAppCreateTime().equals(
				dbUser.getAppLastModifyTime()));
	}

	@Test
	@Order(value = 24)
	public void testPasswordShouldBeEncrypted() {
		WlsUser wlsUser = userDao.get(id);
		String sha1Encode = SecurityUtil.sha1Encode(password);
		Assert.isTrue(sha1Encode.equals(wlsUser.getPassword()));
	}

	@Test
	@Order(value = 30)
	public void testUpdate() {
		WlsUser dbUser = userDao.get(id);
		dbUser.setMobilePhone(mobilePhone);
		userDao.update(dbUser);
		Assert.isTrue(dbUser.getAppLastModifyTime().compareTo(
				dbUser.getAppCreateTime()) > 0);
	}

	@Test
	@Order(value = 40)
	public void testDelete() {
		WlsUser dbUser = userDao.get(id);
		userDao.delete(dbUser);
		dbUser = userDao.get(id);
		Assert.isNull(dbUser);
	}
}
