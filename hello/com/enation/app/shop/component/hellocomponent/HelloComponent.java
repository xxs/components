package com.enation.app.shop.component.hellocomponent;

import org.springframework.stereotype.Component;

import com.enation.framework.component.IComponent;

/**
 * hello 组件
 * @author kingapex
 *2012-6-6下午7:56:42
 */
@Component
public class HelloComponent implements IComponent {

	@Override
	public void install() {
		System.out.println("Hello Component install");

	}

	@Override
	public void unInstall() {
		System.out.println("Hello Component uninstall");

	}

}
