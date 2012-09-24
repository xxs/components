package com.enation.app.shop.component.rechange.service;

import java.util.List;

import com.enation.app.shop.component.rechange.model.MemberWithdraw;
import com.enation.framework.database.Page;




public interface IMemberWithdrawManager {
	public void add(MemberWithdraw memberWithdraw );
	
	public List<MemberWithdraw> getWithdrawByMemberId(Integer memberid);

	public List<MemberWithdraw> getWithdrawByMemberId(Integer memberid,Integer state);
	
	public 	MemberWithdraw getWithdrawByWithdrawId(Integer withdrawid);
	
	public 	MemberWithdraw getWithdrawBySN(String sn);

	public 	void update(Integer state,Integer withdrawid);
	
	public Page list(int pageNo, int pageSize, int status,String order);
	
	public void updateAdvance(Double advance,Integer memberid);

}
