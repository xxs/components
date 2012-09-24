package com.enation.app.shop.component.rechange.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.enation.app.shop.component.rechange.model.DiscountVo;
import com.enation.app.shop.component.rechange.model.GoodsDiscount;
import com.enation.app.shop.component.rechange.model.LossRate;
import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.app.shop.component.rechange.service.ILossRateManager;
import com.enation.framework.action.WWAction;


@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Results({ 
	 @Result(name="input",location="/shop/admin/withdraw/lossrate.jsp")
})
public class LossrateAction extends WWAction {
	
	private ILossRateManager lossRateManager;
	private LossRate lossRate;
	public String execute(){
		this.lossRate = this.lossRateManager.get();
		return "input";
	}
	
	public String updateLossrate(){
		try{
			this.lossRateManager.updateLossRate(lossRate);
			this.showSuccessJson("手续费率更新成功");
			
		}catch(RuntimeException e){
			this.logger.error("手续费率更新出错", e);
			this.showErrorJson("手续费率更新出错:["+e.getMessage()+"]");
		}
		return this.JSON_MESSAGE;
	}
	public String addGoodsDiscount(){
		try{
			this.lossRateManager.saveLossRate(lossRate);
			this.showSuccessJson("手续费率添加成功");
		}catch(RuntimeException e){
			this.logger.error("手续费率添加出错", e);
			this.showErrorJson("手续费率添加出错:["+e.getMessage()+"]");
		}
		return this.JSON_MESSAGE;
	}

	public ILossRateManager getLossRateManager() {
		return lossRateManager;
	}

	public void setLossRateManager(ILossRateManager lossRateManager) {
		this.lossRateManager = lossRateManager;
	}

	public LossRate getLossRate() {
		return lossRate;
	}

	public void setLossRate(LossRate lossRate) {
		this.lossRate = lossRate;
	}
	
}
