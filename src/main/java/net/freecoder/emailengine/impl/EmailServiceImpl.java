/**
 * 
 */
package net.freecoder.emailengine.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import net.freecoder.emailengine.EmailService;
import net.freecoder.emailengine.EmailStatusEnum;
import net.freecoder.emailengine.EmailTemplateEnum;
import net.freecoder.emailengine.TemplateService;
import net.freecoder.emailengine.dao.EmailEngineDao;
import net.freecoder.emailengine.exception.EmailEngineException;
import net.freecoder.emailengine.vo.EmailTask;
import net.freecoder.emailengine.vo.EmailTaskAttr;
import net.freecoder.emailengine.vo.EmailTemplateWrapper;
import net.freecoder.restdemo.util.DateTimeUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author JiTing
 */
public class EmailServiceImpl implements EmailService {
	private static Logger logger = LoggerFactory
			.getLogger(EmailServiceImpl.class);

	@Autowired
	private TemplateService templateService;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	EmailEngineDao emailEngineDao;

	private String defaultSender;

	/**
	 * @return the fromAddress
	 */
	public String getDefaultSender() {
		return defaultSender;
	}

	/**
	 * @param fromAddress
	 *            the fromAddress to set
	 */
	public void setDefaultSender(String defaultSender) {
		this.defaultSender = defaultSender;
	}

	@Override
	public EmailTask createEmailTask(EmailTemplateEnum emailTemplate,
			String toAddress) {
		return createEmailTask(emailTemplate, defaultSender, toAddress);
	}

	@Override
	public EmailTask createEmailTask(EmailTemplateEnum emailTemplate,
			String[] toAddresses) {
		return createEmailTask(emailTemplate, defaultSender, toAddresses);
	}

	@Override
	public EmailTask createEmailTask(EmailTemplateEnum emailTemplate,
			String fromAddress, String toAddress) {
		EmailTask task = new EmailTask(UUIDUtil.uuid8(), fromAddress,
				emailTemplate.name());
		task.addEmailTaskAttr(EMAILTASK_ATTRNAME_TO, toAddress);
		emailEngineDao.save(task);
		return task;
	}

	@Override
	public EmailTask createEmailTask(EmailTemplateEnum emailTemplate,
			String fromAddress, String[] toAddresses) {
		EmailTask task = new EmailTask(UUIDUtil.uuid8(), fromAddress,
				emailTemplate.name());
		for (String address : toAddresses) {
			task.addEmailTaskAttr(EMAILTASK_ATTRNAME_TO, address);
		}
		emailEngineDao.save(task);
		return task;
	}

	@Override
	public void sendEmail(final EmailTask emailTask,
			final Map<String, Object> params) {
		verify(emailTask);
		EmailTemplateWrapper templateWrapper = templateService
				.getEmailTemplate(emailTask.getTemplateName());
		if (templateWrapper == null) {
			logger.error(
					"No template defined for name: {}, cancel send email task.",
					emailTask.getTemplateName());
			return;
		}

		final String htmlContent = ftl2str(
				templateWrapper.getHtmlContentTemplate(), params);
		final String subject = ftl2str(templateWrapper.getSubjectTemplate(),
				params);
		Iterator iterator = emailTask.getEmailTaskAttrs().iterator();
		while (iterator.hasNext()) {
			final EmailTaskAttr attr = (EmailTaskAttr) iterator.next();
			if (EMAILTASK_ATTRNAME_TO.equals(attr.getAttrname())) {
				MimeMessagePreparator preparator = new MimeMessagePreparator() {
					public void prepare(MimeMessage mimeMessage)
							throws Exception {
						MimeMessageHelper message = new MimeMessageHelper(
								mimeMessage);
						message.setTo(attr.getAttrvalue());
						message.setFrom(emailTask.getFromAddress());
						message.setText(htmlContent, true);
						message.setSubject(subject);
					}
				};
				logger.debug("Sending email to: {}, template: {}",
						attr.getAttrvalue(), emailTask.getTemplateName());
				this.mailSender.send(preparator);

				updateTaskStatus(emailTask);
			}
		}
	}

	// TODO:J implement error handler and retry.
	private void updateTaskStatus(EmailTask emailTask) {
		emailTask.setStatus(EmailStatusEnum.SENT_SUCCESS.value());
		emailTask.setSentTime(DateTimeUtil.getGMTDate());
		emailTask.setRetryTimes((byte) 0);
		emailEngineDao.update(emailTask);
	}

	@Override
	public void sendEmailAsync(EmailTask emailTask, Map<String, Object> params,
			Date scheduledTime) {
		throw new EmailEngineException("Not implemented.");
	}

	@Override
	public void cancelEmail(EmailTask emailTask) {
		// TODO Auto-generated method stub
		throw new EmailEngineException("Not implemented.");
	}

	private void verify(EmailTask task) {
		if (task == null) {
			throw new EmailEngineException("Invalid email task: null");
		}
		if (task.getFromAddress() == null || task.getFromAddress().isEmpty()) {
			throw new EmailEngineException(
					"Invalid email task: From address is not set.");
		}
		if (task.getEmailTaskAttrs().size() == 0) {
			throw new EmailEngineException(
					"Invalid email task: EmailTaskAttr is not set.");
		}
	}

	private String ftl2str(Template freemarkerTemplate,
			Map<String, Object> params) {
		String ret = "";
		if (freemarkerTemplate == null) {
			return ret;
		}
		StringWriter sw = new StringWriter();
		try {
			try {
				freemarkerTemplate.process(params, sw);
			} catch (TemplateException e) {
				logger.error("Invalid FreeMarker template. {}", e.getCause());
				throw new EmailEngineException("Invalid FreeMarker template.",
						e);
			} catch (IOException e) {
				logger.error("Convert FreeMarker template to String error. {}",
						e.getCause());
				throw new EmailEngineException(
						"Convert FreeMarker template to String error.", e);
			}
			ret = sw.toString();
		} finally {
			try {
				sw.close();
			} catch (IOException e) {
				logger.error("Convert FreeMarker template to String error. {}",
						e.getCause());
				throw new EmailEngineException(
						"Convert FreeMarker template to String error.", e);
			}
		}
		return ret;
	}

}
