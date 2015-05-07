/**
 * 
 */
package net.freecoder.restdemo.util;

import java.io.IOException;
import java.io.StringWriter;

import net.freecoder.restdemo.exception.ServiceException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author JiTing
 */
public class JsonUtil {

	static public String toString(Object bean) {
		StringWriter writer = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(writer, bean);
		} catch (JsonGenerationException e) {
			throw new ServiceException(e);
		} catch (JsonMappingException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		return writer.getBuffer().toString();
	}
}
