/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.emailengine.EmailService;
import net.freecoder.emailengine.EmailStatusEnum;
import net.freecoder.emailengine.EmailTemplateEnum;
import net.freecoder.emailengine.TemplateService;
import net.freecoder.emailengine.dao.EmailEngineDao;
import net.freecoder.emailengine.exception.EmailEngineException;
import net.freecoder.emailengine.vo.EmailTask;
import net.freecoder.restdemo.constant.UserStatusEnum;
import net.freecoder.restdemo.dao.UserDao;
import net.freecoder.restdemo.model.WlsUser;
import net.freecoder.restdemo.util.DateTimeUtil;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for email service.
 * 
 * @author JiTing
 */
@Controller
@RequestMapping("/email")
public class EmailController extends CommonController {

	private static Logger logger = LoggerFactory
			.getLogger(EmailController.class);

	@Autowired
	UserDao userDao;
	@Autowired
	EmailEngineDao emailEngineDao;
	@Autowired
	EmailService emailService;
	@Autowired
	TemplateService templateService;

	@Value("#{sysconfProperties['wlc.url.website']}")
	private String wlWebSiteUrl;
	@Value("#{sysconfProperties['wlc.url.user.activate']}")
	private String wlUserActivateUrl;
	@Value("#{sysconfProperties['wlc.url.user.retrievepassword']}")
	private String wlRetrievePasswordUrl;
	@Value("#{sysconfProperties['wlc.mail.expire.millis']}")
	private String wlEmailExpireMillis;

	/**
	 * Send email.
	 * 
	 * @param accountId
	 * @return
	 */
	@RequestMapping(value = "/registerconfirm/{emailAddr}/", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result registerConfirm(@PathVariable String emailAddr,
			HttpServletRequest request) {
		logger.debug("Received request for email: {}", emailAddr);
		Result result = new Result();
		SimpleExpression expEmail = Restrictions.eq("email", emailAddr);
		List<WlsUser> findUser = userDao.find(expEmail);
		if (findUser == null || findUser.size() == 0) {
			result.setSuccess(false);
			result.setErrMsg("此邮件地址未注册。");
		} else {
			WlsUser wlsUser = findUser.get(0);
			EmailTask emailTask = emailService.createEmailTask(
					EmailTemplateEnum.EMAIL_REGISTER_CONFIRM,
					wlsUser.getEmail());

			Map<String, Object> params = new HashMap<String, Object>();
			params.put(
					"regConfirmUrl",
					generateEmailUrl(EmailTemplateEnum.EMAIL_REGISTER_CONFIRM,
							wlsUser, emailTask));
			params.put("wlWebsiteUrl", wlWebSiteUrl);
			params.put("currentDate", DateTimeUtil.getCSTDateString());

			emailService.sendEmail(emailTask, params);
		}
		return result;
	}

	@RequestMapping(value = "/retrievepassword/{emailAddr}/", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result retrievePassword(@PathVariable String emailAddr,
			HttpServletRequest request) {
		logger.debug("Received request for email: {}", emailAddr);
		Result result = new Result();
		SimpleExpression expEmail = Restrictions.eq("email", emailAddr);
		List<WlsUser> findUser = userDao.find(expEmail);
		if (findUser == null || findUser.size() == 0) {
			result.setSuccess(false);
			result.setErrMsg("电子邮件不存在。");
		} else {
			EmailTask emailTask = emailService.createEmailTask(
					EmailTemplateEnum.EMAIL_RETRIEVE_PASSWORD, emailAddr);

			WlsUser wlsUser = findUser.get(0);
			Map<String, Object> params = new HashMap<String, Object>();
			if (wlsUser.getUsername() == null) {
				params.put("userDisplayName", wlsUser.getEmail());
			} else {
				params.put("userDisplayName", wlsUser.getUsername());
			}
			params.put(
					"retrievePasswordUrl",
					generateEmailUrl(EmailTemplateEnum.EMAIL_RETRIEVE_PASSWORD,
							wlsUser, emailTask));
			params.put("wlWebsiteUrl", wlWebSiteUrl);
			params.put("currentDate", DateTimeUtil.getCSTDateString());

			emailService.sendEmail(emailTask, params);
		}
		return result;
	}

	@RequestMapping(value = "/verify/{verifyStr}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result verify(@PathVariable String verifyStr, HttpServletRequest request) {
		Result result = verifyEmailCode(verifyStr);
		if (result == null) {
			result = new Result();
			String userId = emailCode2userId(verifyStr);
			String emailTaskId = emailCode2emailTaskId(verifyStr);

			EmailTask emailTask = emailEngineDao.getEmailTask(emailTaskId);

			handlerTaskLogic(emailTask, userDao.get(userId));

			result.setData("userId", userId);
			result.setData("type", emailTask.getTemplateName());
		}
		return result;
	}

	public Result verifyEmailCode(String verifyStr) {
		logger.debug("verifing email: verification string: {}", verifyStr);
		Result result = new Result();
		verifyStr = verifyStr.trim();
		if (verifyStr == null || verifyStr.length() != 16) {
			logger.error(
					"verifing email: verify error, null or wrong length: {}",
					verifyStr);
			return result.setSuccess(false).setErrMsg("对不起，链接无效。");
		}

		String userId = emailCode2userId(verifyStr);
		String emailTaskId = emailCode2emailTaskId(verifyStr);
		logger.debug("verifing email: parsed userId: {}, emailTaskId: {}",
				userId, emailTaskId);

		EmailTask emailTask = emailEngineDao.getEmailTask(emailTaskId);
		if (emailTask == null) {
			logger.error(
					"verifing email: verify error, cannot retrieve emailTask by id: {}",
					emailTaskId);
			return result.setSuccess(false).setErrMsg("对不起，链接无效。");
		}

		EmailStatusEnum emailStatus = EmailStatusEnum.parseValue(emailTask
				.getStatus());
		if (!emailStatus.equals(EmailStatusEnum.SENT_SUCCESS)) {
			logger.error(
					"verifing email: verify error, wrong email status: {}",
					emailStatus.name());
			return result.setSuccess(false).setErrMsg("对不起，链接无效。");
		}

		String toEmail = emailTask
				.getEmailTaskAttr(EmailService.EMAILTASK_ATTRNAME_TO);
		SimpleExpression expEmail = Restrictions.eq("email", toEmail);
		List<WlsUser> findUser = userDao.find(expEmail);
		if (findUser.size() == 0) {
			logger.error(
					"verifing email: verify error, user doesn't exist which the email was sent before: {}",
					toEmail);
			return result.setSuccess(false).setErrMsg("对不起，链接无效。");
		} else {
			String task4userId = findUser.get(0).getId();
			if (!task4userId.equals(userId)) {
				logger.error(
						"verifing email: verify error, userId not match. userId in email link: {}, between userId of the email sent to: {}",
						userId, task4userId);
				return result.setSuccess(false).setErrMsg("对不起，链接无效。");
			}
		}

		if (isExpired(emailTask)) {
			logger.error("verifing email: verify error, expired.");
			return result.setSuccess(false).setErrMsg("对不起，链接已超时。");
		}

		logger.info("verifing email: verify successful.");
		return null;
	}

	public String emailCode2emailTaskId(String verifyStr) {
		String ret = "";
		if (verifyStr != null && verifyStr.length() == 16) {
			ret = verifyStr.substring(8, 16);
		}
		return ret;
	}

	public String emailCode2userId(String verifyStr) {
		String ret = "";
		if (verifyStr != null && verifyStr.length() == 16) {
			ret = verifyStr.substring(0, 8);
		}
		return ret;
	}

	private void handlerTaskLogic(EmailTask emailTask, WlsUser wlsUser) {
		EmailTemplateEnum emailTempalte = EmailTemplateEnum.valueOf(emailTask
				.getTemplateName());
		if (EmailTemplateEnum.EMAIL_REGISTER_CONFIRM.equals(emailTempalte)) {
			wlsUser.setStatus(UserStatusEnum.NORMAL_STATUS_ACTIVATED.value());
			userDao.update(wlsUser);
			emailTask.setStatus(EmailStatusEnum.USER_CONFIRMED.value());
			emailEngineDao.update(emailTask);
		}
	}

	private boolean isExpired(EmailTask emailTask) {
		long sentTime = emailTask.getSentTime().getTime();
		long currentTime = System.currentTimeMillis();
		return (currentTime - sentTime) > Long.parseLong(wlEmailExpireMillis);
	}

	/**
	 * Current method to generate verification string is userId + emailTaskId.
	 * They are all 8 chars length string. When verifing, split the string to
	 * two parts, the
	 * 
	 * @param user
	 */
	private String generateEmailUrl(EmailTemplateEnum template, WlsUser user,
			EmailTask emailTask) {
		if (user.getId() == null || emailTask.getId() == null) {
			logger.error(
					"Generate registration confirm url failed, both user and email task id cannot be null. userId:{}, emailTaskId:{}",
					user.getId(), emailTask.getId());
			throw new EmailEngineException(
					"Generate registration confirm url failed. Please check error log.");
		}
		StringBuffer sb = new StringBuffer(wlWebSiteUrl);
		if (EmailTemplateEnum.EMAIL_REGISTER_CONFIRM.equals(template)) {
			sb.append(wlUserActivateUrl);
		} else if (EmailTemplateEnum.EMAIL_RETRIEVE_PASSWORD.equals(template)) {
			sb.append(wlRetrievePasswordUrl);
		} else {
			return sb.toString();
		}
		sb.append("?").append(user.getId()).append(emailTask.getId());
		return sb.toString();
	}
}
