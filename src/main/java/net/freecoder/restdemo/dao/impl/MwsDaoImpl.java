/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.List;

import net.freecoder.restdemo.dao.MwsDao;
import net.freecoder.restdemo.model.WlsMwsSiteConfig;
import net.freecoder.restdemo.model.WlsMwsSiteConfigId;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class MwsDaoImpl extends CommonDaoImpl<WlsMwsSiteConfig> implements
		MwsDao {

	@Override
	public WlsMwsSiteConfig get(WlsMwsSiteConfigId id) {
		return (WlsMwsSiteConfig) currentSession().get(WlsMwsSiteConfig.class,
				id);
	}

	@Override
	public List<WlsMwsSiteConfig> getListByWxAccountId(String wxAccountId) {
		SimpleExpression exp = Restrictions
				.eq("id.wlsWxAccountId", wxAccountId);
		return find(WlsMwsSiteConfig.class, exp);
	}

	@Override
	public WlsMwsSiteConfigId saveConfig(WlsMwsSiteConfig siteConfig) {
		return (WlsMwsSiteConfigId) currentSession().save(siteConfig);
	}

	@Override
	public void saveBatch(List<WlsMwsSiteConfig> siteConfigs) {
		for (WlsMwsSiteConfig config : siteConfigs) {
			createTime(config);
			currentSession().saveOrUpdate(config);
		}
	}

	@Override
	public void deleteBatch(List<WlsMwsSiteConfig> siteConfigs) {
		for (WlsMwsSiteConfig config : siteConfigs) {
			delete(config);
		}
	}

	@Override
	public void deleteAllAndSave(String wxAccountId,
			List<WlsMwsSiteConfig> siteConfigs) {
		List<WlsMwsSiteConfig> existCongifs = getListByWxAccountId(wxAccountId);
		for (WlsMwsSiteConfig config : existCongifs) {
			delete(config);
		}

		for (WlsMwsSiteConfig config : siteConfigs) {
			createTime(config);
			saveConfig(config);
		}
	}
}
