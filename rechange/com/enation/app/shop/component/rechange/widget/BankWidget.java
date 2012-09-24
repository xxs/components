package com.enation.app.shop.component.rechange.widget;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.rechange.model.MemberBank;
import com.enation.app.shop.component.rechange.service.IMemberBankManager;
import com.enation.app.shop.core.plugin.member.IMemberLoginEvent;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.eop.sdk.widget.AbstractWidget;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.IAjaxExecuteEnable;
import com.enation.framework.util.StringUtil;



/**
 * 充值卡结算挂件
 * @author xxs
 *2012-5-5下午9:35:08
 */
@Component("bank")
public class BankWidget extends AbstractWidget implements IAjaxExecuteEnable,IMemberLoginEvent{
	private IMemberBankManager memberBankManager;
	@Override
	protected void config(Map<String, String> arg0) {
	}

	@Override
	protected void display(Map<String, String> arg0) {
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		Member member  = UserServiceFactory.getUserService().getCurrentMember();
		String action = request.getParameter("action");	
		if("editBank".equals(action)){  //更新银行卡信息（暂时无此功能）
			MemberBank memberBank = memberBankManager.getBankByBankId(StringUtil.toInt(request.getParameter("bankid"),true));
			//判断默认逻辑
			Integer state = StringUtil.toInt(request.getParameter("state"),true);
			if(state==1){
				memberBankManager.cleanBankDefaultState(member.getMember_id());
				memberBank.setState(state);
			}else{
				memberBank.setState(state);
			}
			memberBank.setUpdatetime(Long.valueOf(System.currentTimeMillis()));
			this.memberBankManager.update(memberBank);
		}else if("addBank".equals(action)){    //添加银行卡信息
			MemberBank memberBank = new MemberBank();
			memberBank.setBankname(request.getParameter("bankname"));
			memberBank.setBanknum(request.getParameter("banknum"));
			memberBank.setOpenname(request.getParameter("openname"));
			memberBank.setBankcity(request.getParameter("bankcity"));
			memberBank.setBankdetail(request.getParameter("bankdetail"));
			//判断默认逻辑
			Integer state = StringUtil.toInt(request.getParameter("state"),true);
			if(state==1){
				memberBankManager.cleanBankDefaultState(member.getMember_id());
				memberBank.setState(state);
			}else{
				memberBank.setState(state);
			}
			memberBank.setMemberid(member.getMember_id());
			this.memberBankManager.add(memberBank);
		}else if("edit".equals(action)){    //加载编辑银行卡信息
			MemberBank memberBank = memberBankManager.getBankByBankId(StringUtil.toInt(request.getParameter("bankid"),true));
			this.putData("memberBank",memberBank);
		}
		List<MemberBank> memberBankList = memberBankManager.getBankListByMemberId(member.getMember_id());
		this.putData("memberBankList",memberBankList);
		this.putData("member",member);
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

	@Override
	public void onLogin(Member member, Long arg1) {
		List<MemberBank> memberBankList = memberBankManager.getBankListByMemberId(member.getMember_id());
		if(memberBankList == null || memberBankList.size()<=0){
			this.setPageFolder("member");
			this.setPageName("member_bank");
		}
		
	}

	
}
