/**
 * 
 */
package net.freecoder.emailengine.exception;

import net.freecoder.restdemo.exception.ServiceException;

/**
 * @author JiTing
 */
public class SmsProviderException extends ServiceException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            {@link Throwable}.
	 */
	public SmsProviderException(Throwable root) {
		super(root);
	}

	/**
	 * Constructor.
	 * 
	 * @param string
	 *            Message.
	 * @param root
	 *            {@link Throwable}.
	 */
	public SmsProviderException(String string, Throwable root) {
		super(string, root);
	}

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            Message.
	 */
	public SmsProviderException(String s) {
		super(s);
	}

}
