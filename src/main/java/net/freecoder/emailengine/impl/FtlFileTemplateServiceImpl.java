/**
 * 
 */
package net.freecoder.emailengine.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.freecoder.emailengine.EmailTemplateEnum;
import net.freecoder.emailengine.TemplateService;
import net.freecoder.emailengine.exception.EmailEngineException;
import net.freecoder.emailengine.query.EmailTemplateQuery;
import net.freecoder.emailengine.vo.EmailTemplate;
import net.freecoder.emailengine.vo.EmailTemplateWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * Tempaltes are implemented in FTL files.<br>
 * The default template location is "emailengine/tempaltes" in CLASSPATH, if
 * want to override the configuration, please define value of
 * "emailTemplatePath" property in Spring context file.
 * 
 * @author JiTing
 */
public class FtlFileTemplateServiceImpl implements TemplateService {

	private static Logger logger = LoggerFactory
			.getLogger(FtlFileTemplateServiceImpl.class);

	private String emailTemplatePath = TEMPLATE_PATH_DEFAULT;
	private Configuration freemarkerConfiguration;
	private Map<String, EmailTemplateWrapper> templates = new HashMap<String, EmailTemplateWrapper>();
	private List<String> templatePathNames = new ArrayList<String>();

	/**
	 * Setter for Spring inject.
	 * 
	 * @param emailTemplatePath
	 *            Path of email template.
	 */
	public void setEmailTemplatePath(String emailTemplatePath) {
		this.emailTemplatePath = emailTemplatePath;
	}

	private void createFreemarkerConfigurationInstance() throws IOException {
		freemarkerConfiguration = new Configuration();
		File file4TemplatePath = getFile4TemplatePath();
		freemarkerConfiguration
				.setDirectoryForTemplateLoading(file4TemplatePath);
		freemarkerConfiguration.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfiguration.setDefaultEncoding("UTF-8");
		// TODO:J In productions systems:
		// TemplateExceptionHandler.RETHROW_HANDLER
		freemarkerConfiguration
				.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		logger.debug(
				"Created FreeMarker Configuration instance, templates location is {}",
				file4TemplatePath.getAbsolutePath());

	}

	/**
	 * Constructor.
	 * 
	 * @throws IOException
	 *             Throwed by
	 *             freemarkerConfiguration.setDirectoryForTemplateLoading().
	 * @throws URISyntaxException
	 *             URISyntaxException
	 */
	public FtlFileTemplateServiceImpl() throws IOException, URISyntaxException {
		createFreemarkerConfigurationInstance();
		loadTemplateNames();
	}

	private void loadTemplateNames() throws IOException, URISyntaxException {
		File[] templateNameFiles = getFile4TemplatePath().listFiles();
		if (templateNameFiles == null || templateNameFiles.length == 0) {
			throw new EmailEngineException(
					"Template error: no template defined.");
		}
		for (File templateNameFile : templateNameFiles) {
			File contentFile = new File(templateNameFile, TEMPLATE_FILE_CONTENT);
			File subjectFile = new File(templateNameFile, TEMPLATE_FILE_SUBJECT);
			if (!contentFile.exists() || !subjectFile.exists()) {
				StringBuffer sb = new StringBuffer(TEMPLATE_FILE_CONTENT)
						.append(" or ").append(TEMPLATE_FILE_SUBJECT)
						.append(" was not found in ")
						.append(templateNameFile.getPath());
				logger.error(sb.toString());
				throw new EmailEngineException("Template error: missing files.");
			} else {
				this.templatePathNames.add(templateNameFile.getName());
			}
		}
	}

	/**
	 * Get email template from file in package on server. No version support,
	 * and the consistency between native and DB is not guaranteed. In most of
	 * cases, should get template from DB.
	 * 
	 * Note, freemarker already implemented cache form template loading, we
	 * needn't do it again.
	 * 
	 * @param EmailTemplateEnum
	 *            emailTemplate.
	 * @return EmailTemplate.
	 */
	private EmailTemplateWrapper getEmailTemplateNative(
			EmailTemplateEnum emailTemplate) {
		EmailTemplateWrapper wrapper = null;
		if (templatePathNames.contains(emailTemplate.value())) {
			String templatePathName = emailTemplate.value();
			wrapper = new EmailTemplateWrapper();
			wrapper.setTemplateName(emailTemplate.name());
			try {
				Template htmlContentTemplate = freemarkerConfiguration
						.getTemplate(templatePathName + "/"
								+ TEMPLATE_FILE_CONTENT);
				wrapper.setHtmlContentTemplate(htmlContentTemplate);
			} catch (IOException e) {
				logger.error("Cannot load html content template of {}",
						templatePathName);
				e.printStackTrace();
			}
			try {
				Template subjectTemplate = freemarkerConfiguration
						.getTemplate(templatePathName + "/"
								+ TEMPLATE_FILE_SUBJECT);
				wrapper.setSubjectTemplate(subjectTemplate);
			} catch (IOException e) {
				logger.error("Cannot load subject template of {}",
						templatePathName);
				e.printStackTrace();
			}
			try {
				Template textContentTemplate = freemarkerConfiguration
						.getTemplate(templatePathName + "/"
								+ TEMPLATE_FILE_CONTENT_TEXT);
				wrapper.setSubjectTemplate(textContentTemplate);
			} catch (IOException e) {
				logger.warn(
						"Cannot load text content template of {}, user will only receive email in html version.",
						templatePathName);
			}
		} else {
			logger.error("Template eror: no template for name: {}",
					emailTemplate.name());
		}
		return wrapper;
	}

	/**
	 * Construct File object for email template path in CLASSPATH.
	 * 
	 * @return File File object.
	 */
	private File getFile4TemplatePath() {
		URL emailTemplateUrl = Thread.currentThread().getContextClassLoader()
				.getResource(emailTemplatePath);
		if (emailTemplateUrl == null) {
			throw new EmailEngineException(
					"Email tempalte path doesn't exist in CLASSPATH: "
							+ emailTemplatePath);
		}
		logger.debug("Email template path is：{}", emailTemplateUrl.toString());
		File templatePathFile;
		try {
			templatePathFile = new File(emailTemplateUrl.toURI());
		} catch (URISyntaxException e) {
			throw new EmailEngineException("Invalid email template url: ", e);
		}
		return templatePathFile;
	}

	@Override
	public EmailTemplateWrapper getEmailTemplate(EmailTemplateEnum emailTemplate) {
		return getEmailTemplateNative(emailTemplate);
	}

	@Override
	public EmailTemplateWrapper getEmailTemplate(String templateName) {
		EmailTemplateEnum emailTemplate = EmailTemplateEnum
				.valueOf(templateName);
		return getEmailTemplateNative(emailTemplate);
	}

	@Override
	/**
	 * @deprecated Not implemented in FreeMarker template service.
	 */
	public void createTemplate(EmailTemplate template) {
		throw new EmailEngineException(
				"Not implemented in FreeMarker template service.");
	}

	/**
	 * @deprecated Not implemented in FreeMarker template service.
	 */
	@Override
	public void createTemplates() {
		throw new EmailEngineException(
				"Not implemented in FreeMarker template service.");
	}

	@Override
	/**
	 * @deprecated Not implemented in FreeMarker template service.
	 */
	public void createTemplates(String configFilePath) {
		throw new EmailEngineException(
				"Not implemented in FreeMarker template service.");
	}

	@Override
	/**
	 * @deprecated Not implemented in FreeMarker template service.
	 */
	public void deleteTemplate(String templateId) {
		throw new EmailEngineException(
				"Not implemented in FreeMarker template service.");
	}

	@Override
	/**
	 * @deprecated Not implemented in FreeMarker template service.
	 */
	public EmailTemplateQuery createTemplateQuery() {
		throw new EmailEngineException(
				"Not implemented in FreeMarker template service.");
	}
}
