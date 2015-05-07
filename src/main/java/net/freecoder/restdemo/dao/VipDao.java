/**
 * 
 */
package net.freecoder.restdemo.dao;

import java.util.List;

import net.freecoder.restdemo.model.WlsVip;
import net.freecoder.restdemo.model.WlsVipAddress;
import net.freecoder.restdemo.model.WlsVipPointsHistory;
import net.freecoder.restdemo.model.WlsVipStatistics;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * Dao interfaces for VIP
 * 
 * @author JiTing
 */
public interface VipDao extends CommonDao<WlsVip> {

	WlsVip get(String id);

	WlsVip getByPhone(String phone);

	WlsVip loginByEmail(String email, String password);

	WlsVip loginByPhone(String phone, String password);

	void delete(String id);

	void updatePassword(String id, String newPass);

	/**
	 * Shop owner may get vip user list those bought something in the shop.
	 * 
	 * @param wxAccountId
	 *            wxAccountId of shop owner.
	 * @return
	 */
	List<WlsVip> getByShop(String wxAccountId);

	// interfaces for addresses.
	void saveAddresss(WlsVipAddress address);

	List<WlsVipAddress> getAddresses(String vipId);

	WlsVipAddress getAddress(String vipId, String addressId);

	void deleteAddress(WlsVipAddress address);

	// interfaces for points history
	void savePointsHistory(WlsVipPointsHistory pointsHistory);

	List<WlsVipPointsHistory> getPointsHistories(String vipId);

	List getPointsHistoriesWithOrderInfo(String vipId);

	WlsVipPointsHistory getPointsHistory(String vipId, String phId);

	void deletePointsHistory(WlsVipPointsHistory pointsHistory);

	// interfaces for statistics
	void saveStatistics(WlsVipStatistics stat);

	WlsVipStatistics getStatistics(String vipId);

	void adjustStatTotalPoints(String vipId, int points);

	void deleteStatistics(WlsVipStatistics stat);
}
