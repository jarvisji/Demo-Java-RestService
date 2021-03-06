package net.freecoder.restdemo.model;

// Generated 2014-3-21 16:16:45 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WlsMaterialAudio generated by hbm2java
 */
public class WlsMaterialAudio implements java.io.Serializable {

	private String id;
	private String fileName;
	private String url;
	private Integer size;
	private Short length;
	private String memo;
	private Date appCreateTime;
	private Date appLastModifyTime;
	private String wlsWxAccountId;

	public WlsMaterialAudio() {
	}

	public WlsMaterialAudio(String id, String fileName, String url,
			String wlsWxAccountId) {
		this.id = id;
		this.fileName = fileName;
		this.url = url;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public WlsMaterialAudio(String id, String fileName, String url,
			Integer size, Short length, String memo, Date appCreateTime,
			Date appLastModifyTime, String wlsWxAccountId) {
		this.id = id;
		this.fileName = fileName;
		this.url = url;
		this.size = size;
		this.length = length;
		this.memo = memo;
		this.appCreateTime = appCreateTime;
		this.appLastModifyTime = appLastModifyTime;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSize() {
		return this.size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Short getLength() {
		return this.length;
	}

	public void setLength(Short length) {
		this.length = length;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getAppCreateTime() {
		return this.appCreateTime;
	}

	public void setAppCreateTime(Date appCreateTime) {
		this.appCreateTime = appCreateTime;
	}

	public Date getAppLastModifyTime() {
		return this.appLastModifyTime;
	}

	public void setAppLastModifyTime(Date appLastModifyTime) {
		this.appLastModifyTime = appLastModifyTime;
	}

	public String getWlsWxAccountId() {
		return this.wlsWxAccountId;
	}

	public void setWlsWxAccountId(String wlsWxAccountId) {
		this.wlsWxAccountId = wlsWxAccountId;
	}

}
