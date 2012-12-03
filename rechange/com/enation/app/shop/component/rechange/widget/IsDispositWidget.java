package com.enation.app.shop.component.rechange.widget;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.rechange.model.MemberWithdraw;
import com.enation.app.shop.component.rechange.service.IMemberWithdrawManager;
import com.enation.eop.sdk.user.IUserService;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.eop.sdk.widget.AbstractWidget;

/**
 * 验证提现挂件
 * @author xxs
 *2012-7-5下午9:35:08
 */
@Component("isDisposit")
public class IsDispositWidget extends AbstractWidget{
	private IMemberWithdrawManager memberWithdrawManager;

	@Override
	protected void config(Map<String, String> arg0) {
	}

	@Override
	protected void display(Map<String, String> arg0) {
		 IUserService userService = UserServiceFactory.getUserService();
		 Member member = userService.getCurrentMember();
		List<MemberWithdraw> list = memberWithdrawManager.getWithdrawByMemberId(member.getMember_id(), 0);
		if(list!=null && !list.isEmpty()){
			this.putData("isresult","false");
			this.putData("memberWithdraw",list.get(0));
		}else{
			this.putData("isresult","true");
		}
		
	}

	public IMemberWithdrawManager getMemberWithdrawManager() {
		return memberWithdrawManager;
	}

	public void setMemberWithdrawManager(
			IMemberWithdrawManager memberWithdrawManager) {
		this.memberWithdrawManager = memberWithdrawManager;
	}

}
