package com.enation.app.shop.component.bank.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.bank.model.MemberBank;
import com.enation.app.shop.component.bank.service.IMemberBankManager;
import com.enation.eop.sdk.database.BaseSupport;

@Component
public class MemberBankManager extends BaseSupport implements
		IMemberBankManager {

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(MemberBank memberBank) {
		memberBank.setCreatetime(com.enation.framework.util.DateUtil.getDatelineLong());
		memberBank.setUpdatetime(com.enation.framework.util.DateUtil.getDatelineLong());
		this.baseDaoSupport.insert("member_bank", memberBank);
		
	}

	@Override
	public MemberBank getBankByMemberId(Integer memberid) {
		String sql  ="select * from  member_bank where memberid=? ";
		List<MemberBank> memberBankList = this.baseDaoSupport.queryForList(sql, MemberBank.class,memberid );
		if(memberBankList== null || memberBankList.isEmpty())	{
			return null;
		}		
		else
		{
			return memberBankList.get(0);
		}
	}


	@Override
	public MemberBank getBankByBankId(Integer bankid) {
		String sql  ="select * from  member_bank where bankid=? ";
		List<MemberBank> memberBankList = this.baseDaoSupport.queryForList(sql, MemberBank.class,bankid );
		if(memberBankList== null || memberBankList.isEmpty())	{
			return null;
		}		
		else
		{
			return memberBankList.get(0);
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(MemberBank memberBank) {
		String sql  ="update member_bank set banknum=?,bankname=?,openname=?,bankcity=?,updatetime=?,state=?  where bankid=? ";
		this.baseDaoSupport.execute(sql,memberBank.getBanknum(),memberBank.getBankname(),memberBank.getOpenname(),memberBank.getBankcity(),memberBank.getUpdatetime(),memberBank.getState(),memberBank.getBankid());
		System.out.println("修改银行卡信息完成");
	}

	@Override
	public List<MemberBank> getBankListByMemberId(Integer memberid) {
		String sql  ="select * from  member_bank where memberid=? ";
		return this.baseDaoSupport.queryForList(sql, MemberBank.class,memberid );
	}

	@Override
	public MemberBank getLastUpdate(Integer memberid) {
		String sql  ="select * from  member_bank where memberid=? order by updatetime desc";
		List<MemberBank> memberBankList = this.baseDaoSupport.queryForList(sql, MemberBank.class,memberid );
		if(memberBankList== null || memberBankList.isEmpty())	{
			return null;
		}		
		else
		{
			return memberBankList.get(0);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void cleanBankDefaultState(Integer memberid) {
		String sql  ="update member_bank set state=0 where memberid=? ";
		this.baseDaoSupport.execute(sql,memberid);
		System.out.println("修改银行卡信息完成");
		
	}

	@Override
	public MemberBank getBankDefault(Integer memberid) {
		String sql  ="select * from  member_bank where state=1 and  memberid=?";
		List<MemberBank> memberBankList = this.baseDaoSupport.queryForList(sql, MemberBank.class,memberid );
		if(memberBankList== null || memberBankList.isEmpty())	{
			return null;
		}		
		else
		{
			return memberBankList.get(0);
		}
	}
	
}
