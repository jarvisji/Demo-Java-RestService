/**
 * 
 */
package net.freecoder.restdemo.exception;

/**
 * @author JiTing
 */
public class ServiceDaoForeignKeyConstraintException extends
		ServiceDaoException {
	private static final long serialVersionUID = -3878970663697392671L;

	public ServiceDaoForeignKeyConstraintException() {
		super();
	}

	public ServiceDaoForeignKeyConstraintException(String msg) {
		super(msg);
	}
}
