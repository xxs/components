package com.enation.app.shop.component.rechange.action;

import java.util.Date;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.framework.action.WWAction;




@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Results({ 
	 @Result(name="list", location="/shop/admin/withdraw/countlist.jsp")
})
public class CountAction extends WWAction {
	
	private static final long serialVersionUID = 3482231193926639454L;

	private IGoodsDiscountManager goodsDiscountManager;
	
	private Date beginDate;
	private Date endDate;
	
	private int result;
	
	public String execute(){
		if(null!=beginDate && null!= endDate){
			result = goodsDiscountManager.gettotlenum(beginDate,endDate);
		}else{
			result = goodsDiscountManager.gettotlenum(new Date(),new Date());
		}
		return "list";
	}
	public IGoodsDiscountManager getGoodsDiscountManager() {
		return goodsDiscountManager;
	}

	public void setGoodsDiscountManager(IGoodsDiscountManager goodsDiscountManager) {
		this.goodsDiscountManager = goodsDiscountManager;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
