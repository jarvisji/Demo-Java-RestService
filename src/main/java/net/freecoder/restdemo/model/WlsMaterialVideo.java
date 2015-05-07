package net.freecoder.restdemo.model;

// Generated 2014-3-21 14:20:36 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WlsMaterialVideo generated by hbm2java
 */
public class WlsMaterialVideo implements java.io.Serializable {

	private String id;
	private String fileName;
	private String url;
	private String extenalUrl;
	private String title;
	private String description;
	private Date appCreateTime;
	private Date appLastModifyTime;
	private String wlsWxAccountId;

	public WlsMaterialVideo() {
	}

	public WlsMaterialVideo(String id, String fileName, String url,
			String wlsWxAccountId) {
		this.id = id;
		this.fileName = fileName;
		this.url = url;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public WlsMaterialVideo(String id, String fileName, String url,
			String extenalUrl, String title, String description,
			Date appCreateTime, Date appLastModifyTime, String wlsWxAccountId) {
		this.id = id;
		this.fileName = fileName;
		this.url = url;
		this.extenalUrl = extenalUrl;
		this.title = title;
		this.description = description;
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

	public String getExtenalUrl() {
		return this.extenalUrl;
	}

	public void setExtenalUrl(String extenalUrl) {
		this.extenalUrl = extenalUrl;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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
