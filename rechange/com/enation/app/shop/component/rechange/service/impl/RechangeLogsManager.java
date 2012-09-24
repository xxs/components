package com.enation.app.shop.component.rechange.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.rechange.model.RechangeLogs;
import com.enation.app.shop.component.rechange.service.IRechangeLogsManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

/**
 * 充值卡充值记录
 * @author xxs
 *
 */
@Component
public class RechangeLogsManager extends BaseSupport<RechangeLogs> implements
		IRechangeLogsManager {

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(RechangeLogs account) {
		account.setCreatetime(Long.valueOf(System.currentTimeMillis()));
		 this.baseDaoSupport.insert("rechange_account", account);

	}

	
	@Override
	public Page list(int goodsid, int pageNo, int pageSize) {
		return this.baseDaoSupport.queryForPage("select * from rechange_account where goodsid=? order by create_time desc", pageNo, pageSize, goodsid);
	}


}
