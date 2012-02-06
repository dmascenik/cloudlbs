package com.cloudlbs.web.noauth.client;

import com.cloudlbs.web.noauth.client.view.LoginForm;
import com.cloudlbs.web.noauth.client.view.LoginFormImpl;
import com.cloudlbs.web.noauth.client.view.NewUserForm;
import com.cloudlbs.web.noauth.client.view.NewUserFormImpl;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.cloudlbs.web.noauth.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

public class NoAuthGinModule extends AbstractGinModule {

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
    private void bindViews() {
        bind(new TypeLiteral<LoginForm<LoginCredentials>>() {
        }).to(new TypeLiteral<LoginFormImpl<LoginCredentials>>() {
        }).in(Singleton.class);

        bind(new TypeLiteral<NewUserForm<NewUserDetails>>() {
        }).to(new TypeLiteral<NewUserFormImpl<NewUserDetails>>() {
        }).in(Singleton.class);
    }

}
