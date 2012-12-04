package com.enation.app.shop.component.rechange.service;

import java.util.Date;
import java.util.List;

import com.enation.app.shop.component.rechange.model.DiscountVo;
import com.enation.app.shop.component.rechange.model.GoodsDiscount;
import com.enation.app.shop.core.model.AdvanceLogs;



public interface IGoodsDiscountManager {

	public void saveDiscount(Integer[] cat_ids, Double[] cat_discounts,String[] codes);

	public void add(GoodsDiscount goodsDiscount);
	
	public GoodsDiscount get();
	
	public List<AdvanceLogs> getAdvanceLogsList(int top);
	
	public List<AdvanceLogs> list();
	
	public List<DiscountVo> getDiscountVoList();
	
	public GoodsDiscount getDiscountByCatId(Integer catId);
	//统计日成交额
	public int gettotlenum(Date begin,Date end);
}
