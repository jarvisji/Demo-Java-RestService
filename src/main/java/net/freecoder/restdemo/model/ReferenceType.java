/**
 * 
 */
package net.freecoder.restdemo.model;

/**
 * Keywords reply is different to auto reply and follow reply, it will not only
 * refer to material, but also other modules, for example an article, weather
 * report function, etc. So it has "ref_id", "ref_type", rather than
 * "material_id" and "material_type".
 * 
 * This enum defined all the meta-type that keyword can ref.<br>
 * Notice: if a keyword is refer to a material, its 'ref_type' should be
 * combination of KeywordRefType_MaterialType.
 * 
 * @author JiTing
 */
public enum ReferenceType {
	MATERIAL("material"), MODULE("module");

	private final String value;

	private ReferenceType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	static public ReferenceType parseValue(String value) {
		String _value = value.split("_").length == 2 ? value.split("_")[0]
				: value;
		return ReferenceType.valueOf(_value.toUpperCase());
	}
}
