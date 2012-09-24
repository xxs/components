package com.enation.app.shop.component.rechange.model;

import com.enation.framework.database.PrimaryKeyField;



/***
 * 会员提现申请
 * @author xxs
 *
 */
public class MemberWithdraw {
	
	private Integer withdrawid;
	private String sn;
	private Integer memberid;
	private Double money;
	private Double lossrate;
	private Double totalmoney;
	private String message;
	private Integer bankid;
	private String memo;
	private String rememo;
	private Integer state;
	private Long createtime;
	private Long updatetime;
	
	@PrimaryKeyField
	public Integer getWithdrawid() {
		return withdrawid;
	}

	public void setWithdrawid(Integer withdrawid) {
		this.withdrawid = withdrawid;
	}
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Integer getMemberid() {
		return memberid;
	}
	public void setMemberid(Integer memberid) {
		this.memberid = memberid;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getBankid() {
		return bankid;
	}

	public void setBankid(Integer bankid) {
		this.bankid = bankid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRememo() {
		return rememo;
	}

	public void setRememo(String rememo) {
		this.rememo = rememo;
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

	public Double getLossrate() {
		return lossrate;
	}

	public void setLossrate(Double lossrate) {
		this.lossrate = lossrate;
	}

	public Double getTotalmoney() {
		return totalmoney;
	}

	public void setTotalmoney(Double totalmoney) {
		this.totalmoney = totalmoney;
	}
	
	
}
