/**
 * 
 */
package net.freecoder.emailengine.query;

import net.freecoder.common.query.Query;
import net.freecoder.emailengine.vo.EmailTemplate;

/**
 * Interfaces to query email templates.
 * 
 * @author JiTing
 */
public interface EmailTemplateQuery extends
		Query<EmailTemplateQuery, EmailTemplate> {

	/**
	 * Set template ID as query criteria.
	 * 
	 * @param id
	 *            Template ID.
	 * @return This instance of TemplateQuery.
	 */
	EmailTemplateQuery templateId(String id);

	/**
	 * Set template key as query criteria.
	 * 
	 * @param key
	 *            Template key.
	 * @return This instance of TemplateQuery.
	 */
	EmailTemplateQuery templateKey(String key);

	/**
	 * Set template version as query criteria.
	 * 
	 * @param version
	 *            Template version.
	 * @return This instance of TemplateQuery.
	 */
	EmailTemplateQuery templateVersion(int version);

	/**
	 * Set template deploymentId as query criteria.
	 * 
	 * @param deploymentId
	 *            Template deploymentId.
	 * @return This instance of TemplateQuery.
	 */
	EmailTemplateQuery templateDeploymentId(int deploymentId);
}
