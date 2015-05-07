/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.model.WlsCityCode;

/**
 * @author JiTing
 */
public interface CityCodeDao extends CommonDao<WlsCityCode> {

	List<WlsCityCode> getProvinces();

	List<WlsCityCode> getCities(String provinceCode);

	List<WlsCityCode> getAreas(String cityCode);
}
