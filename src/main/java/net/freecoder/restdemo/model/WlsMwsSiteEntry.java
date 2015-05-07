package net.freecoder.restdemo.model;

// Generated 2014-5-27 11:30:19 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WlsMwsSiteEntry generated by hbm2java
 */
public class WlsMwsSiteEntry implements java.io.Serializable {

	private String wlsWxAccountId;
	private boolean enable;
	private String keyword;
	private String title;
	private String summary;
	private String coverUrl;
	private String externalUrl;
	private Date appCreateTime;
	private Date appLastModifyTime;

	public WlsMwsSiteEntry() {
	}

	public WlsMwsSiteEntry(String wlsWxAccountId, boolean enable,
			String keyword, String title, String coverUrl) {
		this.wlsWxAccountId = wlsWxAccountId;
		this.enable = enable;
		this.keyword = keyword;
		this.title = title;
		this.coverUrl = coverUrl;
	}

	public WlsMwsSiteEntry(String wlsWxAccountId, boolean enable,
			String keyword, String title, String summary, String coverUrl,
			String externalUrl, Date appCreateTime, Date appLastModifyTime) {
		this.wlsWxAccountId = wlsWxAccountId;
		this.enable = enable;
		this.keyword = keyword;
		this.title = title;
		this.summary = summary;
		this.coverUrl = coverUrl;
		this.externalUrl = externalUrl;
		this.appCreateTime = appCreateTime;
		this.appLastModifyTime = appLastModifyTime;
	}

	public String getWlsWxAccountId() {
		return this.wlsWxAccountId;
	}

	public void setWlsWxAccountId(String wlsWxAccountId) {
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public boolean isEnable() {
		return this.enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getCoverUrl() {
		return this.coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getExternalUrl() {
		return this.externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
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

}