package com.cloudlbs.web.pub.client.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cloudlbs.web.i18n.msg.Messages;
import com.cloudlbs.web.pub.client.RPCLoginServiceAsync;
import com.cloudlbs.web.pub.client.event.NewUserRequestEvent;
import com.cloudlbs.web.pub.client.event.NewUserRequestEventHandler;
import com.cloudlbs.web.pub.client.view.LoginForm;
import com.cloudlbs.web.pub.shared.model.LoginCredentials;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoginFormPresenterTest implements NewUserRequestEventHandler {

    @Mock private LoginForm<LoginCredentials> view;
    @Mock private RPCLoginServiceAsync loginService;
    @Mock private Messages messages;
    private LoginFormPresenter presenter;
    private HandlerManager eventBus;

    @Test
    public void testSignInClicked() {
        /*
         * Set up mocks and arg captors
         */
        LoginCredentials creds = new LoginCredentials("user", "password");
        when(view.getLoginCredentials()).thenReturn(creds);
        ArgumentCaptor<LoginCredentials> argCreds = ArgumentCaptor.forClass(LoginCredentials.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<AsyncCallback<Boolean>> argCallback = (ArgumentCaptor<AsyncCallback<Boolean>>) (Object) ArgumentCaptor
                .forClass(AsyncCallback.class);

        /*
         * Simulate a sign in click
         */
        presenter.onSignInClicked();

        /*
         * Verification
         */
        verify(loginService).login(argCreds.capture(), argCallback.capture());
        assertEquals(creds, argCreds.getValue());

        verify(view, times(0)).redirectToAuthenticated();
        verify(view, times(0)).showErrorMessage(anyString());
        verify(messages, times(0)).usernameOrPasswordIncorrect();
        argCallback.getValue().onSuccess(true);
        verify(view, times(1)).redirectToAuthenticated();
        argCallback.getValue().onSuccess(false);
        verify(view, times(1)).showErrorMessage(anyString());
        verify(messages, times(1)).usernameOrPasswordIncorrect();
    }

    @Test
    public void testCreateNewClicked() {
        /*
         * Add the test class as a new user request event handler, then verify
         * that an event is received on click.
         */
        eventBus.addHandler(NewUserRequestEvent.TYPE, this);
        newUserRequestReceived = false;
        presenter.onNewUserClicked();
        assertTrue(newUserRequestReceived);
    }

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        eventBus = new HandlerManager(null);
        presenter = new LoginFormPresenter(eventBus, view, loginService, messages);
    }

    @After
    public void after() throws Exception {
        presenter = null;
        view = null;
        eventBus = null;
        messages = null;
    }

    private boolean newUserRequestReceived;

    @Override
    public void onNewUserRequest(NewUserRequestEvent event) {
        assertNotNull(event);
        newUserRequestReceived = true;
    }

}
