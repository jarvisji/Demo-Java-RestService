/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.constant.ModuleType;
import net.freecoder.restdemo.constant.WxMessageType;
import net.freecoder.restdemo.dao.MaterialAudioDao;
import net.freecoder.restdemo.dao.MaterialImagetextDao;
import net.freecoder.restdemo.dao.MaterialMultiImagetextDao;
import net.freecoder.restdemo.dao.MaterialTextDao;
import net.freecoder.restdemo.dao.MwsSiteEntryDao;
import net.freecoder.restdemo.dao.ReplyAutoDao;
import net.freecoder.restdemo.dao.ReplyKeywordDao;
import net.freecoder.restdemo.dao.ReplySubscribeDao;
import net.freecoder.restdemo.dao.ShopDao;
import net.freecoder.restdemo.dao.UserDao;
import net.freecoder.restdemo.dao.WxAccountDao;
import net.freecoder.restdemo.dao.WxMessageDao;
import net.freecoder.restdemo.model.MaterialType;
import net.freecoder.restdemo.model.ReferenceType;
import net.freecoder.restdemo.model.WlsMaterialImagetext;
import net.freecoder.restdemo.model.WlsMaterialMultiImagetext;
import net.freecoder.restdemo.model.WlsMaterialText;
import net.freecoder.restdemo.model.WlsMwsSiteEntry;
import net.freecoder.restdemo.model.WlsReplyAuto;
import net.freecoder.restdemo.model.WlsReplyKeyword;
import net.freecoder.restdemo.model.WlsReplySubscribe;
import net.freecoder.restdemo.model.WlsShopEntry;
import net.freecoder.restdemo.model.WlsWxAccount;
import net.freecoder.restdemo.model.WlsWxAccountExt;
import net.freecoder.restdemo.model.WlsWxMessage;
import net.freecoder.restdemo.model.WxArticle;
import net.freecoder.restdemo.model.WxMessage;
import net.freecoder.restdemo.util.MiscUtil;
import net.freecoder.restdemo.util.SecurityUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre>
 * Communicate with Weixin server. Include:
 * 1. Verify authentication paramters from Weixin Server.
 * 2. Response server forwarded requests.
 * 3. Generate URL and Token for platform user.
 * etc.
 * 
 * </pre>
 * 
 * @author JiTing
 */
@Controller
@RequestMapping("/wxproxy")
public class WxProxyController extends CommonController {
	private final Logger logger = LoggerFactory
			.getLogger(WxProxyController.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private WxAccountDao wxAccountDao;
	@Autowired
	private ReplyAutoDao replyAutoDao;
	@Autowired
	private ReplySubscribeDao replySubDao;
	@Autowired
	private ReplyKeywordDao replyKwDao;
	@Autowired
	private MaterialTextDao materialTextDao;
	@Autowired
	private MaterialAudioDao materialAudioDao;
	@Autowired
	private MaterialImagetextDao materialSitDao;
	@Autowired
	private MaterialMultiImagetextDao materialMitDao;
	@Autowired
	private WxMessageDao wxMsgDao;
	@Autowired
	private MwsSiteEntryDao mwsSiteEntryDao;
	@Autowired
	private ShopDao shopDao;

	@Value("#{sysconfProperties['wlc.url.website']}")
	private String wlWebSiteUrl;
	@Value("#{sysconfProperties['wlc.url.wms.index']}")
	private String wmsIndexUrl;
	@Value("#{sysconfProperties['wlc.url.wms.content']}")
	private String wmsContentUrl;
	@Value("#{sysconfProperties['wlshop.home']}")
	private String wlshopHome;

	// cache token for each account, decrease DB access to verify every message
	// from Weixin server. Key is wxAccountId, value is token.
	Map<String, String> mAccountTokens = new HashMap<String, String>();

	/**
	 * Reset URL and TOKEN for Weixin server to request.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/entry/{wxAccountId}/new", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	Result resetWxEntry(@PathVariable String wxAccountId,
			HttpServletRequest request) {
		String userId = cred2UserId(getParamCred(request));
		Result result = new Result();
		if (!isWxAccountBelongsToUser(wxAccountId, userId)) {
			result.setSuccess(false).setErrMsg("拒绝访问。");
		} else {
			String token = wxAccountDao
					.resetWxAccountExt_EntryToken(wxAccountId);
			result.setData("url", "/wxproxy/" + wxAccountId);
			result.setData("token", token);
			result.setDataCount(1);
			// update cached token.
			mAccountTokens.put(wxAccountId, token);
		}
		return result;
	}

	/**
	 * Get URL and TOKEN for Weixin server to request.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/entry/{wxAccountId}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	Result getWxEntry(@PathVariable String wxAccountId,
			HttpServletRequest request) {
		logger.debug("get entry for wxAccount: {}", wxAccountId);
		String userId = cred2UserId(getParamCred(request));
		Result result = new Result();
		if (!isWxAccountBelongsToUser(wxAccountId, userId)) {
			result.setSuccess(false).setErrMsg("拒绝访问。");
		} else {
			WlsWxAccountExt propToken = wxAccountDao.getAccountExtProp(
					wxAccountId, WxAccountExtProp.ENTRY_TOKEN);
			if (propToken != null) {
				result.setData("url", "/wxproxy/" + wxAccountId);
				result.setData("token", propToken.getPropValue());
				result.setDataCount(1);
			}
		}
		return result;
	}

	private boolean isWxAccountBelongsToUser(String wxAccountId, String userId) {
		SimpleExpression eqUserId = Restrictions.eq("wlsUserId", userId);
		SimpleExpression eqAccountId = Restrictions.eq("id", wxAccountId);
		LogicalExpression and = Restrictions.and(eqUserId, eqAccountId);
		List<WlsWxAccount> find = wxAccountDao.find(and);
		if (find.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Verify request from WeiXin server. Reference to <br>
	 * http ://mp.weixin.qq.com/wiki/index.php?title=%E6%
	 * 8E%A5%E5%85%A5%E6%8C%87%E5%8D%97
	 * 
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @param model
	 * @return Response "echostr" back to WeiXin service if verify successfully.
	 */
	@RequestMapping(value = "/{wxAccountId}", method = RequestMethod.GET)
	@ResponseBody
	public String doVerify(@PathVariable String wxAccountId, String signature,
			String timestamp, String nonce, String echostr) {
		logger.debug("Into doVerify().");
		String ret = "";
		if (MiscUtil.isNullOrEmpty(wxAccountId)
				|| MiscUtil.isNullOrEmpty(signature)
				|| MiscUtil.isNullOrEmpty(timestamp)
				|| MiscUtil.isNullOrEmpty(nonce)) {
			ret = "Wrong parameters input.";
		} else {
			String token = retrieveToken(wxAccountId);
			boolean verified = verifyWxSerivce(token, timestamp, nonce,
					signature);
			if (verified) {
				ret = echostr;
			} else {
				ret = "Verify failed.";
			}
		}
		return ret;
	}

	private String retrieveToken(String wxAccountId) {
		String ret = "";
		WlsWxAccountExt propToken = wxAccountDao.getAccountExtProp(wxAccountId,
				WxAccountExtProp.ENTRY_TOKEN);
		if (propToken == null) {
			logger.error("Retrieve token failed for wxAccountId: {}",
					wxAccountId);
		} else {
			ret = propToken.getPropValue();
			mAccountTokens.put(wxAccountId, ret);
		}
		return ret;
	}

	private boolean verifyWxSerivce(String token, String timestamp,
			String nonce, String signature) {
		boolean ret = false;
		String[] paramStrings = new String[] { token, timestamp, nonce };
		Arrays.sort(paramStrings);
		String joinedString = StringUtils.join(paramStrings, "");
		String sha1String = SecurityUtil.sha1Encode(joinedString);
		if (sha1String.equalsIgnoreCase(signature)) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Hanler for WeiXin messages.
	 * 
	 * @param wxAccountId
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{wxAccountId}", method = RequestMethod.POST, headers = "Accept=application/xml")
	public @ResponseBody
	WxMessage handlePost(@PathVariable String wxAccountId,
			@RequestBody WxMessage message, String signature, String timestamp,
			String nonce) throws Exception {
		logger.debug("Received request for account: {}", wxAccountId);
		logger.debug("Received message from WeiXin server: {}", message.toXml());

		String token;
		if (mAccountTokens.containsKey(wxAccountId)) {
			token = mAccountTokens.get(wxAccountId);
		} else {
			token = retrieveToken(wxAccountId);
		}

		boolean verified = verifyWxSerivce(token, timestamp, nonce, signature);
		if (!verified) {
			logger.error("Message varification failed, it is not from WeiXin server!");
			return null;
		}

		saveMessageHistory(message, wxAccountId);

		WxMessage retMessage = null;
		WxMessageType msgType = WxMessageType.parseValue(message.getMsgType());
		if (WxMessageType.EVENT.equals(msgType)) {

			WxMessageType eventType = WxMessageType.parseValue(message
					.getMsgType() + "_" + message.getEvent());
			if (WxMessageType.EVENT_SUBSCRIBE.equals(eventType)) {
				retMessage = getWelcomeMsg(wxAccountId, message);
			} else if (WxMessageType.EVENT_UNSUBSCRIBE.equals(eventType)) {
				onUserUnsubscribe(message);
			} else if (WxMessageType.EVENT_CLICK.equals(eventType)) {
				retMessage = onMenuClick(message);
			}

		} else if (WxMessageType.TEXT.equals(msgType)) {
			retMessage = handleTextMessage(wxAccountId, message);
		}

		if (retMessage != null) {
			retMessage.setToUserName(message.getFromUserName());
			retMessage.setFromUserName(message.getToUserName());
			retMessage.setCreateTime(System.currentTimeMillis());
			logger.debug("Reply message to WeiXin server:{}",
					retMessage.toXml());
		} else {
			logger.debug("Reply message to WeiXin server: null");
		}
		return retMessage;
	}

	private void onUserUnsubscribe(WxMessage message) {
		logger.debug("onUserUnsubscribe. {}", message.toString());
		// Nothing to do currently.
	}

	private WxMessage getWelcomeMsg(String wxAccountId,
			WxMessage receivedMessage) {
		List<WlsReplySubscribe> replyList = replySubDao.getListByWxAccountId(
				WlsReplySubscribe.class, wxAccountId);
		logger.debug("Get subscribe reply count: {}" + replyList.size());
		WxMessage ret = null;
		if (replyList.size() > 0) {
			WlsReplySubscribe wlsReplySubscribe = replyList.get(0);
			String materialId = wlsReplySubscribe.getMaterialId();
			MaterialType materialType = MaterialType
					.parseValue(wlsReplySubscribe.getMaterialType());
			ret = convertMaterial2Message(materialType, materialId);
			ret.setMsgType(WxMessageType.parseValue(materialType).value());
		}
		return ret;
	}

	private WxMessage onMenuClick(WxMessage receivedMessage) {
		WxMessage ret = new WxMessage();
		return ret;
	}

	private WxMessage handleTextMessage(String wxAccountId,
			WxMessage receivedMessage) {
		logger.debug("handleTextMessage().");
		WxMessage ret = new WxMessage();
		String recContent = receivedMessage.getContent();
		List<WlsReplyKeyword> ksReplyList = replyKwDao.getByKeyword(
				wxAccountId, recContent);
		if (ksReplyList.size() > 0) {
			String refId = ksReplyList.get(0).getRefId();
			String refType = ksReplyList.get(0).getRefType();
			ReferenceType referenceType = ReferenceType.parseValue(refType);
			if (referenceType == ReferenceType.MATERIAL) {
				ret = convertMaterial2Message(MaterialType.parseValue(refType),
						refId);
			} else if (referenceType == ReferenceType.MODULE) {
				ret = convertModule2Message(ModuleType.parseValue(refType),
						wxAccountId);
			}
		} else {
			List<WlsReplyAuto> autoReplyList = replyAutoDao
					.getListByWxAccountId(WlsReplyAuto.class, wxAccountId);
			if (autoReplyList.size() > 0) {
				String materialId = autoReplyList.get(0).getMaterialId();
				String materialType = autoReplyList.get(0).getMaterialType();
				ret = convertMaterial2Message(
						MaterialType.parseValue(materialType), materialId);
			}
		}
		return ret;
	}

	private WxMessage convertModule2Message(ModuleType moduleType,
			String wxAccountId) {
		WxArticle wxArticle = null;
		if (ModuleType.MWS.equals(moduleType)) {
			WlsMwsSiteEntry wlsMwsSiteEntry = mwsSiteEntryDao.get(wxAccountId);
			if (wlsMwsSiteEntry != null) {
				wxArticle = new WxArticle();
				wxArticle.setTitle(wlsMwsSiteEntry.getTitle())
						.setDescription(wlsMwsSiteEntry.getSummary())
						.setPicUrl(wlsMwsSiteEntry.getCoverUrl())
						.setUrl(getMwsSiteIndex(wxAccountId));

			}
		} else if (ModuleType.SHOP.equals(moduleType)) {
			WlsShopEntry entry = shopDao.get(wxAccountId);
			if (entry != null) {
				wxArticle = new WxArticle();
				wxArticle.setTitle(entry.getTitle())
						.setDescription(entry.getSummary())
						.setPicUrl(entry.getCoverUrl())
						.setUrl(getShopUrl(wxAccountId));
			}
		}
		WxMessage message = null;
		if (wxArticle != null) {
			message = new WxMessage();
			List<WxArticle> articleList = new ArrayList<WxArticle>();
			articleList.add(wxArticle);
			message.setArticles(articleList);
			message.setArticleCount(articleList.size());
			message.setMsgType(WxMessageType.NEWS.value());
		}
		return message;
	}

	/**
	 * Audio, video and image materials are not supported currently, since they
	 * are asked to upload to weixin server.
	 * 
	 * @param materialType
	 * @param materialId
	 * @return
	 */
	private WxMessage convertMaterial2Message(MaterialType materialType,
			String materialId) {
		WxMessage message = new WxMessage();
		if (MaterialType.TEXT.equals(materialType)) {
			WlsMaterialText wlsMaterialText = materialTextDao.get(materialId);
			message.setContent(wlsMaterialText.getContent());
		} else if (MaterialType.SIT.equals(materialType)) {
			WlsMaterialImagetext sit = materialSitDao.get(materialId);
			WxArticle wxArticle = new WxArticle();
			wxArticle.setTitle(sit.getTitle()).setDescription(sit.getSummary())
					.setPicUrl(sit.getCoverUrl());
			wxArticle.setUrl(getMwsContentUrl(sit.getWlsWxAccountId(),
					sit.getId()));
			List<WxArticle> articleList = new ArrayList<WxArticle>();
			articleList.add(wxArticle);
			message.setArticles(articleList);
			message.setArticleCount(articleList.size());
		} else if (MaterialType.MIT.equals(materialType)) {
			WlsMaterialMultiImagetext wlsMaterialMultiImagetext = materialMitDao
					.get(materialId);

			// order sits.
			TreeSet<WlsMaterialImagetext> sitSet = new TreeSet<WlsMaterialImagetext>(
					new Comparator<WlsMaterialImagetext>() {
						@Override
						public int compare(WlsMaterialImagetext o1,
								WlsMaterialImagetext o2) {
							return o1.getPositionInMulti()
									- o2.getPositionInMulti();
						}
					});
			sitSet.addAll(wlsMaterialMultiImagetext.getWlsMaterialImagetexts());

			Iterator<WlsMaterialImagetext> iterator = sitSet.iterator();
			List<WxArticle> articleList = new ArrayList<WxArticle>();
			while (iterator.hasNext()) {
				WlsMaterialImagetext sit = iterator.next();
				WxArticle wxArticle = new WxArticle();
				wxArticle.setTitle(sit.getTitle())
						.setDescription(sit.getSummary())
						.setPicUrl(sit.getCoverUrl());
				wxArticle.setUrl(getMwsContentUrl(sit.getWlsWxAccountId(),
						sit.getId()));
				articleList.add(wxArticle);
			}
			message.setArticles(articleList);
			message.setArticleCount(articleList.size());
		}
		message.setMsgType(WxMessageType.parseValue(materialType).value());
		return message;
	}

	/**
	 * Save mesage to DB.
	 * 
	 * @param message
	 */
	private void saveMessageHistory(WxMessage message, String wxAccountId) {
		Calendar calendar = Calendar.getInstance();
		// weixin post timestamp precision only to seconds, we need
		// milliseconds.
		calendar.setTimeInMillis(message.getCreateTime() * 1000);
		Date createTime = calendar.getTime();
		WxMessageType msgType = WxMessageType.parseValue(message.getMsgType());
		if (WxMessageType.EVENT.equals(msgType)) {
			msgType = WxMessageType.parseValue(message.getMsgType() + "_"
					+ message.getEvent());
		}

		WlsWxMessage dbMessage = new WlsWxMessage(UUIDUtil.uuid8(),
				message.getFromUserName(), message.getToUserName(),
				msgType.toString(), createTime, wxAccountId);
		dbMessage.setMsgId(message.getMsgId());
		dbMessage.setContent(message.toXml());
		wxMsgDao.save(dbMessage);
	}

	/**
	 * 
	 * @param wxAccountId
	 * @return URL of index page of mobile web site.
	 */
	private String getMwsSiteIndex(String wxAccountId) {
		return new StringBuffer(wlWebSiteUrl).append(wmsIndexUrl)
				.append("?site=").append(wxAccountId).toString();
	}

	/**
	 * Get URL of shop.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	private String getShopUrl(String wxAccountId) {
		String shopHomeUrl = MessageFormat.format(wlshopHome, wxAccountId);
		return new StringBuffer(wlWebSiteUrl).append(shopHomeUrl).toString();
	}

	/**
	 * This url is used to display sit content in weixin chat window.
	 * 
	 * @param wxAccountId
	 * @param contentId
	 *            Usually its sit id.
	 * @return
	 */
	private String getMwsContentUrl(String wxAccountId, String contentId) {
		String contentUrl = MessageFormat.format(wmsContentUrl, wxAccountId,
				contentId);
		return wlWebSiteUrl + contentUrl;
	}
}
