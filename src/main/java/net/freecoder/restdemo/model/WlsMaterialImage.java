package net.freecoder.restdemo.model;

// Generated 2014-3-21 14:20:36 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WlsMaterialImage generated by hbm2java
 */
public class WlsMaterialImage implements java.io.Serializable {

	private String id;
	private String fileName;
	private String imageUrl;
	private String thumbUrl;
	private Integer size;
	private Date appCreateTime;
	private Date appLastModifyTime;
	private String wlsWxAccountId;

	public WlsMaterialImage() {
	}

	public WlsMaterialImage(String id, String fileName, String imageUrl,
			String wlsWxAccountId) {
		this.id = id;
		this.fileName = fileName;
		this.imageUrl = imageUrl;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public WlsMaterialImage(String id, String fileName, String imageUrl,
			String thumbUrl, Integer size, Date appCreateTime,
			Date appLastModifyTime, String wlsWxAccountId) {
		this.id = id;
		this.fileName = fileName;
		this.imageUrl = imageUrl;
		this.thumbUrl = thumbUrl;
		this.size = size;
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

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbUrl() {
		return this.thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public Integer getSize() {
		return this.size;
	}

	public void setSize(Integer size) {
		this.size = size;
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
