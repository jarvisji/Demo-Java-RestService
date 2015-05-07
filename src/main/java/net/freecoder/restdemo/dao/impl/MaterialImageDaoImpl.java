/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import net.freecoder.restdemo.dao.MaterialImageDao;
import net.freecoder.restdemo.model.WlsMaterialImage;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class MaterialImageDaoImpl extends CommonDaoImpl<WlsMaterialImage>
		implements MaterialImageDao {

	@Override
	public WlsMaterialImage get(String id) {
		return super.get(WlsMaterialImage.class, id);
	}

}
