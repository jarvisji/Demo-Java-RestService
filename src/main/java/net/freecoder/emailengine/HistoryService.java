/**
 * 
 */
package net.freecoder.emailengine;

import net.freecoder.emailengine.vo.EmailTask;

/**
 * Interfaces of email history.
 * 
 * @author JiTing
 */
public interface HistoryService {

	/**
	 * Archive email task as history. This interface should be used interal.
	 * 
	 * @param emailTask
	 *            EmailTask insance.
	 */
	void createEmailTaskHistory(EmailTask emailTask);

}
