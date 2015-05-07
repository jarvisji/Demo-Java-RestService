/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.annotation.WlsModule;
import net.freecoder.restdemo.constant.SystemModules;
import net.freecoder.restdemo.dao.MaterialAudioDao;
import net.freecoder.restdemo.dao.MaterialImageDao;
import net.freecoder.restdemo.dao.MaterialImagetextDao;
import net.freecoder.restdemo.dao.MaterialMultiImagetextDao;
import net.freecoder.restdemo.dao.MaterialTextDao;
import net.freecoder.restdemo.dao.MaterialVideoDao;
import net.freecoder.restdemo.dao.TagDao;
import net.freecoder.restdemo.model.MaterialType;
import net.freecoder.restdemo.model.WlsMaterialAudio;
import net.freecoder.restdemo.model.WlsMaterialImage;
import net.freecoder.restdemo.model.WlsMaterialImagetext;
import net.freecoder.restdemo.model.WlsMaterialMultiImagetext;
import net.freecoder.restdemo.model.WlsMaterialText;
import net.freecoder.restdemo.model.WlsMaterialVideo;
import net.freecoder.restdemo.model.WlsTag;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author JiTing
 */
@Controller
@RequestMapping("/tag")
@WlsModule(SystemModules.M_BASE)
public class TagController extends CommonController {

	private static Logger logger = LoggerFactory.getLogger(TagController.class);
	@Autowired
	TagDao tagDao;
	@Autowired
	MaterialTextDao textDao;
	@Autowired
	MaterialImageDao imageDao;
	@Autowired
	MaterialAudioDao audioDao;
	@Autowired
	MaterialVideoDao videoDao;
	@Autowired
	MaterialImagetextDao sitDao;
	@Autowired
	MaterialMultiImagetextDao mitDao;

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getTagsByAccount(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsTag> tags = tagDao.getTagsByAccount(wxAccountId);
		Result result = new Result();
		result.setEntity(tags);
		result.setCount(tags.size(), tags.size());
		return result;
	}

	@RequestMapping(value = "/list/material/{materialType}/{materialId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getTagsByMaterial(@PathVariable String materialType,
			@PathVariable String materialId, HttpServletRequest request) {
		List<WlsTag> tags = tagDao.getTagsByMaterial(materialId,
				MaterialType.parseValue(materialType));
		Result result = new Result();
		result.setEntity(tags);
		result.setCount(tags.size(), tags.size());
		return result;
	}

	@RequestMapping(value = "/list/material/{materialType}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getTagsByMaterialType(@PathVariable String materialType,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsTag> tags = tagDao.getTagsByMaterialType(wxAccountId,
				MaterialType.parseValue(materialType));
		Result result = new Result();
		result.setEntity(tags);
		result.setCount(tags.size(), tags.size());
		return result;
	}

	@RequestMapping(value = "/{tagId}/material/{materialType}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getMaterialByTag(@PathVariable String tagId,
			@PathVariable String materialType, HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		MaterialType mType = MaterialType.parseValue(materialType);
		String[] materialIds = tagDao.getMaterialIdByTag(wxAccountId, tagId,
				mType);

		List retList = new ArrayList();
		if (materialIds.length > 0) {
			if (mType.equals(MaterialType.TEXT)) {
				retList = textDao.get(WlsMaterialText.class, materialIds);
			} else if (mType.equals(MaterialType.IMAGE)) {
				retList = imageDao.get(WlsMaterialImage.class, materialIds);
			} else if (mType.equals(MaterialType.AUDIO)) {
				retList = audioDao.get(WlsMaterialAudio.class, materialIds);
			} else if (mType.equals(MaterialType.VIDEO)) {
				retList = videoDao.get(WlsMaterialVideo.class, materialIds);
			} else if (mType.equals(MaterialType.SIT)) {
				retList = sitDao.get(WlsMaterialImagetext.class, materialIds);
			} else if (mType.equals(MaterialType.MIT)) {
				retList = mitDao.get(WlsMaterialMultiImagetext.class,
						materialIds);
			}
		}
		Result result = new Result();
		result.setEntity(retList);
		result.setCount(retList.size(), retList.size());
		return result;
	}

	@RequestMapping(value = "/material/{materialType}/{materialId}", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result addTagMaterial(@RequestBody WlsTag wlsTag,
			@PathVariable String materialType, @PathVariable String materialId,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		if (wlsTag.getWlsWxAccountId() == null) {
			wlsTag.setWlsWxAccountId(wxAccountId);
		}

		try {
			tagDao.addTagOnMaterial(wlsTag, materialId,
					MaterialType.parseValue(materialType));
		} catch (DataIntegrityViolationException e) {
			if (e.contains(ConstraintViolationException.class)) {
				logger.info(
						"ConstraintViolationException: the tag already exists on material {}, {}",
						materialType, materialId);
			} else {
				throw e;
			}
		}
		return new Result();
	}

	@RequestMapping(value = "/{tagId}/material/{materialType}/{materialId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result removeTagMaterial(@PathVariable String tagId,
			@PathVariable String materialType, @PathVariable String materialId,
			HttpServletRequest request) {
		tagDao.removeTagFromMaterial(tagId, materialId);
		Result result = new Result();
		return result;
	}

}
