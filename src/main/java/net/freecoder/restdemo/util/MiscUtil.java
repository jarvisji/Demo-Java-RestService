/**
 * 
 */
package net.freecoder.restdemo.util;

/**
 * @author JiTing
 */
public class MiscUtil {

	/**
	 * Check object is null or empty.
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNullOrEmpty(Object o) {
		boolean ret = false;
		if (o == null) {
			ret = true;
		} else {
			if ((o instanceof String) && ((String) o).isEmpty()) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Check object is not null and not empty.
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNotNullAndNotEmpty(Object o) {
		boolean ret = false;
		if (o instanceof String) {
			if (o != null && !((String) o).isEmpty()) {
				ret = true;
			}
		} else if (o != null) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Generate random number in Short data scope, guarantee 5 digits, so the
	 * number will be 10000-32767.
	 * 
	 * This is used as verification code currently.
	 * 
	 * @return
	 */
	public static Short generateShortRandom() {
		Short ret = (short) (Short.MAX_VALUE * Math.random());
		if (ret < 10000) {
			ret = generateShortRandom();
		}
		return ret;
	}
}
