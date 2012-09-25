package com.enation.app.shop.component.bank.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.bank.model.MemberWithdraw;
import com.enation.app.shop.component.bank.service.IMemberWithdrawManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;



@Component
public class MemberWithdrawManager extends BaseSupport implements
		IMemberWithdrawManager {

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(MemberWithdraw memberWithdraw) {
		System.out.println("添加提现单");
		String ff = this.createSn();
		System.out.println("单据号为："+ff);
		memberWithdraw.setSn(ff);
		memberWithdraw.setCreatetime(com.enation.framework.util.DateUtil.getDatelineLong());
		memberWithdraw.setUpdatetime(com.enation.framework.util.DateUtil.getDatelineLong());
		this.baseDaoSupport.insert("member_withdraw", memberWithdraw);
		System.out.println("添加体现单成功");
	}

	@Override
	public List<MemberWithdraw> getWithdrawByMemberId(Integer memberid) {
		String sql  ="select * from  member_withdraw where memberid=? ";
		return this.baseDaoSupport.queryForList(sql, MemberWithdraw.class,memberid );
		
	}

	@Override
	public MemberWithdraw getWithdrawByWithdrawId(Integer withdrawid) {
		String sql  ="select * from  member_withdraw where withdrawid=? ";
		List<MemberWithdraw> memberWithdrawList = this.baseDaoSupport.queryForList(sql, MemberWithdraw.class,withdrawid );
		if(memberWithdrawList== null || memberWithdrawList.isEmpty())	{
			return null;
		}		
		else
		{
			return memberWithdrawList.get(0);
		}
	}

	@Override
	public MemberWithdraw getWithdrawBySN(String sn) {
		String sql  ="select * from  member_withdraw where sn=? ";
		List<MemberWithdraw> memberWithdrawList = this.baseDaoSupport.queryForList(sql, MemberWithdraw.class,sn );
		if(memberWithdrawList== null || memberWithdrawList.isEmpty())	{
			return null;
		}		
		else
		{
			return memberWithdrawList.get(0);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(Integer state,Integer withdrawid) {
		System.out.println("修改提现单信息");
		String sql  ="update member_withdraw set state=? where withdrawid=? ";
		this.baseDaoSupport.execute(sql,state,withdrawid);
		System.out.println("修改提现单信息完成");
		
	}
	private String createSn() {
		Date now = new Date();
		String sn = "tx"+com.enation.framework.util.DateUtil.toString(now,
				"yyyyMMddhhmmss");
		return sn;
	}

	@Override
	public Page list(int pageNo, int pageSize, int status,String order)
	  {
	     order = StringUtil.isEmpty(order) ? "withdrawid desc" : order;
	     String sql = "";
	     if(status == -100){
	    	  sql = new StringBuilder().append("select * from member_withdraw where 1=1 ").toString();     
	    	 
	     }else{
	    	  sql = new StringBuilder().append("select * from member_withdraw where state=").append(status).toString();     
	    	 
	     }
	     sql = new StringBuilder().append(sql).append(" order by ").append(order).toString();
	     System.out.println(sql);
	     Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize, MemberWithdraw.class, new Object[0]);
	     return page;
	   }

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAdvance(Double advance,Integer memberid) {
		System.out.println("修改用户预存款的余额");
		String sql  ="update member set advance=? where member_id=? ";
		this.baseDaoSupport.execute(sql,advance,memberid);
		System.out.println("修改用户预存款的余额完成");
	}

}
