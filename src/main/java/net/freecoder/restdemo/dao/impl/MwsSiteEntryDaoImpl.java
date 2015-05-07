/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import net.freecoder.restdemo.dao.MwsSiteEntryDao;
import net.freecoder.restdemo.model.WlsMwsSiteEntry;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class MwsSiteEntryDaoImpl extends CommonDaoImpl<WlsMwsSiteEntry>
		implements MwsSiteEntryDao {

	@Override
	public WlsMwsSiteEntry get(String wxAccountId) {
		return this.get(WlsMwsSiteEntry.class, wxAccountId);
	}

}
