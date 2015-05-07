package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.model.WlsUser;
import net.freecoder.restdemo.model.WlsUserPay;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * Dao interfaces for WlsUser entity.
 * 
 * @author JiTing
 */
public interface UserDao extends CommonDao<WlsUser> {

	WlsUser get(String id);

	List<WlsUser> find(Criterion criterion);

	List<WlsUser> find(Criterion criterion, Order order);

	void delete(String id);

	void updatePassword(String id, String newPass);

	WlsUserPay getLastUserPay(String userId);

	List<WlsUserPay> getUserPays(String userId);

	void updateUserPay(WlsUserPay userPay);

	void createUserPay(WlsUserPay userPay);
}
