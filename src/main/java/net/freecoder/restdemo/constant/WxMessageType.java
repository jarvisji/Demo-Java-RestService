/**
 * 
 */
package net.freecoder.restdemo.constant;

import net.freecoder.restdemo.model.MaterialType;

/**
 * @author JiTing
 */
public enum WxMessageType {
	EVENT("event"), TEXT("text"), IMAGE("image"), VOICE("voice"), VIDEO("video"), LOCATION(
			"location"), LINK("link"), MUSIC("music"), NEWS("news"), EVENT_UNSUBSCRIBE(
			"event_unsubscribe"), EVENT_CLICK("event_click"), EVENT_SUBSCRIBE(
			"event_subscribe");

	private final String value;

	private WxMessageType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	/**
	 * Create WxMessageType instant from string.
	 * 
	 * @param value
	 * @return
	 */
	static public WxMessageType parseValue(String value) {
		return WxMessageType.valueOf(value.toUpperCase());
	}

	/**
	 * Convert MaterialType to WxMssageType.
	 * 
	 * @param materialType
	 * @return
	 */
	static public WxMessageType parseValue(MaterialType materialType) {
		WxMessageType ret;
		if (MaterialType.AUDIO.equals(materialType)) {
			ret = VOICE;
		} else if (MaterialType.SIT.equals(materialType)
				|| MaterialType.MIT.equals(materialType)) {
			ret = NEWS;
		} else {
			ret = WxMessageType.parseValue(materialType.toString());
		}
		return ret;
	}
}
