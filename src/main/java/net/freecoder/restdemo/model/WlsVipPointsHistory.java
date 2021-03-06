package net.freecoder.restdemo.model;

// Generated 2014-7-1 18:21:07 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WlsVipPointsHistory generated by hbm2java
 */
public class WlsVipPointsHistory implements java.io.Serializable {

	private WlsVipPointsHistoryId id;
	private short pointsAdjust;
	private String source;
	private String sourceId;
	private Date appCreateTime;
	private Date appLastModifyTime;

	public WlsVipPointsHistory() {
	}

	public WlsVipPointsHistory(WlsVipPointsHistoryId id, short pointsAdjust,
			String source, String sourceId, Date appCreateTime) {
		this.id = id;
		this.pointsAdjust = pointsAdjust;
		this.source = source;
		this.sourceId = sourceId;
		this.appCreateTime = appCreateTime;
	}

	public WlsVipPointsHistory(WlsVipPointsHistoryId id, short pointsAdjust,
			String source, String sourceId, Date appCreateTime,
			Date appLastModifyTime) {
		this.id = id;
		this.pointsAdjust = pointsAdjust;
		this.source = source;
		this.sourceId = sourceId;
		this.appCreateTime = appCreateTime;
		this.appLastModifyTime = appLastModifyTime;
	}

	public WlsVipPointsHistoryId getId() {
		return this.id;
	}

	public void setId(WlsVipPointsHistoryId id) {
		this.id = id;
	}

	public short getPointsAdjust() {
		return this.pointsAdjust;
	}

	public void setPointsAdjust(short pointsAdjust) {
		this.pointsAdjust = pointsAdjust;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
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
