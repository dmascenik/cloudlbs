package com.cloudlbs.web.noauth.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(NoAuthGinModule.class)
public interface NoAuthGinjector extends Ginjector {

	AppController getAppController();
	
}
