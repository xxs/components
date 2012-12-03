package com.enation.app.shop.component.rechange.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.rechange.model.LossRate;
import com.enation.app.shop.component.rechange.service.ILossRateManager;
import com.enation.eop.sdk.database.BaseSupport;

@Component
public class LossRateManager extends BaseSupport implements
		ILossRateManager {

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveLossRate(LossRate lossRate) {
		lossRate.setCreatetime(Long.valueOf(System.currentTimeMillis()));
		lossRate.setUpdatetime(Long.valueOf(System.currentTimeMillis()));
		this.baseDaoSupport.insert("lossrate", lossRate);
	}

	@Override
	public LossRate getLossRateByCatId(Integer lossRateId) {
		String sql = "select * from  lossrate where lossrateid = ?";
		List<LossRate> list = this.baseDaoSupport.queryForList(sql,LossRate.class,lossRateId);
		if(null != list && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public LossRate get() {
		String sql = "select * from  lossrate";
		List<LossRate> list = this.baseDaoSupport.queryForList(sql,LossRate.class);
		if(null != list && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<LossRate> list() {
		String sql = "select * from  lossrate";
		return this.baseDaoSupport.queryForList(sql,LossRate.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateLossRate(LossRate lossRate) {
		String sql  ="update lossrate set lossrate=?,updatetime=? where lossrateid=? ";
		this.baseDaoSupport.execute(sql,lossRate.getLossrate(),Long.valueOf(System.currentTimeMillis()),lossRate.getLossrateid());
	}

}
