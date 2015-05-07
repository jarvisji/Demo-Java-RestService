package net.freecoder.restdemo.model;

// Generated 2014-3-21 14:20:36 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WlsMaterialText generated by hbm2java
 */
public class WlsMaterialText implements java.io.Serializable {

	private String id;
	private String content;
	private Date appCreateTime;
	private Date appLastModifyTime;
	private String wlsWxAccountId;

	public WlsMaterialText() {
	}

	public WlsMaterialText(String id, String content, String wlsWxAccountId) {
		this.id = id;
		this.content = content;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public WlsMaterialText(String id, String content, Date appCreateTime,
			Date appLastModifyTime, String wlsWxAccountId) {
		this.id = id;
		this.content = content;
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

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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
