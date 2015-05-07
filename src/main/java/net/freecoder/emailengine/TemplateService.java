/**
 * 
 */
package net.freecoder.emailengine;

import net.freecoder.emailengine.query.EmailTemplateQuery;
import net.freecoder.emailengine.vo.EmailTemplate;
import net.freecoder.emailengine.vo.EmailTemplateWrapper;

/**
 * Interfaces for maintaining email templates.
 * 
 * @author JiTing
 */
public interface TemplateService {

	String TEMPLATE_PATH_DEFAULT = "emailengine/templates";
	String TEMPLATE_FILE_CONTENT = "content.html";
	String TEMPLATE_FILE_CONTENT_TEXT = "content.txt";
	String TEMPLATE_FILE_SUBJECT = "subject.txt";

	/**
	 * Create new template to data persistence store.
	 * 
	 * @param template
	 *            Instance of EmailTemplate object.
	 */
	void createTemplate(EmailTemplate template);

	/**
	 * Create templates to data persistence store. Email engine will try to load
	 * templates from path "emailengine/templates" in class path, and throws
	 * exception if load configure file failed.
	 */
	void createTemplates();

	/**
	 * Create templates to data persistence store. Use this interface to instead
	 * of default one, by specific configure file of email templates
	 * definitions. <br/>
	 * Email engine will try to load the configure file within CLASSPATH, then
	 * the absolute path.
	 * 
	 * @param configFilePath
	 *            Path of configure file.
	 */
	void createTemplates(String configFilePath);

	/**
	 * Delete template from persistence data store by template ID.
	 * 
	 * @param templateId
	 *            Template ID.
	 */
	void deleteTemplate(String templateId);

	/**
	 * Create TemplateQuery to query templates from persistence data store.
	 * 
	 * @return Instance of TemplateQuery.
	 */
	EmailTemplateQuery createTemplateQuery();

	/**
	 * Get email template by template name, if there are more than one versions,
	 * return the latest version.
	 * 
	 * @param EmailTemplateEnum
	 *            Template name.
	 * @return EmailTemplateWrapper.
	 */
	EmailTemplateWrapper getEmailTemplate(EmailTemplateEnum templateName);

	EmailTemplateWrapper getEmailTemplate(String templateName);

}
