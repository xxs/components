package com.enation.app.shop.component.rechange.widget;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.shop.component.rechange.model.GoodsDiscount;
import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.app.shop.component.rechange.service.IRechangeManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.eop.sdk.widget.AbstractWidget;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;



/**
 * 充值卡结算挂件
 * @author xxs
 *2012-5-5下午9:35:08
 */
@Component("rechange_checkout")
public class RechangeCheckoutWidget extends AbstractWidget {

	private IGoodsManager goodsManager;
	private IRechangeManager rechangeManager;
	private IGoodsDiscountManager goodsDiscountManager;
	@Override
	protected void display(Map<String, String> params) {
		
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		int goodsid= StringUtil.toInt(request.getParameter("goodsid"),true);
		String action = request.getParameter("action");		
		if(StringUtil.isEmpty(action) || "nextshow".equals(action)){
			Goods goods = this.goodsManager.getGoods(goodsid);
			this.putData("goods",goods);
			GoodsDiscount goodsDiscount = goodsDiscountManager.getDiscountByCatId(goods.getCat_id());
			this.putData("goodsDiscount", goodsDiscount);
		}else if("createOrder".equals(action)){
			
			try{
				Goods goods = this.goodsManager.getGoods(goodsid);
				if(null != goods ){			
					//过滤骏网一卡通和盛大一卡通的通用面值情况
					if(goods.getMktprice()==0){
						Double submoney= StringUtil.toDouble(request.getParameter("submoney"),true);
						goods.setMktprice(submoney);
					}
				}
				Order order=rechangeManager.createOrder(goods);
				this.putData("order",order);
				this.showJson("{result:1,ordersn:"+order.getSn()+"}");
			}catch(RuntimeException e){
				this.showJson("{result:0,message:'"+e.getMessage()+"'}");
			}
		}
		Goods goods = this.goodsManager.getGoods(goodsid);
		GoodsDiscount goodsDiscount = goodsDiscountManager.getDiscountByCatId(goods.getCat_id());
		this.putData("goodsDiscount", goodsDiscount);
	}
	

	@Override
	protected void config(Map<String, String> params) {

	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public IRechangeManager getRechangeManager() {
		return rechangeManager;
	}


	public void setRechangeManager(IRechangeManager rechangeManager) {
		this.rechangeManager = rechangeManager;
	}


	public IGoodsDiscountManager getGoodsDiscountManager() {
		return goodsDiscountManager;
	}


	public void setGoodsDiscountManager(IGoodsDiscountManager goodsDiscountManager) {
		this.goodsDiscountManager = goodsDiscountManager;
	}


	
}
