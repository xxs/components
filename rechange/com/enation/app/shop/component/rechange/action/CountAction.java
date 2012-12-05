package com.enation.app.shop.component.rechange.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.framework.action.WWAction;

@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Results({ @Result(name = "list", location = "/shop/admin/withdraw/countlist.jsp") })
public class CountAction extends WWAction {

	private static final long serialVersionUID = 3482231193926639454L;

	private IGoodsDiscountManager goodsDiscountManager;

	private Date beginDate;
	private Date endDate;

	private String begin;
	private String end;

	private int result;

	public String execute() {
		if (null != beginDate && null != endDate) {
			result = goodsDiscountManager.gettotlenum(beginDate, endDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			begin = sdf.format(beginDate);
			end = sdf.format(endDate);
		} else {
			result = goodsDiscountManager.gettotlenum(new Date(new Date().getTime()-24*60*60*1000), new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			begin = sdf.format(new Date(new Date().getTime()-24*60*60*1000));
			end = sdf.format(new Date());
		}
		return "list";
	}

	public IGoodsDiscountManager getGoodsDiscountManager() {
		return goodsDiscountManager;
	}

	public void setGoodsDiscountManager(
			IGoodsDiscountManager goodsDiscountManager) {
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

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
