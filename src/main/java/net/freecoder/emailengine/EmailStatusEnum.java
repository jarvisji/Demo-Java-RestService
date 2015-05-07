/**
 * 
 */
package net.freecoder.emailengine;

import net.freecoder.emailengine.exception.EmailEngineException;

/**
 * @author JiTing
 */
public enum EmailStatusEnum {
	SENT_SUCCESS(0x01), SENT_FAIL(0x02), SENT_CANCELLED(0x03), USER_CONFIRMED(
			0x04);

	private byte value;

	EmailStatusEnum(int value) {
		this.value = (byte) value;
	}

	public byte value() {
		return this.value;
	}

	static public EmailStatusEnum parseValue(byte value) {
		EmailStatusEnum ret;
		switch (value) {
		case 0x01:
			ret = EmailStatusEnum.SENT_SUCCESS;
			break;
		case 0x02:
			ret = EmailStatusEnum.SENT_FAIL;
			break;
		case 0x03:
			ret = EmailStatusEnum.SENT_CANCELLED;
			break;
		case 0x04:
			ret = EmailStatusEnum.USER_CONFIRMED;
			break;
		default:
			throw new EmailEngineException(
					"Parse EmailStatusEnum error, unsupport value: " + value);
		}
		return ret;
	}
}
