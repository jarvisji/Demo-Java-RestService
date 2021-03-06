package net.freecoder.restdemo.model;

// Generated 2014-7-1 15:23:01 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WlsShopVip generated by hbm2java
 */
public class WlsShopVip implements java.io.Serializable {

	private WlsShopVipId id;
	private Date appCreateTime;
	private Date appLastModifyTime;

	public WlsShopVip() {
	}

	public WlsShopVip(WlsShopVipId id, Date appCreateTime) {
		this.id = id;
		this.appCreateTime = appCreateTime;
	}

	public WlsShopVip(WlsShopVipId id, Date appCreateTime,
			Date appLastModifyTime) {
		this.id = id;
		this.appCreateTime = appCreateTime;
		this.appLastModifyTime = appLastModifyTime;
	}

	public WlsShopVipId getId() {
		return this.id;
	}

	public void setId(WlsShopVipId id) {
		this.id = id;
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
