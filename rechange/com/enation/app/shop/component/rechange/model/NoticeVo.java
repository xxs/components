package com.enation.app.shop.component.rechange.model;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.AdvanceLogs;


/***
 * 通知实体
 * @author xxs
 *
 */
public class NoticeVo {
	
	private Member member;
	private AdvanceLogs advanceLogs;
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public AdvanceLogs getAdvanceLogs() {
		return advanceLogs;
	}
	public void setAdvanceLogs(AdvanceLogs advanceLogs) {
		this.advanceLogs = advanceLogs;
	}
	
	
	
}
