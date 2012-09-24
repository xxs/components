package com.enation.app.shop.component.rechange.model;

import com.enation.framework.database.PrimaryKeyField;


/***
 * 折扣率
 * @author xxs
 *
 */
public class GoodsDiscount {
	
	private Integer discountid;
	private Integer catid;
	private Integer memberid;
	private Double discount;
	private String code;
	private int state;
	private long starttime;
	private long endtime;
	private long createtime;
	private long updatetime;
	
	@PrimaryKeyField
	public Integer getDiscountid() {
		return discountid;
	}
	public void setDiscountid(Integer discountid) {
		this.discountid = discountid;
	}
	public Integer getMemberid() {
		return memberid;
	}
	public void setMemberid(Integer memberid) {
		this.memberid = memberid;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
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
	public Integer getCatid() {
		return catid;
	}
	public void setCatid(Integer catid) {
		this.catid = catid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
