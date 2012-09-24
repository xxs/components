package com.enation.app.shop.component.rechange.model;

import com.enation.framework.database.PrimaryKeyField;


/***
 * 银行卡信息
 * @author xxs
 *
 */
public class MemberBank {
	
	private static final long serialVersionUID = 1L;
	private Integer bankid;
	private Integer memberid;
	private String banknum;
	private String bankname;
	private String openname;
	private String bankdetail;
	private String memo;
	private String bankcity;
	private Integer state;
	private Long createtime;
	private Long updatetime;
	public String getBanknum() {
		return banknum;
	}
	public void setBanknum(String banknum) {
		this.banknum = banknum;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBankcity() {
		return bankcity;
	}
	public void setBankcity(String bankcity) {
		this.bankcity = bankcity;
	}
	@PrimaryKeyField
	public Integer getBankid() {
		return bankid;
	}
	public void setBankid(Integer bankid) {
		this.bankid = bankid;
	}
	public Integer getMemberid() {
		return memberid;
	}
	public void setMemberid(Integer memberid) {
		this.memberid = memberid;
	}
	public String getOpenname() {
		return openname;
	}
	public void setOpenname(String openname) {
		this.openname = openname;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public Long getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Long updatetime) {
		this.updatetime = updatetime;
	}
	public String getBankdetail() {
		return bankdetail;
	}
	public void setBankdetail(String bankdetail) {
		this.bankdetail = bankdetail;
	}
	
	
}
