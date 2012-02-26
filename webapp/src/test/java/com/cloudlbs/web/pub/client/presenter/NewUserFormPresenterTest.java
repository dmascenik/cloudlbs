package com.cloudlbs.web.pub.client.presenter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cloudlbs.web.pub.client.event.CancelCreateUserEvent;
import com.cloudlbs.web.pub.client.event.CancelCreateUserEventHandler;
import com.cloudlbs.web.pub.client.presenter.NewUserFormPresenter;
import com.cloudlbs.web.pub.client.view.NewUserForm;
import com.cloudlbs.web.pub.shared.model.NewUserDetails;
import com.google.gwt.event.shared.HandlerManager;

public class NewUserFormPresenterTest implements CancelCreateUserEventHandler {

    @Mock private NewUserForm<NewUserDetails> view;
    private NewUserFormPresenter presenter;
    private HandlerManager eventBus;

    @Test
    public void testSubmitClicked() {
        NewUserDetails dets = new NewUserDetails("user", "email", "pass");
        when(view.getNewUserDetails()).thenReturn(dets);
        presenter.onSubmitClicked();

        // FIXME not a complete test
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
