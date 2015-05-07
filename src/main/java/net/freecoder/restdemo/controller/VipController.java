/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.constant.ActivityType;
import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.ModuleType;
import net.freecoder.restdemo.constant.UserStatusEnum;
import net.freecoder.restdemo.dao.ActivityDao;
import net.freecoder.restdemo.dao.ShopDao;
import net.freecoder.restdemo.dao.VipDao;
import net.freecoder.restdemo.model.WlsActivity;
import net.freecoder.restdemo.model.WlsShopVip;
import net.freecoder.restdemo.model.WlsShopVipId;
import net.freecoder.restdemo.model.WlsVip;
import net.freecoder.restdemo.model.WlsVipAddress;
import net.freecoder.restdemo.model.WlsVipPointsHistory;
import net.freecoder.restdemo.model.WlsVipStatistics;
import net.freecoder.restdemo.service.impl.SmsService;
import net.freecoder.restdemo.util.DateTimeUtil;
import net.freecoder.restdemo.util.MiscUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author JiTing
 */
@Controller
@RequestMapping("/vip")
public class VipController extends CommonController {

	private static Logger logger = LoggerFactory.getLogger(VipController.class);

	@Autowired
	private VipDao vipDao;
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private SmsService smsService;
	@Value("#{sysconfProperties['sms.verificationcode.expire.minites']}")
	private String expireInMinites;

	/**
	 * GET /vip/cred
	 * 
	 * Vip user login.
	 * 
	 * @param identity
	 *            Support mobilePhone or email.
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/cred", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getVipCred(@RequestParam("identity") String identity,
			@RequestParam("password") String password) {
		boolean isMobilePhone = NumberUtils.isNumber(identity);
		WlsVip wlsVip;
		if (isMobilePhone) {
			wlsVip = vipDao.loginByPhone(identity, password);
		} else {
			wlsVip = vipDao.loginByEmail(identity, password);
		}

		Result result = new Result();
		if (wlsVip != null) {
			if (wlsVip.getStatus() == UserStatusEnum.NORMAL_STATUS_ACTIVATED
					.value()) {
				// login success, create cred and save to credMap.
				String cred = UUIDUtil.uuid8();
				saveVipCred(wlsVip.getId(), cred);
				result.setData("cred", cred);
				result.setData("vipId", wlsVip.getId());
			} else {
				result.setSuccess(false).setErrMsg("用户状态错误");
			}
		} else {
			result.setSuccess(false).setErrMsg("登录失败");
		}
		return result;
	}

	/**
	 * GET /vip
	 * 
	 * Return vip user information that associated with given credential.
	 * 
	 * @param cred
	 *            We assume this credential belongs to vip user himself.
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getVip(@RequestParam("c") String cred) {
		String vipId = cred2VipId(cred);
		WlsVip wlsVip = vipDao.get(vipId);
		Result result = new Result();
		if (wlsVip != null) {
			result.setEntity(wlsVip).setCount(1, 1);
		} else {
			result.setSuccess(false).setErrMsg("用户不存在");
		}
		return result;
	}

	/**
	 * GET /vip/existence
	 * 
	 * Get vip user existence by phone number, use to check the phone was
	 * regiested or not. <br>
	 * 
	 * @param phone
	 * @return Result, empty success Result if no match data.
	 */
	@RequestMapping(value = "/existence", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result checkVipExistence(@RequestParam String phone) {
		WlsVip byPhone = vipDao.getByPhone(phone);
		Result result = new Result();
		if (byPhone != null) {
			result.setErrCode(ErrorCode.VIP_PHONENUMBER_ALREADY_EXISTED);
		}
		return result;
	}

	/**
	 * GET /vip/verficationcode Send verification code to user mobile phone, and
	 * save activity for later verification.
	 * 
	 * @param phoneNumber
	 * @param wxAccountId
	 * @return
	 */
	@RequestMapping(value = "/verificationcode", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getVerificationCode(@RequestParam("phone") String phoneNumber,
			@RequestParam("a") String wxAccountId) {
		Short code = MiscUtil.generateShortRandom();
		smsService.sendRegVerificationCode(phoneNumber, code.toString(),
				expireInMinites, wxAccountId);

		// save to activity for later verify.
		WlsActivity wlsActivity = new WlsActivity(UUIDUtil.uuid8(),
				ModuleType.SHOP.toString(), phoneNumber,
				ActivityType.VIP_REG_VERIFICATIONCODE.toString(),
				DateTimeUtil.getGMTDate());
		wlsActivity.setValue(code.toString());
		activityDao.save(wlsActivity);

		return new Result();
	}

	/**
	 * GET /vip/<id>
	 * 
	 * Return vip user information that associated with the shop which the
	 * credential identified.
	 * 
	 * @param vipId
	 * @param cred
	 *            We assume this credential belongs to the user who is shop
	 *            owner.
	 * @return
	 */
	@RequestMapping(value = "/{vipId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getShopVip(@PathVariable String vipId,
			@RequestParam("c") String cred,
			@RequestParam("a") String wxAccountId) {
		Result result = new Result();
		if (validateAccountIdWithCred(wxAccountId, cred)) {
			WlsShopVip shopVip = shopDao.getShopVip(new WlsShopVipId(vipId,
					wxAccountId));
			if (shopVip != null) {
				// means the vip user has bought something from this
				// shop(wxAccount). We can return vip info back.
				WlsVip wlsVip = vipDao.get(vipId);
				if (wlsVip != null) {
					result.setEntity(wlsVip).setCount(1, 1);
				} else {
					result.setSuccess(false).setErrMsg("用户不存在");
				}
			} else {
				result.setSuccess(false).setErrMsg("没有权限查看此会员信息");
			}
		} else {
			result.setSuccess(false).setErrMsg("没有权限查看此会员信息");
		}
		return result;
	}

	/**
	 * Get vip list of a shop.
	 * 
	 * @param cred
	 * @param wxAccountId
	 *            The wxAccountId must be validated with cred.
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getShopVipList(@RequestParam("c") String cred,
			@RequestParam("a") String wxAccountId) {
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			return new Result().setSuccess(false).setErrMsg("没有权限查看会员信息");
		}
		List<WlsVip> vipList = vipDao.getByShop(wxAccountId);
		Result result = new Result();
		return result.setEntity(vipList).setCount(vipList.size(),
				vipList.size());
	}

	/**
	 * POST /vip Validate verification code, and create vip account.
	 * 
	 * @param wlsVip
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, headers = "accept=application/json")
	public @ResponseBody
	Result createVip(@RequestParam String phone, @RequestParam String password,
			@RequestParam String code) {
		WlsActivity codeActivity = activityDao.getLastActivity(phone,
				ActivityType.VIP_REG_VERIFICATIONCODE);
		Result ret = new Result();
		if (codeActivity == null) {
			return ret.setErrCode(ErrorCode.VIP_PHONENUMBER_NOT_REGISTERED);
		}

		if (!codeActivity.getValue().equals(code)) {
			return ret.setErrCode(ErrorCode.VIP_VERIFICATIONCODE_INVALID);
		}

		int expireInMillis = Integer.valueOf(expireInMinites) * 60000;
		if (System.currentTimeMillis() > codeActivity.getAppCreateTime()
				.getTime() + expireInMillis) {
			logger.debug(
					"Verfication code expired, phone: {}, code: {}, createdTime: {}",
					phone, codeActivity.getValue(),
					codeActivity.getAppCreateTime());
			return ret.setErrCode(ErrorCode.VIP_VERIFICATIONCODE_EXPIRED);
		}

		WlsVip byPhone = vipDao.getByPhone(phone);
		if (byPhone != null) {
			return ret.setErrCode(ErrorCode.VIP_PHONENUMBER_ALREADY_EXISTED);
		}

		WlsVip wlsVip = new WlsVip();
		wlsVip.setId(UUIDUtil.uuid8());
		wlsVip.setMobilePhone(phone);
		wlsVip.setPassword(password);
		wlsVip.setStatus(UserStatusEnum.NORMAL_STATUS_ACTIVATED.value());
		vipDao.save(wlsVip);
		ret.setEntity(wlsVip);
		return ret;
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updateVip(@RequestBody WlsVip wlsVip, @RequestParam("c") String cred) {
		String cred2VipId = cred2VipId(cred);
		if (!cred2VipId.equals(wlsVip.getId())) {
			return new Result().setSuccess(false).setErrMsg("没有权限");
		}
		WlsVip dbWlsVip = vipDao.get(wlsVip.getId());
		if (dbWlsVip == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND);
		} else {
			// Since decleard @JsonIgnore on password field of WlsVip, user put
			// data will lost the password.
			wlsVip.setPassword(dbWlsVip.getPassword());
			vipDao.update(wlsVip);
		}
		return new Result();
	}

	/**
	 * PUT /vip/pwd
	 * 
	 * Change password of current login user. The parameter "oldPass" must
	 * equals to original password of current login user.
	 * 
	 * @param data
	 *            Map which should contains "oldPass" and "newPass" two keys.
	 * @param cred
	 *            Credential of current login user.
	 * @return
	 */
	@RequestMapping(value = "/pwd", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result changeVipPassword(@RequestBody HashMap<String, String> data,
			@RequestParam("c") String cred) {
		String vipId = cred2VipId(cred);
		String newPass = data.get("newPass");
		if (newPass == null) {
			return new Result().setSuccess(false).setErrMsg(
					"Invalid new password.");
		}
		WlsVip wlsVip = vipDao.get(vipId);
		Result result = new Result();
		String oldPass = data.get("oldPass");
		if (wlsVip.getPassword().equals(oldPass)) {
			wlsVip.setPassword(newPass);
			vipDao.save(wlsVip);
		} else {
			result.setSuccess(false).setErrMsg("密码错误");
		}
		return result;
	}

	/**
	 * Get vip addresses.
	 * 
	 * @param cred
	 *            Credential of vip user.
	 * @return
	 */
	@RequestMapping(value = "/address/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getAddressList(@RequestParam("c") String cred) {
		String vipId = cred2VipId(cred);
		List<WlsVipAddress> addresses = vipDao.getAddresses(vipId);
		return new Result().setEntity(addresses).setDataTotalCount(
				addresses.size());
	}

	/**
	 * POST /vip/address
	 * 
	 * Create new address. The vipId in address object must match cred
	 * represents.
	 * 
	 * @param address
	 *            WlsVipAddress
	 * @param cred
	 * @return
	 */
	@RequestMapping(value = "/address", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createAddress(@RequestBody WlsVipAddress address,
			@RequestParam("c") String cred) {
		String vipId = cred2VipId(cred);
		if (!vipId.equals(address.getId().getWlsVipId())) {
			return new Result().setSuccess(false).setErrMsg("No privilege");
		}
		address.getId().setId(UUIDUtil.uuid8());
		vipDao.saveAddresss(address);
		return new Result().setEntity(address);
	}

	/**
	 * PUT /vip/address/{addressId}
	 * 
	 * Update exists address.
	 * 
	 * @param addressId
	 *            The id of address entity, need same to the one in PUT data
	 *            WlsVipAddress.
	 * @param address
	 *            WlsVipAddress
	 * @param cred
	 * @return
	 */
	@RequestMapping(value = "/address/{addressId}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updateAddress(@PathVariable String addressId,
			@RequestBody WlsVipAddress address, @RequestParam("c") String cred) {
		String vipId = cred2VipId(cred);
		if (!vipId.equals(address.getId().getWlsVipId())) {
			return new Result().setSuccess(false).setErrMsg("No privilege");
		}
		if (!addressId.equals(address.getId().getId())) {
			return new Result().setSuccess(false).setErrMsg("Invalid data");
		}
		vipDao.saveAddresss(address);
		return new Result();
	}

	/**
	 * DELETE /vip/address/{addressId}
	 * 
	 * Delete exists address.
	 * 
	 * @param addressId
	 * @param cred
	 * @return
	 */
	@RequestMapping(value = "/address/{addressId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteAddress(@PathVariable String addressId,
			@RequestParam("c") String cred) {
		String vipId = cred2VipId(cred);
		vipDao.deleteAddress(vipDao.getAddress(vipId, addressId));
		return new Result();
	}

	/**
	 * GET /vip/points/history/list
	 * 
	 * Get points history of vip user.
	 * 
	 * @param cred
	 *            Identity of vip user.
	 * @return
	 */
	@RequestMapping(value = "/points/history/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getPointsHistoryList(@RequestParam("c") String cred) {
		String vipId = cred2VipId(cred);
		List pointsHistories = vipDao.getPointsHistoriesWithOrderInfo(vipId);
		return new Result().setEntity(pointsHistories).setDataTotalCount(
				pointsHistories.size());
	}

	/**
	 * GET /vip/points/history/{id}
	 * 
	 * Get points history entity.
	 * 
	 * @param historyId
	 * @param cred
	 * @return
	 */
	@RequestMapping(value = "/points/history/{historyId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getPointsHistory(@PathVariable String historyId,
			@RequestParam("c") String cred) {
		String vipId = cred2VipId(cred);
		WlsVipPointsHistory pointsHistory = vipDao.getPointsHistory(vipId,
				historyId);
		return new Result().setEntity(pointsHistory).setDataTotalCount(1);
	}

	/**
	 * GET /vip/stat
	 * 
	 * Get vip statistics by cred.
	 * 
	 * @param cred
	 * @return
	 */
	@RequestMapping(value = "/stat", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getVipStat(@RequestParam("c") String cred) {
		String vipId = cred2VipId(cred);
		WlsVipStatistics statistics = vipDao.getStatistics(vipId);
		if (statistics == null) {
			statistics = new WlsVipStatistics();
			statistics.setWlsVipId(vipId);
		}
		return new Result().setEntity(statistics);
	}
}
