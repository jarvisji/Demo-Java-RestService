/**
 * 
 */
package net.freecoder.restdemo.util;

import java.util.UUID;

/**
 * Utility for UUID generator. Currently use Activiti implementation.
 * 
 * @author JiTing
 */
public final class UUIDUtil {

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };

	private UUIDUtil() {
	}

	/**
	 * Get a new UUID, remove "-" and conver to upper cases.
	 * 
	 * @return UUID string.
	 */
	public static String uuid() {
		String uuid = UUID.randomUUID().toString();
		String[] uuidPartials = uuid.split("-");
		StringBuffer sb = new StringBuffer();
		for (String partial : uuidPartials) {
			sb.append(partial);
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * Generate a short UUID which length is 8.
	 * 
	 * @return 8 charactors UUID string.
	 */
	public static String uuid8() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}
}
