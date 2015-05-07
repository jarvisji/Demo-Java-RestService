/**
 * 
 */
package net.freecoder.restdemo.controller;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.annotation.WlsModule;
import net.freecoder.restdemo.constant.SystemModules;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author JiTing
 */
@Controller
@RequestMapping("/module")
@WlsModule(SystemModules.M_BASE)
public class ModuleController extends CommonController {

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Content-Type=application/json")
	public @ResponseBody
	Result getModuleList(HttpServletRequest request) {
		getUserCred(getParamCred(request));

		SystemModules[] modules = SystemModules.values();
		String[] moduleNames = new String[modules.length];
		for (int i = 0; i < modules.length; i++) {
			moduleNames[i] = modules[i].toString();
		}
		return new Result().setEntity(moduleNames);
	}
}
