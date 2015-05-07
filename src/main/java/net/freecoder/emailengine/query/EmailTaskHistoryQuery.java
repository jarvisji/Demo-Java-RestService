/**
 * 
 */
package net.freecoder.emailengine.query;

import java.util.Date;

import net.freecoder.common.query.Query;
import net.freecoder.emailengine.vo.EmailTaskHistory;

/**
 * Interfaces to query email task history.
 * 
 * @author JiTing
 */
public interface EmailTaskHistoryQuery extends
		Query<EmailTaskHistoryQuery, EmailTaskHistory> {

	/**
	 * Query email task history by EmailTask ID.
	 * 
	 * @param emailTaskId
	 *            EmailTask ID.
	 * @return This EmailTaskHistoryQuery instance.
	 */
	EmailTaskHistoryQuery emailTaskId(String emailTaskId);

	/**
	 * Query email task history by from address.
	 * 
	 * @param fromAddress
	 *            Email address.
	 * @return This EmailTaskHistoryQuery instance.
	 */
	EmailTaskHistoryQuery emailTaskFromAddress(String fromAddress);

	/**
	 * Query email task history by email template ID.
	 * 
	 * @param emailTemplateId
	 *            EmailTemplate ID.
	 * @return This EmailTaskHistoryQuery instance.
	 */
	EmailTaskHistoryQuery emailTaskTemplateId(String emailTemplateId);

	/**
	 * Query email task history which retry times great than given times.
	 * 
	 * @param retryTimes
	 *            Retry times criteria.
	 * @return This EmailTaskHistoryQuery instance.
	 */
	EmailTaskHistoryQuery emailTaskRetryTimesGreatThan(int retryTimes);

	/**
	 * Query email task history which sent time after given time.
	 * 
	 * @param time
	 *            Time criteria.
	 * @return This EmailTaskHistoryQuery instance.
	 */
	EmailTaskHistoryQuery emailTaskSentTimeAfter(Date time);

	/**
	 * Query email task history which sent time before given time.
	 * 
	 * @param time
	 *            Time criteria.
	 * @return This EmailTaskHistoryQuery instance.
	 */
	EmailTaskHistoryQuery emailTaskSentTimeBefore(Date time);

	/**
	 * Set order by retry time.
	 * 
	 * @return This EmailTaskHistoryQuery instance.
	 */
	EmailTaskHistoryQuery orderByRetryTime();

	/**
	 * Set order by sent time.
	 * 
	 * @return This EmailTaskHistoryQuery instance.
	 */
	EmailTaskHistoryQuery orderBySentTime();
}
