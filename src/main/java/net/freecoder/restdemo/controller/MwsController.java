package net.freecoder.restdemo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.annotation.WlsModule;
import net.freecoder.restdemo.constant.SystemModules;
import net.freecoder.restdemo.dao.MwsDao;
import net.freecoder.restdemo.dao.MwsSiteEntryDao;
import net.freecoder.restdemo.model.WlsMwsSiteConfig;
import net.freecoder.restdemo.model.WlsMwsSiteConfigId;
import net.freecoder.restdemo.model.WlsMwsSiteEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for MobileWebSite module.
 * 
 * @author JiTing
 */
@Controller
@RequestMapping("/mws")
@WlsModule(SystemModules.M_SITE)
public class MwsController extends CommonController {

	@Autowired
	MwsDao mwsDao;
	@Autowired
	MwsSiteEntryDao siteEntryDao;

	@RequestMapping(value = "/siteconfig", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getSiteConfig(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsMwsSiteConfig> configList = mwsDao
				.getListByWxAccountId(wxAccountId);
		Result result = new Result().setEntity(configList);
		result.setCount(configList.size(), configList.size());
		return result;
	}

	@RequestMapping(value = "/siteconfig/{propName}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getSiteConfigProp(@PathVariable String propName,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		WlsMwsSiteConfigId siteConfigId = new WlsMwsSiteConfigId(propName,
				wxAccountId);
		WlsMwsSiteConfig config = mwsDao.get(siteConfigId);
		Result result = new Result().setEntity(config);
		return result;
	}

	@RequestMapping(value = "/entry", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getSiteEntry(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		WlsMwsSiteEntry wlsMwsSiteEntry = siteEntryDao.get(wxAccountId);
		Result result = new Result().setEntity(wlsMwsSiteEntry);
		return result;
	}

	@RequestMapping(value = "/siteconfig", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result saveSiteConfig(@RequestBody List<WlsMwsSiteConfig> configList,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		for (WlsMwsSiteConfig siteConfig : configList) {
			siteConfig.getId().setWlsWxAccountId(wxAccountId);
		}
		mwsDao.saveBatch(configList);
		return new Result();
	}

	@RequestMapping(value = "/entry", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result saveSiteEntry(@RequestBody WlsMwsSiteEntry siteEntry,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		siteEntry.setWlsWxAccountId(wxAccountId);
		siteEntryDao.save(siteEntry);
		return new Result().setEntity(siteEntryDao.get(wxAccountId));
	}

	@RequestMapping(value = "/siteconfig", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result overwriteSiteConfig(@RequestBody List<WlsMwsSiteConfig> configList,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		mwsDao.deleteAllAndSave(wxAccountId, configList);
		return new Result();
	}

	@RequestMapping(value = "/entry", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result overwriteSiteEntry(@RequestBody WlsMwsSiteEntry siteEntry,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		siteEntry.setWlsWxAccountId(wxAccountId);
		siteEntryDao.update(siteEntry);
		return new Result();
	}

	@RequestMapping(value = "/siteconfig", method = RequestMethod.DELETE, headers = "Content-Type=application/json")
	public @ResponseBody
	Result deleteSiteConfig(@RequestBody List<WlsMwsSiteConfig> configList) {
		mwsDao.deleteBatch(configList);
		return new Result();
	}

	@RequestMapping(value = "/entry", method = RequestMethod.DELETE, headers = "Content-Type=application/json")
	public @ResponseBody
	Result deleteSiteEntry(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		WlsMwsSiteEntry siteEntry = siteEntryDao.get(wxAccountId);
		if (siteEntry != null) {
			siteEntryDao.delete(siteEntry);
		}
		return new Result();
	}
}
