/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.model.WlsMaterialVideo;

/**
 * @author JiTing
 */
public interface MaterialVideoDao extends CommonDao<WlsMaterialVideo> {

	WlsMaterialVideo get(String id);
}
