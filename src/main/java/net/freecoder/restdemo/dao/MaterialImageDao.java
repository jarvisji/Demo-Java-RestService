/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.model.WlsMaterialImage;

/**
 * @author JiTing
 */
public interface MaterialImageDao extends CommonDao<WlsMaterialImage> {

	WlsMaterialImage get(String id);
}
