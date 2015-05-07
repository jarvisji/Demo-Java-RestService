/**
 * 
 */
package net.freecoder.restdemo.dao;

import net.freecoder.restdemo.model.WlsMaterialText;

/**
 * @author JiTing
 */
public interface MaterialTextDao extends CommonDao<WlsMaterialText> {

	WlsMaterialText get(String id);
}
