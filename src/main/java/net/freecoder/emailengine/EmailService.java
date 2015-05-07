/**
 * 
 */
package net.freecoder.emailengine;

import java.util.Date;
import java.util.Map;

import net.freecoder.emailengine.vo.EmailTask;

/**
 * Interfaces to handle email actions.
 * 
 * @author JiTing
 */
public interface EmailService {

	String EMAILTASK_ATTRNAME_CC = "cc";
	String EMAILTASK_ATTRNAME_TO = "to";

	/**
	 * Create email task according to specific email template.
	 * 
	 * @param EmailTemplateEnum
	 *            emailTemplate.
	 * @param toAddress
	 *            Email address of receiver.
	 * @return Instance of email task.
	 */
	EmailTask createEmailTask(EmailTemplateEnum emailTemplate, String toAddress);

	/**
	 * Create email task according to specific email template.
	 * 
	 * @param EmailTemplateEnum
	 *            emailTemplate.
	 * @param toAddresses
	 *            Email address of receivers.
	 * @return Instance of email task.
	 */
	EmailTask createEmailTask(EmailTemplateEnum emailTemplate,
			String[] toAddresses);

	/**
	 * Create email task according to specific email template.
	 * 
	 * @param EmailTemplateEnum
	 *            emailTemplate.
	 * @param fromAddress
	 *            Email address of sender.
	 * @param toAddress
	 *            Email address of receiver.
	 * @return Instance of email task.
	 */
	EmailTask createEmailTask(EmailTemplateEnum emailTemplate,
			String fromAddress, String toAddress);

	/**
	 * Create email task according to specific email template.
	 * 
	 * @param EmailTemplateEnum
	 *            emailTemplate.
	 * @param fromAddress
	 *            Email address of sender.
	 * @param toAddresses
	 *            Email address of receivers.
	 * @return Instance of email task.
	 */
	EmailTask createEmailTask(EmailTemplateEnum emailTemplate,
			String fromAddress, String[] toAddresses);

	/**
	 * Process sending of email task. The email will be sent out immediately. <br>
	 * If send email failed, Email engine will put it in query and try again.
	 * 
	 * @param emailTask
	 *            EmailTask instance.
	 * @param params
	 *            Parameters to replace variables in email template.
	 */
	void sendEmail(EmailTask emailTask, Map<String, Object> params);

	/**
	 * Process sending of email task asynchronously. The email will be sent out
	 * on the {@code EmailTask.scheduledTime}.<br>
	 * Throw exception if {@code EmailTask.scheduledTime} was not set.
	 * 
	 * @param emailTask
	 *            EmailTask instance.
	 * @param params
	 *            Parameters to replace variables in email template.
	 * @param scheduledTime
	 *            Time to send the scheduled email out.
	 */
	void sendEmailAsync(EmailTask emailTask, Map<String, Object> params,
			Date scheduledTime);

	/**
	 * Cancel scheduled email task.<br>
	 * Notice: cannot cancel immediately email task.
	 * 
	 * @param emailTask
	 *            EmailTask instance.
	 */
	void cancelEmail(EmailTask emailTask);
}
