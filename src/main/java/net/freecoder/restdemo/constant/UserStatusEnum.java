/**
 * 
 */
package net.freecoder.restdemo.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Value those great than value of PENDING_STATUS_VERIFICATIONCODE, are all
 * indicate verification status, valid number scope is 10000-32767.
 * 
 * @author JiTing
 */
public enum UserStatusEnum {
	NORMAL_STATUS_ACTIVATED(1), PENDING_STATUS_INACTIVE(2), PENDING_STATUS_AUDIT(
			3), PENDING_STATUS_VERIFICATION(10000);

	// for parseValue use.
	private static final Map<Short, UserStatusEnum> valuesMap = new HashMap<Short, UserStatusEnum>();

	static {
		for (UserStatusEnum status : values()) {
			valuesMap.put(status.value(), status);
		}
	}

	private short value;

	UserStatusEnum(int value) {
		this.value = (short) value;
	}

	public short value() {
		return this.value;
	}

	static public UserStatusEnum parseValue(String value) {
		Short sValue = Short.valueOf(value);
		return parseValue(sValue);
	}

	static public UserStatusEnum parseValue(Short value) {
		if (value >= PENDING_STATUS_VERIFICATION.value) {
			return UserStatusEnum.PENDING_STATUS_VERIFICATION;
		} else {
			return valuesMap.get(value);
		}
	}
}
