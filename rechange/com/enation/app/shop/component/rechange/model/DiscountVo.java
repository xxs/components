package com.enation.app.shop.component.rechange.model;

import com.enation.app.shop.core.model.Cat;


/***
 * 折扣率实体 
 * @author xxs
 *
 */
public class DiscountVo {
	
	private GoodsDiscount goodsDiscount;
	private Cat cat;
	public GoodsDiscount getGoodsDiscount() {
		return goodsDiscount;
	}
	public void setGoodsDiscount(GoodsDiscount goodsDiscount) {
		this.goodsDiscount = goodsDiscount;
	}
	public Cat getCat() {
		return cat;
	}
	public void setCat(Cat cat) {
		this.cat = cat;
	}
	
}
