/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.model.ReferenceType;
import net.freecoder.restdemo.model.WlsReplyKeyword;

/**
 * @author JiTing
 */
public interface ReplyKeywordDao extends CommonDao<WlsReplyKeyword> {
	/**
	 * Get keyword reply list of the special weixin account and ref type.
	 * 
	 * @param wxAccountId
	 * @param refType
	 *            KeywordRefType enum.
	 * @return List
	 */
	List<WlsReplyKeyword> getListByWxAccountIdAndRefType(String wxAccountId,
			ReferenceType refType);

	/**
	 * Save a collection of WlsReplyKeyword batch. <br/>
	 * <b>This will earse all of original entitis with the same ref_type.</b>
	 * 
	 * @param wxAccountId
	 * @param entities
	 * @return
	 */
	String[] saveAll(String wxAccountId, WlsReplyKeyword[] entities);

	/**
	 * Get keyword reply by special weixin account and keyword.
	 * 
	 * @param wxAccountId
	 * @param keyword
	 * @return List of WlsReplyKeyword. The expect size is 0 or 1.
	 */
	List<WlsReplyKeyword> getByKeyword(String wxAccountId, String keyword);

	/**
	 * Delete keyword replies by group name.
	 * 
	 * @param wxAccountId
	 * @param groupName
	 */
	void deleteByGroup(String wxAccountId, String groupName);
}
