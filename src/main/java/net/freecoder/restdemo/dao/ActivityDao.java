/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.constant.ActivityType;
import net.freecoder.restdemo.model.WlsActivity;

/**
 * @author JiTing
 */
public interface ActivityDao extends CommonDao<WlsActivity> {

	/**
	 * Get last activity by initiator and type.
	 * 
	 * @param initiator
	 * @param type
	 *            ActivityType
	 * @return
	 */
	WlsActivity getLastActivity(String initiator, ActivityType type);
}
