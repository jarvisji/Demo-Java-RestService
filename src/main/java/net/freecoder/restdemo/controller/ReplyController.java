/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.annotation.WlsModule;
import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.SystemModules;
import net.freecoder.restdemo.dao.ReplyAutoDao;
import net.freecoder.restdemo.dao.ReplyKeywordDao;
import net.freecoder.restdemo.dao.ReplySubscribeDao;
import net.freecoder.restdemo.model.ReferenceType;
import net.freecoder.restdemo.model.WlsReplyAuto;
import net.freecoder.restdemo.model.WlsReplyKeyword;
import net.freecoder.restdemo.model.WlsReplySubscribe;
import net.freecoder.restdemo.util.MiscUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controll to handler requests of Replies.
 * 
 * @author JiTing
 */
@Controller
@RequestMapping("/reply")
@WlsModule(SystemModules.M_BASE)
public class ReplyController extends CommonController {

	@Autowired
	ReplyAutoDao mAutoDao;
	@Autowired
	ReplyKeywordDao mKwDao;
	@Autowired
	ReplySubscribeDao mSubDao;

	@RequestMapping(value = "/auto/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getReplyAuto(@PathVariable String id) {
		WlsReplyAuto entity = mAutoDao.get(WlsReplyAuto.class, id);
		Result result = new Result().setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/keyword/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getReplyKeyword(@PathVariable String id) {
		WlsReplyKeyword entity = mKwDao.get(WlsReplyKeyword.class, id);
		Result result = new Result().setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/subscribe/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getReplySubscribe(@PathVariable String id) {
		WlsReplySubscribe entity = mSubDao.get(WlsReplySubscribe.class, id);
		Result result = new Result().setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/auto/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getReplyAutoList(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsReplyAuto> entities = mAutoDao.getListByWxAccountId(
				WlsReplyAuto.class, wxAccountId);
		Result result = new Result().setCount(entities.size(), entities.size())
				.setEntity(entities);
		return result;
	}

	@RequestMapping(value = "/keyword/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getReplyKeywordList(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsReplyKeyword> entities = mKwDao.getListByWxAccountId(
				WlsReplyKeyword.class, wxAccountId);
		Result result = new Result().setCount(entities.size(), entities.size())
				.setEntity(entities);
		return result;
	}

	@RequestMapping(value = "/keyword/{refType}/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getReplyKeywordListByReftype(@PathVariable String refType,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsReplyKeyword> entities = mKwDao.getListByWxAccountIdAndRefType(
				wxAccountId, ReferenceType.parseValue(refType));
		Result result = new Result().setCount(entities.size(), entities.size())
				.setEntity(entities);
		return result;
	}

	@RequestMapping(value = "/subscribe/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getReplySubscribeList(HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		List<WlsReplySubscribe> entities = mSubDao.getListByWxAccountId(
				WlsReplySubscribe.class, wxAccountId);
		Result result = new Result().setCount(entities.size(), entities.size())
				.setEntity(entities);
		return result;
	}

	@RequestMapping(value = "/auto", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createReplyAuto(@RequestBody WlsReplyAuto entity,
			HttpServletRequest request) {
		if (MiscUtil.isNotNullAndNotEmpty(entity.getId())) {
			return new Result()
					.setErrCode(ErrorCode.INVALID_DATA)
					.setErrMsg(
							"Identity of entity to be created is not null, please use PUT to update entity.");
		}
		String wxAccountId = getParamAccountId(request);
		entity.setId(UUIDUtil.uuid8());
		entity.setWlsWxAccountId(wxAccountId);
		mAutoDao.save(entity);
		Result result = new Result().setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/keyword", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createReplyKeyword(@RequestBody WlsReplyKeyword entity,
			HttpServletRequest request) {
		if (MiscUtil.isNotNullAndNotEmpty(entity.getId())) {
			return new Result()
					.setErrCode(ErrorCode.INVALID_DATA)
					.setErrMsg(
							"Identity of entity to be created is not null, please use PUT to update entity.");
		}
		String wxAccountId = getParamAccountId(request);
		entity.setId(UUIDUtil.uuid8());
		entity.setWlsWxAccountId(wxAccountId);
		mKwDao.save(entity);
		Result result = new Result().setEntity(entity);
		return result;
	}

	/**
	 * Will override exist keywords, means this api will delete all of original
	 * keywords with the same RefType, and create new keywords.
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/keywords", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createReplyKeywords(@RequestBody WlsReplyKeyword[] entities,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		for (WlsReplyKeyword entity : entities) {
			entity.setId(UUIDUtil.uuid8());
			entity.setWlsWxAccountId(wxAccountId);
		}
		String[] ids = mKwDao.saveAll(wxAccountId, entities);
		Result result = new Result().setData("id", ids)
				.setDataCount(ids.length);
		return result;
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createReplySubscribe(@RequestBody WlsReplySubscribe entity,
			HttpServletRequest request) {
		if (MiscUtil.isNotNullAndNotEmpty(entity.getId())) {
			return new Result()
					.setErrCode(ErrorCode.INVALID_DATA)
					.setErrMsg(
							"Identity of entity to be created is not null, please use PUT to update entity.");
		}
		String wxAccountId = getParamAccountId(request);
		entity.setId(UUIDUtil.uuid8());
		entity.setWlsWxAccountId(wxAccountId);
		mSubDao.save(entity);
		Result result = new Result().setEntity(entity);
		return result;
	}

	@RequestMapping(value = "/auto/{id}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateReplyAuto(@PathVariable String id,
			@RequestBody WlsReplyAuto entity, HttpServletRequest request) {
		entity.setWlsWxAccountId(getParamAccountId(request));
		entity.setId(id);
		mAutoDao.update(entity);
		return new Result().setEntity(mAutoDao.get(WlsReplyAuto.class, id));
	}

	@RequestMapping(value = "/keyword/{id}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateReplyKeyword(@PathVariable String id,
			@RequestBody WlsReplyKeyword entity, HttpServletRequest request) {
		entity.setWlsWxAccountId(getParamAccountId(request));
		entity.setId(id);
		mKwDao.update(entity);
		return new Result().setEntity(mKwDao.get(WlsReplyKeyword.class, id));
	}

	@RequestMapping(value = "/subscribe/{id}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateReplySubscribe(@PathVariable String id,
			@RequestBody WlsReplySubscribe entity, HttpServletRequest request) {
		entity.setWlsWxAccountId(getParamAccountId(request));
		entity.setId(id);
		mSubDao.update(entity);
		return new Result().setEntity(mSubDao.get(WlsReplySubscribe.class, id));
	}

	@RequestMapping(value = "/auto/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteReplyAuto(@PathVariable String id, HttpServletRequest request) {
		WlsReplyAuto entity = mAutoDao.get(WlsReplyAuto.class, id);
		mAutoDao.delete(entity);
		return new Result();
	}

	@RequestMapping(value = "/keyword/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteReplyKeyword(@PathVariable String id,
			HttpServletRequest request) {
		WlsReplyKeyword entity = mKwDao.get(WlsReplyKeyword.class, id);
		mKwDao.delete(entity);
		return new Result();
	}

	@RequestMapping(value = "/keyword/group/{groupName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteReplyKeywordGroup(@PathVariable String groupName,
			HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		mKwDao.deleteByGroup(wxAccountId, groupName);
		return new Result();
	}

	@RequestMapping(value = "/subscribe/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteReplySubscribe(@PathVariable String id,
			HttpServletRequest request) {
		WlsReplySubscribe entity = mSubDao.get(WlsReplySubscribe.class, id);
		mSubDao.delete(entity);
		return new Result();
	}
}
