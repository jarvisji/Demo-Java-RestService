/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.Date;
import java.util.List;

import net.freecoder.restdemo.constant.WxMessageType;
import net.freecoder.restdemo.model.WlsWxMessage;

/**
 * @author JiTing
 */
public interface WxMessageDao extends CommonDao<WlsWxMessage> {

	/**
	 * Get weixin message of special message type.
	 * 
	 * @param msgType
	 * @param wxAccountId
	 * @return List of WlsWxMessage
	 */
	List<WlsWxMessage> getList(WxMessageType msgType, String wxAccountId);

	/**
	 * Get weixin message of special message type in the time range.
	 * 
	 * @param start
	 * @param end
	 * @param msgType
	 * @param wxAccountId
	 * @return List of WlsWxMessage
	 */
	List<WlsWxMessage> getList(Date start, Date end, WxMessageType msgType,
			String wxAccountId);

	/**
	 * Get total count of weixin message of special message type.
	 * 
	 * @param msgType
	 * @param wxAccountId
	 * @return
	 */
	Long getCount(WxMessageType msgType, String wxAccountId);

	/**
	 * Get total count of weixin message of special message type, distinct by
	 * property name.
	 * 
	 * @param msgType
	 * @param wxAccountId
	 * @param distinctPropName
	 * @return
	 */
	Long getCount(WxMessageType msgType, String wxAccountId,
			String distinctPropName);

	/**
	 * Get count of weixin message of special message type in the time range.
	 * 
	 * @param start
	 * @param end
	 * @param msgType
	 * @param wxAccountId
	 * @return Long, the count.
	 */
	Long getCount(Date start, Date end, WxMessageType msgType,
			String wxAccountId);

	/**
	 * Get count of weixin message of special message type in the time range,
	 * distinct by property name.
	 * 
	 * @param start
	 * @param end
	 * @param msgType
	 * @param wxAccountId
	 * @return Long, the count.
	 */
	Long getCount(Date start, Date end, WxMessageType msgType,
			String wxAccountId, String distinctPropName);

}
