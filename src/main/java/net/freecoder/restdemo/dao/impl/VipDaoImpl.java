/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.freecoder.restdemo.dao.VipDao;
import net.freecoder.restdemo.model.WlsVip;
import net.freecoder.restdemo.model.WlsVipAddress;
import net.freecoder.restdemo.model.WlsVipAddressId;
import net.freecoder.restdemo.model.WlsVipPointsHistory;
import net.freecoder.restdemo.model.WlsVipPointsHistoryId;
import net.freecoder.restdemo.model.WlsVipStatistics;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class VipDaoImpl extends CommonDaoImpl<WlsVip> implements VipDao {

	@Override
	public WlsVip get(String id) {
		return super.get(WlsVip.class, id);
	}

	@Override
	public WlsVip getByPhone(String phone) {
		String hql = "from WlsVip where mobilePhone=?";
		List list = currentSession().createQuery(hql).setString(0, phone)
				.list();
		if (list != null && list.size() == 1) {
			return (WlsVip) list.get(0);
		}
		return null;
	}

	@Override
	public WlsVip loginByEmail(String email, String password) {
		String hql = "from WlsVip where email=? and password=?";
		List list = currentSession().createQuery(hql).setString(0, email)
				.setString(1, password).list();
		if (list != null && list.size() == 1) {
			return (WlsVip) list.get(0);
		}
		return null;
	}

	@Override
	public WlsVip loginByPhone(String phone, String password) {
		String hql = "from WlsVip where mobilePhone=? and password=?";
		List list = currentSession().createQuery(hql).setString(0, phone)
				.setString(1, password).list();
		if (list != null && list.size() == 1) {
			return (WlsVip) list.get(0);
		}
		return null;
	}

	@Override
	public List<WlsVip> getByShop(String wxAccountId) {
		String hql = "from WlsVip where id in (select id.wlsVipId from WlsShopVip where id.wlsWxAccountId=?)";
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.list();
	}

	@Override
	public void delete(String id) {
		super.delete(get(id));
	}

	@Override
	public void updatePassword(String id, String newPass) {
		String hql = "update WlsVip u set u.password=? where u.id=?";
		currentSession().createQuery(hql).setString(0, newPass)
				.setString(1, id).executeUpdate();
	}

	@Override
	public void saveAddresss(WlsVipAddress address) {
		createTime(address);
		currentSession().saveOrUpdate(address);
	}

	@Override
	public List<WlsVipAddress> getAddresses(String vipId) {
		String hql = "from WlsVipAddress where id.wlsVipId=?";
		return currentSession().createQuery(hql).setString(0, vipId).list();
	}

	@Override
	public WlsVipAddress getAddress(String vipId, String addressId) {
		WlsVipAddressId wlsVipAddressId = new WlsVipAddressId(addressId, vipId);
		return (WlsVipAddress) currentSession().get(WlsVipAddress.class,
				wlsVipAddressId);
	}

	@Override
	public void deleteAddress(WlsVipAddress address) {
		currentSession().delete(address);
	}

	@Override
	public void savePointsHistory(WlsVipPointsHistory pointsHistory) {
		createTime(pointsHistory);
		currentSession().saveOrUpdate(pointsHistory);

	}

	@Override
	public List<WlsVipPointsHistory> getPointsHistories(String vipId) {
		String hql = "from WlsVipPointsHistory where id.wlsVipId=?";
		return currentSession().createQuery(hql).setString(0, vipId).list();
	}

	@Override
	public List getPointsHistoriesWithOrderInfo(String vipId) {
		String hql = "select ph, o.no from WlsVipPointsHistory ph, WlsShopOrder o where ph.id.wlsVipId=? and ph.sourceId=o.id";
		// Notice select segment of upon hql, the result is ArrayList<Object>,
		// each element is Object[2], the first is
		// WlsVipPointsHistory object, the second is orderNo string. We need
		// convert each Object[] to HashMap to
		// keep object identity by map key.
		List list = currentSession().createQuery(hql).setString(0, vipId)
				.list();
		List result = new ArrayList(list.size());
		for (int i = 0; i < list.size(); i++) {
			Object[] row = (Object[]) list.get(i);
			Map<String, Object> map = new HashMap<String, Object>(list.size());
			map.put("WlsVipPointsHistory", row[0]);
			map.put("orderNo", row[1]);
			result.add(map);
		}
		return result;
	}

	@Override
	public WlsVipPointsHistory getPointsHistory(String vipId, String phId) {
		WlsVipPointsHistoryId wlsVipPointsHistoryId = new WlsVipPointsHistoryId(
				phId, vipId);
		return (WlsVipPointsHistory) currentSession().get(
				WlsVipPointsHistory.class, wlsVipPointsHistoryId);
	}

	@Override
	public void deletePointsHistory(WlsVipPointsHistory pointsHistory) {
		currentSession().delete(pointsHistory);

	}

	@Override
	public void saveStatistics(WlsVipStatistics stat) {
		createTime(stat);
		currentSession().saveOrUpdate(stat);
	}

	@Override
	public void adjustStatTotalPoints(String vipId, int points) {
		String hql = "update WlsVipStatistics set totalPoints = totalPoints + ? where wlsVipId=?";
		currentSession().createQuery(hql).setInteger(0, points)
				.setString(1, vipId).executeUpdate();
	}

	@Override
	public WlsVipStatistics getStatistics(String vipId) {
		String hql = "from WlsVipStatistics where wlsVipId=?";
		List list = currentSession().createQuery(hql).setString(0, vipId)
				.list();
		if (list != null && list.size() > 0) {
			return (WlsVipStatistics) list.get(0);
		}
		return null;
	}

	@Override
	public void deleteStatistics(WlsVipStatistics stat) {
		currentSession().delete(stat);
	}
}
