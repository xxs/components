package com.enation.app.shop.component.rechange.model;

import com.enation.framework.database.PrimaryKeyField;


/***
 * 提现率
 * @author xxs
 *
 */
public class LossRate {
	
	private Integer lossrateid;
	private Double lossrate;
	private int state;
	private long starttime;
	private long endtime;
	private long createtime;
	private long updatetime;
	
	@PrimaryKeyField
	public Integer getLossrateid() {
		return lossrateid;
	}
	public void setLossrateid(Integer lossrateid) {
		this.lossrateid = lossrateid;
	}
	public Double getLossrate() {
		return lossrate;
	}
	public void setLossrate(Double lossrate) {
		this.lossrate = lossrate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getStarttime() {
		return starttime;
	}
	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}
	public long getEndtime() {
		return endtime;
	}
	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(long updatetime) {
		this.updatetime = updatetime;
	}
	
	
}
