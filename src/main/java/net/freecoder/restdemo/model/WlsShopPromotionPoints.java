package net.freecoder.restdemo.model;

// Generated 2014-7-2 12:07:44 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

/**
 * WlsShopPromotionPoints generated by hbm2java
 */
public class WlsShopPromotionPoints implements java.io.Serializable {

	private String id;
	private String itemId;
	private String itemName;
	private String itemBrief;
	private Integer availableCount;
	private int requirePoints;
	private BigDecimal requireOrderAmount;
	private Integer limitCountPerOrder;
	private boolean isExclusive;
	private boolean isEnable;
	private String wlsWxAccountId;
	private Date appCreateTime;
	private Date appLastModifyTime;

	public WlsShopPromotionPoints() {
	}

	public WlsShopPromotionPoints(String id, int requirePoints,
			String wlsWxAccountId, Date appCreateTime) {
		this.id = id;
		this.requirePoints = requirePoints;
		this.wlsWxAccountId = wlsWxAccountId;
		this.appCreateTime = appCreateTime;
	}

	public WlsShopPromotionPoints(String id, String itemId, String itemName,
			String itemBrief, Integer availableCount, int requirePoints,
			BigDecimal requireOrderAmount, Integer limitCountPerOrder,
			boolean isExclusive, boolean isEnable, String wlsWxAccountId,
			Date appCreateTime, Date appLastModifyTime) {
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemBrief = itemBrief;
		this.availableCount = availableCount;
		this.requirePoints = requirePoints;
		this.requireOrderAmount = requireOrderAmount;
		this.limitCountPerOrder = limitCountPerOrder;
		this.isExclusive = isExclusive;
		this.isEnable = isEnable;
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

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemBrief() {
		return this.itemBrief;
	}

	public void setItemBrief(String itemBrief) {
		this.itemBrief = itemBrief;
	}

	public Integer getAvailableCount() {
		return this.availableCount;
	}

	public void setAvailableCount(Integer availableCount) {
		this.availableCount = availableCount;
	}

	public int getRequirePoints() {
		return this.requirePoints;
	}

	public void setRequirePoints(int requirePoints) {
		this.requirePoints = requirePoints;
	}

	public BigDecimal getRequireOrderAmount() {
		return this.requireOrderAmount;
	}

	public void setRequireOrderAmount(BigDecimal requireOrderAmount) {
		this.requireOrderAmount = requireOrderAmount;
	}

	public Integer getLimitCountPerOrder() {
		return this.limitCountPerOrder;
	}

	public void setLimitCountPerOrder(Integer limitCountPerOrder) {
		this.limitCountPerOrder = limitCountPerOrder;
	}

	public boolean isIsExclusive() {
		return this.isExclusive;
	}

	public void setIsExclusive(boolean isExclusive) {
		this.isExclusive = isExclusive;
	}

	/**
	 * @return the isEnable
	 */
	public boolean isIsEnable() {
		return isEnable;
	}

	/**
	 * @param isEnable
	 *            the isEnable to set
	 */
	public void setIsEnable(boolean isEnable) {
		this.isEnable = isEnable;
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
