/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.controller.WxAccountExtProp;
import net.freecoder.restdemo.model.WlsWxAccount;
import net.freecoder.restdemo.model.WlsWxAccountExt;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * @author JiTing
 */
public interface WxAccountDao extends CommonDao<WlsWxAccount> {

	/**
	 * Get WxAccount by id.
	 * 
	 * @param id
	 * @return
	 */
	WlsWxAccount get(String id);

	/**
	 * Get WxAccount by user id.
	 * 
	 * @param userId
	 * @return
	 */
	List<WlsWxAccount> getByUserId(String userId);

	/**
	 * Delete WxAccount by id.
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * Get WxAccount list by special creiterion.
	 * 
	 * @param criterion
	 * @return
	 */
	List<WlsWxAccount> find(Criterion criterion);

	/**
	 * Get WxAccount list by special creiterion and order.
	 * 
	 * @param criterion
	 * @param order
	 * @return
	 */
	List<WlsWxAccount> find(Criterion criterion, Order order);

	/**
	 * Get WxAccountExt entity list by special account id.
	 * 
	 * @param accountId
	 * @return
	 */
	List<WlsWxAccountExt> getAccountExt(String accountId);

	/**
	 * Get special WxAccountExt entity by account id and propName.
	 * 
	 * @param accountId
	 * @param propName
	 * @return
	 */
	WlsWxAccountExt getAccountExtProp(String accountId,
			WxAccountExtProp propName);

	/**
	 * Create entity of WxAccountExt.
	 * 
	 * @param entity
	 * @return
	 */
	String createWxAccountExt(WlsWxAccountExt entity);

	/**
	 * Reset token for Weixin server request entry, delete old token and create
	 * new one.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	String resetWxAccountExt_EntryToken(String wxAccountId);
}
