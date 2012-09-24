package com.enation.app.shop.component.rechange.service;

import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Order;


/**
 * 
 * @author xxs
 *2012-6-13下午4:20:48
 */
public interface IRechangeManager {
	
	
	/**
	 * 创建虚拟商品的订单
	 */
	public Order createOrder(Goods goods);
	/**
	 * 订单支付
	 */
	public void paySuccess(String ordersn, String tradeno);
	
	

}
