package net.freecoder.restdemo.constant;

import net.freecoder.restdemo.model.ReferenceType;

/**
 * Module type enum for console defined.
 * 
 * @author JiTing
 */
public enum ModuleType {
	MWS("mws"), SHOP("shop"), CONSOLE("console"), VIP("vip"), ALBUM("album");

	private final String value;

	private ModuleType(String value) {
		this.value = value;
	}

	public String value() {
		return ReferenceType.MODULE.value() + "_" + this.value;
	}

	static public ModuleType parseValue(String value) {
		String _value = value.toUpperCase();
		ModuleType ret;
		if (_value.startsWith(ReferenceType.MODULE.value().toUpperCase() + "_")) {
			ret = ModuleType.valueOf(_value.split("_")[1]);
		} else {
			ret = ModuleType.valueOf(_value);
		}
		return ret;
	}
}
