/**
 * 
 */
package net.freecoder.restdemo.util;

import net.freecoder.restdemo.model.MaterialType;
import net.freecoder.restdemo.model.ReferenceType;

/**
 * Utilities for DB value handler.
 * 
 * @author JiTing
 */
public class DBValueUtil {

	/**
	 * Value of KeywordRefType in DB may be composed by two parts:
	 * KeywordRefType + MaterialType, and they are splitted by "_".
	 * 
	 * @param dbRefTypeVal
	 * @return
	 */
	public static ReferenceType parseKeywordRefType(String dbRefTypeVal) {
		String refTypePrefix = dbRefTypeVal.split("_")[0];
		return ReferenceType.parseValue(refTypePrefix);
	}

	/**
	 * Value of KeywordRefType in DB may be composed by two parts:
	 * KeywordRefType + MaterialType, and they are splitted by "_".
	 * 
	 * @param dbRefTypeVal
	 * @return
	 */
	public static MaterialType parseMaterialType(String dbRefTypeVal) {
		return MaterialType.parseValue(dbRefTypeVal);
	}
}
