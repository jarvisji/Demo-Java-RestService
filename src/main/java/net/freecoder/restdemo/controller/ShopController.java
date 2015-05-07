/**
 * 
 */
package net.freecoder.restdemo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.freecoder.restdemo.annotation.WlsModule;
import net.freecoder.restdemo.constant.ErrorCode;
import net.freecoder.restdemo.constant.ShopConfigPropPrefix;
import net.freecoder.restdemo.constant.ShopOrderPayment;
import net.freecoder.restdemo.constant.ShopOrderStatus;
import net.freecoder.restdemo.constant.SystemModules;
import net.freecoder.restdemo.constant.VipPointsSource;
import net.freecoder.restdemo.dao.ShopDao;
import net.freecoder.restdemo.dao.VipDao;
import net.freecoder.restdemo.model.WlsShopCategory;
import net.freecoder.restdemo.model.WlsShopConfig;
import net.freecoder.restdemo.model.WlsShopConfigId;
import net.freecoder.restdemo.model.WlsShopEntry;
import net.freecoder.restdemo.model.WlsShopItem;
import net.freecoder.restdemo.model.WlsShopOrder;
import net.freecoder.restdemo.model.WlsShopOrderItem;
import net.freecoder.restdemo.model.WlsShopOrderItemId;
import net.freecoder.restdemo.model.WlsShopPromotionPoints;
import net.freecoder.restdemo.model.WlsShopVip;
import net.freecoder.restdemo.model.WlsShopVipId;
import net.freecoder.restdemo.model.WlsVipPointsHistory;
import net.freecoder.restdemo.model.WlsVipPointsHistoryId;
import net.freecoder.restdemo.model.WlsVipStatistics;
import net.freecoder.restdemo.service.impl.SmsService;
import net.freecoder.restdemo.util.DateTimeUtil;
import net.freecoder.restdemo.util.UUIDUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/shop")
@WlsModule(SystemModules.M_SHOP)
public class ShopController extends CommonController {

	@Autowired
	ShopDao shopDao;
	@Autowired
	VipDao vipDao;
	@Autowired
	SmsService smsService;

	private static final int INVENTORY_INF = 999999;

	private static final Logger logger = LoggerFactory
			.getLogger(ShopController.class);
	/**
	 * If client user hasn't login, and placed an order, the vipId should be set
	 * to "novip_"+xxx. When user try to get this order, need send this id via
	 * "cred" parameter.
	 */
	private static final String CRED_NOVIP = "novip_";

	/**
	 * GET /shop/entry/{id}<br>
	 * 
	 * Get shop entry. This is public to anybody.
	 * 
	 * @param wxAccountId
	 * @return Result object contains WlsShopEntry data.
	 */
	@RequestMapping(value = "/entry/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getEntry(@PathVariable("id") String wxAccountId) {
		WlsShopEntry wlsShopEntry = shopDao.get(wxAccountId);
		Result result = new Result();
		if (wlsShopEntry != null) {
			result.setEntity(wlsShopEntry);
		}
		return result;
	}

	/**
	 * POST /shop/entry?c=<br>
	 * 
	 * Create shop entry.
	 * 
	 * @param cred
	 * @param entry
	 *            WlsShopEntry, within wlsWxAccountId should match the
	 *            credential.
	 * @return Result
	 */
	@RequestMapping(value = "/entry", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createEntry(@RequestParam("c") String cred,
			@RequestBody WlsShopEntry entry) {
		if (!validateAccountIdWithCred(entry.getWlsWxAccountId(), cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, entry.getWlsWxAccountId());
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		shopDao.save(entry);
		return new Result().setEntity(entry);
	}

	/**
	 * PUT /shop/entry/{id}?c=<br>
	 * 
	 * Update shop entry.
	 * 
	 * @param wxAccountId
	 *            Pathvariable("id") indicate which entry is being update,
	 *            should match the value in entry data.
	 * @param cred
	 * @param entry
	 *            WlsShopEntry, within wlsWxAccountId should match the
	 *            credential.
	 * @return
	 */
	@RequestMapping(value = "/entry/{id}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updateEntry(@PathVariable("id") String wxAccountId,
			@RequestParam("c") String cred, @RequestBody WlsShopEntry entry) {
		if (!validateAccountIdWithCred(entry.getWlsWxAccountId(), cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, entry.getWlsWxAccountId());
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		if (!wxAccountId.equals(entry.getWlsWxAccountId())) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA);
		}
		shopDao.update(entry);
		return new Result();
	}

	/**
	 * DELETE /shop/entry/{id}?c=<br>
	 * 
	 * Delete shop entry.
	 * 
	 * @param wxAccountId
	 *            Pathvariable("id") indicate which entry is being delete,
	 *            should match the credential.
	 * @param cred
	 * @return Result
	 */
	@RequestMapping(value = "/entry/{id}", method = RequestMethod.DELETE, headers = "Content-type=application/json")
	public @ResponseBody
	Result deleteEntry(@PathVariable("id") String wxAccountId,
			@RequestParam("c") String cred) {
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, wxAccountId);
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		shopDao.delete(wxAccountId);
		return new Result();
	}

	/**
	 * GET /shop/{id}/info<br>
	 * 
	 * Get shop base information. This is public to anybody.
	 * 
	 * @param wxAccountId
	 *            PathVariable "id" indicate shop id.
	 * @return Result object contains key-value config items.
	 */
	@RequestMapping(value = "/{id}/info", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getInfo(@PathVariable("id") String wxAccountId) {
		List<WlsShopConfig> configs = shopDao.getConfigs(wxAccountId,
				ShopConfigPropPrefix.BASE);
		Result result = new Result();
		for (WlsShopConfig config : configs) {
			result.setData(config.getId().getPropName(), config.getPropValue());
		}
		return result.setCount(configs.size(), configs.size());
	}

	/**
	 * GET /shop/{id}/config/{propName}<br>
	 * 
	 * Get special config item.
	 * 
	 * @param propName
	 *            Config propery name.
	 * @param wxAccountId
	 *            Shop identity, should match the credential.
	 * @return Result object contains config item.
	 */
	@RequestMapping(value = "/{id}/config/{propName}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getConfigItem(@PathVariable String propName,
			@PathVariable("id") String wxAccountId) {
		WlsShopConfig config = shopDao.getConfig(propName, wxAccountId);
		return new Result().setEntity(config);
	}

	/**
	 * GET /shop/{id}/config/prefix/{prefix}
	 * 
	 * Get configs by special prefix.
	 * 
	 * @param prefix
	 *            Refer to {@link ShopConfigPropPrefix}
	 * @param wxAccountId
	 * @return
	 */
	@RequestMapping(value = "/{id}/config/prefix/{prefix}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getConfigItemByPrefix(@PathVariable String prefix,
			@PathVariable("id") String wxAccountId) {
		List<WlsShopConfig> configs = shopDao.getConfigs(wxAccountId,
				ShopConfigPropPrefix.parseValue(prefix));
		Result result = new Result();
		for (WlsShopConfig config : configs) {
			result.setData(config.getId().getPropName(), config.getPropValue());
		}
		return result.setCount(configs.size(), configs.size());
	}

	/**
	 * GET /shop/{id}/config/list<br>
	 * 
	 * Get shop all config items.
	 * 
	 * @param wxAccountId
	 *            Shop identity, should match the credential.
	 * @return Result object contains key-value pairs of all config items.
	 */
	@RequestMapping(value = "/{id}/config/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getConfigs(@PathVariable("id") String wxAccountId) {
		List<WlsShopConfig> configs = shopDao.getConfigs(wxAccountId);
		Result result = new Result();
		for (WlsShopConfig config : configs) {
			result.setData(config.getId().getPropName(), config.getPropValue());
		}
		return result.setCount(configs.size(), configs.size());
	}

	/**
	 * POST /shop/{id}/config<br>
	 * 
	 * Create shop config items.
	 * 
	 * @param wxAccountId
	 *            Shop identity, should match the credential.
	 * @param configs
	 * @param cred
	 * @return
	 */
	@RequestMapping(value = "/{id}/config", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createConfig(@PathVariable("id") String wxAccountId,
			@RequestBody Map<String, String> configs,
			@RequestParam("c") String cred) {
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, wxAccountId);
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		// Convert key-value pairs to WlsShopConfig objects.
		List<WlsShopConfig> listConfigs = new ArrayList<WlsShopConfig>();
		for (Entry<String, String> config : configs.entrySet()) {
			WlsShopConfigId wlsShopConfigId = new WlsShopConfigId(
					config.getKey(), wxAccountId);
			WlsShopConfig wlsShopConfig = new WlsShopConfig(wlsShopConfigId,
					config.getValue(), DateTimeUtil.getGMTDate());
			listConfigs.add(wlsShopConfig);
		}
		WlsShopConfig[] arrConfigs = listConfigs.toArray(new WlsShopConfig[0]);

		shopDao.saveConfigs(arrConfigs);
		return new Result().setEntity(arrConfigs);
	}

	/**
	 * DELETE /shop/{id}/config/{propName}<br>
	 * 
	 * Delete config item for special shop.
	 * 
	 * @param propName
	 * @param wxAccountId
	 *            Shop identity, should match the credential.
	 * @param cred
	 * @return
	 */
	@RequestMapping(value = "/{id}/config/{propName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody
	Result deleteConfigItem(@PathVariable String propName,
			@PathVariable("id") String wxAccountId,
			@RequestParam("c") String cred) {
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, wxAccountId);
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		shopDao.deleteConfig(propName, wxAccountId);
		return new Result();
	}

	/**
	 * GET /shop/{shopId}/category/{catId}<br>
	 * 
	 * Get special category entity of a shop.
	 * 
	 * @param wxAccountId
	 *            Shop identity, should match the credential.
	 * @param catId
	 * @return Result object contains WlsShopConfig data.
	 */
	@RequestMapping(value = "/{shopId}/category/{catId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getCategory(@PathVariable("shopId") String wxAccountId,
			@PathVariable String catId) {
		WlsShopCategory category = shopDao.getCategory(catId);
		if (category == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND);
		}
		if (!category.getWlsWxAccountId().equals(wxAccountId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		return new Result().setEntity(category);
	}

	/**
	 * GET /shop/{shopId}/category/list<br>
	 * 
	 * Get all category entities of a shop. This is public for anybody.
	 * 
	 * @param wxAccountId
	 *            Shop identity, should match the credential.
	 * @return Result object contains WlsShopConfig array data.
	 */
	@RequestMapping(value = "/{shopId}/category/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getCategories(@PathVariable("shopId") String wxAccountId) {
		List<WlsShopCategory> categories = shopDao
				.getCategoriesByShop(wxAccountId);
		return new Result().setEntity(categories).setDataTotalCount(
				categories.size());
	}

	/**
	 * POST /shop/category<br>
	 * 
	 * Create new category of shop.
	 * 
	 * @param cred
	 * @param category
	 * @return Result object contains new created WlsShopCategory.
	 */
	@RequestMapping(value = "/category", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createCategory(@RequestParam("c") String cred,
			@RequestBody WlsShopCategory category) {
		String wxAccountId = category.getWlsWxAccountId();
		if (wxAccountId == null) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"missed wlsWxAccountId.");
		}
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, wxAccountId);
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		category.setId(UUIDUtil.uuid8());
		shopDao.saveCategory(category);
		return new Result().setEntity(category);
	}

	/**
	 * PUT /shop/category/{catId}<br>
	 * 
	 * Update special shop category.
	 * 
	 * @param catId
	 *            Category entity id.
	 * @param cred
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/category/{catId}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updateCategory(@PathVariable String catId,
			@RequestParam("c") String cred,
			@RequestBody WlsShopCategory category) {
		String wxAccountId = category.getWlsWxAccountId();
		if (wxAccountId == null) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"missed wlsWxAccountId.");
		}
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, wxAccountId);
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		if (category.getId() != null && !catId.equals(category.getId())) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"category id mismatch");
		}
		if (category.getId() == null) {
			category.setId(catId);
		}
		shopDao.saveCategory(category);
		return new Result();
	}

	/**
	 * DELETE /shop/{shipId}/category/{catId}
	 * 
	 * Delete a special category.
	 * 
	 * @param wxAccountId
	 *            Shop identity, should match the credential.
	 * @param catId
	 * @param cred
	 * @return
	 */
	@RequestMapping(value = "/{shopId}/category/{catId}", method = RequestMethod.DELETE, headers = "Content-type=application/json")
	public @ResponseBody
	Result deleteCategory(@PathVariable("shopId") String wxAccountId,
			@PathVariable String catId, @RequestParam("c") String cred) {
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, wxAccountId);
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		shopDao.deleteCategory(shopDao.getCategory(catId));
		return new Result();
	}

	/**
	 * GET /shop/{shopId}/item/{itemId}<br>
	 * 
	 * Get an item of shop. This is public to anybody.
	 * 
	 * @param wxAccountId
	 *            Shop identity, the item should belongs to the shop.
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "/{shopId}/item/{itemId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getItem(@PathVariable("shopId") String wxAccountId,
			@PathVariable String itemId) {
		WlsShopItem item = shopDao.getItem(itemId);
		if (item == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND);
		}
		if (!item.getWlsWxAccountId().equals(wxAccountId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		return new Result().setEntity(item);
	}

	/**
	 * GET /shop/{shopId}/item/list<br>
	 * 
	 * Get all items in shop. This is public to anybody.
	 * 
	 * @param wxAccountId
	 *            Shop identity.
	 * @return
	 */
	@RequestMapping(value = "/{shopId}/item/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getItems(@PathVariable("shopId") String wxAccountId) {
		List<WlsShopItem> items = shopDao.getItems(wxAccountId);
		return new Result().setEntity(items);
	}

	@RequestMapping(value = "/{shopId}/item/list/insell", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getItemsInSell(@PathVariable("shopId") String wxAccountId) {
		Map<String, Object> criterions = new HashMap<String, Object>(1);
		criterions.put("isInSell", true);
		List<WlsShopItem> items = shopDao.getItems(wxAccountId, criterions);
		return new Result().setEntity(items);
	}

	/**
	 * GET /shop/{shopId}/category/{catId}/item/list<br>
	 * 
	 * Get item list of a special category in shop. This is public to anybody.
	 * 
	 * @param wxAccountId
	 * @param catId
	 * @return
	 */
	@RequestMapping(value = "/{shopId}/category/{catId}/item/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getItemsByCategory(@PathVariable("shopId") String wxAccountId,
			@PathVariable String catId) {
		List<WlsShopItem> items = shopDao.getItems(wxAccountId, catId);
		return new Result().setEntity(items);
	}

	@RequestMapping(value = "/{shopId}/category/{catId}/item/list/insell", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getItemsByCategoryInSell(@PathVariable("shopId") String wxAccountId,
			@PathVariable String catId) {
		Map<String, Object> criterions = new HashMap<String, Object>(2);
		criterions.put("categoryId", catId);
		criterions.put("isInSell", true);
		List<WlsShopItem> items = shopDao.getItems(wxAccountId, criterions);
		return new Result().setEntity(items);
	}

	/**
	 * POST /shop/item<br>
	 * 
	 * Create new item in shop.
	 * 
	 * @param cred
	 * @param item
	 * @return
	 */
	@RequestMapping(value = "/item", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createItem(@RequestParam("c") String cred,
			@RequestBody WlsShopItem item) {
		String wxAccountId = item.getWlsWxAccountId();
		if (wxAccountId == null) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"missed wlsWxAccountId.");
		}
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, wxAccountId);
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		item.setId(UUIDUtil.uuid8());
		shopDao.saveItem(item);
		return new Result().setEntity(item);
	}

	/**
	 * PUT /shop/item/{itemId}<br>
	 * 
	 * Update special item in shop.
	 * 
	 * @param itemId
	 * @param cred
	 * @param item
	 *            WlsWxAccountId must match cred.
	 * @return
	 */
	@RequestMapping(value = "/item/{itemId}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result createItem(@PathVariable String itemId,
			@RequestParam("c") String cred, @RequestBody WlsShopItem item) {
		String wxAccountId = item.getWlsWxAccountId();
		if (wxAccountId == null) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"missed wlsWxAccountId.");
		}
		if (!validateAccountIdWithCred(wxAccountId, cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, wxAccountId);
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		if (item.getId() != null && !item.getId().equals(itemId)) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"itemId mismatch");
		}
		if (item.getId() == null) {
			item.setId(itemId);
		}
		shopDao.saveItem(item);
		return new Result();
	}

	/**
	 * Delete /shop/item/{itemId}<br>
	 * 
	 * Delete special in shop.
	 * 
	 * @param itemId
	 * @param cred
	 * @return
	 */
	@RequestMapping(value = "/item/{itemId}", method = RequestMethod.DELETE, headers = "Content-type=application/json")
	public @ResponseBody
	Result createItem(@PathVariable String itemId,
			@RequestParam("c") String cred) {
		WlsShopItem item = shopDao.getItem(itemId);
		if (item == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND);
		}
		if (!validateAccountIdWithCred(item.getWlsWxAccountId(), cred)) {
			logger.error("{}, cred={}, wxAccountId={}",
					ErrorCode.NO_PRIVILIEGE, cred, item.getWlsWxAccountId());
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		shopDao.deleteItem(item);
		return new Result();
	}

	/**
	 * GET /shop/{shopId}/order/{orderId}<br>
	 * 
	 * Get special order.
	 * 
	 * @param orderId
	 * @param cred
	 *            Cred string may has three cases:<br>
	 *            1. Start with "novip_", this is not login user.<br>
	 *            2. Match wxAccountId, this is shop user.<br>
	 *            3. Match vipId, this is vip user.<br>
	 *            Otherwise, will throw exception "invalid credential".
	 * 
	 * @return Result object contains order data.
	 */
	@RequestMapping(value = "/{shopId}/order/{orderId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getOrder(@PathVariable("shopId") String wxAccountId,
			@PathVariable String orderId, @RequestParam("c") String cred) {
		WlsShopOrder order = shopDao.getOrder(orderId);
		if (order == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND);
		}
		if (!order.getWlsWxAccountId().equals(wxAccountId)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}

		// non-vip
		if (isNonVip(cred)) {
			if (order.getVipId().equals(cred)) {
				return new Result().setEntity(order);
			} else {
				return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
			}
		}

		// shop user
		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds != null) {
			if (cred2AccountIds.contains(wxAccountId)) {
				return new Result().setEntity(order);
			} else {
				return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
			}
		}

		// vip user. Exception will be throwed if the cred still not valid.
		String cred2VipId = cred2VipId(cred);
		if (order.getVipId().equals(cred2VipId)) {
			return new Result().setEntity(order);
		} else {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
	}

	/**
	 * GET /shop/{shopId}/order/list<br>
	 * 
	 * Get history orders of a special shop.
	 * 
	 * @param wxAccountId
	 * @param cred
	 *            Cred string may has two cases:<br>
	 *            1. Match wxAccountId, this is shop user.<br>
	 *            2. Match vipId, this is vip user.<br>
	 *            Otherwise, will throw exception "invalid credential".
	 * @return
	 */
	@RequestMapping(value = "/{shopId}/order/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getOrders(@PathVariable("shopId") String wxAccountId,
			@RequestParam("c") String cred,
			@RequestParam(value = "start", required = false) String strStart,
			@RequestParam(value = "end", required = false) String strEnd,
			@RequestParam(value = "before", required = false) String strBefore,
			@RequestParam(value = "count", required = false) Integer pageSize) {
		// shop user
		Date start = null, end = null;
		long before = 0;
		if (strStart != null && strEnd != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			try {
				start = DateTimeUtil.getDayBegin(simpleDateFormat
						.parse(strStart));
				end = DateTimeUtil.getDayEnd(simpleDateFormat.parse(strEnd));
			} catch (ParseException e) {
				return new Result().setErrCode(ErrorCode.INVALID_PARAM)
						.setErrMsg("Invalid date string");
			}
		} else if (strBefore != null && pageSize != null) {
			try {
				before = Long.valueOf(strBefore);
			} catch (NumberFormatException e) {
				return new Result().setErrCode(ErrorCode.INVALID_PARAM)
						.setErrMsg("Invalid timestamp string");
			}
			if (pageSize.intValue() < 1) {
				return new Result().setErrCode(ErrorCode.INVALID_PARAM)
						.setErrMsg("Invalid count");
			}
		}

		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds != null) {
			if (cred2AccountIds.contains(wxAccountId)) {
				List<WlsShopOrder> ordersByShop;
				Result result = new Result();
				if (start != null && end != null) {
					logger.debug(
							"getOrders(), wxAccountId={}, start={}, end={}",
							wxAccountId, start, end);
					ordersByShop = shopDao.getOrdersByShop(wxAccountId, start,
							end);
				} else if (before > 0 && pageSize != null) {
					logger.debug(
							"getOrders(), wxAccountId={}, before={}, count={}",
							wxAccountId, before, pageSize);
					ordersByShop = shopDao.getOrdersByShop(wxAccountId, before,
							pageSize.intValue());
					result.setDataTotalCount(shopDao
							.countOrdersByShop(wxAccountId));
				} else {
					logger.debug("getOrders(), wxAccountId={}", wxAccountId);
					ordersByShop = shopDao.getOrdersByShop(wxAccountId);
				}
				return result.setEntity(ordersByShop);
			} else {
				return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
			}
		}

		// vip user. Exception will be throwed if the cred still not valid.
		String cred2VipId = cred2VipId(cred);
		List<WlsShopOrder> ordersByVip;
		Result result = new Result();
		if (start != null && end != null) {
			logger.debug(
					"getOrders(), wxAccountId={}, vipId={}, start={}, end={}",
					wxAccountId, cred2VipId, start, end);
			ordersByVip = shopDao.getOrdersByShopAndVip(wxAccountId,
					cred2VipId, start, end);
		} else if (before > 0 && pageSize != null) {
			logger.debug(
					"getOrders(), wxAccountId={}, vipId={}, before={}, count={}",
					wxAccountId, cred2VipId, before, pageSize);
			ordersByVip = shopDao.getOrdersByShopAndVip(wxAccountId,
					cred2VipId, before, pageSize.intValue());
			result.setDataTotalCount(shopDao.countOrdersByShopAndVip(
					wxAccountId, cred2VipId));
		} else {
			logger.debug("getOrders(), wxAccountId={}, vipId={}", wxAccountId,
					cred2VipId);
			ordersByVip = shopDao
					.getOrdersByShopAndVip(wxAccountId, cred2VipId);
		}
		return result.setEntity(ordersByVip);
	}

	@RequestMapping(value = "/{shopId}/order/list/{timeRange}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getOrdersByRange(@PathVariable("shopId") String wxAccountId,
			@PathVariable String timeRange, @RequestParam("c") String cred) {
		Date now = new Date();
		Date start = getRangeBeginDate(now, timeRange);
		Date end = getRangeEndDate(now, timeRange);
		if (start == null || end == null) {
			return new Result().setErrCode(ErrorCode.INVALID_PARAM).setErrMsg(
					"Invalid time range.");
		}
		// shop user
		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds != null) {
			if (cred2AccountIds.contains(wxAccountId)) {
				logger.debug(
						"getOrdersByRange(), wxAccountId={}, start={}, end={}",
						wxAccountId, start, end);
				List<WlsShopOrder> ordersByShop = shopDao.getOrdersByShop(
						wxAccountId, start, end);
				return new Result().setEntity(ordersByShop);
			} else {
				return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
			}
		}

		// vip user. Exception will be throwed if the cred still not valid.
		String cred2VipId = cred2VipId(cred);
		logger.debug(
				"getOrdersByRange(), wxAccountId={}, vipId={}, start={}, end={}",
				wxAccountId, cred2VipId, start, end);
		List<WlsShopOrder> ordersByVip = shopDao.getOrdersByShopAndVip(
				wxAccountId, cred2VipId, start, end);
		return new Result().setEntity(ordersByVip);
	}

	/**
	 * <pre>
	 * POST /shop/order
	 * 
	 * Create new order. 
	 * 1. Check item inventory. 
	 * 2. Create order. 
	 * 3. Increase inventory. 
	 * 4. Check shop-vip relationship.
	 * </pre>
	 * 
	 * @param cred
	 *            Cred string may has two cases:<br>
	 *            1. Start with "novip_", this is not login user.<br>
	 *            2. Match vipId, this is vip user.<br>
	 *            Otherwise, will throw exception "invalid credential".
	 * @param order
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/order", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createOrder(@RequestParam("c") String cred,
			@RequestBody WlsShopOrder order) {
		String wxAccountId = order.getWlsWxAccountId();
		if (wxAccountId == null) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA);
		}

		if (isNonVip(cred)) {
			order.setVipId(cred);
		} else {
			String vipId = cred2VipId(cred);
			order.setVipId(vipId);
		}
		String orderId = UUIDUtil.uuid8();
		order.setId(orderId);

		// check item inventory.
		Map<String, Integer> outOfStock = new HashMap<String, Integer>();
		List<WlsShopItem> itemsInventory = new ArrayList<WlsShopItem>(order
				.getWlsShopOrderItems().size());
		for (WlsShopOrderItem orderItem : order.getWlsShopOrderItems()) {
			WlsShopItem item = shopDao.getItem(orderItem.getId().getItemId());
			if (item == null) {
				logger.info(
						"Update item inventory failed since the item is not exist. itemId={}",
						orderItem.getId().getItemId());
			} else if (item.getInventory() != INVENTORY_INF) {
				if (item.getInventory() < orderItem.getItemCount()) {
					outOfStock.put(item.getId(), item.getInventory());
				} else {
					item.setInventory(item.getInventory()
							- orderItem.getItemCount());
					itemsInventory.add(item);
				}
			}
		}
		if (!outOfStock.isEmpty()) {
			return new Result().setErrCode(ErrorCode.ITEM_OUT_OF_STOCK)
					.setEntity(outOfStock);
		}

		// Save order, set new generated orderId for order items.
		Date currentTime = DateTimeUtil.getGMTDate();
		for (WlsShopOrderItem orderItem : order.getWlsShopOrderItems()) {
			orderItem.getId().setOrderId(orderId);
			orderItem.setAppCreateTime(currentTime);
		}

		shopDao.saveOrder(order);

		// Send sms
		String PROP_SMS_ENABLED = "reminder_smsEnabled";
		String PROP_SMS_PHONENUM = "reminder_smsPhoneNum";
		WlsShopConfig cfgSmsEnabled = shopDao.getConfig(PROP_SMS_ENABLED,
				wxAccountId);
		WlsShopConfig cfgSmsPhoneNum = shopDao.getConfig(PROP_SMS_PHONENUM,
				wxAccountId);
		if (cfgSmsEnabled == null) {
			logger.error("Cannot load shop setting:{}", PROP_SMS_ENABLED);
		} else if (cfgSmsPhoneNum == null) {
			logger.error("Cannot load shop setting:{}", PROP_SMS_PHONENUM);
		} else {
			if (Boolean.valueOf(cfgSmsEnabled.getPropValue())) {
				String phoneNumber = cfgSmsPhoneNum.getPropValue();
				StringBuffer sbGoods = new StringBuffer();
				Iterator<WlsShopOrderItem> iteratorOrderItems = order
						.getWlsShopOrderItems().iterator();
				while (iteratorOrderItems.hasNext()) {
					WlsShopOrderItem item = iteratorOrderItems.next();
					sbGoods.append(item.getItemName()).append("x")
							.append(item.getItemCount()).append("\n");
				}
				smsService.sendOrderCreatedDetail(phoneNumber, order.getNo(),
						sbGoods.toString(), order.getItemTotalCount(),
						order.getItemTotalPrice(), order.getShippingName(),
						order.getShippingPhone(), order.getShippingAddress(),
						wxAccountId);
				logger.debug("Sent order created sms to {}, order no:{}",
						phoneNumber, order.getNo());
			} else {
				logger.debug(
						"Skip send sms for order created since shop setting reminder_smsEnabled={}",
						cfgSmsEnabled);
			}
		}

		// Update item inventory
		for (int i = 0; i < itemsInventory.size(); i++) {
			shopDao.saveItem(itemsInventory.get(i));
		}

		// Update shop-vip relationship.
		if (!isNonVip(cred)) {
			// update WlsShopVip if it doesn't exist.
			WlsShopVipId wlsShopVipId = new WlsShopVipId(order.getVipId(),
					wxAccountId);
			if (shopDao.getShopVip(wlsShopVipId) == null) {
				WlsShopVip wlsShopVip = new WlsShopVip(wlsShopVipId,
						DateTimeUtil.getGMTDate());
				shopDao.saveShopVip(wlsShopVip);
			}
		}

		return new Result().setEntity(order);
	}

	/**
	 * PUT /order/{orderId}<br>
	 * 
	 * Update an order.
	 * 
	 * @param orderId
	 * @param cred
	 *            Cred string may has two cases:<br>
	 *            1. Start with "novip_", this is not login user.<br>
	 *            2. Match vipId, this is vip user.<br>
	 *            Otherwise, will throw exception "invalid credential".
	 * @param order
	 * @return
	 * 
	 *         TODO: update order should update inventory also.
	 */
	@Transactional
	@RequestMapping(value = "/order/{orderId}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updateOrder(@PathVariable String orderId,
			@RequestParam("c") String cred, @RequestBody WlsShopOrder order) {
		if (!orderId.equals(order.getId())) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA);
		}

		if (isNonVip(cred)) {
			order.setVipId(cred);
		} else {
			String vipId = cred2VipId(cred);
			order.setVipId(vipId);
		}

		// We only support full-update for order, so delete all exists order
		// items first, then create new items.
		String hql = "delete from WlsShopOrderItem where id.orderId=:orderId";
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("orderId", order.getId());
		shopDao.execute(hql, params);

		// Set UUID for new order items.
		Date currentTime = DateTimeUtil.getGMTDate();
		for (WlsShopOrderItem orderItem : order.getWlsShopOrderItems()) {
			if (orderItem.getId() == null) {
				WlsShopOrderItemId id = new WlsShopOrderItemId(orderId,
						UUIDUtil.uuid8());
				orderItem.setId(id);
				orderItem.setAppCreateTime(currentTime);
			}
			orderItem.setAppLastModifyTime(currentTime);
		}

		shopDao.saveOrder(order);
		return new Result().setEntity(order);
	}

	/**
	 * <pre>
	 * PUT /shop/order/{orderId}/status/{status}
	 * 
	 * Update order status. 
	 * 1. Check privilege to update status.
	 * 2. Validate status.
	 * 3. Restore item inventory if status is completed abnormal.
	 * 4. Update user points if status is completed normal.
	 * </pre>
	 * 
	 * @param orderId
	 * @param status
	 * @param cred
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/order/{orderId}/status/{status}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updateOrderStatus(@PathVariable String orderId,
			@PathVariable String status, @RequestParam("c") String cred,
			@RequestBody(required = false) Map<String, Object> data) {
		logger.debug("updateOrderStatus, orderId={}, status={}, data={}",
				orderId, status, data);

		ShopOrderStatus newStatus;
		try {
			newStatus = ShopOrderStatus.valueOf(status);
		} catch (Exception e) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"Invalid status.");
		}

		if (isNonVip(cred)) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE).setErrMsg(
					"Please login to update order status.");
		}

		WlsShopOrder order = shopDao.getOrder(orderId);
		if (order == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND).setErrMsg(
					"Order doesn't exist.");
		}

		// only the following 4 status can be changed by this interface.
		ShopOrderStatus[] validNewStatus = new ShopOrderStatus[] {
				ShopOrderStatus.WAITING_SHIPMENT, ShopOrderStatus.SHIPPED,
				ShopOrderStatus.COMPLETED_NORMAL,
				ShopOrderStatus.COMPLETED_ABNORMAL };
		if (!Arrays.asList(validNewStatus).contains(newStatus)) {
			logger.error("ErrorCode:{}, Invalid status to change:{}",
					ErrorCode.INVALID_DATA, newStatus);
			return new Result().setErrCode(ErrorCode.INVALID_DATA).setErrMsg(
					"Invalid status to change.");
		}
		// new status should match the dependence of current status
		ShopOrderStatus curStatus = ShopOrderStatus.valueOf(order.getStatus());
		boolean validStatusDep = true;
		if (newStatus.equals(ShopOrderStatus.WAITING_SHIPMENT)
				&& !curStatus.equals(ShopOrderStatus.WAITING_PAYMENT)) {
			validStatusDep = false;
		} else if (newStatus.equals(ShopOrderStatus.SHIPPED)
				&& !curStatus.equals(ShopOrderStatus.WAITING_SHIPMENT)) {
			validStatusDep = false;
		} else if (newStatus.equals(ShopOrderStatus.COMPLETED_NORMAL)
				&& !curStatus.equals(ShopOrderStatus.SHIPPED)) {
			validStatusDep = false;
		} else if ((newStatus.equals(ShopOrderStatus.COMPLETED_ABNORMAL)
				|| newStatus
						.equals(ShopOrderStatus.COMPLETED_ABNORMAL_CUSTOMER) || newStatus
					.equals(ShopOrderStatus.COMPLETED_ABNORMAL_SHOP))
				&& !(curStatus.equals(ShopOrderStatus.WAITING_PAYMENT) || curStatus
						.equals(ShopOrderStatus.WAITING_SHIPMENT))) {
			validStatusDep = false;
		}
		if (validStatusDep == false) {
			logger.error("ErrorCode:{}, current status:{}, new status:{}",
					ErrorCode.ORDER_INVALID_STATUS_DEP, curStatus, newStatus);
			return new Result().setErrCode(ErrorCode.ORDER_INVALID_STATUS_DEP);
		}

		// check if shop user.
		String statusDesc = null;
		if (data != null) {
			statusDesc = String.valueOf(data.get("statusDesc"));
		}
		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (cred2AccountIds != null) {
			if (cred2AccountIds.contains(order.getWlsWxAccountId())) {
				order.setStatus(status);
				order.setStatusDesc(statusDesc);
				shopDao.saveOrder(order);
			} else {
				return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
			}
		} else {
			// finally, if not vip, throw exception.
			String vipId = cred2VipId(cred);
			if (vipId.equals(order.getVipId())) {
				order.setStatus(status);
				order.setStatusDesc(statusDesc);
				shopDao.saveOrder(order);
			} else {
				return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
			}
		}

		// ============================================================================
		// passed all validations, do update.
		ShopOrderStatus orderStatus = ShopOrderStatus.valueOf(status);
		// test_smsEnabled is a temporaory setting in dev pharse, to save SMS
		// quota.
		WlsShopConfig config = shopDao.getConfig("test_smsEnabled",
				order.getWlsWxAccountId());
		if (config == null || config.getPropValue().equals("true")) {
			// send sms
			if (ShopOrderStatus.SHIPPED.equals(orderStatus)) {
				// get shop information by order.
				config = shopDao.getConfig("base_name",
						order.getWlsWxAccountId());
				String shopName = "";
				if (config != null) {
					shopName = config.getPropValue();
				}
				smsService.sendOrderShippedShop(order.getShippingPhone(),
						order.getNo(), shopName, order.getWlsWxAccountId());
			}

			if (ShopOrderStatus.WAITING_SHIPMENT.equals(orderStatus)) {
				config = shopDao.getConfig("reminder_smsPhoneNum",
						order.getWlsWxAccountId());
				if (config != null) {
					smsService.sendOrderCreatedSimple(config.getPropValue(),
							getOrderUrl(order), order.getWlsWxAccountId());
				}

			}
		}

		// Restore item inventory if the order completed abnormal.
		if (status.startsWith(ShopOrderStatus.COMPLETED_ABNORMAL.toString())) {
			logger.debug(
					"Restore item inventory for order {}, since its status is {}",
					order.getId(), status);
			for (WlsShopOrderItem orderItem : order.getWlsShopOrderItems()) {
				WlsShopItem item = shopDao.getItem(orderItem.getId()
						.getItemId());
				if (item == null) {
					logger.info(
							"Restore item inventory failed, since the item doesn't exist. itemId={}",
							orderItem.getId().getItemId());
				} else if (item.getInventory() != INVENTORY_INF) {
					item.setInventory(item.getInventory()
							+ orderItem.getItemCount());
					shopDao.saveItem(item);
				}
			}
		} else if (ShopOrderStatus.COMPLETED_NORMAL.toString().equals(status)) {
			String vipId = cred2VipId(cred);
			recordPointsHistoryOfOrder(order, vipId);
			updateVipStatistics(order, vipId);
		}
		return new Result();

	}

	private void updateVipStatistics(WlsShopOrder order, String vipId) {
		WlsVipStatistics statistics = vipDao.getStatistics(vipId);
		if (statistics != null) {
			statistics.setTotalPoints(statistics.getTotalPoints()
					+ order.getGainPoints());
		} else {
			statistics = new WlsVipStatistics();
			statistics.setWlsVipId(vipId);
			statistics.setTotalPoints(order.getGainPoints());
		}
		vipDao.saveStatistics(statistics);
	}

	/**
	 * <pre>
	 * When an order was completed success, there are two situations cause user points change:
	 * 1. Gain points by order price.
	 * 2. Spend points if user exchanged some items.
	 * </pre>
	 * 
	 * @param order
	 * @param vipId
	 */
	private void recordPointsHistoryOfOrder(WlsShopOrder order, String vipId) {
		Date date = DateTimeUtil.getGMTDate();
		// Spend points if user exchanged some items.
		int spendPoints = 0;
		for (WlsShopOrderItem item : order.getWlsShopOrderItems()) {
			if (ShopOrderPayment.POINTS.name().equalsIgnoreCase(
					item.getSource())) {
				spendPoints += item.getItemPrice().shortValue();
			}
		}
		if (spendPoints > 0) {
			String pointsSource = VipPointsSource.ORDER_ITEM_EXCHANGE
					.toString();
			short points = (short) (spendPoints * -1);
			logger.debug(
					"Adjust vip points, source={},  orderId={}, vipId={}, points={}",
					pointsSource, order.getId(), vipId, points);
			WlsVipPointsHistoryId id = new WlsVipPointsHistoryId(
					UUIDUtil.uuid8(), vipId);
			WlsVipPointsHistory wlsVipPointsHistory = new WlsVipPointsHistory(
					id, points, pointsSource, order.getId(), date);
			vipDao.savePointsHistory(wlsVipPointsHistory);
		}

		// Gain points in order already decreased the spend points from all
		// points by order price. They are two pieces
		// of history, so we need calculate order price points.
		int orderPricePoints = order.getGainPoints() + spendPoints;
		if (orderPricePoints != 0) {
			String pointsSource = VipPointsSource.ORDER_PRICE.toString();
			logger.debug(
					"Adjust vip points, source={}, orderId={}, vipId={}, points={}",
					pointsSource, order.getId(), vipId, orderPricePoints);
			WlsVipPointsHistoryId id = new WlsVipPointsHistoryId(
					UUIDUtil.uuid8(), vipId);
			WlsVipPointsHistory wlsVipPointsHistory = new WlsVipPointsHistory(
					id, (short) orderPricePoints, pointsSource, order.getId(),
					date);
			vipDao.savePointsHistory(wlsVipPointsHistory);
		}
	}

	/**
	 * DELETE /shop/order/{orderId}<br>
	 * 
	 * Delete an order.
	 * 
	 * @param orderId
	 * @param cred
	 *            Cred string may has two cases:<br>
	 *            1. Start with "novip_", this is not login user.<br>
	 *            2. Match vipId, this is vip user.<br>
	 *            Otherwise, will throw exception "invalid credential".
	 * @return
	 */
	@RequestMapping(value = "/order/{orderId}", method = RequestMethod.DELETE, headers = "Content-type=application/json")
	public @ResponseBody
	Result deleteOrder(@PathVariable String orderId,
			@RequestParam("c") String cred) {
		WlsShopOrder order = shopDao.getOrder(orderId);
		if (order == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND);
		}
		String vipId = isNonVip(cred) ? cred : cred2VipId(cred);
		if (order.getVipId().equals(vipId)) {
			shopDao.deleteOrder(order);
			return new Result();
		} else {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
	}

	/**
	 * DELETE /shop/order/{orderId}/item/{itemId}<br>
	 * 
	 * Delete an item in order.
	 * 
	 * @param orderId
	 * @param itemId
	 * @param credCred
	 *            string may has two cases:<br>
	 *            1. Start with "novip_", this is not login user.<br>
	 *            2. Match vipId, this is vip user.<br>
	 *            Otherwise, will throw exception "invalid credential".
	 * @return
	 */
	@RequestMapping(value = "/order/{orderId}/item/{itemId}", method = RequestMethod.DELETE, headers = "Content-type=application/json")
	public @ResponseBody
	Result deleteOrder(@PathVariable String orderId,
			@PathVariable String itemId, @RequestParam("c") String cred) {
		WlsShopOrder order = shopDao.getOrder(orderId);
		if (order == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND);
		}

		WlsShopOrderItem foundItem = null;
		Set<WlsShopOrderItem> wlsShopOrderItems = order.getWlsShopOrderItems();
		for (WlsShopOrderItem item : wlsShopOrderItems) {
			if (item.getId().getItemId().equals(itemId)) {
				foundItem = item;
				break;
			}
		}
		if (foundItem == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND).setErrMsg(
					"Item not found in the order.");
		}

		// check priviliege.
		String vipId = isNonVip(cred) ? cred : cred2VipId(cred);
		if (order.getVipId().equals(vipId)) {
			shopDao.deleteOrderItem(foundItem);
			return new Result();
		} else {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
	}

	/**
	 * GET /shop/{shopId}/prom/points/list<br>
	 * 
	 * Get list of points promotion. This is public to anybody.
	 * 
	 * @param wxAccountId
	 * @return
	 */
	@RequestMapping(value = "/{shopId}/prom/points/list/{type}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	Result getPointsPromList(@PathVariable("shopId") String wxAccountId,
			@PathVariable String type) {
		boolean isFilterEnabled = "enabled".equals(type);
		List<WlsShopPromotionPoints> promotionPointsByShop = shopDao
				.getPromotionPointsByShop(wxAccountId, isFilterEnabled);
		return new Result().setEntity(promotionPointsByShop);
	}

	/**
	 * POST /shop/prom/points<br>
	 * Create new points promotion.
	 * 
	 * @param cred
	 *            Must be shop user.
	 * @param points
	 * @return
	 */
	@RequestMapping(value = "/prom/points", method = RequestMethod.POST, headers = "Content-type=application/json")
	public @ResponseBody
	Result createPointsProm(@RequestParam("c") String cred,
			@RequestBody WlsShopPromotionPoints points) {
		Set<String> wxAccountIds = cred2AccountIds(cred);
		if (wxAccountIds == null
				|| !wxAccountIds.contains(points.getWlsWxAccountId())) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		points.setId(UUIDUtil.uuid8());
		shopDao.savePromotionPoints(points);
		return new Result().setEntity(points);
	}

	/**
	 * PUT /shop/prom/points/{id}<br>
	 * 
	 * Update a points promotion.
	 * 
	 * @param id
	 * @param cred
	 *            Must be shop user.
	 * @param points
	 * @return
	 */
	@RequestMapping(value = "/prom/points/{id}", method = RequestMethod.PUT, headers = "Content-type=application/json")
	public @ResponseBody
	Result updatePointsProm(@PathVariable String id,
			@RequestParam("c") String cred,
			@RequestBody WlsShopPromotionPoints points) {
		Set<String> wxAccountIds = cred2AccountIds(cred);
		if (wxAccountIds == null) {
			return new Result().setErrCode(ErrorCode.INVALID_CRED);
		}
		if (!wxAccountIds.contains(points.getWlsWxAccountId())) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}
		if (!id.equals(points.getId())) {
			return new Result().setErrCode(ErrorCode.INVALID_DATA);
		}
		shopDao.savePromotionPoints(points);
		return new Result();
	}

	/**
	 * DELETE /shop/prom/points/{id}<br>
	 * 
	 * Delete a points promotion.
	 * 
	 * @param id
	 * @param cred
	 *            Must be shop user.
	 * @return
	 */
	@RequestMapping(value = "/prom/points/{id}", method = RequestMethod.DELETE, headers = "Content-type=application/json")
	public @ResponseBody
	Result deletePointsProm(@PathVariable String id,
			@RequestParam("c") String cred) {
		WlsShopPromotionPoints promotionPoints = shopDao.getPromotionPoints(id);
		if (promotionPoints == null) {
			return new Result().setErrCode(ErrorCode.NOT_FOUND);
		}

		Set<String> cred2AccountIds = cred2AccountIds(cred);
		if (!cred2AccountIds.contains(promotionPoints.getWlsWxAccountId())) {
			return new Result().setErrCode(ErrorCode.NO_PRIVILIEGE);
		}

		shopDao.deletePromotionPoints(promotionPoints);
		return new Result();
	}

	private boolean isNonVip(String cred) {
		return cred.startsWith(CRED_NOVIP);
	}

	/**
	 * Get corresponding URL for sinal order, saller may review the order by
	 * this. The URL will be sent via SMS, as short as better.
	 * 
	 * @param order
	 * @return
	 */
	private String getOrderUrl(WlsShopOrder order) {
		// TODO: implement getOrderUrl.
		return "not impled";
	}
}
