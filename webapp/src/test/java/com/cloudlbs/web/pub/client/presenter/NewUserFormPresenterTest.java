package com.cloudlbs.web.pub.client.presenter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cloudlbs.web.pub.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.pub.client.event.CancelCreateUserEventHandler;
import com.cloudlbs.web.pub.client.view.NewUserForm;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NewUserFormPresenterTest implements CancelCreateUserEventHandler {

    @Mock private NewUserForm<NewUserDetails> view;
    private NewUserFormPresenter presenter;
    private HandlerManager eventBus;

    @Test
    public void testSubmitClicked() {
        /*
         * Set up mocks and arg captors
         */
        NewUserDetails dets = new NewUserDetails("user", "email", "pass");
        when(view.getNewUserDetails()).thenReturn(dets);
        ArgumentCaptor<NewUserDetails> argCreds = ArgumentCaptor.forClass(NewUserDetails.class);
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
//        verify()

//     verify(loginService).login(argCreds.capture(), argCallback.capture());
//     assertEquals(creds, argCreds.getValue());
//
//     verify(view, times(0)).redirectToAuthenticated();
//     verify(view, times(0)).showErrorMessage(anyString());
//     verify(messages, times(0)).usernameOrPasswordIncorrect();
//     argCallback.getValue().onSuccess(true);
//     verify(view, times(1)).redirectToAuthenticated();
//     argCallback.getValue().onSuccess(false);
//     verify(view, times(1)).showErrorMessage(anyString());
//     verify(messages, times(1)).usernameOrPasswordIncorrect();
    }

    @Test
    public void testCancelClicked() {
        eventBus.addHandler(CancelCreateUserEvent.TYPE, this);
        cancelCalled = false;
        presenter.onCancelClicked();
        assertTrue(cancelCalled);
    }

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        eventBus = new HandlerManager(null);
        presenter = new NewUserFormPresenter(eventBus, view);
    }

    @After
    public void after() throws Exception {
        presenter = null;
        view = null;
        eventBus = null;
    }

    private boolean cancelCalled;

    @Override
    public void onCancelCreateUser(CancelCreateUserEvent event) {
        assertNotNull(event);
        cancelCalled = true;
    }

}
