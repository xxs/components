package com.enation.app.shop.component.bank.widget;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.bank.model.MemberBank;
import com.enation.app.shop.component.bank.model.MemberWithdraw;
import com.enation.app.shop.component.bank.service.IMemberBankManager;
import com.enation.app.shop.component.bank.service.IMemberWithdrawManager;
import com.enation.app.shop.core.model.AdvanceLogs;
import com.enation.app.shop.core.service.IAdvanceLogsManager;
import com.enation.app.shop.core.service.IMemberManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.eop.sdk.widget.AbstractWidget;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.IAjaxExecuteEnable;
import com.enation.framework.util.StringUtil;


/**
 * 充值和提现挂件
 * @author xxs
 *2012-7-5下午9:35:08
 */
@Component("withdraw")
public class WithdrawWidget extends AbstractWidget implements IAjaxExecuteEnable{
	private IMemberBankManager memberBankManager;
	private IMemberManager memberManager;
	private IAdvanceLogsManager advanceLogsManager;
	private IMemberWithdrawManager memberWithdrawManager;
	@Override
	protected void config(Map<String, String> arg0) {
		System.out.println("do config function..................");
	}

	@Override
	protected void display(Map<String, String> arg0) {
		System.out.println("do display function..................");
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		Member member  = UserServiceFactory.getUserService().getCurrentMember();
		if(member!=null){
			System.out.println("当前用户为："+member.getName());
		}
		String action = request.getParameter("action");	
		String message = request.getParameter("message");	
		Double money =StringUtil.toDouble(request.getParameter("money"),true);	
		
		AdvanceLogs advanceLogs = new AdvanceLogs();
		
		List advanceLogsList =advanceLogsManager.listAdvanceLogsByMemberId(member.getMember_id());
		if("apply".equals(action)){
			advanceLogs.setMember_id(member.getMember_id());
			advanceLogs.setMoney(money);
			advanceLogs.setMessage(message);
			advanceLogs.setMtime(com.enation.framework.util.DateUtil.getDatelineLong());
			advanceLogs.setMemo("会员自主提现");
			advanceLogs.setOrder_id(1);
			advanceLogs.setPayment_id(0);
			advanceLogs.setImport_money(0.000);
			advanceLogs.setExplode_money(money);
			if(advanceLogsList!=null){
				AdvanceLogs adl = (AdvanceLogs)advanceLogsList.get(0);
				System.out.println("getShop_advance:"+adl.getShop_advance());
				System.out.println("getMember_advance:"+adl.getMember_advance());
				advanceLogs.setShop_advance(adl.getShop_advance()+money);
				advanceLogs.setMember_advance(adl.getMember_advance()+money);
			}else{
				advanceLogs.setShop_advance(money);
				advanceLogs.setMember_advance(money);
			}
			advanceLogs.setDisabled("true");  //状态为申请状态，为false则为已起效。
			advanceLogsManager.add(advanceLogs);
		}else if("list".equals(action)){
			this.putData("advanceLogsList",advanceLogsList);
		}else if("withdraw".equals(action)){
			System.out.println("开始添加体现单");
			Integer bankid = StringUtil.toInt(request.getParameter("bankid"), true);
			MemberWithdraw memberWithdraw = new MemberWithdraw();
			memberWithdraw.setMemberid(member.getMember_id());
			memberWithdraw.setMessage(message);
			memberWithdraw.setMoney(money);
			memberWithdraw.setMemo("用户提现申请");
			memberWithdraw.setState(0);
			memberWithdraw.setBankid(bankid);
			memberWithdrawManager.add(memberWithdraw);
			System.out.println("结束添加体现单");
		}

		List<MemberBank> memberBankList = memberBankManager.getBankListByMemberId(member.getMember_id());
		if(memberBankList != null){
			System.out.println("用户"+member.getName()+"有"+memberBankList.size()+"张银行卡");
			for (int i = 0; i < memberBankList.size(); i++) {
				MemberBank mm =  memberBankList.get(i);
				System.out.println("银行卡号为："+mm.getBanknum());
			}
		}else{
			System.out.println("无银行卡信息");
		}
		Member newMember = this.memberManager.get(member.getMember_id());
		this.putData("memberBankList",memberBankList);
		this.putData("member",newMember);
		
	}

	@Override
	public String execute() {
		return null;
	}

	public IMemberBankManager getMemberBankManager() {
		return memberBankManager;
	}

	public void setMemberBankManager(IMemberBankManager memberBankManager) {
		this.memberBankManager = memberBankManager;
	}

	public IAdvanceLogsManager getAdvanceLogsManager() {
		return advanceLogsManager;
	}

	public void setAdvanceLogsManager(IAdvanceLogsManager advanceLogsManager) {
		this.advanceLogsManager = advanceLogsManager;
	}

	public IMemberWithdrawManager getMemberWithdrawManager() {
		return memberWithdrawManager;
	}

	public void setMemberWithdrawManager(
			IMemberWithdrawManager memberWithdrawManager) {
		this.memberWithdrawManager = memberWithdrawManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}



	
	
}
