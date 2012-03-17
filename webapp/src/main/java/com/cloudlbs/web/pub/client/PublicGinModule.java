package com.cloudlbs.web.pub.client;

import com.cloudlbs.web.pub.client.view.LoginForm;
import com.cloudlbs.web.pub.client.view.LoginFormImpl;
import com.cloudlbs.web.pub.client.view.NewUserForm;
import com.cloudlbs.web.pub.client.view.NewUserFormImpl;
import com.cloudlbs.web.pub.shared.model.UsernamePasswordAuthentication;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

public class PublicGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bindViews();
    }

    @Provides
    @Singleton
    HandlerManager getEventBus() {
        return new HandlerManager(null);
    }

    /**
     * Views use generics, so their bindings require TypeLiteral and can become
     * rather verbose.
     */
    protected void bindViews() {
        bind(new TypeLiteral<LoginForm<UsernamePasswordAuthentication>>() {
        }).to(new TypeLiteral<LoginFormImpl<UsernamePasswordAuthentication>>() {
        }).in(Singleton.class);

        bind(new TypeLiteral<NewUserForm<NewUserDetails>>() {
        }).to(new TypeLiteral<NewUserFormImpl<NewUserDetails>>() {
        }).in(Singleton.class);
    }

}
