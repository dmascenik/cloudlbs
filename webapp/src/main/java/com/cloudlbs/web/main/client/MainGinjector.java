package com.cloudlbs.web.main.client;

import com.cloudlbs.web.core.gwt.CoreGinModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ CoreGinModule.class, MainGinModule.class })
public interface MainGinjector extends Ginjector {

    AppController getAppController();

}
