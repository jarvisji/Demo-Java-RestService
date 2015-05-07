/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import net.freecoder.restdemo.dao.WxRepliedMessageDao;
import net.freecoder.restdemo.model.WlsWxRepliedMessage;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class WxRepliedMessageDaoImpl extends CommonDaoImpl<WlsWxRepliedMessage>
		implements WxRepliedMessageDao {

}
