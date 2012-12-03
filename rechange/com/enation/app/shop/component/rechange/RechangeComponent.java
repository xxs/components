package com.enation.app.shop.component.rechange;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.auth.IAuthActionManager;
import com.enation.app.base.core.service.auth.impl.PermissionConfig;
import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.eop.resource.IMenuManager;
import com.enation.eop.resource.model.Menu;
import com.enation.framework.component.IComponent;



/**
 * 银行卡组件安装和卸载方法
 * @author xxs
 */
@Component
public class RechangeComponent implements IComponent {
	private IMenuManager menuManager;
	private IAuthActionManager authActionManager;
	@Override
	public void install() {
		int superAdminAuthId= PermissionConfig.getAuthId("super_admin"); //超级管理员权限id
	 	//添加提现申请的菜单
		Menu parentMenu = menuManager.get("订单");

		Menu menu = new Menu();
		menu.setTitle("提现申请");
		menu.setPid(parentMenu.getId());
		menu.setUrl("#");
		menu.setSorder(101);
		menu.setMenutype(Menu.MENU_TYPE_APP);
		int menuid = this.menuManager.add(menu);
	
		Menu validMenu = new Menu();		
		validMenu.setPid(menuid);
		validMenu.setTitle("提现申请单");
		validMenu.setUrl("/shop/admin/withdraw.do?state=-100");
		validMenu.setSorder(1);
		validMenu.setMenutype(Menu.MENU_TYPE_APP );
		int listmenuid =menuManager.add(validMenu);
		
		Menu validMenu2 = new Menu();		
		validMenu2.setPid(menuid);
		validMenu2.setTitle("提现率设置");
		validMenu2.setUrl("/shop/admin/withdraw.do?state=-100");
		validMenu2.setSorder(1);
		validMenu2.setMenutype(Menu.MENU_TYPE_APP );
		int listmenuid2 =menuManager.add(validMenu2);

		this.authActionManager.addMenu(superAdminAuthId, new Integer[]{menuid,listmenuid});
		this.authActionManager.addMenu(superAdminAuthId, new Integer[]{menuid,listmenuid2});
		
		//添加提现申请的菜单
		Menu parentMenu1 = menuManager.get("日常操作");		
		Menu menu1 = new Menu();
		menu1.setTitle("充值卡");
		menu1.setPid(parentMenu1.getId());
		menu1.setUrl("#");
		menu1.setSorder(101);
		menu1.setMenutype(Menu.MENU_TYPE_APP);
		int menuid1 = this.menuManager.add(menu1);
		
		Menu validMenu1 = new Menu();		
		validMenu1.setPid(menuid1);
		validMenu1.setTitle("充值卡设置");
		validMenu1.setUrl("/shop/admin/discount.do");
		validMenu1.setSorder(1);
		validMenu1.setMenutype(Menu.MENU_TYPE_APP );
		int listmenuid1 =menuManager.add(validMenu1);

		this.authActionManager.addMenu(superAdminAuthId, new Integer[]{menuid1,listmenuid1});
		try{
			DBSolutionFactory.dbImport("file:com/enation/app/shop/component/rechange/rechange_install.xml","es_");
			System.out.println("执行安装银行卡组件的方法......................");
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}

	@Override
	public void unInstall() {
		//删除提现申请后台菜单
		int superAdminAuthId= PermissionConfig.getAuthId("super_admin"); //超级管理员权限id
		
		int vmenu = menuManager.get("提现申请").getId();
		int menuid = menuManager.get("提现申请单").getId();	
		int menuid2 = menuManager.get("提现率设置").getId();	
		this.authActionManager.deleteMenu(superAdminAuthId, new Integer[]{vmenu,menuid});
		this.authActionManager.deleteMenu(superAdminAuthId, new Integer[]{vmenu,menuid2});
		this.menuManager.delete("提现申请");
		this.menuManager.delete("提现申请单");
		this.menuManager.delete("提现率设置");
		//删除充值卡后台菜单
		int vmenu1 = menuManager.get("充值卡").getId();
		int menuid1 = menuManager.get("充值卡设置").getId();		
		this.authActionManager.deleteMenu(superAdminAuthId, new Integer[]{vmenu1,menuid1});		
		this.menuManager.delete("充值卡");
		this.menuManager.delete("充值卡设置");
		try{
			DBSolutionFactory.dbImport("file:com/enation/app/shop/component/rechange/rechange_uninstall.xml","es_");
			System.out.println("执行卸载银行卡组件的方法......................");
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}

	public IMenuManager getMenuManager() {
		return menuManager;
	}

	public void setMenuManager(IMenuManager menuManager) {
		this.menuManager = menuManager;
	}

	public IAuthActionManager getAuthActionManager() {
		return authActionManager;
	}

	public void setAuthActionManager(IAuthActionManager authActionManager) {
		this.authActionManager = authActionManager;
	}	
}
