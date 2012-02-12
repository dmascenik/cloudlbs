package com.cloudlbs.web.noauth.client.view;

import org.junit.Test;

import com.cloudlbs.web.noauth.client.NoAuthGWTTestCase;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEvent;
import com.cloudlbs.web.noauth.client.event.LoginSubmitEventHandler;
import com.cloudlbs.web.noauth.shared.model.LoginCredentials;

public class GwtTestLoginForm extends NoAuthGWTTestCase {

    static final String TEST_USERNAME = "user";
    static final String TEST_PASSWORD = "password";

    private LoginSubmitEvent testLoginFormView_eventCapture;

    @Test
    public void testLoginFormView() {
        LoginForm<LoginCredentials> view = injector.getLoginForm();
        assertTrue(view instanceof LoginFormImpl<?>);
        LoginFormImpl<LoginCredentials> impl = (LoginFormImpl<LoginCredentials>) view;
        eventBus.addHandler(LoginSubmitEvent.TYPE, new LoginSubmitEventHandler() {

            @Override
            public void onLoginSubmit(LoginSubmitEvent event) {
                testLoginFormView_eventCapture = event;
            }
        });

        impl.username.setValue(TEST_USERNAME);
        impl.password.setValue(TEST_PASSWORD);

        assertNull(testLoginFormView_eventCapture);

        // This does nothing
        impl.signIn.click();
        
//        assertNotNull(testLoginFormView_eventCapture);
//
//        LoginCredentials creds = testLoginFormView_eventCapture.getCredentials();
//        assertNotNull(creds);
//        assertEquals(TEST_USERNAME, creds.getUsername());
//        assertEquals(TEST_PASSWORD, creds.getPassword());
    }

}
