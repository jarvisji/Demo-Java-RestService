/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.UserType;
import net.freecoder.restdemo.dao.WlsLogDao;
import net.freecoder.restdemo.exception.ServiceException;
import net.freecoder.restdemo.model.WlsLog;
import net.freecoder.restdemo.util.DateTimeUtil;
import net.freecoder.restdemo.util.JsonUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * For common functions for controller.
 * 
 * @author JiTing
 * @param <idField>
 */
public class CommonController {

	private static Logger logger = LoggerFactory
			.getLogger(CommonController.class);

	private static Map<String, TimelyCred> userCredMap = new HashMap<String, TimelyCred>();
	private static Map<String, TimelyCred> vipCredMap = new HashMap<String, TimelyCred>();

	@Autowired
	WlsLogDao wlsLogDao;

	public class TimelyCred {
		private String userId;
		private String cred;
		private long expireTime;
		private UserType userType;
		private List<String> modules;
		private Date serviceEndDate;
		private Map<String, Object> data = new HashMap<String, Object>();
		// expire in 2 hours
		private final static long EXPIRE = 7200000;
		private final static String DATA_KEY_WXACCOUNTIDS = "wxAccountIds";

		public TimelyCred(String userId, String cred) {
			this.userId = userId;
			this.cred = cred;
			this.expireTime = System.currentTimeMillis() + EXPIRE;
		}

		/**
		 * @return the userId
		 */
		public String getUserId() {
			return userId;
		}

		/**
		 * @return the cred
		 */
		public String getCred() {
			return cred;
		}

		/**
		 * @return the expireTime
		 */
		public long getExpireTime() {
			return expireTime;
		}

		public Object getData(String key) {
			return this.data.get(key);
		}

		public void setData(String key, Object value) {
			this.data.put(key, value);
		}

		public boolean checkCred(String cred) {
			return this.cred.equals(cred);
		}

		public boolean isExpired(long curTs) {
			autoRenew(curTs);
			return this.expireTime < curTs;
		}

		public UserType getUserType() {
			return userType;
		}

		public void setUserType(UserType userType) {
			this.userType = userType;
		}

		public List<String> getModules() {
			return modules;
		}

		public void setModules(List<String> modules) {
			this.modules = modules;
		}

		public Date getServiceEndDate() {
			return serviceEndDate;
		}

		public void setServiceEndDate(Date serviceEndDate) {
			this.serviceEndDate = serviceEndDate;
		}

		/**
		 * Auto renew user credential, avoid force user re-login every 2 hours.
		 * If a given timestamp is not expried, but the time duration to expire
		 * time is less then half of EXPIRE, renew it.
		 * 
		 * @param curTs
		 */
		private void autoRenew(long curTs) {
			long duration = this.expireTime - curTs;
			long halfExpireTime = EXPIRE / 2;
			if (duration > 0 && duration < halfExpireTime) {
				logger.debug(
						"cred {} will expire in {} milliseconds, less than {}, renew it.",
						this.cred, duration, halfExpireTime);
				this.expireTime = curTs + EXPIRE;
			}
		}

		@SuppressWarnings("unchecked")
		public void setData_WxAccountId(String wxAccountId) {
			if (this.getData(DATA_KEY_WXACCOUNTIDS) == null) {
				this.setData(DATA_KEY_WXACCOUNTIDS, new HashSet<String>());
			}
			((Set<String>) this.getData(DATA_KEY_WXACCOUNTIDS))
					.add(wxAccountId);
		}

		@SuppressWarnings("unchecked")
		public Set<String> getData_WxAccountIds() {
			return (Set<String>) this.getData(DATA_KEY_WXACCOUNTIDS);
		}
	}

	/**
	 * When user login, should create new TimelyCred object and save for later
	 * verify. <br>
	 * This solution only work on single server node.Consider to distribution,
	 * should save it in cache server.
	 * 
	 * @param userId
	 * @param cred
	 * @return TimelyCred Set extend data if need.
	 */
	protected TimelyCred saveUserCred(String userId, String cred) {
		TimelyCred timelyCred = new TimelyCred(userId, cred);
		userCredMap.put(cred, timelyCred);
		return timelyCred;
	}

	protected TimelyCred saveUserCred(String userId, String cred,
			UserType userType) {
		TimelyCred timelyCred = new TimelyCred(userId, cred);
		timelyCred.setUserType(userType);
		userCredMap.put(cred, timelyCred);
		return timelyCred;
	}

	protected TimelyCred saveVipCred(String userId, String cred) {
		TimelyCred timelyCred = new TimelyCred(userId, cred);
		vipCredMap.put(cred, timelyCred);
		return timelyCred;
	}

	protected void removeUserCred(String cred) {
		userCredMap.remove(cred);
	}

	protected void removeVipCred(String cred) {
		vipCredMap.remove(cred);
	}

	/**
	 * @deprecated use cred2UserId(getParamCred()) to instead. Get URL parameter
	 *             "u", its id of current logined user.
	 * 
	 * @param request
	 * @return
	 */
	protected String getParamUserId(HttpServletRequest request) {
		String userId = request.getParameter("u");
		if (userId == null) {
			throw new ServiceException("User id missed.");
		}
		return userId;
	}

	/**
	 * Get URL parameter "c", its cred of current login user.
	 * 
	 * @param request
	 * @return
	 */
	public String getParamCred(HttpServletRequest request) {
		return getParamCred(request, false);
	}

	public String getParamCred(HttpServletRequest request, boolean allowNull) {
		String cred = request.getParameter("c");
		if (!allowNull && cred == null) {
			throw new ServiceException("Cred missed.");
		}
		return cred;
	}

	/**
	 * @deprecated use cred2AccountId(getParamCred()) to instead. Get URL
	 *             parameter "a", its id of current selected weixin account.
	 * 
	 * @param request
	 * @return
	 */
	protected String getParamAccountId(HttpServletRequest request) {
		String wxAccountId = request.getParameter("a");
		if (wxAccountId == null) {
			throw new ServiceException("WeiXin account id missed.");
		}
		return wxAccountId;
	}

	/**
	 * Verify cred is valid and make sure it is not expired before get detail
	 * data.
	 * 
	 * @param cred
	 * @return TimelyCred contains user data.
	 */
	private TimelyCred checkCred(Map<String, TimelyCred> credMap, String cred) {
		if (credMap.containsKey(cred)) {
			TimelyCred timelyCred = credMap.get(cred);
			if (timelyCred.isExpired(System.currentTimeMillis())) {
				throw new ServiceException(ErrorCode.CRED_EXPIRED.toString());
			}
			return timelyCred;
		} else {
			return null;
		}
	}

	public TimelyCred getUserCred(String cred) {
		if (!userCredMap.containsKey(cred)) {
			throw new ServiceException(ErrorCode.INVALID_CRED.toString());
		}
		return userCredMap.get(cred);
	}

	protected TimelyCred getVipCred(String cred) {
		return vipCredMap.get(cred);
	}

	/**
	 * Get user id by cred.
	 * 
	 * @param cred
	 *            String
	 * @return uesrId String
	 */
	public String cred2UserId(String cred) {
		return cred2UserId(cred, /* isRetNullAcceptable */false);
	}

	public String cred2UserId(String cred, boolean isRetNullAcceptable) {
		TimelyCred checkCred = checkCred(userCredMap, cred);
		if (checkCred != null) {
			return checkCred.getUserId();
		} else {
			if (isRetNullAcceptable)
				return null;
			else
				throw new ServiceException(ErrorCode.INVALID_CRED.name());
		}
	}

	public UserType cred2UserType(String cred) {
		return cred2UserType(cred, /* isRetNullAcceptable */false);
	}

	public UserType cred2UserType(String cred, boolean isRetNullAcceptable) {
		TimelyCred checkCred = checkCred(userCredMap, cred);
		if (checkCred != null) {
			return checkCred.getUserType();
		} else {
			if (isRetNullAcceptable)
				return null;
			else
				throw new ServiceException(ErrorCode.INVALID_CRED.name());
		}
	}

	/**
	 * Get vip id by cred.
	 * 
	 * @param cred
	 * @return
	 */
	public String cred2VipId(String cred) {
		return cred2VipId(cred,/* isRetNullAcceptable */false);
	}

	public String cred2VipId(String cred, boolean isRetNullAcceptable) {
		TimelyCred checkCred = checkCred(vipCredMap, cred);
		if (checkCred != null) {
			return checkCred.getUserId();
		} else {
			if (isRetNullAcceptable)
				return null;
			else
				throw new ServiceException(ErrorCode.INVALID_CRED.name());
		}
	}

	/**
	 * Get wxAccountId by cred. <br>
	 * This method will not retrieve data from database, the wxAccountId must be
	 * set when user login.
	 * 
	 * @param cred
	 * @return wxAccountId
	 */
	protected Set<String> cred2AccountIds(String cred) {
		TimelyCred checkCred = checkCred(userCredMap, cred);
		if (checkCred != null) {
			return checkCred.getData_WxAccountIds();
		} else {
			return null;
		}

	}

	/**
	 * Credential is identity of user, a user may bind multiple weixin accounts.
	 * This method is to verify a wxAccountId is belongs to the user(identify by
	 * credential) or not.
	 * 
	 * @param wxAccountId
	 * @param cred
	 * @return
	 */
	protected boolean validateAccountIdWithCred(String wxAccountId, String cred) {
		TimelyCred checkCred = checkCred(userCredMap, cred);
		if (checkCred != null) {
			Set<String> data_WxAccountIds = checkCred.getData_WxAccountIds();
			return data_WxAccountIds.contains(wxAccountId);
		} else {
			return false;
		}
	}

	/**
	 * Get the begin date of the time range by given date.
	 * 
	 * @param date
	 * @param timeRange
	 *            Valid value is day|week|month.
	 * @return
	 */
	protected Date getRangeBeginDate(Date date, String timeRange) {
		Date ret = null;
		if ("day".equals(timeRange)) {
			ret = DateTimeUtil.getDayBegin(date);
		} else if ("week".equals(timeRange)) {
			ret = DateTimeUtil.getWeekBegin(date);
		} else if ("month".equals(timeRange)) {
			ret = DateTimeUtil.getMonthBegin(date);
		}
		return ret;
	}

	/**
	 * Get the end date of the time range by given date.
	 * 
	 * @param date
	 * @param timeRange
	 *            Valid value is day|week|month.
	 * @return
	 */
	protected Date getRangeEndDate(Date date, String timeRange) {
		Date ret = null;
		if ("day".equals(timeRange)) {
			ret = DateTimeUtil.getDayEnd(date);
		} else if ("week".equals(timeRange)) {
			ret = DateTimeUtil.getWeekEnd(date);
		} else if ("month".equals(timeRange)) {
			ret = DateTimeUtil.getMonthEnd(date);
		}
		return ret;
	}

	/**
	 * Handle Exceptions occurred in controllers, return Result object to
	 * client.
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public @ResponseBody
	Result exceptionHandler(Exception exception) {
		String errMsg = exception.getMessage();
		Result result = new Result();
		if (errMsg != null) {
			try {
				result.setErrCode(ErrorCode.valueOf(exception.getMessage()));
			} catch (IllegalArgumentException e) {
				result.setErrCode(ErrorCode.SERVICE_ERROR).setErrMsg(
						exception.getMessage());
			}
		} else {
			result.setErrCode(ErrorCode.SERVICE_ERROR).setErrMsg(
					exception.getClass().getSimpleName());
		}
		return result;
	}

	protected void saveWlsLog(Object oldEntity, Object newEntity,
			String operatorId) {
		// All service entity are implemented Serializable interface.
		if (newEntity == null || !(newEntity instanceof Serializable)) {
			logger.warn("Skip write WlsLog since newEntity is not instance of Serializable.");
			return;
		}
		if (oldEntity != null && !(oldEntity instanceof Serializable)) {
			logger.warn("Skip write WlsLog since oldEntity is not instance of Serializable.");
			return;
		}
		WlsLog wlsLog = new WlsLog();
		wlsLog.setId(UUIDUtil.uuid8());
		wlsLog.setNewValue(JsonUtil.toString(newEntity));
		if (oldEntity != null) {
			wlsLog.setOldValue(JsonUtil.toString(oldEntity));
		}
		wlsLog.setOperatorId(operatorId);
		wlsLog.setTableName(newEntity.getClass().getSimpleName());

		// get entity id.
		Field idField = null;
		try {
			idField = newEntity.getClass().getDeclaredField("id");
		} catch (NoSuchFieldException e) {
			logger.error("Cannot get 'id' field of entity to save WlsLog.");
		} catch (SecurityException e) {
		}
		if (idField != null) {
			idField.setAccessible(true);
			String idValue = "";
			try {
				idValue = (String) idField.get(newEntity);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
			wlsLog.setTableRowId(idValue);
		}
		wlsLogDao.save(wlsLog);
	}
}
