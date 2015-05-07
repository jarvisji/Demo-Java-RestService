/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.List;

import net.freecoder.restdemo.dao.CityCodeDao;
import net.freecoder.restdemo.model.WlsCityCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author JiTing
 */
@Controller
@RequestMapping("/citycode")
public class CityCodeController extends CommonController {

	@Autowired
	CityCodeDao cityCodeDao;

	@RequestMapping(value = "/provinces", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getProvinces() {
		List<WlsCityCode> provinces = cityCodeDao.getProvinces();
		return new Result().setEntity(provinces);
	}

	@RequestMapping(value = "/province/{code}/cities", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getCities(@PathVariable String code) {
		List<WlsCityCode> cities = cityCodeDao.getCities(code);
		return new Result().setEntity(cities);
	}

	@RequestMapping(value = "/city/{code}/areas", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getAreas(@PathVariable String code) {
		List<WlsCityCode> areas = cityCodeDao.getAreas(code);
		return new Result().setEntity(areas);
	}
}