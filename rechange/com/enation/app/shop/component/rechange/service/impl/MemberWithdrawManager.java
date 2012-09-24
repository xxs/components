package com.enation.app.shop.component.rechange.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.rechange.model.MemberWithdraw;
import com.enation.app.shop.component.rechange.service.IMemberWithdrawManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;




@Component
public class MemberWithdrawManager extends BaseSupport implements
		IMemberWithdrawManager {

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(MemberWithdraw memberWithdraw) {
		memberWithdraw.setCreatetime(Long.valueOf(System.currentTimeMillis()));
		this.baseDaoSupport.insert("member_withdraw", memberWithdraw);
	}

	@Override
	public List<MemberWithdraw> getWithdrawByMemberId(Integer memberid) {
		String sql  ="select * from  member_withdraw where memberid=? ";
		return this.baseDaoSupport.queryForList(sql, MemberWithdraw.class,memberid );
		
	}
	
	@Override
	public List<MemberWithdraw> getWithdrawByMemberId(Integer memberid,Integer state) {
		String sql  ="select * from  member_withdraw where memberid=? and state=? order by createtime desc";
		return this.baseDaoSupport.queryForList(sql, MemberWithdraw.class,memberid,state );
		
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
		String sql  ="update member_withdraw set state=? ,updatetime=? where withdrawid=? ";
		this.baseDaoSupport.execute(sql,state,Long.valueOf(System.currentTimeMillis()),withdrawid);
		
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
	     Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize, MemberWithdraw.class, new Object[0]);
	     return page;
	   }

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAdvance(Double advance,Integer memberid) {
		String sql  ="update member set advance=? where member_id=? ";
		this.baseDaoSupport.execute(sql,advance,memberid);
	}

}
