package com.enation.app.shop.component.hellocomponent.widget;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.eop.sdk.widget.AbstractWidget;

@Component("hello_component")
@Scope("prototype")
public class HelloWidget extends AbstractWidget {

	@Override
	protected void config(Map<String, String> arg0) {
	 

	}

	@Override
	protected void display(Map<String, String> arg0) {
		

	}

}
