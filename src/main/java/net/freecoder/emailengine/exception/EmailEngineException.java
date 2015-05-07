package net.freecoder.emailengine.exception;

/**
 * The base {@link Throwable} type for SpinERP. Carry our own exceptions in biz
 * layer.
 * 
 * @author JiTing
 */
public class EmailEngineException extends RuntimeException {

	private static final long serialVersionUID = -7199925386050216073L;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            {@link Throwable}.
	 */
	public EmailEngineException(Throwable root) {
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
	public EmailEngineException(String string, Throwable root) {
		super(string, root);
	}

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            Message.
	 */
	public EmailEngineException(String s) {
		super(s);
	}

}
