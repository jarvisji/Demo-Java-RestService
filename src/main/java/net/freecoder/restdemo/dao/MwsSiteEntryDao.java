/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.model.WlsMwsSiteEntry;

/**
 * @author JiTing
 */
public interface MwsSiteEntryDao extends CommonDao<WlsMwsSiteEntry> {
	/**
	 * Get site entry information by accountId.
	 * 
	 * @param id
	 * @return
	 */
	WlsMwsSiteEntry get(String wxAccountId);
}
