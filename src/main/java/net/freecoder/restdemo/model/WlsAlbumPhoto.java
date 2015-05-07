package net.freecoder.restdemo.model;

// Generated 2014-11-6 10:17:22 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * WlsAlbumPhoto generated by hbm2java
 */
public class WlsAlbumPhoto implements java.io.Serializable {

	private String id;
	@JsonIgnore
	private WlsAlbum wlsAlbum;
	private String filename;
	private String type;
	private Short size;
	private Boolean isPin;
	private String description;
	private String wlsWxAccountId;
	private Date appCreateTime;
	private Date appLastModifyTime;

	public WlsAlbumPhoto() {
	}

	public WlsAlbumPhoto(String id, String filename, String wlsWxAccountId) {
		this.id = id;
		this.filename = filename;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public WlsAlbumPhoto(String id, WlsAlbum wlsAlbum, String filename,
			String type, Short size, Boolean isPin, String description,
			String wlsWxAccountId, Date appCreateTime, Date appLastModifyTime) {
		this.id = id;
		this.wlsAlbum = wlsAlbum;
		this.filename = filename;
		this.type = type;
		this.size = size;
		this.isPin = isPin;
		this.description = description;
		this.wlsWxAccountId = wlsWxAccountId;
		this.appCreateTime = appCreateTime;
		this.appLastModifyTime = appLastModifyTime;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public WlsAlbum getWlsAlbum() {
		return this.wlsAlbum;
	}

	public void setWlsAlbum(WlsAlbum wlsAlbum) {
		this.wlsAlbum = wlsAlbum;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Short getSize() {
		return this.size;
	}

	public void setSize(Short size) {
		this.size = size;
	}

	public Boolean getIsPin() {
		return this.isPin == null ? Boolean.FALSE : this.isPin;
	}

	public void setIsPin(Boolean isPin) {
		this.isPin = isPin;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWlsWxAccountId() {
		return this.wlsWxAccountId;
	}

	public void setWlsWxAccountId(String wlsWxAccountId) {
		this.wlsWxAccountId = wlsWxAccountId;
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
