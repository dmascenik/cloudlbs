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
import com.cloudlbs.web.pub.client.view.NewUserForm;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NewUserFormPresenterTest {

    @Mock private NewUserForm<NewUserDetails> view;
    @Mock private RPCUserServiceAsync userService;
    @Mock private Messages messages;
    @Mock private ChangeViewEventHandler cvHandler;
    private NewUserFormPresenter presenter;
    private HandlerManager eventBus;

    @Test
    public void testSubmitClicked() {
        /*
         * Set up mocks and arg captors
         */
        NewUserDetails dets = new NewUserDetails("user", "email", "pass");
        when(view.getNewUserDetails()).thenReturn(dets);
        ArgumentCaptor<NewUserDetails> argDets = ArgumentCaptor.forClass(NewUserDetails.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<AsyncCallback<Boolean>> argCallback = (ArgumentCaptor<AsyncCallback<Boolean>>) (Object) ArgumentCaptor
                .forClass(AsyncCallback.class);

        /*
         * Simulate a create click
         */
        presenter.onSubmitClicked();

        /*
         * Verification
         */
        verify(userService).createUser(argDets.capture(), argCallback.capture());
        assertEquals(dets, argDets.getValue());

        verify(view, times(0)).alert(anyString());
        argCallback.getValue().onSuccess(true);
        verify(view, times(1)).alert(anyString());
        argCallback.getValue().onSuccess(false);
        verify(view, times(2)).alert(anyString());
    }

    @Test
    public void testCancelClicked() {
        eventBus.addHandler(ChangeViewEvent.TYPE, cvHandler);
        ArgumentCaptor<ChangeViewEvent> argEvt = ArgumentCaptor.forClass(ChangeViewEvent.class);
        presenter.onCancelClicked();
        verify(cvHandler).doChangeView(argEvt.capture());
        assertEquals(HistoryToken.LOGIN.asToken(), argEvt.getValue().getHistoryToken());
    }

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        eventBus = new HandlerManager(null);
        presenter = new NewUserFormPresenter(eventBus, view, userService, messages);
    }

    @After
    public void after() throws Exception {
        presenter = null;
        view = null;
        eventBus = null;
        messages = null;
    }

}
