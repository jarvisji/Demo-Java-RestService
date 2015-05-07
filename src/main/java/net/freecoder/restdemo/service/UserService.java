/**
 * 
 */
package net.freecoder.restdemo.service;

import java.util.List;

import net.freecoder.restdemo.constant.UserType;
import net.freecoder.restdemo.model.WlsUser;
import net.freecoder.restdemo.model.WlsWxAccount;

/**
 * Service interface of User.
 * 
 * @author JiTing
 */
public interface UserService {

	/**
	 * Create new user. This interface will generate a new UUID as user id, and
	 * set current datetime as createTime and lastModifyTime.
	 * 
	 * @param user
	 *            The WlsUser object to create.
	 * @return UserId if create successful.
	 */
	void createUser(WlsUser user);

	WlsUser getUser(String id);

	/**
	 * Get user by email and password.
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	WlsUser getUser(String email, String password);

	WlsUser getUser(String email, String password, UserType userType);

	WlsUser getUserByEmail(String email);

	List<WlsUser> getUsers();

	List<WlsUser> getCSRs();

	void updateUser(WlsUser user);

	void deleteUser(String id);

	void updatePassword(String id, String password);

	String getCredential(String email, String password);

	/**
	 * @deprecated This interface is not implemented currently.
	 * @param cred
	 * @return
	 */
	boolean destroyCredential(String cred);

	/**
	 * Create new bind of WeiXin account to user.
	 * 
	 * @param account
	 */
	List<WlsWxAccount> getWxAccounts(String userId);

	WlsWxAccount getWxAccount(String accountId);

	void createWxAccount(String userId, WlsWxAccount account);

	void updateWxAccount(WlsWxAccount account);

	void deleteWxAccount(String accountId);

}
