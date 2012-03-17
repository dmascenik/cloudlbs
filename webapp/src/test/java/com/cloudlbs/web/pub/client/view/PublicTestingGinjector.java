package com.cloudlbs.web.pub.client.view;

import com.cloudlbs.web.pub.client.PublicGinModule;
import com.cloudlbs.web.pub.client.PublicGinjector;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.GinModules;

@GinModules(PublicGinModule.class)
public interface PublicTestingGinjector extends PublicGinjector {

    LoginForm getLoginForm();
    NewUserForm getNewUserForm();
    HandlerManager getEventBus();

}
