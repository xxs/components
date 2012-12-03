package com.enation.app.shop.component.rechange.service;


import com.enation.app.shop.component.rechange.model.RechangeLogs;
import com.enation.framework.database.Page;

/**
 * 充值卡管理接口
 * @author xxs
 *
 */
public interface IRechangeLogsManager {
	
	/**
	 * 添加虚拟账号
	 * @param account
	 */
	public void add(RechangeLogs account);
	
	
	/**
	 * 分页读取某个商品的虚拟账号
	 * @param goodsid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page list(int goodsid,int pageNo,int pageSize);
	
	
	
}
