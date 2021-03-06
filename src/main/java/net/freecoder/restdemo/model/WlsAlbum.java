package net.freecoder.restdemo.model;

// Generated 2014-11-6 10:17:22 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * WlsAlbum generated by hbm2java
 */
public class WlsAlbum implements java.io.Serializable {

	private String id;
	private String albumCode;
	private String albumName;
	private String coverUrl;
	private String parentId;
	private Boolean isPin;
	private String description;
	private Boolean isPrivate;
	private String wlsWxAccountId;
	private Date appCreateTime;
	private Date appLastModifyTime;
	@JsonIgnore
	private Set wlsAlbumPhotos = new HashSet(0);

	public WlsAlbum() {
	}

	public WlsAlbum(String id, String wlsWxAccountId) {
		this.id = id;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public WlsAlbum(String id, String albumCode, String albumName,
			String parentId, Boolean isPin, String description,
			Boolean isPrivate, String wlsWxAccountId, Date appCreateTime,
			Date appLastModifyTime, Set wlsAlbumPhotos) {
		this.id = id;
		this.albumCode = albumCode;
		this.albumName = albumName;
		this.parentId = parentId;
		this.isPin = isPin;
		this.description = description;
		this.isPrivate = isPrivate;
		this.wlsWxAccountId = wlsWxAccountId;
		this.appCreateTime = appCreateTime;
		this.appLastModifyTime = appLastModifyTime;
		this.wlsAlbumPhotos = wlsAlbumPhotos;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlbumCode() {
		return this.albumCode;
	}

	public void setAlbumCode(String albumCode) {
		this.albumCode = albumCode;
	}

	public String getAlbumName() {
		return this.albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public Boolean getIsPrivate() {
		return this.isPrivate == null ? Boolean.FALSE : this.isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
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

	public Set getWlsAlbumPhotos() {
		return this.wlsAlbumPhotos;
	}

	public void setWlsAlbumPhotos(Set wlsAlbumPhotos) {
		this.wlsAlbumPhotos = wlsAlbumPhotos;
	}

}
