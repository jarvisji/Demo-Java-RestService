/**
 * 
 */
package net.freecoder.restdemo.service.impl;

import java.util.List;

import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.UserStatusEnum;
import net.freecoder.restdemo.constant.UserType;
import net.freecoder.restdemo.dao.UserDao;
import net.freecoder.restdemo.dao.WxAccountDao;
import net.freecoder.restdemo.exception.ServiceException;
import net.freecoder.restdemo.model.WlsUser;
import net.freecoder.restdemo.model.WlsWxAccount;
import net.freecoder.restdemo.service.UserService;
import net.freecoder.restdemo.util.DateTimeUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author JiTing
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private WxAccountDao wxAccountDao;

	@Override
	public void createUser(WlsUser user) {
		if (user == null || user.getEmail() == null
				|| user.getEmail().isEmpty() || user.getPassword() == null
				|| user.getPassword().isEmpty()) {
			throw new ServiceException(ErrorCode.INVALID_DATA.toString());
		}
		SimpleExpression eq = Restrictions.eq("email", user.getEmail());
		List<WlsUser> find = userDao.find(eq);
		if (find.size() > 0) {
			throw new ServiceException(ErrorCode.DUPLICATE_DATA.toString());
		}

		String uuid8 = UUIDUtil.uuid8();
		user.setId(uuid8);
		user.setAppCreateTime(DateTimeUtil.getGMTDate());
		user.setAppLastModifyTime(DateTimeUtil.getGMTDate());
		if (user.getStatus() == null) {
			user.setStatus(UserStatusEnum.PENDING_STATUS_INACTIVE.value());
		}
		userDao.save(user);
	}

	@Override
	public WlsUser getUser(String id) {
		WlsUser wlsUser = userDao.get(id);
		return wlsUser;
	}

	@Override
	public WlsUser getUserByEmail(String email) {
		SimpleExpression eqEmail = Restrictions.eq("email", email);
		List<WlsUser> find = userDao.find(eqEmail);
		if (find.size() > 0) {
			return find.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<WlsUser> getUsers() {
		SimpleExpression eqUser = Restrictions.eq("userType",
				UserType.USER.toString());
		return userDao.find(eqUser);
	}

	@Override
	public List<WlsUser> getCSRs() {
		SimpleExpression eqCsr = Restrictions.eq("userType",
				UserType.CSR.toString());
		return userDao.find(eqCsr);
	}

	@Override
	public void updateUser(WlsUser user) {
		userDao.update(user);
	}

	@Override
	public void deleteUser(String id) {
		userDao.delete(id);
	}

	/**
	 * Generate credential for special and current login. Each time user login
	 * should generate a new credential. Credential also have time limitation,
	 * before its expired, server should renew it if user still online.<br>
	 * <b>NOTE:</b> Currently just generate a UUID as user credendial, the other
	 * logic such as expire are handle in TimelyCred object.
	 */
	@Override
	public String getCredential(String email, String password) {
		return UUIDUtil.uuid8();
	}

	@Override
	public WlsUser getUser(String email, String password) {
		LogicalExpression exp = Restrictions.and(
				Restrictions.eq("email", email),
				Restrictions.eq("password", password));
		List<WlsUser> findUsers = userDao.find(exp);
		if (findUsers.size() == 1) {
			return findUsers.get(0);
		}
		return null;
	}

	@Override
	public WlsUser getUser(String email, String password, UserType userType) {
		LogicalExpression authExp = Restrictions.and(
				Restrictions.eq("email", email),
				Restrictions.eq("password", password));
		SimpleExpression utExp = Restrictions.eq("userType",
				userType.toString());
		LogicalExpression exp = Restrictions.and(authExp, utExp);
		List<WlsUser> findUsers = userDao.find(exp);
		if (findUsers.size() == 1) {
			return findUsers.get(0);
		}
		return null;
	}

	@Override
	public boolean destroyCredential(String cred) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createWxAccount(String userId, WlsWxAccount account) {
		WlsUser wlsUser = userDao.get(userId);
		if (wlsUser == null) {
			throw new ServiceException("绑定公众号失败：用户信息错误。");
		}
		account.setId(UUIDUtil.uuid8());
		account.setWlsUserId(userId);
		wxAccountDao.save(account);
	}

	@Override
	public void updateWxAccount(WlsWxAccount account) {
		wxAccountDao.update(account);
	}

	@Override
	public void deleteWxAccount(String accountId) {
		wxAccountDao.delete(accountId);
	}

	@Override
	public WlsWxAccount getWxAccount(String accountId) {
		return wxAccountDao.get(accountId);
	}

	@Override
	public List<WlsWxAccount> getWxAccounts(String userId) {
		List<WlsWxAccount> wxAccountList = wxAccountDao.find(Restrictions.eq(
				"wlsUserId", userId));
		return wxAccountList;
	}

	@Override
	public void updatePassword(String id, String password) {
		userDao.updatePassword(id, password);
	}
}
