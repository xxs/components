package com.enation.app.shop.component.bank;

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
public class BankComponent implements IComponent {
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
		validMenu.setUrl("/shop/admin/money.do");
		validMenu.setSorder(1);
		validMenu.setMenutype(Menu.MENU_TYPE_APP );
		int listmenuid =menuManager.add(validMenu);

		this.authActionManager.addMenu(superAdminAuthId, new Integer[]{menuid,listmenuid});
		try{
			DBSolutionFactory.dbImport("file:com/enation/app/shop/component/bank/bank_install.xml","es_");
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
		this.authActionManager.deleteMenu(superAdminAuthId, new Integer[]{vmenu,menuid});		
		this.menuManager.delete("提现申请");
		this.menuManager.delete("提现申请单");
		try{
			DBSolutionFactory.dbImport("file:com/enation/app/shop/component/bank/bank_uninstall.xml","es_");
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
