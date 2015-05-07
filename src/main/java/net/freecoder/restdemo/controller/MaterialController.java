/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.annotation.WlsModule;
import net.freecoder.restdemo.constant.ErrorCode;
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
import net.freecoder.restdemo.util.UUIDUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO:J check object owner is current user or not, user only can change his
 * own materials. This should be done in AOP, for all CRUD operations.
 * 
 * @author JiTing
 */
@Controller
@RequestMapping("/material")
@WlsModule(SystemModules.M_BASE)
public class MaterialController extends CommonController {

	final static Logger logger = LoggerFactory
			.getLogger(MaterialController.class);

	@Autowired
	MaterialTextDao mTextDao;
	@Autowired
	MaterialImageDao mImageDao;
	@Autowired
	MaterialAudioDao mAudioDao;
	@Autowired
	MaterialVideoDao mVideoDao;
	@Autowired
	MaterialImagetextDao mImagetextDao;
	@Autowired
	MaterialMultiImagetextDao mMultiImagetextDao;
	@Autowired
	TagDao tagDao;

	@RequestMapping(value = "/text/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getTextList(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsMaterialText> find = mTextDao.getListByWxAccountId(
				WlsMaterialText.class, wxAccountId);
		Result result = new Result();
		result.setCount(find.size(), find.size());
		result.setEntity(find);
		return result;
	}

	@RequestMapping(value = "/image/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getImageList(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsMaterialImage> find = mImageDao.getListByWxAccountId(
				WlsMaterialImage.class, wxAccountId);
		Result result = new Result();
		result.setCount(find.size(), find.size());
		result.setEntity(find);
		return result;
	}

	@RequestMapping(value = "/audio/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getAudioList(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsMaterialAudio> find = mAudioDao.getListByWxAccountId(
				WlsMaterialAudio.class, wxAccountId);
		Result result = new Result();
		result.setCount(find.size(), find.size());
		result.setEntity(find);
		return result;
	}

	@RequestMapping(value = "/video/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getVideoList(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsMaterialVideo> find = mVideoDao.getListByWxAccountId(
				WlsMaterialVideo.class, wxAccountId);
		Result result = new Result();
		result.setCount(find.size(), find.size());
		result.setEntity(find);
		return result;
	}

	@RequestMapping(value = "/sit/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getImagetextList(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsMaterialImagetext> find = mImagetextDao
				.getListByWxAccountId(wxAccountId);
		Result result = new Result();
		result.setCount(find.size(), find.size());
		result.setEntity(find);
		return result;
	}

	@RequestMapping(value = "/mit/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getMultiImagetextList(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsMaterialMultiImagetext> find = mMultiImagetextDao
				.getListByWxAccountId(WlsMaterialMultiImagetext.class,
						wxAccountId);
		Result result = new Result();
		result.setCount(find.size(), find.size());
		result.setEntity(find);
		return result;
	}

	@RequestMapping(value = "/text/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getText(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialText entity = mTextDao.get(WlsMaterialText.class, id);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/image/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getImage(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialImage entity = mImageDao.get(WlsMaterialImage.class, id);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/audio/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getAudio(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialAudio entity = mAudioDao.get(WlsMaterialAudio.class, id);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/video/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getVideo(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialVideo entity = mVideoDao.get(WlsMaterialVideo.class, id);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/sit/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getImagetext(@PathVariable String id, HttpServletRequest request) {
		Result result = new Result();
		if (id.indexOf(",") != -1) {
			String[] ids = id.split(",");
			List<WlsMaterialImagetext> list = mImagetextDao.get(
					WlsMaterialImagetext.class, ids);
			result.setEntity(list);
			result.setCount(list.size(), list.size());
		} else {
			WlsMaterialImagetext entity = mImagetextDao.get(
					WlsMaterialImagetext.class, id);
			result.setEntity(entity);
		}
		return result;
	}

	@RequestMapping(value = "/mit/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getMultiImagetext(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialMultiImagetext entity = mMultiImagetextDao.get(
				WlsMaterialMultiImagetext.class, id);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/text", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createText(@RequestBody WlsMaterialText entity,
			HttpServletRequest request) {
		if (entity.getId() != null && !entity.getId().isEmpty()) {
			return new Result()
					.setErrCode(ErrorCode.INVALID_DATA)
					.setErrMsg(
							"Identity of entity to be created is not null, please use PUT to update entity.");
		}
		String wxAccountId = getParamAccountId(request);
		entity.setId(UUIDUtil.uuid8());
		entity.setWlsWxAccountId(wxAccountId);
		mTextDao.save(entity);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/image", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createImage(@RequestBody WlsMaterialImage entity,
			HttpServletRequest request) {
		if (entity.getId() != null && !entity.getId().isEmpty()) {
			return new Result()
					.setErrCode(ErrorCode.INVALID_DATA)
					.setErrMsg(
							"Identity of entity to be created is not null, please use PUT to update entity.");
		}
		String wxAccountId = getParamAccountId(request);
		entity.setId(UUIDUtil.uuid8());
		entity.setWlsWxAccountId(wxAccountId);
		mImageDao.save(entity);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/audio", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createAudio(@RequestBody WlsMaterialAudio entity,
			HttpServletRequest request) {
		if (entity.getId() != null && !entity.getId().isEmpty()) {
			return new Result()
					.setErrCode(ErrorCode.INVALID_DATA)
					.setErrMsg(
							"Identity of entity to be created is not null, please use PUT to update entity.");
		}
		String wxAccountId = getParamAccountId(request);
		entity.setId(UUIDUtil.uuid8());
		entity.setWlsWxAccountId(wxAccountId);
		mAudioDao.save(entity);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/video", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createVideo(@RequestBody WlsMaterialVideo entity,
			HttpServletRequest request) {
		if (entity.getId() != null && !entity.getId().isEmpty()) {
			return new Result()
					.setErrCode(ErrorCode.INVALID_DATA)
					.setErrMsg(
							"Identity of entity to be created is not null, please use PUT to update entity.");
		}
		String wxAccountId = getParamAccountId(request);
		entity.setId(UUIDUtil.uuid8());
		entity.setWlsWxAccountId(wxAccountId);
		mVideoDao.save(entity);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/sit", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createImagetext(@RequestBody WlsMaterialImagetext entity,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		if (entity.getId() != null && !entity.getId().isEmpty()) {
			return new Result()
					.setErrCode(ErrorCode.INVALID_DATA)
					.setErrMsg(
							"Identity of entity to be created is not null, please use PUT to update entity.");
		}
		entity.setId(UUIDUtil.uuid8());
		entity.setWlsWxAccountId(wxAccountId);
		mImagetextDao.save(entity);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/mit", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createMultiImagetext(@RequestBody WlsMaterialMultiImagetext entity,
			HttpServletRequest request) {
		if (entity.getId() != null && !entity.getId().isEmpty()) {
			return new Result()
					.setErrCode(ErrorCode.INVALID_DATA)
					.setErrMsg(
							"Identity of entity to be created is not null, please use PUT to update entity.");
		}
		String wxAccountId = getParamAccountId(request);
		entity.setId(UUIDUtil.uuid8());
		entity.setWlsWxAccountId(wxAccountId);

		Iterator<WlsMaterialImagetext> iterator = entity
				.getWlsMaterialImagetexts().iterator();
		while (iterator.hasNext()) {
			WlsMaterialImagetext sit = iterator.next();
			sit.setId(UUIDUtil.uuid8());
			sit.setWlsMaterialMultiImagetext(entity);
			sit.setWlsWxAccountId(wxAccountId);
		}

		mMultiImagetextDao.save(entity);
		Result result = new Result();
		result.setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/text/{id}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateText(@PathVariable String id,
			@RequestBody WlsMaterialText entity, HttpServletRequest request) {
		entity.setWlsWxAccountId(getParamAccountId(request));
		entity.setId(id);
		mTextDao.update(entity);
		return new Result().setEntity(mTextDao.get(id));
	}

	@RequestMapping(value = "/image/{id}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateImage(@PathVariable String id,
			@RequestBody WlsMaterialImage entity, HttpServletRequest request) {
		entity.setWlsWxAccountId(getParamAccountId(request));
		entity.setId(id);
		mImageDao.update(entity);
		return new Result().setEntity(mImageDao.get(id));
	}

	@RequestMapping(value = "/audio/{id}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateAudio(@PathVariable String id,
			@RequestBody WlsMaterialAudio entity, HttpServletRequest request) {
		entity.setWlsWxAccountId(getParamAccountId(request));
		entity.setId(id);
		mAudioDao.update(entity);
		return new Result().setEntity(mAudioDao.get(id));
	}

	@RequestMapping(value = "/video/{id}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateVideo(@PathVariable String id,
			@RequestBody WlsMaterialVideo entity, HttpServletRequest request) {
		entity.setWlsWxAccountId(getParamAccountId(request));
		entity.setId(id);
		mVideoDao.update(entity);
		return new Result().setEntity(mVideoDao.get(id));
	}

	@RequestMapping(value = "/sit/{id}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateImagetext(@PathVariable String id,
			@RequestBody WlsMaterialImagetext entity, HttpServletRequest request) {
		entity.setWlsWxAccountId(getParamAccountId(request));
		entity.setId(id);
		mImagetextDao.update(entity);
		return new Result().setEntity(mImagetextDao.get(id));
	}

	@RequestMapping(value = "/mit/{id}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateMultiImagetext(@PathVariable String id,
			@RequestBody WlsMaterialMultiImagetext entity,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		entity.setWlsWxAccountId(wxAccountId);
		entity.setId(id);

		// Get current mit, compare with new, delete the items those not exsited
		// in new entity.
		WlsMaterialMultiImagetext dbMit = mMultiImagetextDao.get(id);
		for (WlsMaterialImagetext dbMitItem : dbMit.getWlsMaterialImagetexts()) {
			boolean isRemoved = true;
			for (WlsMaterialImagetext entityItem : entity
					.getWlsMaterialImagetexts()) {
				if (dbMitItem.getId().equals(entityItem.getId())) {
					isRemoved = false;
					break;
				}
			}
			if (isRemoved) {
				mImagetextDao.delete(dbMitItem.getId());
			}
		}

		// Update items.
		Iterator<WlsMaterialImagetext> iterator = entity
				.getWlsMaterialImagetexts().iterator();
		while (iterator.hasNext()) {
			WlsMaterialImagetext sit = iterator.next();
			if (sit.getId() == null) {
				sit.setId(UUIDUtil.uuid8());
			}
			if (sit.getWlsWxAccountId() == null) {
				sit.setWlsWxAccountId(wxAccountId);
			}

			sit.setWlsMaterialMultiImagetext(entity);
		}

		mMultiImagetextDao.update(entity);
		return new Result().setEntity(mMultiImagetextDao.get(id));
	}

	@RequestMapping(value = "/text/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteText(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialText wlsMaterialText = mTextDao.get(WlsMaterialText.class,
				id);
		mTextDao.delete(wlsMaterialText);
		return new Result();
	}

	@RequestMapping(value = "/image/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteImage(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialImage entity = mImageDao.get(WlsMaterialImage.class, id);
		mImageDao.delete(entity);
		return new Result();
	}

	@RequestMapping(value = "/audio/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteAudio(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialAudio entity = mAudioDao.get(WlsMaterialAudio.class, id);
		mAudioDao.delete(entity);
		return new Result();
	}

	@RequestMapping(value = "/video/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteVideo(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialVideo entity = mVideoDao.get(WlsMaterialVideo.class, id);
		mVideoDao.delete(entity);
		return new Result();
	}

	/**
	 * Delete sit material by id.
	 * <p>
	 * Also delete tags on the material.
	 * </p>
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/sit/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteImagetext(@PathVariable String id, HttpServletRequest request) {
		WlsMaterialImagetext entity = mImagetextDao.get(
				WlsMaterialImagetext.class, id);
		mImagetextDao.delete(entity);
		List<WlsTag> tagsByMaterial = tagDao.getTagsByMaterial(id,
				MaterialType.parseValue("sit"));
		for (WlsTag tag : tagsByMaterial) {
			tagDao.removeTagFromMaterial(tag.getId(), id);
		}
		return new Result();
	}

	@RequestMapping(value = "/mit/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteMultiImagetext(@PathVariable String id,
			HttpServletRequest request) {
		WlsMaterialMultiImagetext entity = mMultiImagetextDao.get(
				WlsMaterialMultiImagetext.class, id);
		mMultiImagetextDao.delete(entity);
		return new Result();
	}
}
