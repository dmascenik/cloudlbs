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

import com.cloudlbs.web.noauth.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.noauth.client.event.CancelCreateUserEventHandler;
import com.cloudlbs.web.noauth.client.event.CreateUserEvent;
import com.cloudlbs.web.noauth.client.event.CreateUserEventHandler;
import com.cloudlbs.web.noauth.client.view.NewUserForm;
import com.cloudlbs.web.noauth.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;

public class NewUserFormPresenterTest implements CreateUserEventHandler, CancelCreateUserEventHandler {

    @Mock private NewUserForm<NewUserDetails> view;
    private NewUserFormPresenter presenter;
    private HandlerManager eventBus;

    @Test
    public void testSubmitClicked() {
        NewUserDetails dets = new NewUserDetails("user", "email", "pass");
        when(view.getNewUserDetails()).thenReturn(dets);
        eventBus.addHandler(CreateUserEvent.TYPE, this);
        returned = null;
        presenter.onSubmitClicked();
        assertNotNull(returned);
        assertEquals(dets, returned);
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

    private NewUserDetails returned;
    private boolean cancelCalled;

    @Override
    public void onCancelCreateUser(CancelCreateUserEvent event) {
        assertNotNull(event);
        cancelCalled = true;
    }

    @Override
    public void onCreateUser(CreateUserEvent event) {
        assertNotNull(event);
        returned = event.getUserDetails();
    }

}