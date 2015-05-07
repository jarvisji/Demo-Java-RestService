/**
 * 
 */
package net.freecoder.restdemo.exception;

/**
 * @author JiTing
 */
public class ServiceDaoException extends ServiceException {

	private static final long serialVersionUID = 1491169037316492924L;

	/**
	 * @param s
	 */
	public ServiceDaoException(String s) {
		super(s);
	}

	public ServiceDaoException() {
		super();
	}

}
