/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import net.freecoder.restdemo.dao.WxMenuDao;
import net.freecoder.restdemo.model.WlsWxMenu;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class WxMenuDaoImpl extends CommonDaoImpl<WlsWxMenu> implements
		WxMenuDao {

}
