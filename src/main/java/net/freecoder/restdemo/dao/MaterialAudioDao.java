/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.model.WlsMaterialAudio;

/**
 * @author JiTing
 */
public interface MaterialAudioDao extends CommonDao<WlsMaterialAudio> {

	WlsMaterialAudio get(String id);
}
