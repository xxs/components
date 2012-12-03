package com.enation.app.shop.component.rechange.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.rechange.model.GoodsDiscount;
import com.enation.app.shop.component.rechange.model.NoticeVo;
import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.app.shop.core.model.AdvanceLogs;
import com.enation.app.shop.core.service.IMemberManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.eop.sdk.widget.AbstractWidget;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.IAjaxExecuteEnable;
import com.enation.framework.util.RequestUtil;

/**
 * 通知信息挂件
 * @author xxs
 *2012-7-5下午9:35:08
 */
@Component("notice")
public class NoticeWidget extends AbstractWidget implements IAjaxExecuteEnable{
	private IGoodsDiscountManager goodsDiscountManager;
	private IMemberManager memberManager;
	@Override
	protected void config(Map<String, String> arg0) {
	}

	@Override
	protected void display(Map<String, String> arg0) {
		HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
		String url = RequestUtil.getRequestUrl(httpRequest);
		String[] actions = getAction(url);
		if(null != actions ){
			String artID = actions[1];
			this.putData("artID",artID);
		}
		Member member  = UserServiceFactory.getUserService().getCurrentMember();
		GoodsDiscount goodsDiscount = goodsDiscountManager.get();
		List<AdvanceLogs> advanceLogsList = goodsDiscountManager.getAdvanceLogsList(5);
		List<NoticeVo> noticeVoList = new ArrayList<NoticeVo>();
		NoticeVo noticeVo = null;
		Member member2 = new Member();
		AdvanceLogs advanceLogs = new AdvanceLogs();
		for (int i = 0; i < advanceLogsList.size(); i++) {
			noticeVo = new NoticeVo();
			advanceLogs = advanceLogsList.get(i);
			if( null !=advanceLogs.getExplode_money()){ //判断管理员输入负数的金额的情况
				if(advanceLogs.getExplode_money() < 0){
					advanceLogs.setImport_money(Math.abs(advanceLogs.getExplode_money()));
					advanceLogs.setExplode_money((double) 0);
				}
			}
			if( null !=advanceLogs.getImport_money()){ //判断管理员输入负数的金额的情况
				if(advanceLogs.getImport_money() < 0){
					advanceLogs.setExplode_money(Math.abs(advanceLogs.getImport_money()));
					advanceLogs.setImport_money((double) 0);
				}
			}
			member2 = memberManager.get(advanceLogs.getMember_id());
			noticeVo.setMember(member2);
			noticeVo.setAdvanceLogs(advanceLogs);
			noticeVoList.add(noticeVo);
		}
		List<AdvanceLogs> lists = goodsDiscountManager.list();
		Double fulinum = 0.0;
		if(lists!=null){
			for (int i = 0; i < lists.size(); i++) {
				AdvanceLogs g = lists.get(i);
				if(g!=null){
					if(g.getImport_money()!=0){
						fulinum = fulinum+g.getImport_money();
					}
				}
			}
		}
		this.putData("fulinum",fulinum);
		this.putData("advanceLogsList",advanceLogsList);
		this.putData("noticeVoList",noticeVoList);
		this.putData("goodsDiscount",goodsDiscount);
		this.putData("member",member);
	}

	@Override
	public String execute() {
		return null;
	}

	public IGoodsDiscountManager getGoodsDiscountManager() {
		return goodsDiscountManager;
	}

	public void setGoodsDiscountManager(IGoodsDiscountManager goodsDiscountManager) {
		this.goodsDiscountManager = goodsDiscountManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}
	 private String[] getAction(String url) {
		 String action = null;
		 String pluginid = null;
		 String pattern = "/(\\w+)-(\\w+)-(\\w+).html(.*)";
		 Pattern p = Pattern.compile(pattern, 34);
		 Matcher m = p.matcher(url);
		 if (m.find()) {
				 action = m.replaceAll("$2");
				 pluginid = m.replaceAll("$3");
				 return new String[] { action, pluginid };
		  }
		    return null;
		  }
}
