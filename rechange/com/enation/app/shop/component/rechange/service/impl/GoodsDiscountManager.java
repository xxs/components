package com.enation.app.shop.component.rechange.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.rechange.model.DiscountVo;
import com.enation.app.shop.component.rechange.model.GoodsDiscount;
import com.enation.app.shop.component.rechange.service.IGoodsDiscountManager;
import com.enation.app.shop.core.model.AdvanceLogs;
import com.enation.app.shop.core.model.Cat;
import com.enation.eop.sdk.database.BaseSupport;

@Component
public class GoodsDiscountManager extends BaseSupport implements
		IGoodsDiscountManager {

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveDiscount(Integer[] cat_ids, Double[] cat_discounts,String[] codes) {
		String sql = "";
		String excuteSql = "";
		if (cat_ids != null && cat_discounts != null && cat_ids.length == cat_discounts.length) {
			for (int i = 0; i < cat_ids.length; i++) {
				sql = "select * from goods_discount gg where gg.catid =" + cat_ids[i];
				List<GoodsDiscount> GDList = this.baseDaoSupport.queryForList(sql,GoodsDiscount.class);
				if(GDList.size() > 0){
					excuteSql = "update goods_discount gg set gg.discount  =" + cat_discounts[i]
					          + ",gg.code = '"+codes[i] +"',gg.createtime = '"+Long.valueOf(System.currentTimeMillis())+"' where gg.catid =" + cat_ids[i];
					this.baseDaoSupport.execute(excuteSql);	
				}else{
					GoodsDiscount gg = new GoodsDiscount();
					gg.setDiscount(cat_discounts[i]);
					gg.setCatid(cat_ids[i]);
					gg.setMemberid(1);
					gg.setStarttime(Long.valueOf(System.currentTimeMillis()));
					gg.setEndtime(Long.valueOf(System.currentTimeMillis()));
					add(gg);
				}
			}
		}
	}
	
	@Override
	public GoodsDiscount get() {
		String sql = "select * from  goods_discount ";
		List<GoodsDiscount> goodsDiscountList = this.baseDaoSupport
				.queryForList(sql, GoodsDiscount.class);
		if (goodsDiscountList == null || goodsDiscountList.isEmpty()) {
			return null;
		} else {
			return goodsDiscountList.get(0);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(GoodsDiscount goodsDiscount) {
		goodsDiscount.setCreatetime(Long.valueOf(System.currentTimeMillis()));
		goodsDiscount.setUpdatetime(Long.valueOf(System.currentTimeMillis()));
		this.baseDaoSupport.insert("goods_discount", goodsDiscount);
	}

	@Override
	public List<AdvanceLogs> getAdvanceLogsList(int top) {
		return (List<AdvanceLogs>) this.baseDaoSupport.queryForList(
				"select * from advance_logs order by mtime desc limit 0,?",
				AdvanceLogs.class, new Object[] { Integer.valueOf(top) });
	}

	@Override
	public List<AdvanceLogs> list() {
		String sql = "select * from  advance_logs";
		List<AdvanceLogs> list = this.baseDaoSupport.queryForList(sql,
				AdvanceLogs.class);
		return list;
	}

	@Override
	public List<DiscountVo> getDiscountVoList() {
		String sql = "select * from goods_cat gg where gg.parent_id = 0 ORDER BY gg.cat_order";
		List<Cat> cats = this.baseDaoSupport.queryForList(sql, Cat.class);
		List<DiscountVo> lists = new ArrayList<DiscountVo>();
		GoodsDiscount goodsDiscount = null;
		DiscountVo discountVo = null;
		Cat cat = null;
		String sqls = "";
		if (cats != null) {
			for (int i = 0; i < cats.size(); i++) {
				discountVo = new DiscountVo();
				cat = cats.get(i);
				sqls = "select * from goods_discount tt where tt.catid = "
						+ cat.getCat_id();
				List<GoodsDiscount> gds = this.baseDaoSupport.queryForList(
						sqls, GoodsDiscount.class);
				if (null!=gds && !gds.isEmpty()) {
					goodsDiscount = gds.get(0);
				}else{
					goodsDiscount = new GoodsDiscount();
					goodsDiscount.setDiscount(0.00);
				}
				discountVo.setCat(cat);
				discountVo.setGoodsDiscount(goodsDiscount);
				lists.add(discountVo);
			}
		}

		return lists;
	}

	@Override
	public GoodsDiscount getDiscountByCatId(Integer catId) {
		String sql = "select * from goods_discount ff where ff.catid = "+ catId;
		List<GoodsDiscount> list = this.baseDaoSupport.queryForList(sql,
				GoodsDiscount.class);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}

}
