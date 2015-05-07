/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.constant.ErrorCode;

/**
 * Result is common object for all of controllers. Provides uniform data format
 * for client.
 * 
 * @author JiTing
 */
public class Result {

	public static String COUNT = "count";
	public static String TOTALCOUNT = "totalCount";

	private boolean success = true;
	private String errMsg;
	private ErrorCode errCode;
	private Map<String, Object> data = new HashMap<String, Object>(1);

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public Result setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	/**
	 * @return the errCode
	 */
	public ErrorCode getErrCode() {
		return errCode;
	}

	/**
	 * @param errCode
	 *            the errCode to set
	 */
	public Result setErrCode(ErrorCode errCode) {
		this.success = false;
		this.errCode = errCode;
		return this;
	}

	/**
	 * @return the errMsg
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * @param errMsg
	 *            the errMsg to set
	 */
	public Result setErrMsg(String errMsg) {
		this.success = false;
		this.errMsg = errMsg;
		return this;
	}

	/**
	 * @return the data
	 */
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public Result setDataMap(Map<String, Object> data) {
		this.data = data;
		return this;
	}

	/**
	 * 
	 * @param key
	 *            the key of value.
	 * @param value
	 *            the value to set.
	 */
	public Result setData(String key, Object value) {
		this.data.put(key, value);
		// this.data.put("entityType", value.getClass().getSimpleName());
		return this;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getData(String key) {
		return this.data.get(key);
	}

	/**
	 * 
	 * @param value
	 */
	public Result setData(Object value) {
		this.data.put(value.getClass().getSimpleName(), value);
		return this;
	}

	/**
	 * Set count and total count for result.
	 * 
	 * @param count
	 * @param totalCount
	 */
	public Result setCount(long count, long totalCount) {
		this.data.put(Result.COUNT, count);
		this.data.put(Result.TOTALCOUNT, totalCount);
		return this;
	}

	/**
	 * Set count for result.
	 * 
	 * @param count
	 */
	public Result setDataCount(long count) {
		this.data.put(Result.COUNT, count);
		return this;
	}

	/**
	 * Set total count for result.
	 * 
	 * @param totalCount
	 */
	public Result setDataTotalCount(long totalCount) {
		this.data.put(Result.TOTALCOUNT, totalCount);
		return this;
	}

	/**
	 * Quick method to setData("entity", entity);
	 * 
	 * @param entity
	 * @return
	 */
	public Result setEntity(Object entity) {
		this.data.put("entity", entity);
		if (entity == null) {
			setDataCount(0);
		} else if (entity instanceof List) {
			setDataCount(((List) entity).size());
		} else if (entity instanceof Object[]) {
			setDataCount(((Object[]) entity).length);
		} else {
			setDataCount(1);
		}
		return this;
	}
}
