/**
 * 
 */
package net.freecoder.restdemo.constant;

/**
 * This enum defines order status of shop module.
 * 
 * @author JiTing
 */
public enum ShopOrderStatus {
	WAITING_PAYMENT, WAITING_SHIPMENT, SHIPPED, RECEIVED, COMPLETED_NORMAL, COMPLETED_ABNORMAL, COMPLETED_ABNORMAL_SHOP, COMPLETED_ABNORMAL_CUSTOMER;
}
