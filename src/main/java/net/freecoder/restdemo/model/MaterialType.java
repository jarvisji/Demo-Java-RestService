/**
 * 
 */
package net.freecoder.restdemo.model;

/**
 * This is enum of material types what WeiXin platform supports. Some resouces,
 * for example Reply, need create relationship to materials and special
 * MaterialType.
 * 
 * @author JiTing
 */
public enum MaterialType {
	TEXT("text"), IMAGE("image"), AUDIO("audio"), VIDEO("video"), SIT("sit"), MIT(
			"mit");

	private final String value;

	private MaterialType(String value) {
		this.value = value;
	}

	public String value() {
		return ReferenceType.MATERIAL.value() + "_" + this.value;
	}

	static public MaterialType parseValue(String value) {
		String _value = value.toUpperCase();
		MaterialType ret;
		if (_value.startsWith(ReferenceType.MATERIAL.value().toUpperCase()
				+ "_")) {
			ret = MaterialType.valueOf(_value.split("_")[1]);
		} else {
			ret = MaterialType.valueOf(_value);
		}
		return ret;
	}
}
