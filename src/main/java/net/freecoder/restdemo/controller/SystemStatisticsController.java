/**
 * 
 */
package net.freecoder.restdemo.controller;

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
@RequestMapping("/stat")
@WlsModule(SystemModules.R_ADMIN)
public class SystemStatisticsController {
	private static long serverStartTime;

	static {
		serverStartTime = System.currentTimeMillis();
	}

	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getServerStat() {
		// there is a bean: ServerStat, consider how to build it.
		long started = System.currentTimeMillis() - serverStartTime;
		return new Result().setData("ServerStarted", started / 1000
				+ " seconds");
	}
}
