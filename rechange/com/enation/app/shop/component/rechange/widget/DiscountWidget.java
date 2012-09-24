package com.enation.app.shop.component.rechange.widget;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.component.rechange.model.GoodsDiscount;
import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.eop.sdk.widget.AbstractWidget;



/**
 * 收卡折扣
 * @author xxs
 *2012-7-5下午9:35:08
 */
@Component("discount")
public class DiscountWidget extends AbstractWidget{
	private IGoodsDiscountManager goodsDiscountManager;
	@Override
	protected void config(Map<String, String> arg0) {
	}

	@Override
	protected void display(Map<String, String> arg0) {
		GoodsDiscount goodsDiscount = goodsDiscountManager.get();
		this.putData("goodsDiscount",goodsDiscount);
		
	}

	public IGoodsDiscountManager getGoodsDiscountManager() {
		return goodsDiscountManager;
	}

	public void setGoodsDiscountManager(IGoodsDiscountManager goodsDiscountManager) {
		this.goodsDiscountManager = goodsDiscountManager;
	}	
	
}
