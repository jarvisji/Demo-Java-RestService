/**
 * 
 */
package net.freecoder.emailengine.vo;

import freemarker.template.Template;

/**
 * @author JiTing
 */
public class EmailTemplateWrapper {
	private String templateName;
	private Template textContentTemplate;
	private Template htmlContentTemplate;
	private Template subjectTemplate;

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName
	 *            the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the textContentTemplate
	 */
	public Template getTextContentTemplate() {
		return textContentTemplate;
	}

	/**
	 * @param textContentTemplate
	 *            the textContentTemplate to set
	 */
	public void setTextContentTemplate(Template textContentTemplate) {
		this.textContentTemplate = textContentTemplate;
	}

	/**
	 * @return the htmlContentTemplate
	 */
	public Template getHtmlContentTemplate() {
		return htmlContentTemplate;
	}

	/**
	 * @param htmlContentTemplate
	 *            the htmlContentTemplate to set
	 */
	public void setHtmlContentTemplate(Template htmlContentTemplate) {
		this.htmlContentTemplate = htmlContentTemplate;
	}

	/**
	 * @return the subjectTemplate
	 */
	public Template getSubjectTemplate() {
		return subjectTemplate;
	}

	/**
	 * @param subjectTemplate
	 *            the subjectTemplate to set
	 */
	public void setSubjectTemplate(Template subjectTemplate) {
		this.subjectTemplate = subjectTemplate;
	}
}
