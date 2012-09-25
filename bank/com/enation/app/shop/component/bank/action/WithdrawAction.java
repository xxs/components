package com.enation.app.shop.component.bank.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.bank.model.MemberWithdraw;
import com.enation.app.shop.component.bank.service.IMemberWithdrawManager;
import com.enation.app.shop.core.model.AdvanceLogs;
import com.enation.app.shop.core.service.IAdvanceLogsManager;
import com.enation.app.shop.core.service.IMemberManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.database.Page;



/**
 * 提现操作action
 * @author xxs
 *
 */


@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Results({ 
	 @Result(name="list", location="/shop/admin/withdraw/list.jsp") ,
	 @Result(name="detail", location="/shop/admin/withdraw/detail.jsp") 
})
public class WithdrawAction extends WWAction {
	private IMemberWithdrawManager memberWithdrawManager;
	private IMemberManager memberManager;
	private IAdvanceLogsManager advanceLogsManager;
	private Member member;
	private MemberWithdraw memberWithdraw;
	private Integer withdrawid;
	private String order;
	private Integer state;
	private Integer targetaction;
	
	public String execute(){
		System.out.println("state:" + state);
		this.webpage = this.memberWithdrawManager.list(1,20, state, order);
		List<MemberWithdraw> list = (List<MemberWithdraw>)this.webpage.getResult();
		System.out.println("list size"+list.size());
		if(list.size()!=0&& list!=null){
			for (int i = 0; i < list.size(); i++) {
				MemberWithdraw g = (MemberWithdraw)list.get(i);
				System.out.println(g.getSn());
			}
		}
		System.out.println("num:"+this.webpage.getTotalCount());
		return "list";
	}
	
	public String detail(){
		this.memberWithdraw = memberWithdrawManager.getWithdrawByWithdrawId(withdrawid);
		return "detail";
	}
	public String dispose(){
		if(targetaction == 1){
			System.out.println("通过申请......");
			this.memberWithdraw = memberWithdrawManager.getWithdrawByWithdrawId(withdrawid);
			this.memberWithdraw.setState(targetaction);
			this.memberWithdrawManager.update(targetaction,this.memberWithdraw.getWithdrawid());
			member = (Member) memberManager.get(memberWithdraw.getMemberid());
			member.setAdvance(member.getAdvance()-this.memberWithdraw.getMoney());
			//--------------------提现记录表插入记录-----------------
			List advanceLogsList =advanceLogsManager.listAdvanceLogsByMemberId(member.getMember_id());
			AdvanceLogs advanceLogs = new AdvanceLogs();
			advanceLogs.setMember_id(member.getMember_id());
			advanceLogs.setMoney(this.memberWithdraw.getMoney());
			advanceLogs.setMessage(memberWithdraw.getRememo());
			advanceLogs.setMtime(com.enation.framework.util.DateUtil.getDatelineLong());
			advanceLogs.setMemo("会员自主提现");
			advanceLogs.setOrder_id(this.memberWithdraw.getWithdrawid());
			advanceLogs.setPayment_id(0);
			advanceLogs.setImport_money(0.000);
			advanceLogs.setExplode_money(this.memberWithdraw.getMoney());
			if(advanceLogsList!=null){
				AdvanceLogs adl = (AdvanceLogs)advanceLogsList.get(0);
				System.out.println("getShop_advance:"+adl.getShop_advance());
				System.out.println("getMember_advance:"+adl.getMember_advance());
				advanceLogs.setShop_advance(adl.getShop_advance()-this.memberWithdraw.getMoney());
				advanceLogs.setMember_advance(adl.getMember_advance()-this.memberWithdraw.getMoney());
			}else{
				advanceLogs.setShop_advance(-this.memberWithdraw.getMoney());
				advanceLogs.setMember_advance(-this.memberWithdraw.getMoney());
			}
			advanceLogs.setDisabled("false");  //状态为申请状态，为false则为已起效。
			advanceLogsManager.add(advanceLogs);
			this.memberWithdrawManager.updateAdvance(member.getAdvance(),member.getMember_id());
			System.out.println("通过申请结束......");
		}else if(targetaction == 2){
			this.memberWithdraw = memberWithdrawManager.getWithdrawByWithdrawId(withdrawid);
			this.memberWithdraw.setState(targetaction);
			this.memberWithdrawManager.update(targetaction,this.memberWithdraw.getWithdrawid());
		}
		System.out.println("订单中的用户ID："+memberWithdraw.getMemberid());
		member = (Member) memberManager.get(memberWithdraw.getMemberid());
		return "detail";
	}

	public IMemberWithdrawManager getMemberWithdrawManager() {
		return memberWithdrawManager;
	}

	public void setMemberWithdrawManager(
			IMemberWithdrawManager memberWithdrawManager) {
		this.memberWithdrawManager = memberWithdrawManager;
	}

	public Page getWebpage() {
		return webpage;
	}

	public void setWebpage(Page webpage) {
		this.webpage = webpage;
	}

	public MemberWithdraw getMemberWithdraw() {
		return memberWithdraw;
	}

	public void setMemberWithdraw(MemberWithdraw memberWithdraw) {
		this.memberWithdraw = memberWithdraw;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getWithdrawid() {
		return withdrawid;
	}

	public void setWithdrawid(Integer withdrawid) {
		this.withdrawid = withdrawid;
	}

	public Integer getTargetaction() {
		return targetaction;
	}

	public void setTargetaction(Integer targetaction) {
		this.targetaction = targetaction;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public IAdvanceLogsManager getAdvanceLogsManager() {
		return advanceLogsManager;
	}

	public void setAdvanceLogsManager(IAdvanceLogsManager advanceLogsManager) {
		this.advanceLogsManager = advanceLogsManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}
	
	
}
