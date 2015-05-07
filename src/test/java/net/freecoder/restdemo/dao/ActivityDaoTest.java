/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.common.OrderedSpringJUnit4ClassRunner;
import net.freecoder.restdemo.constant.ActivityType;
import net.freecoder.restdemo.constant.ModuleType;
import net.freecoder.restdemo.dao.ActivityDao;
import net.freecoder.restdemo.model.WlsActivity;
import net.freecoder.restdemo.util.DateTimeUtil;
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
public class ActivityDaoTest {

	@Autowired
	ActivityDao activityDao;

	@Test
	public void testActivity() {
		String initiator = "13955100547";
		WlsActivity wlsActivity = new WlsActivity(UUIDUtil.uuid8(),
				ModuleType.VIP.toString(), initiator,
				ActivityType.VIP_REG_VERIFICATIONCODE.toString(),
				DateTimeUtil.getGMTDate());
		activityDao.save(wlsActivity);

		WlsActivity lastActivity = activityDao.getLastActivity(initiator,
				ActivityType.VIP_REG_VERIFICATIONCODE);
		Assert.assertEquals(lastActivity.getId(), wlsActivity.getId());
	}

}
