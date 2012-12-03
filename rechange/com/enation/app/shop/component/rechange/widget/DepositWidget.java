package com.enation.app.shop.component.rechange.widget;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.rechange.model.MemberBank;
import com.enation.app.shop.component.rechange.model.MemberWithdraw;
import com.enation.app.shop.component.rechange.service.IMemberBankManager;
import com.enation.app.shop.component.rechange.service.IMemberWithdrawManager;
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
@Component("deposit")
public class DepositWidget extends AbstractWidget implements IAjaxExecuteEnable{
	private IMemberBankManager memberBankManager;
	private IMemberManager memberManager;
	private IAdvanceLogsManager advanceLogsManager;
	private IMemberWithdrawManager memberWithdrawManager;
	@Override
	protected void config(Map<String, String> arg0) {
	}

	@Override
	protected void display(Map<String, String> arg0) {
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		Member member  = UserServiceFactory.getUserService().getCurrentMember();
		String action = request.getParameter("action");	
		String message = request.getParameter("message");	
		Double money =StringUtil.toDouble(request.getParameter("money"),true);	
		
		AdvanceLogs advanceLogs = new AdvanceLogs();
		
		List advanceLogsList =advanceLogsManager.listAdvanceLogsByMemberId(member.getMember_id());
		if("apply".equals(action)){
			advanceLogs.setMember_id(member.getMember_id());
			advanceLogs.setMoney(money);
			advanceLogs.setMessage(message);
			advanceLogs.setMtime(Long.valueOf(System.currentTimeMillis()));
			advanceLogs.setMemo("会员自主提现");
			advanceLogs.setOrder_id(1);
			advanceLogs.setPayment_id(0);
			advanceLogs.setImport_money(0.000);
			advanceLogs.setExplode_money(money);
			if(advanceLogsList!=null){
				AdvanceLogs adl = (AdvanceLogs)advanceLogsList.get(0);
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
			Integer memberbank_id = StringUtil.toInt(request.getParameter("memberbank_id"), true);
			MemberWithdraw memberWithdraw = new MemberWithdraw();
			memberWithdraw.setMemberid(member.getMember_id());
			memberWithdraw.setMessage(message);
			memberWithdraw.setMoney(money);
			memberWithdraw.setCreatetime(Long.valueOf(System.currentTimeMillis()));
			memberWithdraw.setMemo("用户提现申请");
			memberWithdraw.setState(0);
			memberWithdraw.setMemberid(memberbank_id);
			memberWithdraw.setUpdatetime(Long.valueOf(System.currentTimeMillis()));
			memberWithdrawManager.add(memberWithdraw);
		}

		MemberBank memberBank = memberBankManager.getBankByMemberId(member.getMember_id());
		Member newMember = this.memberManager.get(member.getMember_id());
		this.putData("memberBank",memberBank);
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
