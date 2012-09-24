package com.enation.app.shop.component.rechange.service;

import java.util.List;

import com.enation.app.shop.component.rechange.model.MemberBank;



public interface IMemberBankManager {
	public void add(MemberBank memberBank);
	
	public MemberBank getBankByMemberId(Integer memberid);

	public List<MemberBank> getBankListByMemberId(Integer memberid);
	
	public 	MemberBank getBankByBankId(Integer bankid);

	public 	MemberBank getLastUpdate(Integer memberid);
	
	public 	void update(MemberBank memberBank);
	/**
	 * 清除银行卡默认设置（便于重新设定）
	 * @param memberBank
	 */
	public 	void cleanBankDefaultState(Integer memberid);
	/**
	 * 获取默认银行卡
	 * @param memberBank
	 */
	public 	MemberBank getBankDefault(Integer memberid);
}
