package com.enation.app.shop.component.bank.widget;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.bank.model.MemberBank;
import com.enation.app.shop.component.bank.service.IMemberBankManager;
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
		System.out.println("do config function..................");
	}

	@Override
	protected void display(Map<String, String> arg0) {
		System.out.println("do display function..................");
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		Member member  = UserServiceFactory.getUserService().getCurrentMember();
		String action = request.getParameter("action");	
		if("editBank".equals(action)){  //更新银行卡信息（暂时无此功能）
			System.out.println("开始更新");
			MemberBank memberBank = memberBankManager.getBankByBankId(StringUtil.toInt(request.getParameter("bankid"),true));
			memberBank.setBankname(request.getParameter("bankname"));
			memberBank.setBanknum(request.getParameter("banknum"));
			memberBank.setOpenname(request.getParameter("openname"));
			memberBank.setBankcity(request.getParameter("bankcity"));
			//判断默认逻辑
			Integer state = StringUtil.toInt(request.getParameter("state"),true);
			System.out.println("state:"+state);
			if(state==1){
				memberBankManager.cleanBankDefaultState(member.getMember_id());
				memberBank.setState(state);
			}else{
				memberBank.setState(state);
			}
			memberBank.setUpdatetime(com.enation.framework.util.DateUtil.getDatelineLong());
			this.memberBankManager.update(memberBank);
		}else if("addBank".equals(action)){    //添加银行卡信息
			MemberBank memberBank = new MemberBank();
			memberBank.setBankname(request.getParameter("bankname"));
			memberBank.setBanknum(request.getParameter("banknum"));
			memberBank.setOpenname(request.getParameter("openname"));
			memberBank.setBankcity(request.getParameter("bankcity"));
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
			System.out.println("将要更新的bankid为："+StringUtil.toInt(request.getParameter("bankid"),true));
			MemberBank memberBank = memberBankManager.getBankByBankId(StringUtil.toInt(request.getParameter("bankid"),true));
			System.out.println(memberBank.getOpenname());
			this.putData("memberBank",memberBank);
		}
		List<MemberBank> memberBankList = memberBankManager.getBankListByMemberId(member.getMember_id());
		System.out.println("银行卡数量为："+memberBankList.size());
		this.putData("memberBankList",memberBankList);
		this.putData("member",member);
		System.out.println("执行完成");
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
		System.out.println("list size"+memberBankList.size());
		if(memberBankList == null || memberBankList.size()<=0){
			System.out.println("没有银行卡，跳转银行卡设置页面");
			this.setPageFolder("member");
			this.setPageName("member_bank");
		}else{
			System.out.println("银行卡信息完整");
		}
		
	}

	
}
