/**
 * 
 */
package net.freecoder.restdemo.service;

import java.util.Map;

import net.freecoder.emailengine.exception.SmsProviderException;
import net.freecoder.restdemo.model.WlsSmsHistory;

/**
 * Interface of SMS.
 * 
 * @author JiTing
 */
public interface SmsProvider {

	/**
	 * Send content to special phone number.
	 * 
	 * @param phoneNumber
	 * @param content
	 * */
	WlsSmsHistory send(String phoneNumber, String content);

	/**
	 * Batch send same content to different numbers.
	 * 
	 * @param phoneNumbers
	 *            Array of phone numbers.
	 * @param content
	 */
	WlsSmsHistory send(String[] phoneNumbers, String content);

	/**
	 * Batch send different contents to different numbers.
	 * 
	 * @param mapNumberContent
	 *            Key is phone number, value is content.
	 */
	WlsSmsHistory send(Map<String, String> mapNumberContent);

	/**
	 * Check response, throw exception if error has occurred.
	 * 
	 * @param respText
	 * @throws SmsProviderException
	 */
	void checkResponse(String respText) throws SmsProviderException;

	/**
	 * <pre>
	 * If content length great than 70 charactors, service provider will split it to two pieces.
	 * If content length great than 140 charactors, it will be splited to 3 pieces, instead of 2.
	 * Different service provide has different counting solution, so we need implement individually.
	 * </pre>
	 * 
	 * @param contentLength
	 * @return count of pieces.
	 */
	int countPieces(int contentLength);

}
