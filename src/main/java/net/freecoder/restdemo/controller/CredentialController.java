/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.UserStatusEnum;
import net.freecoder.restdemo.constant.UserType;
import net.freecoder.restdemo.dao.UserDao;
import net.freecoder.restdemo.model.WlsUser;
import net.freecoder.restdemo.model.WlsUserPay;
import net.freecoder.restdemo.model.WlsWxAccount;
import net.freecoder.restdemo.service.UserService;
import net.freecoder.restdemo.util.UUIDUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/credential")
public class CredentialController extends CommonController {

	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao;

	@Value("#{sysconfProperties['admin.email']}")
	private String gAdminEmail;
	@Value("#{sysconfProperties['admin.password']}")
	private String gAdminPassword;

	/**
	 * Generate credential of user, usually they are business user.
	 * 
	 * POST: /credential
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result generateCredential(@RequestParam("email") String email,
			@RequestParam("password") String password,
			HttpServletRequest request) {
		String paramRole = request.getParameter("role");
		Result result = null;
		if (paramRole == null) {
			result = userLogin(email, password);
		} else {
			UserType userType = null;
			try {
				userType = UserType.valueOf(paramRole);
			} catch (IllegalArgumentException e) {
				result = new Result().setErrCode(ErrorCode.INVALID_PARAM);
			}
			if (UserType.ADM.equals(userType)) {
				result = adminLogin(email, password);
			} else if (UserType.CSR.equals(userType)) {
				result = csrLogin(email, password);
			}
		}
		return result;
	}

	private Result userLogin(String email, String password) {
		Result result = new Result();
		WlsUser dbUser = userService.getUser(email, password);
		if (dbUser == null) {
			result.setSuccess(false);
			result.setErrCode(ErrorCode.AUTH_ERROR);
			result.setErrMsg("用户名或密码错误。");
		} else {
			if (dbUser.getStatus() != null
					&& dbUser.getStatus() == UserStatusEnum.NORMAL_STATUS_ACTIVATED
							.value()) {
				String credential = userService.getCredential(email, password);
				String userId = dbUser.getId();
				result.setData("userId", userId);
				result.setData("cred", credential);
				// save userId, credential, and binded wxAccountIds to map.
				TimelyCred savedCred = saveUserCred(userId, credential);
				List<WlsWxAccount> wxAccounts = userService
						.getWxAccounts(userId);
				for (WlsWxAccount account : wxAccounts) {
					savedCred.setData_WxAccountId(account.getId());
				}
				// get user modules and store in cred.
				WlsUserPay lastUserPay = userDao.getLastUserPay(userId);
				if (lastUserPay != null && lastUserPay.getPackageType() != null) {
					List<String> modules = Arrays.asList(lastUserPay
							.getPackageType().split("\\|"));
					savedCred.setModules(modules);
					savedCred
							.setServiceEndDate(lastUserPay.getServerStopTime());
					result.setData("modules", modules);
					result.setData("serviceEndDate",
							lastUserPay.getServerStopTime());
					result.setData("serviceStatus",
							lastUserPay.getServiceStatus());
					if (System.currentTimeMillis() >= lastUserPay
							.getServerStopTime().getTime()) {
						result.setErrCode(ErrorCode.SERVICE_EXPIRED);
					}
				}
			} else {
				result.setSuccess(false).setErrMsg(
						"Cannot login, invalid status.");
				result.setData("userStatus", dbUser.getStatus());
			}
		}
		return result;
	}

	private Result adminLogin(String email, String password) {
		Result result = new Result();
		if (email.equals(gAdminEmail) && password.equals(gAdminPassword)) {
			String credential = userService.getCredential(email, password);
			String userId = UUIDUtil.uuid8();
			saveUserCred(userId, credential, UserType.ADM);
			result.setData("userId", userId);
			result.setData("cred", credential);
			result.setData("role", UserType.ADM.toString());
		} else {
			result.setErrCode(ErrorCode.AUTH_ERROR);
		}
		return result;
	}

	private Result csrLogin(String email, String password) {
		Result result = new Result();
		WlsUser dbUser = userService.getUser(email, password, UserType.CSR);
		if (dbUser == null) {
			result.setErrCode(ErrorCode.AUTH_ERROR);
			result.setErrMsg("用户名或密码错误。");
		} else {
			String credential = userService.getCredential(email, password);
			String userId = dbUser.getId();
			result.setData("userId", userId);
			result.setData("cred", credential);
			result.setData("role", UserType.CSR.toString());
			saveUserCred(userId, credential, UserType.CSR);
		}
		return result;
	}

	@RequestMapping(value = "/{cred}", method = RequestMethod.DELETE, headers = "Content-Type=application/json")
	public @ResponseBody
	Result deleteCredential(@PathVariable String cred) {
		removeUserCred(cred);
		return new Result();
	}
}
