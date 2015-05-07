/**
 * 
 */
package net.freecoder.emailengine;

/**
 * @author JiTing
 */
public enum EmailTemplateEnum {

	EMAIL_REGISTER_CONFIRM("WL-REG-01-RegisterConfirm"), EMAIL_RETRIEVE_PASSWORD(
			"WL-REG-02-RetrievePassword"), EMAIL_REGISTER_AUDIT_FAILURE(
			"WL-REG-03-RegisterAuditFailure"), EMAIL_REGISTER_AUDIT_SUCCESS(
			"WL-REG-04-RegisterAuditSuccess");

	private String value;

	EmailTemplateEnum(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
