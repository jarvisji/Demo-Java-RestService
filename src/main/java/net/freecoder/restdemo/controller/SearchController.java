/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.freecoder.restdemo.exception.ServiceException;
import net.freecoder.restdemo.model.MaterialType;
import net.freecoder.restdemo.model.WlsMaterialAudio;
import net.freecoder.restdemo.model.WlsMaterialImage;
import net.freecoder.restdemo.model.WlsMaterialImagetext;
import net.freecoder.restdemo.model.WlsMaterialMultiImagetext;
import net.freecoder.restdemo.model.WlsMaterialText;
import net.freecoder.restdemo.model.WlsMaterialVideo;

import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author JiTing
 */
@Controller
@RequestMapping("/search")
@WlsModule(SystemModules.M_BASE)
public class SearchController extends CommonController {

	private static Logger logger = LoggerFactory
			.getLogger(SearchController.class);
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

	@RequestMapping(value = "/material/{materialType}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result searchMaterialByTitle(@PathVariable String materialType,
			@RequestParam("q") String queryStr, HttpServletRequest request) {
		String wxAccountId = getParamAccountId(request);
		MaterialType mType = MaterialType.parseValue(materialType);

		logger.debug("Search string: {}", queryStr);
		try {
			queryStr = new String(queryStr.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}

		SimpleExpression eqAccountId = Restrictions.eq("wlsWxAccountId",
				wxAccountId);
		List retList = new ArrayList();

		// Search fields are deceided by which are displaying on UI.
		if (mType.equals(MaterialType.TEXT)) {
			SimpleExpression like = Restrictions.like("content", queryStr,
					MatchMode.ANYWHERE);
			LogicalExpression and = Restrictions.and(like, eqAccountId);
			retList = textDao.find(WlsMaterialText.class, and);
		} else if (mType.equals(MaterialType.IMAGE)) {
			SimpleExpression like = Restrictions.like("fileName", queryStr,
					MatchMode.ANYWHERE);
			LogicalExpression and = Restrictions.and(like, eqAccountId);
			retList = imageDao.find(WlsMaterialImage.class, and);
		} else if (mType.equals(MaterialType.AUDIO)) {
			SimpleExpression likeFileName = Restrictions.like("fileName",
					queryStr, MatchMode.ANYWHERE);
			SimpleExpression likeMemo = Restrictions.like("memo", queryStr,
					MatchMode.ANYWHERE);
			LogicalExpression or = Restrictions.or(likeFileName, likeMemo);
			LogicalExpression and = Restrictions.and(or, eqAccountId);
			retList = audioDao.find(WlsMaterialAudio.class, and);
		} else if (mType.equals(MaterialType.VIDEO)) {
			SimpleExpression likeFileName = Restrictions.like("fileName",
					queryStr, MatchMode.ANYWHERE);
			SimpleExpression likeTitle = Restrictions.like("title", queryStr,
					MatchMode.ANYWHERE);
			LogicalExpression or = Restrictions.or(likeFileName, likeTitle);
			LogicalExpression and = Restrictions.and(or, eqAccountId);
			retList = videoDao.find(WlsMaterialVideo.class, and);
		} else if (mType.equals(MaterialType.SIT)) {
			SimpleExpression likeTitle = Restrictions.like("title", queryStr,
					MatchMode.ANYWHERE);
			SimpleExpression likeSummary = Restrictions.like("summary",
					queryStr, MatchMode.ANYWHERE);
			LogicalExpression or = Restrictions.or(likeTitle, likeSummary);
			LogicalExpression and = Restrictions.and(or, eqAccountId);
			retList = sitDao.find(and);
		} else if (mType.equals(MaterialType.MIT)) {
			String hql = "select distinct wlsMaterialMultiImagetext.id from WlsMaterialImagetext where wlsMaterialMultiImagetext.id in (SELECT id FROM WlsMaterialMultiImagetext where wlsWxAccountId=:wxAccountId) and title like :search";
			Map<String, String> params = new HashMap<String, String>(2);
			params.put("wxAccountId", wxAccountId);
			params.put("search", "%" + queryStr + "%");
			List<WlsMaterialImagetext> find = sitDao.find(hql, params);
			String[] ids = new String[find.size()];
			find.toArray(ids);
			retList = mitDao.get(WlsMaterialMultiImagetext.class, ids);
		}

		Result result = new Result();
		result.setEntity(retList);
		result.setCount(retList.size(), retList.size());
		return result;
	}
}
