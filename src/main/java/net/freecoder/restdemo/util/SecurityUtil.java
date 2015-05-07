package net.freecoder.restdemo.util;

import java.security.MessageDigest;

import net.freecoder.restdemo.exception.ServiceException;

/**
 * @author JiTing
 */
public class SecurityUtil {
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Encode string by using SHA1 algorithm.
	 * 
	 * @param str
	 *            Input string.
	 * @return Encoded string.
	 */
	public static String sha1Encode(String str) {
		String ret = "";
		if (str != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				byte[] byteArr = md.digest(str.getBytes("UTF-8"));
				ret = bytes2HexString(byteArr);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
		return ret;
	}

	/**
	 * Get HEX string for given byte array.
	 * 
	 * @param bytes
	 *            Byte array.
	 * @return HEX string.
	 */
	public static String bytes2HexString(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
}
