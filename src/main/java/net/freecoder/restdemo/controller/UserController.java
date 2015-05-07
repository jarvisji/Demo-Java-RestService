package net.freecoder.restdemo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.emailengine.EmailService;
import net.freecoder.emailengine.EmailTemplateEnum;
import net.freecoder.emailengine.vo.EmailTask;
import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.ServiceStatus;
import net.freecoder.restdemo.constant.UserStatusEnum;
import net.freecoder.restdemo.constant.UserType;
import net.freecoder.restdemo.dao.UserDao;
import net.freecoder.restdemo.exception.ServiceException;
import net.freecoder.restdemo.model.WlsUser;
import net.freecoder.restdemo.model.WlsUserPay;
import net.freecoder.restdemo.model.WlsWxAccount;
import net.freecoder.restdemo.service.UserService;
import net.freecoder.restdemo.util.DateTimeUtil;
import net.freecoder.restdemo.util.SecurityUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller handles URL of User resource.
 * 
 * @author JiTing
 */
@Controller
@RequestMapping("/user")
public class UserController extends CommonController {

	@Autowired
	private UserService userService;
	@Autowired
	private EmailController emailController;
	@Autowired
	EmailService emailService;
	@Autowired
	private UserDao userDao;

	@Value("#{sysconfProperties['wlc.url.website']}")
	private String wlWebSiteUrl;
	@Value("#{sysconfProperties['wlc.url.user.login']}")
	private String wlLoginUrl;

	/**
	 * Get user. GET: /user
	 * 
	 * @param id
	 * @return current logined user.
	 */
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getUser(HttpServletRequest request) {
		String userId = cred2UserId(getParamCred(request));
		WlsUser user = userService.getUser(userId);
		Result result = new Result();
		if (user != null) {
			result.setData(user).setCount(1, 1);
		} else {
			result.setSuccess(false);
			result.setErrMsg("用户不存在。");
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getUsers(HttpServletRequest request) {
		UserType cred2UserType = cred2UserType(getParamCred(request));
		if (!UserType.ADM.equals(cred2UserType)
				&& !UserType.CSR.equals(cred2UserType)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		List<WlsUser> users = userService.getUsers();
		return new Result().setEntity(users);
	}

	@RequestMapping(value = "/list/CSR", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getUsersByType(HttpServletRequest request) {
		UserType cred2UserType = cred2UserType(getParamCred(request));
		if (!UserType.ADM.equals(cred2UserType)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		List<WlsUser> csrs = userService.getCSRs();
		return new Result().setEntity(csrs);
	}

	/**
	 * Find user by email/mobile or else. Only ADM|CSR user has privilege to
	 * call this interface.
	 * 
	 * @param info
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/find", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getUsersByInfo(@RequestParam String info, HttpServletRequest request) {
		UserType cred2UserType = cred2UserType(getParamCred(request));
		if (!UserType.ADM.equals(cred2UserType)
				&& !UserType.CSR.equals(cred2UserType)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		SimpleExpression eqMobile = Restrictions.eq("mobilePhone", info);
		SimpleExpression eqEmail = Restrictions.eq("email", info);
		LogicalExpression exp = Restrictions.or(eqMobile, eqEmail);
		List<WlsUser> find = userDao.find(exp);
		return new Result().setEntity(find);
	}

	@RequestMapping(value = "/existence", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result checkUserExistence(@RequestParam String email) {
		WlsUser userByEmail = userService.getUserByEmail(email);
		Result result = new Result();
		if (userByEmail != null) {
			result.setErrCode(ErrorCode.USER_ALREADY_EXISTED);
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * Create user. POST: /user
	 * 
	 * @param Map
	 *            In User entity, add @jsonIgnore to "password" field, so we
	 *            cannot use User object here.
	 * @param result
	 * @param response
	 * @return
	 * @throws BindException
	 */
	@RequestMapping(method = RequestMethod.POST, headers = "Content-Type=application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody
	Result createUser(@RequestBody Map<String, String> userInfo) {
		if (userInfo == null || !userInfo.containsKey("email")
				|| !userInfo.containsKey("password")) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"Invalid regiestration information.");
		}
		WlsUser user = new WlsUser();
		user.setEmail(userInfo.get("email"));
		user.setPassword(userInfo.get("password"));
		user.setFullName(userInfo.get("fullName"));
		user.setMobilePhone(userInfo.get("mobilePhone"));
		user.setQq(userInfo.get("qq"));
		user.setCityCode(userInfo.get("area"));
		user.setIndustry(userInfo.get("industry"));
		user.setStatus(UserStatusEnum.PENDING_STATUS_AUDIT.value());
		user.setUserType(UserType.USER.toString());
		Result result = new Result();
		try {
			userService.createUser(user);
		} catch (ServiceException e) {
			result.setSuccess(false).setErrMsg(e.getMessage());
			return result;
		}
		return result.setData(user);
	}

	/**
	 * POST /user/CSR
	 * 
	 * Only ADM user has the privilege to create CSR account. If the email
	 * doesn't exist, create new account, otherwise, update userType.
	 * 
	 * @param userInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/CSR", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createUserCSR(@RequestBody Map<String, String> userInfo,
			HttpServletRequest request) {
		if (userInfo == null || !userInfo.containsKey("email")) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"Invalid regiestration information.");
		}
		String paramCred = getParamCred(request);
		UserType cred2UserType = cred2UserType(paramCred);
		if (!UserType.ADM.equals(cred2UserType)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}

		String email = userInfo.get("email");
		WlsUser userByEmail = userService.getUserByEmail(email);
		Result result = new Result();
		if (userByEmail == null) {
			WlsUser user = new WlsUser();
			user.setEmail(email);
			// CSR default password is 'pass'.
			user.setPassword(SecurityUtil.sha1Encode("pass"));
			user.setFullName(userInfo.get("fullName"));
			user.setUserType(UserType.CSR.toString());
			user.setStatus(UserStatusEnum.NORMAL_STATUS_ACTIVATED.value());
			try {
				userService.createUser(user);
				result.setEntity(user);
			} catch (ServiceException e) {
				result.setSuccess(false).setErrMsg(e.getMessage());
				return result;
			}
		} else {
			userByEmail.setUserType(UserType.CSR.toString());
			userService.updateUser(userByEmail);
			result.setEntity(userByEmail);
		}
		return result;
	}

	@RequestMapping(value = "/CSR", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateUserCSR(@RequestBody WlsUser user, HttpServletRequest request) {
		String paramCred = getParamCred(request);
		UserType cred2UserType = cred2UserType(paramCred);
		if (!UserType.ADM.equals(cred2UserType)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		userService.updateUser(user);
		Result result = new Result();
		return result;
	}

	@RequestMapping(value = "/CSR/{userId}", method = RequestMethod.DELETE, headers = "Content-Type=application/json")
	public @ResponseBody
	Result deleteUserCSR(@PathVariable String userId, HttpServletRequest request) {
		String paramCred = getParamCred(request);
		UserType cred2UserType = cred2UserType(paramCred);
		if (!UserType.ADM.equals(cred2UserType)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		userService.deleteUser(userId);
		Result result = new Result();
		return result;
	}

	/**
	 * Update WlsUser. Only can update current logined user.
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateUser(@RequestBody WlsUser user, HttpServletRequest request) {
		String paramCred = getParamCred(request);
		String userId = cred2UserId(paramCred);
		if (userId.equals(user.getId())) {
			userService.updateUser(user);
			return new Result();
		} else {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
	}

	@RequestMapping(value = "{userId}/status/{status}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateUserStatus(@PathVariable String userId,
			@PathVariable String status, HttpServletRequest request) {
		String paramCred = getParamCred(request);
		UserType cred2UserType = cred2UserType(paramCred);
		Result result = new Result();

		if (!UserType.ADM.equals(cred2UserType)
				&& !UserType.CSR.equals(cred2UserType)) {
			return result.setErrCode(ErrorCode.NO_PRIVILIEGE);
		}

		UserStatusEnum newStatus = null;
		try {
			newStatus = UserStatusEnum.parseValue(status);
		} catch (Exception e) {
			return result.setErrCode(ErrorCode.INVALID_DATA);
		}

		WlsUser user = userService.getUser(userId);
		if (user == null) {
			return result.setErrCode(ErrorCode.NOT_FOUND);
		} else {
			UserStatusEnum originalStatus = UserStatusEnum.parseValue(user
					.getStatus());
			user.setStatus(newStatus.value());
			userService.updateUser(user);
			sendEmail4StatusChange(originalStatus, newStatus, user);
			return result;
		}
	}

	private void sendEmail4StatusChange(UserStatusEnum orginalStatus,
			UserStatusEnum newStatus, WlsUser user) {
		if (UserStatusEnum.PENDING_STATUS_AUDIT.equals(orginalStatus)) {
			if (UserStatusEnum.NORMAL_STATUS_ACTIVATED.equals(newStatus)) {
				EmailTask emailTask = emailService.createEmailTask(
						EmailTemplateEnum.EMAIL_REGISTER_AUDIT_SUCCESS,
						user.getEmail());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(
						"userName",
						(user.getFullName() == null || user.getFullName()
								.isEmpty()) ? user.getEmail() : user
								.getFullName());
				params.put("loginUrl", wlWebSiteUrl + wlLoginUrl);
				params.put("currentDate", DateTimeUtil.getCSTDateString());
				emailService.sendEmail(emailTask, params);
			} else if (UserStatusEnum.PENDING_STATUS_INACTIVE.equals(newStatus)) {
				EmailTask emailTask = emailService.createEmailTask(
						EmailTemplateEnum.EMAIL_REGISTER_AUDIT_FAILURE,
						user.getEmail());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(
						"userName",
						(user.getFullName() == null || user.getFullName()
								.isEmpty()) ? user.getEmail() : user
								.getFullName());
				params.put("currentDate", DateTimeUtil.getCSTDateString());
				emailService.sendEmail(emailTask, params);
			}
		}
	}

	@RequestMapping(value = "/pwd", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody
	Result changePassword(@RequestBody HashMap<String, String> data,
			HttpServletRequest request) {
		String cred = getParamCred(request);
		String userId = cred2UserId(cred);
		String oldPass = data.get("oldPass");
		String newPass = data.get("newPass");
		if (oldPass == null || newPass == null) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"Password cannot be null.");
		} else if (oldPass.equals(newPass)) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"New and old passwords are same.");
		}
		WlsUser user = userService.getUser(userId);
		if (!user.getPassword().equals(oldPass)) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"Incorrect old password.");
		} else {
			userService.updatePassword(userId, newPass);
			return new Result();
		}
	}

	@RequestMapping(value = "/pwd/{emailCode}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody
	Result changePasswordByEmail(@RequestBody HashMap<String, String> data,
			@PathVariable String emailCode) {
		Result result = emailController.verifyEmailCode(emailCode);
		if (result == null) {
			result = new Result();
			String newPass = data.get("newPass");
			if (newPass != null) {
				String userId = emailController.emailCode2userId(emailCode);
				userService.updatePassword(userId, newPass);
			} else {
				result.setSuccess(false).setErrMsg("密码无效[newPass]。");
			}
		}
		return result;
	}

	/**
	 * 获取用户绑定的微信公众号列表：GET /user/wxaccounts
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/wxaccounts", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getUserWxAccounts(HttpServletRequest request) {
		String userId = cred2UserId(getParamCred(request));
		List<WlsWxAccount> wxAccounts = userService.getWxAccounts(userId);
		int count = wxAccounts.size();
		Result result = new Result();
		result.setData(wxAccounts);
		result.setCount(count, count);
		return result;
	}

	/**
	 * 创建用户绑定的微信公众号：POST /user/wxaccount
	 * 
	 * @param userId
	 * @param wxAccount
	 * @return
	 */
	@RequestMapping(value = "/wxaccount", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createWxAccount(@RequestBody WlsWxAccount wxAccount,
			HttpServletRequest request) {
		String cred = getParamCred(request);
		String userId = cred2UserId(cred);
		userService.createWxAccount(userId, wxAccount);
		// Save new binded wxAccountId to credMap. id already generated after
		// calling userService.createWxAccount().
		getUserCred(cred).setData_WxAccountId(wxAccount.getId());

		Result result = new Result();
		result.setData(wxAccount);
		return result;
	}

	/**
	 * 获取用户绑定的微信公众号：GET /user/wxaccount/<accountId>
	 * 
	 * @param accountId
	 * @return
	 */
	@RequestMapping(value = "/wxaccount/{accountId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getWxAccount(@PathVariable String accountId,
			HttpServletRequest request) {
		String userId = cred2UserId(getParamCred(request));

		WlsWxAccount wxAccount = userService.getWxAccount(accountId);
		if (!wxAccount.getWlsUserId().equals(userId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		Result result = new Result();
		result.setData(wxAccount);
		return result;
	}

	/**
	 * 删除用户绑定的微信公众号：DELETE /user/wxaccount/<accountId>
	 * 
	 * @param accountId
	 * @return
	 */
	@RequestMapping(value = "/wxaccount/{accountId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteWxAccount(@PathVariable String accountId) {
		userService.deleteWxAccount(accountId);
		Result result = new Result();
		return result;
	}

	/**
	 * 更新用户绑定的微信公众号：PUT /user/wxaccount/<accountId>
	 * 
	 * @param accountId
	 * @param wxAccount
	 * @return
	 */
	@RequestMapping(value = "/wxaccount/{accountId}", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateWxAccount(@PathVariable String accountId,
			@RequestBody WlsWxAccount wxAccount) {
		wxAccount.setId(accountId);
		userService.updateWxAccount(wxAccount);
		Result result = new Result();
		return result;
	}

	@RequestMapping(value = "/{userId}/pay/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getPayList(@PathVariable String userId, HttpServletRequest request) {
		String paramCred = getParamCred(request);
		String cred2UserId = cred2UserId(paramCred);
		UserType cred2UserType = cred2UserType(paramCred);
		if (UserType.ADM.equals(cred2UserType)
				|| UserType.CSR.equals(cred2UserType)
				|| userId.equals(cred2UserId)) {
			List<WlsUserPay> userPays = userDao.getUserPays(userId);
			return new Result().setEntity(userPays);
		} else {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
	}

	@RequestMapping(value = "/{userId}/pay", method = RequestMethod.POST, headers = "Content-Type=application/json")
	public @ResponseBody
	Result createUserPay(@PathVariable String userId,
			@RequestBody WlsUserPay userPay, HttpServletRequest request) {
		String paramCred = getParamCred(request);
		String cred2UserId = cred2UserId(paramCred);
		UserType cred2UserType = cred2UserType(paramCred);
		if (UserType.ADM.equals(cred2UserType)
				|| UserType.CSR.equals(cred2UserType)
				|| userId.equals(cred2UserId)) {
			try {
				ServiceStatus.valueOf(userPay.getServiceStatus());
			} catch (Exception e) {
				return new Result().setErrCode(ErrorCode.INVALID_DATA);
			}
			userPay.setId(UUIDUtil.uuid8());
			userPay.setOperatorId(cred2UserId);
			userDao.createUserPay(userPay);

			saveWlsLog(
					null,
					userPay,
					UserType.ADM.equals(cred2UserType) ? UserType.ADM
							.toString() : cred2UserId);

			return new Result().setEntity(userPay);
		} else {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
	}

	@RequestMapping(value = "/{userId}/pay", method = RequestMethod.PUT, headers = "Content-Type=application/json")
	public @ResponseBody
	Result updateUserPay(@PathVariable String userId,
			@RequestBody WlsUserPay userPay, HttpServletRequest request) {
		WlsUserPay lastUserPay = userDao.getLastUserPay(userId);
		String paramCred = getParamCred(request);
		String cred2UserId = cred2UserId(paramCred);
		UserType cred2UserType = cred2UserType(paramCred);
		if (UserType.ADM.equals(cred2UserType)
				|| UserType.CSR.equals(cred2UserType)
				|| userId.equals(cred2UserId)) {
			try {
				ServiceStatus.valueOf(userPay.getServiceStatus());
			} catch (Exception e) {
				return new Result().setErrCode(ErrorCode.INVALID_DATA);
			}
			userPay.setOperatorId(cred2UserId);
			userDao.updateUserPay(userPay);

			saveWlsLog(
					lastUserPay,
					userPay,
					UserType.ADM.equals(cred2UserType) ? UserType.ADM
							.toString() : cred2UserId);

			return new Result();
		} else {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
	}
}
