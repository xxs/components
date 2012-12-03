package com.enation.app.shop.component.rechange.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.enation.app.shop.component.rechange.model.DiscountVo;
import com.enation.app.shop.component.rechange.model.GoodsDiscount;
import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.framework.action.WWAction;


@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Results({ 
	 @Result(name="input",location="/shop/admin/withdraw/input.jsp")
})
public class DiscountAction extends WWAction {
	
	private GoodsDiscount goodsDiscount;
	private IGoodsDiscountManager goodsDiscountManager;
	private List<DiscountVo> lists;
	private Double[] discounts;
	private String[] codes;
	private Integer[] cat_ids;
	public String execute(){
		this.goodsDiscount = this.goodsDiscountManager.get();
		this.lists = this.goodsDiscountManager.getDiscountVoList();
		return "input";
	}
	
	public String updateGoodsDiscount(){
		try{
			this.goodsDiscountManager.saveDiscount(cat_ids,discounts,codes);
			this.showSuccessJson("充值卡折扣更新成功");
			
		}catch(RuntimeException e){
			this.logger.error("充值卡折扣更新出错", e);
			this.showErrorJson("充值卡折扣更新出错:["+e.getMessage()+"]");
		}
		return this.JSON_MESSAGE;
	}
	public String addGoodsDiscount(){
		try{
			this.goodsDiscountManager.add(goodsDiscount);
			this.showSuccessJson("充值卡折扣添加成功");
		}catch(RuntimeException e){
			this.logger.error("充值卡折扣添加出错", e);
			this.showErrorJson("充值卡折扣添加出错:["+e.getMessage()+"]");
		}
		return this.JSON_MESSAGE;
	}

	public IGoodsDiscountManager getGoodsDiscountManager() {
		return goodsDiscountManager;
	}

	public void setGoodsDiscountManager(IGoodsDiscountManager goodsDiscountManager) {
		this.goodsDiscountManager = goodsDiscountManager;
	}

	public GoodsDiscount getGoodsDiscount() {
		return goodsDiscount;
	}

	public void setGoodsDiscount(GoodsDiscount goodsDiscount) {
		this.goodsDiscount = goodsDiscount;
	}

	public List<DiscountVo> getLists() {
		return lists;
	}

	public void setLists(List<DiscountVo> lists) {
		this.lists = lists;
	}

	public Double[] getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Double[] discounts) {
		this.discounts = discounts;
	}

	public Integer[] getCat_ids() {
		return cat_ids;
	}

	public void setCat_ids(Integer[] cat_ids) {
		this.cat_ids = cat_ids;
	}

	public String[] getCodes() {
		return codes;
	}

	public void setCodes(String[] codes) {
		this.codes = codes;
	}

}
