package com.enation.app.shop.component.rechange.model;

import com.enation.framework.database.PrimaryKeyField;

/***
 * 充值卡信息
 * @author xxs
 *
 */
public class RechangeLogs {
	
	private Integer logid;
	private Integer orderid;
	private Integer goodsid;
	private String keys;
	private String values;
	private Integer state;
	private Long createtime;
	
	@PrimaryKeyField
	public Integer getLogid() {
		return logid;
	}
	public void setLogid(Integer logid) {
		this.logid = logid;
	}
	public Integer getOrderid() {
		return orderid;
	}
	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}
	public Integer getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(Integer goodsid) {
		this.goodsid = goodsid;
	}
	public String getKeys() {
		return keys;
	}
	public void setKeys(String keys) {
		this.keys = keys;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}
	
	
}
