/**
 * 
 */
package net.freecoder.restdemo.constant;

/**
 * Shop config is collection of many properties. To filter the propNames more
 * easier, we define prefix in this enum for propName.
 * 
 * @author JiTing
 */
public enum ShopConfigPropPrefix {
	BASE("base"), WXPUB("wxpub"), PAYMENT("payment"), SHIPPING("shipping"), POINTS(
			"points"), REMINDER("reminder");

	private String value;

	private ShopConfigPropPrefix(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	/**
	 * Try to parse enum instance from propName. If propName has "_", we will
	 * assume the partial before the first "_" is prefix, otherwise, we will try
	 * to parse the whole string.
	 * 
	 * @param str
	 * @return
	 */
	static public ShopConfigPropPrefix parseValue(String str) {
		String val = str.split("_")[0];
		return ShopConfigPropPrefix.valueOf(val.toUpperCase());
	}
}
