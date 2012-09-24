package com.enation.app.shop.component.rechange.plugin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.rechange.model.MemberBank;
import com.enation.app.shop.component.rechange.service.IMemberBankManager;
import com.enation.app.shop.core.plugin.member.IMemberTabShowEvent;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.plugin.IAjaxExecuteEnable;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;





/**
 * 银行卡管理插件
 * @author xxs
 * 2012-6-14下午20:32:11
 */
@Component
public class BankAdminPlugin extends AutoRegisterPlugin implements
				IMemberTabShowEvent,IAjaxExecuteEnable  {
	private IMemberBankManager memberBankManager;
	@Override
	public boolean canBeExecute(Member arg0) {
		return true;
	}

	@Override
	public int getOrder() {
		return 200;
	}

	@Override
	public String getTabName(Member arg0) {
		return "他的银行卡";
	}

	@Override
	public String onShowMemberDetailHtml(Member member) {
		FreeMarkerPaser freeMarkerPaser =FreeMarkerPaser.getInstance();
		freeMarkerPaser.setClz(this.getClass());
		freeMarkerPaser.putData("member",member);		 //为页面put变量 
		List<MemberBank> memberBankList =  memberBankManager.getBankListByMemberId(member.getMember_id());
		freeMarkerPaser.putData("memberBankList",memberBankList);		 //为页面put变量 
		freeMarkerPaser.setPageName("bankinfo");//解析此类同级目录中的bankinfo.html
		return freeMarkerPaser.proessPageContent();//返回上述页面的内容作为tab页的内容
	}
	
	@Override
	public String execute() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String member_banknum = request.getParameter("member_banknum");
		String member_bankusername = request.getParameter("member_bankusername");
		String member_bankname = request.getParameter("member_bankname");
		String member_bankcity = request.getParameter("member_bankcity");
		int memberid  = StringUtil.toInt(request.getParameter("memberid"),true);
		MemberBank memberBank = new MemberBank();
		memberBank.setBankname(member_bankname);
		memberBank.setBankcity(member_bankcity);
		memberBank.setBanknum(member_banknum);
		memberBank.setOpenname(member_bankusername);
		memberBank.setMemberid(memberid);
		try {
			//memberManager.edit(member);
			memberBankManager.add(memberBank);
			return JsonMessageUtil.getSuccessJson("银行卡信息修改成功");
		} catch (Exception e) {
			 this.logger.error("修改会员银行卡信息",e);
			return JsonMessageUtil.getErrorJson("银行卡信息修改失败"); 
		}

	}

	public IMemberBankManager getMemberBankManager() {
		return memberBankManager;
	}

	public void setMemberBankManager(IMemberBankManager memberBankManager) {
		this.memberBankManager = memberBankManager;
	}
	
}
