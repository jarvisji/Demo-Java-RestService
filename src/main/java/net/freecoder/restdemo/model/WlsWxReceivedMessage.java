package net.freecoder.restdemo.model;

// Generated 2014-3-21 14:20:36 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WlsWxReceivedMessage generated by hbm2java
 */
public class WlsWxReceivedMessage implements java.io.Serializable {

	private String id;
	private String toUsername;
	private String fromUsername;
	private Date msgCreateTime;
	private String msgType;
	private String content;
	private String msgId;
	private String picUrl;
	private String mediaId;
	private String format;
	private String thumbMediaId;
	private String locationX;
	private String locationY;
	private String title;
	private String description;
	private String url;
	private Date appCreateTime;
	private String wlsWxAccountId;

	public WlsWxReceivedMessage() {
	}

	public WlsWxReceivedMessage(String id, String wlsWxAccountId) {
		this.id = id;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public WlsWxReceivedMessage(String id, String toUsername,
			String fromUsername, Date msgCreateTime, String msgType,
			String content, String msgId, String picUrl, String mediaId,
			String format, String thumbMediaId, String locationX,
			String locationY, String title, String description, String url,
			Date appCreateTime, String wlsWxAccountId) {
		this.id = id;
		this.toUsername = toUsername;
		this.fromUsername = fromUsername;
		this.msgCreateTime = msgCreateTime;
		this.msgType = msgType;
		this.content = content;
		this.msgId = msgId;
		this.picUrl = picUrl;
		this.mediaId = mediaId;
		this.format = format;
		this.thumbMediaId = thumbMediaId;
		this.locationX = locationX;
		this.locationY = locationY;
		this.title = title;
		this.description = description;
		this.url = url;
		this.appCreateTime = appCreateTime;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToUsername() {
		return this.toUsername;
	}

	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}

	public String getFromUsername() {
		return this.fromUsername;
	}

	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}

	public Date getMsgCreateTime() {
		return this.msgCreateTime;
	}

	public void setMsgCreateTime(Date msgCreateTime) {
		this.msgCreateTime = msgCreateTime;
	}

	public String getMsgType() {
		return this.msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getPicUrl() {
		return this.picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getThumbMediaId() {
		return this.thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getLocationX() {
		return this.locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return this.locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getAppCreateTime() {
		return this.appCreateTime;
	}

	public void setAppCreateTime(Date appCreateTime) {
		this.appCreateTime = appCreateTime;
	}

	public String getWlsWxAccountId() {
		return this.wlsWxAccountId;
	}

	public void setWlsWxAccountId(String wlsWxAccountId) {
		this.wlsWxAccountId = wlsWxAccountId;
	}

}
