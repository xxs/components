package com.enation.app.shop.component.hellocomponent.plugin;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.plugin.goods.IGoodsTabShowEvent;
import com.enation.framework.plugin.AutoRegisterPlugin;

@Component
public class HelloPlugin extends AutoRegisterPlugin implements
		IGoodsTabShowEvent {

	@Override
	public int getOrder() {
		 
		return 900;
	}

	@Override
	public String getTabName() {
		 
		return "hello";
	}

}
