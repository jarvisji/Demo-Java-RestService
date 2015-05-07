package net.freecoder.restdemo.exception;

/**
 * The base {@link Throwable} type for SpinERP. Carry our own exceptions in biz
 * layer.
 * 
 * @author JiTing
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -7199925386050216073L;

	/**
	 * Constructor.
	 * 
	 * @param root
	 *            {@link Throwable}.
	 */
	public ServiceException(Throwable root) {
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
	public ServiceException(String string, Throwable root) {
		super(string, root);
	}

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            Message.
	 */
	public ServiceException(String s) {
		super(s);
	}

	/**
	 * Constructor.
	 * 
	 */
	public ServiceException() {
		super();
	}
}
