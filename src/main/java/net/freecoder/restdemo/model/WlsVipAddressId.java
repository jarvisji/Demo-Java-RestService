package net.freecoder.restdemo.model;

// Generated 2014-7-1 18:21:07 by Hibernate Tools 3.4.0.CR1

/**
 * WlsVipAddressId generated by hbm2java
 */
public class WlsVipAddressId implements java.io.Serializable {

	private String id;
	private String wlsVipId;

	public WlsVipAddressId() {
	}

	public WlsVipAddressId(String id, String wlsVipId) {
		this.id = id;
		this.wlsVipId = wlsVipId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWlsVipId() {
		return this.wlsVipId;
	}

	public void setWlsVipId(String wlsVipId) {
		this.wlsVipId = wlsVipId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof WlsVipAddressId))
			return false;
		WlsVipAddressId castOther = (WlsVipAddressId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getWlsVipId() == castOther.getWlsVipId()) || (this
						.getWlsVipId() != null
						&& castOther.getWlsVipId() != null && this
						.getWlsVipId().equals(castOther.getWlsVipId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getWlsVipId() == null ? 0 : this.getWlsVipId().hashCode());
		return result;
	}

}
