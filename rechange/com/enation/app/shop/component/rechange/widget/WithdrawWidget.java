package com.enation.app.shop.component.rechange.widget;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.rechange.model.LossRate;
import com.enation.app.shop.component.rechange.model.MemberBank;
import com.enation.app.shop.component.rechange.model.MemberWithdraw;
import com.enation.app.shop.component.rechange.service.ILossRateManager;
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
@Component("withdraw")
public class WithdrawWidget extends AbstractWidget implements IAjaxExecuteEnable{
	private IMemberBankManager memberBankManager;
	private IMemberManager memberManager;
	private IAdvanceLogsManager advanceLogsManager;
	private IMemberWithdrawManager memberWithdrawManager;
	private ILossRateManager lossRateManager;
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
		LossRate lossRate = lossRateManager.get();    //获取当前的折扣率
		this.putData("lossRate",lossRate);
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
			try{
				Integer bankid = StringUtil.toInt(request.getParameter("bankid"), true);
				MemberWithdraw memberWithdraw = new MemberWithdraw();
				memberWithdraw.setMemberid(member.getMember_id());
				memberWithdraw.setMessage(message);
				memberWithdraw.setMoney(money);
				if(lossRate.getLossrate()==0 || null == lossRate || null == lossRate.getLossrate()){
					memberWithdraw.setLossrate((double) 0);//放入折扣率
					memberWithdraw.setTotalmoney(money);
				}else{
					memberWithdraw.setLossrate(lossRate.getLossrate());//放入折扣率
					memberWithdraw.setTotalmoney(money*(1-lossRate.getLossrate()));//计算折扣后的金额
				}
				memberWithdraw.setMemo("用户提现申请");
				memberWithdraw.setState(0);
				memberWithdraw.setBankid(bankid);
				String orderSn = this.createSn();
				memberWithdraw.setSn(orderSn);
				memberWithdrawManager.add(memberWithdraw);
				this.putData("memberWithdraw",memberWithdraw);
				this.setPageFolder("member");
				this.setPageName("member_deposit");
			}catch(RuntimeException e){
				this.showJson("{result:0,message:'"+e.getMessage()+"'}");
			}

		}
		List<MemberBank> memberBankList = memberBankManager.getBankListByMemberId(member.getMember_id());
		Member newMember = this.memberManager.get(member.getMember_id());
		List<MemberWithdraw> listMW = memberWithdrawManager.getWithdrawByMemberId(member.getMember_id(), 0);//获取到状态为零（提现中）的申请单
		if(listMW!=null && !listMW.isEmpty()){
			this.putData("isresult","false");
			this.putData("memberWithdraw",listMW.get(0));
		}else{
			this.putData("isresult","true");
		}
		this.putData("memberBankList",memberBankList);
		this.putData("member",newMember);
		
	}
	@Override
	public String execute() {
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		Member member  = UserServiceFactory.getUserService().getCurrentMember();
		String action = request.getParameter("action");	
		String message = request.getParameter("message");	
		Double money =StringUtil.toDouble(request.getParameter("money"),true);
		if("withdraw".equals(action)){
			try{
				Integer bankid = StringUtil.toInt(request.getParameter("bankid"), true);
				MemberWithdraw memberWithdraw = new MemberWithdraw();
				memberWithdraw.setMemberid(member.getMember_id());
				memberWithdraw.setMessage(message);
				memberWithdraw.setMoney(money);
				memberWithdraw.setMemo("用户提现申请");
				memberWithdraw.setState(0);
				memberWithdraw.setBankid(bankid);
				String orderSn = this.createSn();
				memberWithdraw.setSn(orderSn);
				memberWithdrawManager.add(memberWithdraw);
				this.putData("memberWithdraw",memberWithdraw);
				this.setPageFolder("member");
				this.setPageName("member_deposit");
				this.showJson("{result:1,ordersn:"+memberWithdraw.getSn()+"}");
			}catch(RuntimeException e){
				this.showJson("{result:0,message:'"+e.getMessage()+"'}");
			}
		}
		return null;
		
	}
	/**
	 * 生成提现单据号
	 */
	private String createSn() {
		Date now = new Date();
		String sn = "tx"+com.enation.framework.util.DateUtil.toString(now,
				"yyyyMMddhhmmss");
		return sn;
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

	public ILossRateManager getLossRateManager() {
		return lossRateManager;
	}

	public void setLossRateManager(ILossRateManager lossRateManager) {
		this.lossRateManager = lossRateManager;
	}
	
}
