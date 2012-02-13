package com.cloudlbs.web.noauth.client;

import com.cloudlbs.web.core.gwt.CoreGinModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ CoreGinModule.class, NoAuthGinModule.class })
public interface NoAuthGinjector extends Ginjector {

    AppController getAppController();

}
