/**
 * 
 */
package net.freecoder.restdemo.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.freecoder.restdemo.constant.ShopConfigPropPrefix;
import net.freecoder.restdemo.dao.ShopDao;
import net.freecoder.restdemo.exception.ServiceDaoForeignKeyConstraintException;
import net.freecoder.restdemo.model.WlsShopCategory;
import net.freecoder.restdemo.model.WlsShopConfig;
import net.freecoder.restdemo.model.WlsShopEntry;
import net.freecoder.restdemo.model.WlsShopItem;
import net.freecoder.restdemo.model.WlsShopOrder;
import net.freecoder.restdemo.model.WlsShopOrderItem;
import net.freecoder.restdemo.model.WlsShopPromotionPoints;
import net.freecoder.restdemo.model.WlsShopVip;
import net.freecoder.restdemo.model.WlsShopVipId;
import net.freecoder.restdemo.util.DateTimeUtil;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JiTing
 */
@Repository
@Transactional
public class ShopDaoImpl extends CommonDaoImpl<WlsShopEntry> implements ShopDao {

	@Override
	public WlsShopEntry get(String wxAccountId) {
		return super.get(WlsShopEntry.class, wxAccountId);
	}

	@Override
	public void delete(String wxAccountId) {
		delete(get(wxAccountId));
	}

	@Override
	public void saveConfig(WlsShopConfig config) {
		createTime(config);
		currentSession().saveOrUpdate(config);
	}

	@Override
	public void saveConfigs(WlsShopConfig[] configs) {
		for (WlsShopConfig config : configs) {
			createTime(config);
			currentSession().saveOrUpdate(config);
		}
	}

	@Override
	public List<WlsShopConfig> getConfigs(String wxAccountId) {
		String hql = "from WlsShopConfig where id.wlsWxAccountId=?";
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.list();
	}

	@Override
	public List<WlsShopConfig> getConfigs(String wxAccountId,
			ShopConfigPropPrefix prefix) {
		String hql = "from WlsShopConfig where id.wlsWxAccountId=? and id.propName like ?";
		String prefixString = prefix.value() + "_%";
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.setString(1, prefixString).list();
	}

	@Override
	public WlsShopConfig getConfig(String propName, String wxAccountId) {
		String hql = "from WlsShopConfig where id.wlsWxAccountId=? and id.propName=?";
		List list = currentSession().createQuery(hql).setString(0, wxAccountId)
				.setString(1, propName).list();
		if (list != null && list.size() > 0) {
			return (WlsShopConfig) list.get(0);
		}
		return null;
	}

	@Override
	public void deleteConfig(String propName, String wxAccountId) {
		String hql = "delete from WlsShopConfig where id.wlsWxAccountId=? and id.propName=?";
		currentSession().createQuery(hql).setString(0, wxAccountId)
				.setString(1, propName).executeUpdate();
	}

	@Override
	public void saveCategory(WlsShopCategory category) {
		createTime(category);
		currentSession().saveOrUpdate(category);
	}

	@Override
	public WlsShopCategory getCategory(String id) {
		return (WlsShopCategory) currentSession()
				.get(WlsShopCategory.class, id);
	}

	@Override
	public List<WlsShopCategory> getCategoriesByShop(String wxAccountId) {
		String hql = "from WlsShopCategory where wlsWxAccountId=? order by displayOrder";
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.list();
	}

	@Override
	public void deleteCategory(WlsShopCategory category) {
		List<WlsShopItem> items = getItems(category.getWlsWxAccountId(),
				category.getId());
		if (items.size() > 0) {
			throw new ServiceDaoForeignKeyConstraintException();
		}
		currentSession().delete(category);
	}

	@Override
	public void saveItem(WlsShopItem item) {
		createTime(item);
		currentSession().saveOrUpdate(item);
	}

	@Override
	public WlsShopItem getItem(String itemId) {
		return (WlsShopItem) currentSession().get(WlsShopItem.class, itemId);
	}

	@Override
	public List<WlsShopItem> getItems(String wxAccountId) {
		String hql = "from WlsShopItem where wlsWxAccountId=?";
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.list();
	}

	@Override
	public List<WlsShopItem> getItems(String wxAccountId, String categoryId) {
		String hql = "from WlsShopItem where wlsWxAccountId=? and categoryId=?";
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.setString(1, categoryId).list();
	}

	@Override
	public List<WlsShopItem> getItems(String wxAccountId,
			Map<String, Object> criterions) {
		SimpleExpression seWxAccountId = Restrictions.eq("wlsWxAccountId",
				wxAccountId);
		Criteria criteria = currentSession().createCriteria(WlsShopItem.class)
				.add(seWxAccountId);

		for (Entry<String, Object> criterion : criterions.entrySet()) {
			SimpleExpression eq = Restrictions.eq(criterion.getKey(),
					criterion.getValue());
			criteria.add(eq);
		}

		return criteria.list();
	}

	@Override
	public void deleteItem(WlsShopItem item) {
		currentSession().delete(item);
	}

	@Override
	public WlsShopOrder getOrder(String orderId) {
		return (WlsShopOrder) currentSession().get(WlsShopOrder.class, orderId);
	}

	@Override
	public List<WlsShopOrder> getOrdersByShop(String wxAccountId) {
		String hql = "from WlsShopOrder where wlsWxAccountId=? order by appCreateTime desc";
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.list();
	}

	@Override
	public List<WlsShopOrder> getOrdersByShop(String wxAccountId, Date start,
			Date end) {
		// "from WlsShopOrder where wlsWxAccountId=? and appCreateTime between ? and ? order by appCreateTime desc";
		return currentSession().createCriteria(WlsShopOrder.class)
				.add(Restrictions.eq("wlsWxAccountId", wxAccountId))
				.add(Restrictions.between("appCreateTime", start, end))
				.addOrder(Order.desc("appCreateTime")).list();
	}

	@Override
	public List<WlsShopOrder> getOrdersByShop(String wxAccountId, long start,
			int pagesize) {
		// "from WlsShopOrder where wlsWxAccountId=? and appCreateTime < ? order by appCreateTime desc limit 0, ?";
		return currentSession().createCriteria(WlsShopOrder.class)
				.add(Restrictions.eq("wlsWxAccountId", wxAccountId))
				.add(Restrictions.lt("appCreateTime", new Date(start)))
				.addOrder(Order.desc("appCreateTime")).setMaxResults(pagesize)
				.list();
	}

	@Override
	public List<WlsShopOrder> getOrdersByVip(String vipId) {
		String hql = "from WlsShopOrder where vipId=? order by appCreateTime desc";
		return currentSession().createQuery(hql).setString(0, vipId).list();
	}

	@Override
	public List<WlsShopOrder> getOrdersByVip(String vipId, Date start, Date end) {
		// "from WlsShopOrder where vipId=? and appCreateTime between ? and ? order by appCreateTime desc";
		return currentSession().createCriteria(WlsShopOrder.class)
				.add(Restrictions.eq("vipId", vipId))
				.add(Restrictions.between("appCreateTime", start, end))
				.addOrder(Order.desc("appCreateTime")).list();
	}

	@Override
	public List<WlsShopOrder> getOrdersByVip(String vipId, long start,
			int pagesize) {
		// "from WlsShopOrder where vipId=? and appCreateTime < ? order by appCreateTime desc limit 0, ?";
		return currentSession().createCriteria(WlsShopOrder.class)
				.add(Restrictions.eq("vipId", vipId))
				.add(Restrictions.lt("appCreateTime", new Date(start)))
				.addOrder(Order.desc("appCreateTime")).setMaxResults(pagesize)
				.list();
	}

	@Override
	public List<WlsShopOrder> getOrdersByShopAndVip(String wxAccountId,
			String vipId) {
		String hql = "from WlsShopOrder where wlsWxAccountId=? and vipId=? order by appCreateTime desc";
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.setString(1, vipId).list();
	}

	@Override
	public List<WlsShopOrder> getOrdersByShopAndVip(String wxAccountId,
			String vipId, Date start, Date end) {
		// "from WlsShopOrder where wlsWxAccountId=? and vipId=? and appCreateTime between ? and ? order by appCreateTime desc";
		return currentSession().createCriteria(WlsShopOrder.class)
				.add(Restrictions.eq("wlsWxAccountId", wxAccountId))
				.add(Restrictions.eq("vipId", vipId))
				.add(Restrictions.between("appCreateTime", start, end))
				.addOrder(Order.desc("appCreateTime")).list();
	}

	@Override
	public List<WlsShopOrder> getOrdersByShopAndVip(String wxAccountId,
			String vipId, long start, int pagesize) {
		// "from WlsShopOrder where wlsWxAccountId=? and vipId=? and appCreateTime < ? order by appCreateTime desc limit 0, ?";
		return currentSession().createCriteria(WlsShopOrder.class)
				.add(Restrictions.eq("wlsWxAccountId", wxAccountId))
				.add(Restrictions.eq("vipId", vipId))
				.add(Restrictions.lt("appCreateTime", new Date(start)))
				.addOrder(Order.desc("appCreateTime")).setMaxResults(pagesize)
				.list();
	}

	@Override
	public int countOrdersByShop(String wxAccountId) {
		Object uniqueResult = currentSession()
				.createCriteria(WlsShopOrder.class)
				.add(Restrictions.eq("wlsWxAccountId", wxAccountId))
				.setProjection(Projections.rowCount()).uniqueResult();
		return Integer.valueOf(uniqueResult.toString()).intValue();
	}

	@Override
	public int countOrdersByVip(String vipId) {
		Object uniqueResult = currentSession()
				.createCriteria(WlsShopOrder.class)
				.add(Restrictions.eq("vipId", vipId))
				.setProjection(Projections.rowCount()).uniqueResult();
		return Integer.valueOf(uniqueResult.toString()).intValue();
	}

	@Override
	public int countOrdersByShopAndVip(String wxAccountId, String vipId) {
		Object uniqueResult = currentSession()
				.createCriteria(WlsShopOrder.class)
				.add(Restrictions.eq("wlsWxAccountId", wxAccountId))
				.add(Restrictions.eq("vipId", vipId))
				.setProjection(Projections.rowCount()).uniqueResult();
		return Integer.valueOf(uniqueResult.toString()).intValue();
	}

	@Override
	public void deleteOrder(WlsShopOrder order) {
		currentSession().delete(order);
	}

	@Override
	public void deleteOrderItem(WlsShopOrderItem orderItem) {
		currentSession().delete(orderItem);
	}

	@Override
	public void saveOrder(WlsShopOrder order) {
		createTime(order);
		currentSession().saveOrUpdate(order);
	}

	@Override
	public WlsShopVip getShopVip(WlsShopVipId id) {
		return (WlsShopVip) currentSession().get(WlsShopVip.class, id);
	}

	@Override
	public List<WlsShopVip> getShopVips(String wxAccountId) {
		String hql = "from WlsShopVip where id in (select id.wlsVipId from WlsShopVip where id.wlsWxAccountId=?)";
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.list();
	}

	@Override
	public void saveShopVip(WlsShopVip entity) {
		createTime(entity);
		currentSession().saveOrUpdate(entity);
	}

	@Override
	public void deleteShopVip(WlsShopVip entity) {
		currentSession().delete(entity);
	}

	@Override
	public void savePromotionPoints(WlsShopPromotionPoints entity) {
		createTime(entity);
		currentSession().saveOrUpdate(entity);
	}

	@Override
	public WlsShopPromotionPoints getPromotionPoints(String entityId) {
		return (WlsShopPromotionPoints) currentSession().get(
				WlsShopPromotionPoints.class, entityId);
	}

	@Override
	public List<WlsShopPromotionPoints> getPromotionPointsByShop(
			String wxAccountId, boolean isFilterEnabled) {
		String hql = "from WlsShopPromotionPoints where wlsWxAccountId=? order by appCreateTime desc";
		if (isFilterEnabled) {
			hql = "from WlsShopPromotionPoints where wlsWxAccountId=? and isEnable = true order by appCreateTime desc";
		}
		return currentSession().createQuery(hql).setString(0, wxAccountId)
				.list();
	}

	@Override
	public void deletePromotionPoints(WlsShopPromotionPoints entity) {
		currentSession().delete(entity);
	}
}
