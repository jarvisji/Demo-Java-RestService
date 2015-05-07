package net.freecoder.restdemo.model;

// Generated 2014-8-7 11:13:38 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * WlsShopOrder generated by hbm2java
 */
public class WlsShopOrder implements java.io.Serializable {

	private String id;
	private String no;
	private int itemTotalCount;
	private BigDecimal itemTotalPrice;
	private String memo;
	private String vipId;
	private String shippingAddress;
	private String shippingName;
	private String shippingPhone;
	private BigDecimal shippingFee;
	private Integer gainPoints;
	private String status;
	private String statusDesc;
	private String payment;
	private String wlsWxAccountId;
	private Date appCreateTime;
	private Date appLastModifyTime;
	private Set<WlsShopOrderItem> wlsShopOrderItems = new HashSet<WlsShopOrderItem>(
			0);

	public WlsShopOrder() {
	}

	public WlsShopOrder(String id, String no, int itemTotalCount,
			BigDecimal itemTotalPrice, String vipId, String shippingAddress,
			String shippingName, String shippingPhone, String status,
			String payment, String wlsWxAccountId, Date appCreateTime) {
		this.id = id;
		this.no = no;
		this.itemTotalCount = itemTotalCount;
		this.itemTotalPrice = itemTotalPrice;
		this.vipId = vipId;
		this.shippingAddress = shippingAddress;
		this.shippingName = shippingName;
		this.shippingPhone = shippingPhone;
		this.status = status;
		this.payment = payment;
		this.wlsWxAccountId = wlsWxAccountId;
		this.appCreateTime = appCreateTime;
	}

	public WlsShopOrder(String id, String no, int itemTotalCount,
			BigDecimal itemTotalPrice, String memo, String vipId,
			String shippingAddress, String shippingName, String shippingPhone,
			BigDecimal shippingFee, String status, String payment,
			String wlsWxAccountId, Date appCreateTime, Date appLastModifyTime,
			Set wlsShopOrderItems) {
		this.id = id;
		this.no = no;
		this.itemTotalCount = itemTotalCount;
		this.itemTotalPrice = itemTotalPrice;
		this.memo = memo;
		this.vipId = vipId;
		this.shippingAddress = shippingAddress;
		this.shippingName = shippingName;
		this.shippingPhone = shippingPhone;
		this.shippingFee = shippingFee;
		this.status = status;
		this.payment = payment;
		this.wlsWxAccountId = wlsWxAccountId;
		this.appCreateTime = appCreateTime;
		this.appLastModifyTime = appLastModifyTime;
		this.wlsShopOrderItems = wlsShopOrderItems;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNo() {
		return this.no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getItemTotalCount() {
		return this.itemTotalCount;
	}

	public void setItemTotalCount(int itemTotalCount) {
		this.itemTotalCount = itemTotalCount;
	}

	public BigDecimal getItemTotalPrice() {
		return this.itemTotalPrice;
	}

	public void setItemTotalPrice(BigDecimal itemTotalPrice) {
		this.itemTotalPrice = itemTotalPrice;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getVipId() {
		return this.vipId;
	}

	public void setVipId(String vipId) {
		this.vipId = vipId;
	}

	public String getShippingAddress() {
		return this.shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingName() {
		return this.shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShippingPhone() {
		return this.shippingPhone;
	}

	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}

	public BigDecimal getShippingFee() {
		return this.shippingFee;
	}

	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	public Integer getGainPoints() {
		return gainPoints;
	}

	public void setGainPoints(Integer gainPoints) {
		this.gainPoints = gainPoints;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getPayment() {
		return this.payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
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

	public Set<WlsShopOrderItem> getWlsShopOrderItems() {
		return this.wlsShopOrderItems;
	}

	public void setWlsShopOrderItems(Set<WlsShopOrderItem> wlsShopOrderItems) {
		this.wlsShopOrderItems = wlsShopOrderItems;
	}

}