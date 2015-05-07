/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import net.freecoder.restdemo.dao.MaterialTextDao;
import net.freecoder.restdemo.model.WlsMaterialText;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class MaterialTextDaoImpl extends CommonDaoImpl<WlsMaterialText>
		implements MaterialTextDao {

	@Override
	public WlsMaterialText get(String id) {
		return super.get(WlsMaterialText.class, id);
	}
}
