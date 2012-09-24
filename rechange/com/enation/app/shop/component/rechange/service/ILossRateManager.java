package com.enation.app.shop.component.rechange.service;

import java.util.List;

import com.enation.app.shop.component.rechange.model.LossRate;



public interface ILossRateManager {

	public void saveLossRate(LossRate lossRate);

	public void updateLossRate(LossRate lossRate);

	public LossRate get();
	
	public List<LossRate> list();
	
	public LossRate getLossRateByCatId(Integer lossRateId);
}
