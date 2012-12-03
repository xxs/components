package com.enation.app.shop.component.rechange.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.rechange.model.MemberBank;
import com.enation.app.shop.component.rechange.model.MemberWithdraw;
import com.enation.app.shop.component.rechange.service.IMemberBankManager;
import com.enation.app.shop.component.rechange.service.IMemberWithdrawManager;
import com.enation.app.shop.core.model.AdvanceLogs;
import com.enation.app.shop.core.service.IAdvanceLogsManager;
import com.enation.app.shop.core.service.IMemberManager;
import com.enation.framework.action.WWAction;




@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Results({ 
	 @Result(name="list", location="/shop/admin/withdraw/list.jsp") ,
	 @Result(name="detail", location="/shop/admin/withdraw/detail.jsp") 
})
public class WithdrawAction extends WWAction {
	private IMemberWithdrawManager memberWithdrawManager;
	private IMemberManager memberManager;
	private MemberWithdraw memberWithdraw;
	private IMemberBankManager memberBankManager;
	private IAdvanceLogsManager advanceLogsManager;
	private Member member;
	private MemberBank memberBank;
	private String order;
	private Integer state;
	private Integer withdrawid;
	private Integer targetaction;
	
	public String execute(){
		if(this.page==0){
			this.page = 1;
		}
		if(null==state){
			this.webpage = this.memberWithdrawManager.list(this.page,10, -100, order);
		}else{
			this.webpage = this.memberWithdrawManager.list(this.page,10, state, order);
		}
		List<MemberWithdraw> list = (List<MemberWithdraw>)this.webpage.getResult();
		if(list.size()!=0&& list!=null){
			for (int i = 0; i < list.size(); i++) {
				MemberWithdraw g = (MemberWithdraw)list.get(i);
			}
		}
		return "list";
	}
	
	public String detail(){
		this.memberWithdraw = memberWithdrawManager.getWithdrawByWithdrawId(withdrawid);
		this.member = this.memberManager.get(this.memberWithdraw.getMemberid());
		this.memberBank = this.memberBankManager.getBankByBankId(this.memberWithdraw.getBankid());
		return "detail";
	}
	public String dispose(){
		try{
			if(targetaction == 1){
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
				advanceLogs.setMtime(Long.valueOf(System.currentTimeMillis()));
				advanceLogs.setMemo("会员自主提现");
				advanceLogs.setOrder_id(this.memberWithdraw.getWithdrawid());
				advanceLogs.setPayment_id(0);
				advanceLogs.setImport_money(0.000);
				advanceLogs.setExplode_money(this.memberWithdraw.getMoney());
				if(advanceLogsList!=null){
					AdvanceLogs adl = (AdvanceLogs)advanceLogsList.get(0);
					advanceLogs.setShop_advance(adl.getShop_advance()-this.memberWithdraw.getMoney());
					advanceLogs.setMember_advance(adl.getMember_advance()-this.memberWithdraw.getMoney());
				}else{
					advanceLogs.setShop_advance(-this.memberWithdraw.getMoney());
					advanceLogs.setMember_advance(-this.memberWithdraw.getMoney());
				}
				advanceLogs.setDisabled("false");  //状态为申请状态，为false则为已起效。
				advanceLogsManager.add(advanceLogs);
				this.memberWithdrawManager.updateAdvance(member.getAdvance(),member.getMember_id());
				this.showSuccessJson("审核成功");
			}else if(targetaction == 2){
				this.memberWithdraw = memberWithdrawManager.getWithdrawByWithdrawId(withdrawid);
				this.memberWithdraw.setState(targetaction);
				this.memberWithdrawManager.update(targetaction,this.memberWithdraw.getWithdrawid());
				this.showSuccessJson("操作成功");
			}
			member = (Member) memberManager.get(memberWithdraw.getMemberid());
			
		}catch(RuntimeException e){
			this.logger.error("操作出错", e);
			this.showErrorJson("操作出错:["+e.getMessage()+"]");
		}
		return this.JSON_MESSAGE;
	}

	public IMemberWithdrawManager getMemberWithdrawManager() {
		return memberWithdrawManager;
	}

	public void setMemberWithdrawManager(
			IMemberWithdrawManager memberWithdrawManager) {
		this.memberWithdrawManager = memberWithdrawManager;
	}


	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public MemberWithdraw getMemberWithdraw() {
		return memberWithdraw;
	}

	public void setMemberWithdraw(MemberWithdraw memberWithdraw) {
		this.memberWithdraw = memberWithdraw;
	}

	public IMemberBankManager getMemberBankManager() {
		return memberBankManager;
	}

	public void setMemberBankManager(IMemberBankManager memberBankManager) {
		this.memberBankManager = memberBankManager;
	}

	public MemberBank getMemberBank() {
		return memberBank;
	}

	public void setMemberBank(MemberBank memberBank) {
		this.memberBank = memberBank;
	}

	public Integer getWithdrawid() {
		return withdrawid;
	}

	public void setWithdrawid(Integer withdrawid) {
		this.withdrawid = withdrawid;
	}

	public IAdvanceLogsManager getAdvanceLogsManager() {
		return advanceLogsManager;
	}

	public void setAdvanceLogsManager(IAdvanceLogsManager advanceLogsManager) {
		this.advanceLogsManager = advanceLogsManager;
	}

	public Integer getTargetaction() {
		return targetaction;
	}

	public void setTargetaction(Integer targetaction) {
		this.targetaction = targetaction;
	}

}
