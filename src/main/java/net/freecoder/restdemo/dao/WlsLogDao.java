/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.Date;
import java.util.List;

import net.freecoder.restdemo.model.WlsLog;

/**
 * @author JiTing
 */
public interface WlsLogDao extends CommonDao<WlsLog> {

	List<Object> getList();

	List<Object> getList(Date start, Date end);

	List<Object> getList(long start, int pagesize);

	int totalCount();

}
