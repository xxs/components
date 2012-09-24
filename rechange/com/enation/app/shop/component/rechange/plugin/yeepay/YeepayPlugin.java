package com.enation.app.shop.component.rechange.plugin.yeepay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.shop.component.rechange.model.GoodsDiscount;
import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.app.shop.component.rechange.service.IRechangeManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.plugin.payment.AbstractPaymentPlugin;
import com.enation.app.shop.core.plugin.payment.IPaymentEvent;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IOrderFlowManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;

@Component
public class YeepayPlugin extends AbstractPaymentPlugin implements
		IPaymentEvent {
	private IOrderManager orderManager;
	private IGoodsManager goodsManager;
	private IGoodsDiscountManager goodsDiscountManager;
	private IOrderFlowManager orderFlowManager;
	private IRechangeManager rechangeManager;
	@Override
	public String onCallBack() {
		System.out.println("onCallBack......");
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
	    String keyValue = "";
	    String p1_MerId1 = "";
	    String r0_Cmd = "";
	    String r1_Code = "";
	    String r2_TrxId = "";
	    String r3_Amt = "";
	    String r4_Cur = "";
	    String r5_Pid = "";
	    String r6_Order = "";
	    String r7_Uid = "";
	    String r8_MP = "";
	    String r9_BType = "";
	    String rb_BankId = "";
	    String ro_BankOrderId = "";
	    String rp_PayDate = "";
	    String rq_CardNo = "";
	    String ru_Trxtime = "";
	    String hmac = "";
		try {
			Map params1 = this.paymentManager.getConfigParams(getId());
			keyValue = jiema((String) params1.get("keyValue"));// 商家密钥
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	 
	     p1_MerId1 = request.getParameter("p1_MerId");
		 r0_Cmd = request.getParameter("r0_Cmd");
		 r1_Code = request.getParameter("r1_Code");
		 r2_TrxId = request.getParameter("r2_TrxId");
		 r3_Amt = request.getParameter("r3_Amt");
		 r4_Cur = request.getParameter("r4_Cur");
		 r5_Pid = request.getParameter("r5_Pid");
		 r6_Order = request.getParameter("r6_Order");
		 r7_Uid = request.getParameter("r7_Uid");
		 r8_MP = request.getParameter("r8_MP");
		 r9_BType = request.getParameter("r9_BType");
		 rb_BankId = request.getParameter("rb_BankId");
		 ro_BankOrderId = request.getParameter("ro_BankOrderId");
		 rp_PayDate = request.getParameter("rp_PayDate");
		 rq_CardNo = request.getParameter("rq_CardNo");
		 ru_Trxtime = request.getParameter("ru_Trxtime");
		 hmac = request.getParameter("hmac");
		 boolean res = PaymentForOnlineService.verifyCallback(hmac, p1_MerId1, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
		if(res){
			try {
				p1_MerId1 = jiema(p1_MerId1);
				r0_Cmd = jiema(r0_Cmd);
				r1_Code = jiema(r1_Code);
				r2_TrxId = jiema(r2_TrxId);
				r3_Amt = jiema(r3_Amt);
				r4_Cur = jiema(r4_Cur);
				r5_Pid = jiema(r5_Pid);
				r6_Order = jiema(r6_Order);
				r7_Uid = jiema(r7_Uid);
				r8_MP = jiema(r8_MP);
				r9_BType = jiema(r9_BType);
				rb_BankId = jiema(rb_BankId);
				ro_BankOrderId = jiema(ro_BankOrderId);
				rp_PayDate = jiema(rp_PayDate);
				rq_CardNo = jiema(rq_CardNo);
				ru_Trxtime = jiema(ru_Trxtime);
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Order order = orderManager.get(r6_Order);
			Goods goods = goodsManager.getGoodBySn(r5_Pid);
			PayCfg payCfg = paymentManager.get(order.getPayment_type());
			if(Double.parseDouble(r3_Amt)==order.getOrder_amount()){
				if(order.getStatus() == 0){
					rechangeManager.paySuccess(order.getSn(), r2_TrxId);
				}
				return "success";
			}else{
				return "fail";
			}
		}else{
			return "fail";
		}
	}

	@Override
	public String onPay(PayCfg payCfg, Order order) {
		System.out.println("准备付款");
		String spprice = "";
		String spID = "";
		String tongdao = "";
		spprice = String.valueOf(order.getOrder_amount());
		List list = orderManager.getItemsByOrderid(order.getOrder_id());
		if(list != null){
			OrderItem orderItem = new OrderItem();
			Map map = (Map) list.get(0);
			orderItem.setGoods_id((Integer) map.get("goods_id"));
			orderItem.setCat_id((Integer) map.get("cat_id"));
			Goods good = goodsManager.getGoods(orderItem.getGoods_id());
			if(good != null){
				spID = good.getSn();
			}
			GoodsDiscount goodsDiscount = goodsDiscountManager.getDiscountByCatId(orderItem.getCat_id());
			if(goodsDiscount != null){
				tongdao = goodsDiscount.getCode().trim().toUpperCase();
			}
		}
		Map params = this.paymentManager.getConfigParams(getId());
		try {
		String keyValue = bianma((String) params.get("keyValue")); // 商家密钥
		String p1_MerId = bianma((String) params.get("p1_MerId"));// 商户编号
		String callback_encoding = bianma((String) params.get("callback_encoding"));
		String return_encoding = bianma((String) params.get("return_encoding"));
		String nodeAuthorizationURL =(String) params.get("URL");// 请求路径
		String p0_Cmd =  bianma("Buy");// 在线支付请求，固定值 "Buy"
		String p2_Order = bianma(order.getSn());// 商户订单号
		String p3_Amt = bianma(spprice);// 支付金额
		String p4_Cur = bianma("CNY");// 交易币种
		String p5_Pid = bianma(spID);// 商品名称
		String p6_Pcat = bianma("虚拟卡") ;// 商品种类
		String p7_Pdesc =bianma("虚拟卡");// 商品描述
		String p8_Url = getReturnUrl(payCfg);// 商户接收支付成功数据的地址return
		String p9_SAF = bianma("0");// 需要填写送货信息 0：不需要 1:需要
		String pa_MP =bianma("暂无") ;// 商户扩展信息
		String pd_FrpId =bianma(tongdao) ;// 支付通道编码
		String pr_NeedResponse = bianma("1");// 默认为"1"，需要应答机制
		String hmac = PaymentForOnlineService.getReqMd5HmacForOnlinePayment(
				p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat,
				p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse,
				keyValue);
		String strHtml = "";
		strHtml = strHtml + "<form name=\"yeepay\" id=\"kqPay\" action=\""+nodeAuthorizationURL+"\" method=\"post\">";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p0_Cmd\" value=\"" +p0_Cmd + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p1_MerId\" value=\"" + p1_MerId + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p2_Order\" value=\"" + p2_Order + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p3_Amt\" value=\"" + p3_Amt + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p4_Cur\" value=\"" + p4_Cur + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p5_Pid\" value=\"" + p5_Pid + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p6_Pcat\" value=\"" + p6_Pcat + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p7_Pdesc\" value=\"" + p7_Pdesc + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p8_Url\" value=\"" + p8_Url + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"p9_SAF\" value=\"" + p9_SAF + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"pa_MP\" value=\"" + pa_MP + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"pd_FrpId\" value=\"" + pd_FrpId + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"pr_NeedResponse\" value=\"" + pr_NeedResponse + "\"/>";
		strHtml = strHtml + "<input type=\"hidden\" name=\"hmac\" value=\"" + hmac + "\"/>";
		strHtml = strHtml + "</form>";
		strHtml = strHtml + "<script type=\"text/javascript\">document.forms['yeepay'].submit();</script>";
		return strHtml;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "参数异常";

	}

	@Override
	public String onReturn() {
		System.out.println("onreturn......");
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
	    String keyValue = "";
	    String p1_MerId1 = "";
	    String r0_Cmd = "";
	    String r1_Code = "";
	    String r2_TrxId = "";
	    String r3_Amt = "";
	    String r4_Cur = "";
	    String r5_Pid = "";
	    String r6_Order = "";
	    String r7_Uid = "";
	    String r8_MP = "";
	    String r9_BType = "";
	    String rb_BankId = "";
	    String ro_BankOrderId = "";
	    String rp_PayDate = "";
	    String rq_CardNo = "";
	    String ru_Trxtime = "";
	    String hmac = "";
		try {
			Map params1 = this.paymentManager.getConfigParams(getId());
			keyValue = jiema((String) params1.get("keyValue"));// 商家密钥
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	 
	     p1_MerId1 = request.getParameter("p1_MerId");
		 r0_Cmd = request.getParameter("r0_Cmd");
		 r1_Code = request.getParameter("r1_Code");
		 r2_TrxId = request.getParameter("r2_TrxId");
		 r3_Amt = request.getParameter("r3_Amt");
		 r4_Cur = request.getParameter("r4_Cur");
		 r5_Pid = request.getParameter("r5_Pid");
		 r6_Order = request.getParameter("r6_Order");
		 r7_Uid = request.getParameter("r7_Uid");
		 r8_MP = request.getParameter("r8_MP");
		 r9_BType = request.getParameter("r9_BType");
		 rb_BankId = request.getParameter("rb_BankId");
		 ro_BankOrderId = request.getParameter("ro_BankOrderId");
		 rp_PayDate = request.getParameter("rp_PayDate");
		 rq_CardNo = request.getParameter("rq_CardNo");
		 ru_Trxtime = request.getParameter("ru_Trxtime");
		 hmac = request.getParameter("hmac");
		 boolean res = PaymentForOnlineService.verifyCallback(hmac, p1_MerId1, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
		if(res){
			try {
				p1_MerId1 = jiema(p1_MerId1);
				r0_Cmd = jiema(r0_Cmd);
				r1_Code = jiema(r1_Code);
				r2_TrxId = jiema(r2_TrxId);
				r3_Amt = jiema(r3_Amt);
				r4_Cur = jiema(r4_Cur);
				r5_Pid = jiema(r5_Pid);
				r6_Order = jiema(r6_Order);
				r7_Uid = jiema(r7_Uid);
				r8_MP = jiema(r8_MP);
				r9_BType = jiema(r9_BType);
				rb_BankId = jiema(rb_BankId);
				ro_BankOrderId = jiema(ro_BankOrderId);
				rp_PayDate = jiema(rp_PayDate);
				rq_CardNo = jiema(rq_CardNo);
				ru_Trxtime = jiema(ru_Trxtime);
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			Order order = orderManager.get(r6_Order);
			Goods goods = goodsManager.getGoodBySn(r5_Pid);
			PayCfg payCfg = paymentManager.get(order.getPayment_type());
			if(Double.parseDouble(r3_Amt)==order.getOrder_amount()){
				if(order.getStatus() == 0){
					rechangeManager.paySuccess(order.getSn(), r2_TrxId);
				}
				return order.getSn();
			}else{
				return "fail";
			}
		}else{
			return "fail";
		}
	}

	@Override
	public String getId() {
		return "yeepayPlugin";
	}

	@Override
	public String getName() {
		return "易宝非银行卡支付";
	}
    private static String bianma(String str) throws UnsupportedEncodingException{
		 if(null == str){
			 str="";
		 }else{
			 str.replace(" ", "").trim();
		 }
		 return URLEncoder.encode(str,"GBK");
		 //return StringUtil.to(str, "GB2312");
		 //return new String(str.getBytes("GBK"),"GBK");
    }
    private static String jiema(String str) throws UnsupportedEncodingException{
		 if(null == str){
			 str="";
		 }else{
			 str.replace(" ", "").trim();
		 }
		 return URLDecoder.decode(str,"GBK");
		 //return StringUtil.to(str, "GB2312");
		 //return new String(str.getBytes("GBK"),"GBK");
   }
	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public IGoodsDiscountManager getGoodsDiscountManager() {
		return goodsDiscountManager;
	}

	public void setGoodsDiscountManager(IGoodsDiscountManager goodsDiscountManager) {
		this.goodsDiscountManager = goodsDiscountManager;
	}
	
	
	public IOrderFlowManager getOrderFlowManager() {
		return orderFlowManager;
	}

	public void setOrderFlowManager(IOrderFlowManager orderFlowManager) {
		this.orderFlowManager = orderFlowManager;
	}

	public IRechangeManager getRechangeManager() {
		return rechangeManager;
	}

	public void setRechangeManager(IRechangeManager rechangeManager) {
		this.rechangeManager = rechangeManager;
	}
	
}
