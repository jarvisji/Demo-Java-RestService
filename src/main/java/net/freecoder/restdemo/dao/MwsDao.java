/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.model.WlsMwsSiteConfig;
import net.freecoder.restdemo.model.WlsMwsSiteConfigId;

/**
 * @author JiTing
 */
public interface MwsDao extends CommonDao<WlsMwsSiteConfig> {

	/**
	 * If use save() interface to persistence new site config item, do not use
	 * the return value, because id of WlsMwsSiteConfigId is composite value,
	 * and not implement correct toString() method.
	 * 
	 * @param siteConfig
	 * @return
	 */
	WlsMwsSiteConfigId saveConfig(WlsMwsSiteConfig siteConfig);

	/**
	 * Save site config items in batch.
	 * 
	 * @param siteConfigs
	 */
	void saveBatch(List<WlsMwsSiteConfig> siteConfigs);

	/**
	 * Overwrite (delete all and create new) all exists site configurations.
	 * 
	 * @param siteconfigs
	 */
	void deleteAllAndSave(String wxAccountId, List<WlsMwsSiteConfig> siteConfigs);

	/**
	 * Get site config item of special wxAccountId and propName.
	 * 
	 * @param id
	 * @return
	 */
	WlsMwsSiteConfig get(WlsMwsSiteConfigId id);

	/**
	 * Get all site config items of special wxAccountId.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	List<WlsMwsSiteConfig> getListByWxAccountId(String wxAccountId);

	/**
	 * Delete site config items in batch.
	 * 
	 * @param siteConfigs
	 */
	void deleteBatch(List<WlsMwsSiteConfig> siteConfigs);

}
