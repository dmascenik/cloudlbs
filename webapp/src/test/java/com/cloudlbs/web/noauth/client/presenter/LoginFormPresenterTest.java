package com.cloudlbs.web.noauth.client.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cloudlbs.web.noauth.client.event.LoginSubmitEvent;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEventHandler;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEventHandler;
import com.cloudlbs.web.noauth.client.view.LoginForm;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.event.shared.HandlerManager;

public class LoginFormPresenterTest implements LoginSubmitEventHandler, NewUserRequestEventHandler {

    @Mock private LoginForm<LoginCredentials> view;
    private LoginFormPresenter presenter;
    private HandlerManager eventBus;

    @Test
    public void testSignInClicked() {
        LoginCredentials creds = new LoginCredentials("user", "password");
        when(view.getLoginCredentials()).thenReturn(creds);
        eventBus.addHandler(LoginSubmitEvent.TYPE, this);
        returned = null;
        presenter.onSignInClicked();
        assertNotNull(returned);
        assertEquals(creds, returned);
    }

    @Test
    public void testCreateNewClicked() {
        eventBus.addHandler(NewUserRequestEvent.TYPE, this);
        newUserRequestReceived = false;
        presenter.onNewUserClicked();
        assertTrue(newUserRequestReceived);
    }

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        eventBus = new HandlerManager(null);
        presenter = new LoginFormPresenter(eventBus, view);
    }

    @After
    public void after() throws Exception {
        presenter = null;
        view = null;
        eventBus = null;
    }

    private LoginCredentials returned;
    private boolean newUserRequestReceived;

    @Override
    public void onLoginSubmit(LoginSubmitEvent event) {
        assertNotNull(event);
        returned = event.getCredentials();
    }

    @Override
    public void onNewUserRequest(NewUserRequestEvent event) {
        assertNotNull(event);
        newUserRequestReceived = true;
    }

}
