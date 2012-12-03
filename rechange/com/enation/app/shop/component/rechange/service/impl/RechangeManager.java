package com.enation.app.shop.component.rechange.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.rechange.model.GoodsDiscount;
import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.app.shop.component.rechange.service.IRechangeLogsManager;
import com.enation.app.shop.component.rechange.service.IRechangeManager;
import com.enation.app.shop.core.model.AdvanceLogs;
import com.enation.app.shop.core.model.DlyType;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.model.OrderLog;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.model.PaymentLog;
import com.enation.app.shop.core.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.service.IAdvanceLogsManager;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IMemberManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IPaymentManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.eop.sdk.user.IUserService;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.jms.EmailProducer;
import com.enation.framework.util.DateUtil;


/**
 * 
 * @author xxs
 *2012-6-17下午4:29:32
 */
@Component
public class RechangeManager extends BaseSupport  implements
		IRechangeManager {

	private IMemberManager memberManager;
	private EmailProducer mailMessageProducer; 
	private IOrderManager orderManager;
	private IPaymentManager paymentManager;
	private IDlyTypeManager dlyTypeManager;
	private IGoodsManager goodsManager;
	private IProductManager productManager;
	private OrderPluginBundle orderPluginBundle;
	private CartPluginBundle cartPluginBundle;
	private IAdvanceLogsManager advanceLogsManager;
	private IRechangeLogsManager rechangeLogsManager;
	private IGoodsDiscountManager goodsDiscountManager;
	@Override
	public Order createOrder(Goods goods) {
		
		String opname = "匿名";
		Order order = new Order();
		
		/************ 支付方式价格及名称 ************************/
		PayCfg payCfg = this.getPayMentType();
		if (payCfg == null)
			throw new RuntimeException("支付方式：[易宝非银行卡支付]未能找到，不能创建订单");
		order.setPaymoney(this.paymentManager.countPayPrice(order.getOrder_id()));
		order.setPayment_name(payCfg.getName());		
		order.setPayment_type(payCfg.getType());
		order.setPayment_id(payCfg.getId());
		order.setOrder_amount(goods.getMktprice()); //将单价作为订单中的商品总价
		/************ 配送方式id及名称 ************************/
		DlyType dlyType = this.getDlyType();
		if (dlyType == null)
			throw new RuntimeException("配送方式：[虚拟商品自动发货]未能找到，不能创建订单");
		order.setShipping_id(dlyType.getType_id());
		order.setShipping_type(dlyType.getName());	
		
		
		/************************** 用户信息 ****************************/
		IUserService userService = UserServiceFactory.getUserService();
		Member member = userService.getCurrentMember();
		// 非匿名购买
		if (member != null) {
			order.setMember_id(member.getMember_id());
			opname = member.getUname();
		}else{
			throw new RuntimeException("当前用户已失效，不能创建订单，请重新登陆");
		}
		
		order.setGoods_amount( goods.getPrice());
		order.setWeight((double) 0);		

		order.setDiscount(0D);
		order.setProtect_price(0D);
		order.setShipping_amount(0D);
		
		/************ 创建订单 ************************/
		order.setCreate_time(Long.valueOf(System.currentTimeMillis()));
		order.setSn(this.createSn());
		order.setStatus(0);
		order.setDisabled(0);
		order.setPay_status(OrderStatus.PAY_NO);
		order.setShip_status(OrderStatus.SHIP_ALLOCATION_NO);
		
		this.baseDaoSupport.insert("order", order);
		Order orders = getLastAddOrderID(member.getMember_id());
		int orderId = orders.getOrder_id();
		
		
		
		/************ 写入订单日志 ************************/
		OrderLog log = new OrderLog();
		log.setMessage("订单创建");
		log.setOp_name(opname);
		log.setOrder_id(orderId);
		log.setOp_time(Long.valueOf(System.currentTimeMillis()));
		this.baseDaoSupport.insert("order_log", log);
		order.setOrder_id(orderId);
		
		/************ 写入订单货 ************************/		
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder_id(orderId);
		orderItem.setCat_id(goods.getCat_id());
		orderItem.setName(goods.getName());
		orderItem.setNum(1);
		orderItem.setSn(goods.getName());
		orderItem.setImage(goods.getImage_default());
		orderItem.setGoods_id(goods.getGoods_id());
		orderItem.setShip_num(0);
		
		orderItem.setGainedpoint(0);
		
		this.baseDaoSupport.insert("order_items", orderItem);
		
		return order;
		
	}
	
	
	private String createSn() {
		Date now = new Date();
		String sn = ""+com.enation.framework.util.DateUtil.toString(now,
				"yyyyMMddhhmmss");
		return sn;
	}
	
	/**
	 * 获取自动发货的配送方式
	 * @param name
	 * @return
	 */
	private DlyType getDlyType( ){		
		String sql ="select  * from  dly_type where name='虚拟商品自动发货'";
		return (DlyType)this.baseDaoSupport.queryForObject(sql, DlyType.class);
	}
	/**
	 * 获取易宝非银行卡支付方式
	 * @param name
	 * @return
	 */
	private PayCfg getPayMentType(){		
		String sql ="select * from  payment_cfg where type='yeepayPlugin'";
		return (PayCfg)this.baseDaoSupport.queryForObject(sql, PayCfg.class);
	}
	private Order getLastAddOrderID(Integer memberid){
		String sql ="select * from order where member_id = "+memberid+" ORDER BY create_time DESC LIMIT 0,1";
		System.out.println(sql);
		return (Order)this.baseDaoSupport.queryForObject(sql, Order.class);
	}
	public IMemberManager getMemberManager() {
		return memberManager;
	}



	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}



	public EmailProducer getMailMessageProducer() {
		return mailMessageProducer;
	}



	public void setMailMessageProducer(EmailProducer mailMessageProducer) {
		this.mailMessageProducer = mailMessageProducer;
	}



	public IOrderManager getOrderManager() {
		return orderManager;
	}



	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}



	public IPaymentManager getPaymentManager() {
		return paymentManager;
	}



	public void setPaymentManager(IPaymentManager paymentManager) {
		this.paymentManager = paymentManager;
	}



	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}



	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}



	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}



	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}



	public IProductManager getProductManager() {
		return productManager;
	}



	public void setProductManager(IProductManager productManager) {
		this.productManager = productManager;
	}



	public OrderPluginBundle getOrderPluginBundle() {
		return orderPluginBundle;
	}



	public void setOrderPluginBundle(OrderPluginBundle orderPluginBundle) {
		this.orderPluginBundle = orderPluginBundle;
	}



	public CartPluginBundle getCartPluginBundle() {
		return cartPluginBundle;
	}



	public IGoodsDiscountManager getGoodsDiscountManager() {
		return goodsDiscountManager;
	}


	public void setGoodsDiscountManager(IGoodsDiscountManager goodsDiscountManager) {
		this.goodsDiscountManager = goodsDiscountManager;
	}


	public void setCartPluginBundle(CartPluginBundle cartPluginBundle) {
		this.cartPluginBundle = cartPluginBundle;
	}


	public IRechangeLogsManager getRechangeLogsManager() {
		return rechangeLogsManager;
	}


	public void setRechangeLogsManager(IRechangeLogsManager rechangeLogsManager) {
		this.rechangeLogsManager = rechangeLogsManager;
	}
	


	public IAdvanceLogsManager getAdvanceLogsManager() {
		return advanceLogsManager;
	}


	public void setAdvanceLogsManager(IAdvanceLogsManager advanceLogsManager) {
		this.advanceLogsManager = advanceLogsManager;
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void paySuccess(String ordersn, String tradeno) {
		Order order = this.orderManager.get(ordersn);
		if (order.getPay_status().intValue() != 0) {
			return;
		}
		if (order.getPay_status().intValue() == 1 || order.getPay_status().intValue() == 7 || order.getPay_status().intValue() == 8) {
			return;
		}
		PaymentLog paymentLog = new PaymentLog();
		Member member = memberManager.get(order.getMember_id());
		if (member != null) {
			paymentLog.setMember_id(member.getMember_id());
			paymentLog.setPay_user(member.getUname());
		} else {
			throw new IllegalArgumentException("用户为空");
		}
		paymentLog.setRemark("易宝支付");
		paymentLog.setMoney(order.getOrder_amount());
		paymentLog.setOrder_sn(order.getSn());
		paymentLog.setPay_method(order.getPayment_name());
		paymentLog.setSn(tradeno);
		paymentLog.setOrder_id(order.getOrder_id().intValue());
		paymentLog.setPay_date(DateUtil.getDatelineLong());
		paymentLog.setType(1);
		paymentLog.setStatus(Integer.valueOf(0));
		paymentLog.setCreate_time(Long.valueOf(System.currentTimeMillis()));
		this.baseDaoSupport.insert("payment_logs", paymentLog);
		this.baseDaoSupport.execute("update order set status=?,pay_status=?  where order_id=?", new Object[] { Integer.valueOf(7), Integer.valueOf(1), order.getOrder_id() });
		//-------------添加支付成功的订单日志------------
		System.out.println("添加订单日志");
		OrderLog orderLog = new OrderLog();
		orderLog.setMessage("支付完成");
		orderLog.setOp_name(member.getUname());
		orderLog.setOp_id(member.getMember_id());
		orderLog.setOrder_id(order.getOrder_id());
		orderLog.setOp_time(Long.valueOf(System.currentTimeMillis()));
		this.baseDaoSupport.insert("order_log", orderLog);
		System.out.println("添加订单日志完成");
		
		//-------------更新用户预存款信息及预存款日志------------
		//要按照设定好的折扣为会员增加预存款
		//获取此订单中充值卡相应的折扣
		Map map = new HashMap();
		List<Map> list = orderManager.getItemsByOrderid(order.getOrder_id());
		if(list != null && list.size()>0){
			map =  list.get(0);
		}
		GoodsDiscount goodsDiscount = goodsDiscountManager.getDiscountByCatId(Integer.parseInt(map.get("cat_id").toString()));
		
		//计算会员账户应添加的金额
		Double dpeice =  order.getOrder_amount()*goodsDiscount.getDiscount();
		
		Double newadnace = member.getAdvance()+dpeice;
		this.baseDaoSupport.execute("update member set advance = ? where member_id = ?", newadnace ,member.getMember_id());
		AdvanceLogs advanceLogs = new AdvanceLogs();
		advanceLogs.setMember_id(member.getMember_id());
		advanceLogs.setMoney(dpeice);
		advanceLogs.setMessage("在线充值");
		advanceLogs.setMtime(Long.valueOf(System.currentTimeMillis()));
		advanceLogs.setMemo("单据号："+tradeno);
		advanceLogs.setOrder_id(0);
		advanceLogs.setPayment_id(0);
		advanceLogs.setImport_money(dpeice);
		advanceLogs.setExplode_money(0.000);
		List advanceLogsList =advanceLogsManager.listAdvanceLogsByMemberId(member.getMember_id());
		if(advanceLogsList!=null && advanceLogsList.size()>0){
			AdvanceLogs adl = (AdvanceLogs)advanceLogsList.get(0);
			advanceLogs.setShop_advance(adl.getShop_advance()+dpeice);
			advanceLogs.setMember_advance(adl.getMember_advance()+dpeice);
		}else{
			advanceLogs.setShop_advance(dpeice);
			advanceLogs.setMember_advance(dpeice);
		}
		advanceLogs.setDisabled("false");  //状态为申请状态，为false则为已起效。(在线充值，直接起效)
		advanceLogsManager.add(advanceLogs);
		
	}
	
}
