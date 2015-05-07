/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import net.freecoder.restdemo.dao.WxReceivedMessageDao;
import net.freecoder.restdemo.model.WlsWxReceivedMessage;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class WxReceivedMessageDaoImpl extends
		CommonDaoImpl<WlsWxReceivedMessage> implements WxReceivedMessageDao {

}
