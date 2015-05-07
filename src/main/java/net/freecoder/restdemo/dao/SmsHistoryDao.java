/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.Date;
import java.util.List;

import net.freecoder.restdemo.model.WlsSmsHistory;

/**
 * Interfaces for SMS data.
 * 
 * @author JiTing
 */
public interface SmsHistoryDao extends CommonDao<WlsSmsHistory> {

	/**
	 * Get sms history by uuid.
	 * 
	 * @param id
	 * @return
	 */
	WlsSmsHistory get(String id);

	/**
	 * Get sms history list by weixin account id.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	List<WlsSmsHistory> getByWxAccountId(String wxAccountId);

	/**
	 * Get SMS history list in time piriod by special weixin account id.
	 * 
	 * @param wxAccountId
	 * @param start
	 * @param end
	 * @return
	 */
	List<WlsSmsHistory> getByWxAccountId(String wxAccountId, Date start,
			Date end);

	/**
	 * Get SMS history list by target phone number.
	 * 
	 * @param phoneNumber
	 * @return
	 */
	List<WlsSmsHistory> getByPhoneNumber(String phoneNumber);

	/**
	 * Get SMS history list in time range by phone number.
	 * 
	 * @param phoneNumber
	 * @param start
	 * @param end
	 * @return
	 */
	List<WlsSmsHistory> getByPhoneNumber(String phoneNumber, Date start,
			Date end);

	/**
	 * Get SMS history list by weixin account and phone number.
	 * 
	 * @param wxAccountId
	 * @param phoneNumber
	 * @return
	 */
	List<WlsSmsHistory> getByAccountPhone(String wxAccountId, String phoneNumber);

	/**
	 * Get SMS history list in time range by weixin account and phone number.
	 * 
	 * @param wxAccountId
	 * @param phoneNumber
	 * @param start
	 * @param end
	 * @return
	 */
	List<WlsSmsHistory> getByAccountPhone(String wxAccountId,
			String phoneNumber, Date start, Date end);
}
