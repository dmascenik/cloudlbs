package com.cloudlbs.web.pub.client;

import com.cloudlbs.web.core.gwt.CoreGinModule;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ CoreGinModule.class, PublicGinModule.class })
public interface PublicGinjector extends Ginjector {

    AppController getAppController();

}
