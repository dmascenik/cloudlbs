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

import com.cloudlbs.web.noauth.client.RPCLoginServiceAsync;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEvent;
import com.cloudlbs.web.noauth.client.event.NewUserRequestEventHandler;
import com.cloudlbs.web.noauth.client.view.LoginForm;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;
import com.google.gwt.event.shared.HandlerManager;

public class LoginFormPresenterTest implements NewUserRequestEventHandler {

    @Mock private LoginForm<LoginCredentials> view;
    @Mock private RPCLoginServiceAsync loginService;
    private LoginFormPresenter presenter;
    private HandlerManager eventBus;

    @Test
    public void testSignInClicked() {
        LoginCredentials creds = new LoginCredentials("user", "password");
        when(view.getLoginCredentials()).thenReturn(creds);
//        eventBus.addHandler(LoginSubmitEvent.TYPE, this);
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
        presenter = new LoginFormPresenter(eventBus, view, loginService);
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
    public void onNewUserRequest(NewUserRequestEvent event) {
        assertNotNull(event);
        newUserRequestReceived = true;
    }

}
