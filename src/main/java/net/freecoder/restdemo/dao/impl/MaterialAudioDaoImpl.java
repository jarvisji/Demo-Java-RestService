/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import net.freecoder.restdemo.dao.MaterialAudioDao;
import net.freecoder.restdemo.model.WlsMaterialAudio;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class MaterialAudioDaoImpl extends CommonDaoImpl<WlsMaterialAudio>
		implements MaterialAudioDao {

	@Override
	public WlsMaterialAudio get(String id) {
		return super.get(WlsMaterialAudio.class, id);
	}

}
