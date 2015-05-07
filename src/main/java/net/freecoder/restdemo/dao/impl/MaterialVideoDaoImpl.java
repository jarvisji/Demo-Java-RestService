/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import net.freecoder.restdemo.dao.MaterialVideoDao;
import net.freecoder.restdemo.model.WlsMaterialVideo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class MaterialVideoDaoImpl extends CommonDaoImpl<WlsMaterialVideo>
		implements MaterialVideoDao {

	@Override
	public WlsMaterialVideo get(String id) {
		return super.get(WlsMaterialVideo.class, id);
	}

}
