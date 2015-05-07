package net.freecoder.restdemo.model;

// Generated 2014-7-16 15:10:23 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WlsSmsHistory generated by hbm2java
 */
public class WlsSmsHistory implements java.io.Serializable {

	private String id;
	private String phoneNumber;
	private String content;
	private String proxyResponse;
	private String wlsWxAccountId;
	private String proxyServer;
	private String proxyCall;
	private Integer smsCount;
	private Date appCreateTime;
	private Date appLastModifyTime;

	public WlsSmsHistory() {
	}

	public WlsSmsHistory(String id, String phoneNumber, String content,
			String wlsWxAccountId) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.content = content;
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public WlsSmsHistory(String id, String phoneNumber, String content,
			String proxyResponse, String wlsWxAccountId, String proxyServer,
			String proxyCall, Integer smsCount, Date appCreateTime,
			Date appLastModifyTime) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.content = content;
		this.proxyResponse = proxyResponse;
		this.wlsWxAccountId = wlsWxAccountId;
		this.proxyServer = proxyServer;
		this.proxyCall = proxyCall;
		this.smsCount = smsCount;
		this.appCreateTime = appCreateTime;
		this.appLastModifyTime = appLastModifyTime;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProxyResponse() {
		return this.proxyResponse;
	}

	public void setProxyResponse(String proxyResponse) {
		this.proxyResponse = proxyResponse;
	}

	public String getWlsWxAccountId() {
		return this.wlsWxAccountId;
	}

	public void setWlsWxAccountId(String wlsWxAccountId) {
		this.wlsWxAccountId = wlsWxAccountId;
	}

	public String getProxyServer() {
		return this.proxyServer;
	}

	public void setProxyServer(String proxyServer) {
		this.proxyServer = proxyServer;
	}

	public String getProxyCall() {
		return this.proxyCall;
	}

	public void setProxyCall(String proxyCall) {
		this.proxyCall = proxyCall;
	}

	public Integer getSmsCount() {
		return this.smsCount;
	}

	public void setSmsCount(Integer smsCount) {
		this.smsCount = smsCount;
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
