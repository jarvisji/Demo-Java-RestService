package net.freecoder.restdemo.model;

// Generated 2014-9-19 13:35:14 by Hibernate Tools 3.4.0.CR1

/**
 * WlsCityCode generated by hbm2java
 */
public class WlsCityCode implements java.io.Serializable {

	private String cityCode;
	private String cityName;
	private String pinYin;

	public WlsCityCode() {
	}

	public WlsCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public WlsCityCode(String cityCode, String cityName, String pinYin) {
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.pinYin = pinYin;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPinYin() {
		return this.pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

}
