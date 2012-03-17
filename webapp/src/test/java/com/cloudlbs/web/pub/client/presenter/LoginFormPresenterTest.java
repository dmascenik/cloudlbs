package com.cloudlbs.web.pub.client.presenter;

import static org.junit.Assert.assertEquals;
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
import com.cloudlbs.web.pub.client.AppController.HistoryToken;
import com.cloudlbs.web.pub.client.event.ChangeViewEvent;
import com.cloudlbs.web.pub.client.event.ChangeViewEventHandler;
import com.cloudlbs.web.pub.client.rpc.RPCUserServiceAsync;
import com.cloudlbs.web.pub.client.view.LoginForm;
import com.cloudlbs.web.pub.shared.model.UsernamePasswordAuthentication;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoginFormPresenterTest {

    @Mock private LoginForm<UsernamePasswordAuthentication> view;
    @Mock private RPCUserServiceAsync userService;
    @Mock private Messages messages;
    @Mock private ChangeViewEventHandler cvHandler;
    private LoginFormPresenter presenter;
    private HandlerManager eventBus;

    @Test
    public void testSignInClicked() {
        /*
         * Set up mocks and arg captors
         */
        UsernamePasswordAuthentication creds = new UsernamePasswordAuthentication("user", "password");
        when(view.getLoginCredentials()).thenReturn(creds);
        ArgumentCaptor<UsernamePasswordAuthentication> argCreds = ArgumentCaptor.forClass(UsernamePasswordAuthentication.class);
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
        verify(userService).login(argCreds.capture(), argCallback.capture());
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
        eventBus.addHandler(ChangeViewEvent.TYPE, cvHandler);
        ArgumentCaptor<ChangeViewEvent> argEvt = ArgumentCaptor.forClass(ChangeViewEvent.class);
        presenter.onNewUserClicked();
        verify(cvHandler).doChangeView(argEvt.capture());
        assertEquals(HistoryToken.NEW_USER.asToken(), argEvt.getValue().getHistoryToken());
    }

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        eventBus = new HandlerManager(null);
        presenter = new LoginFormPresenter(eventBus, view, userService, messages);
    }

    @After
    public void after() throws Exception {
        presenter = null;
        view = null;
        eventBus = null;
        messages = null;
    }

}
