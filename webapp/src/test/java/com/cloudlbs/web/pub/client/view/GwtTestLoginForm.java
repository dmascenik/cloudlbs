package com.cloudlbs.web.pub.client.view;

import org.junit.Test;

import com.cloudlbs.web.core.gwt.Counter;
import com.cloudlbs.web.pub.client.PublicGWTTestCase;

public class GwtTestLoginForm extends PublicGWTTestCase {

    LoginFormImpl viewImpl;
    final Counter signInCounter = new Counter();
    final Counter createNewCounter = new Counter();

    /**
     * Tests that the view is connected to the presenter
     */
    @Test
    public void testSignInClick() {
        assertEquals(0, signInCounter.get());
        viewImpl.onSignInClicked(null);
        assertEquals(1, signInCounter.get());
    }

    /**
     * Tests that the view is connected to the presenter
     */
    @Test
    public void testNewUserClick() {
        assertEquals(0, createNewCounter.get());
        viewImpl.onNewUserClicked(null);
        assertEquals(1, createNewCounter.get());
    }

    @Test
    public void testSpinnerAndErrorMsgInitiallyHidden() {
        assertFalse(viewImpl.workingSpinner.isVisible());
        assertFalse(viewImpl.errorLabel.isVisible());
    }

    /**
     * Should appear/disappear via setWorking(boolean)
     */
    @Test
    public void testWorkingSpinner() {
        assertFalse(viewImpl.workingSpinner.isVisible());
        viewImpl.setWorking(true);
        assertTrue(viewImpl.workingSpinner.isVisible());
        viewImpl.setWorking(false);
        assertFalse(viewImpl.workingSpinner.isVisible());
    }

    /**
     * Should disappear when signIn or newUser clicked
     */
    @Test
    public void testErrorMessage() {
        String msg = "error";
        assertFalse(viewImpl.errorLabel.isVisible());
        viewImpl.showErrorMessage(msg);
        assertTrue(viewImpl.errorLabel.isVisible());
        assertEquals(msg, viewImpl.errorLabel.getText());
        viewImpl.onSignInClicked(null);
        assertFalse(viewImpl.errorLabel.isVisible());

        viewImpl.showErrorMessage(msg);
        viewImpl.onNewUserClicked(null);
        assertFalse(viewImpl.errorLabel.isVisible());
    }

    /**
     * Sign in button should be disabled when isWorking
     */
    @Test
    public void testSignInButtonDisabledWhenWorking() {
        assertTrue(viewImpl.signIn.isEnabled());
        viewImpl.setWorking(true);
        assertFalse(viewImpl.signIn.isEnabled());
        viewImpl.setWorking(false);
        assertTrue(viewImpl.signIn.isEnabled());
    }

    @Test
    public void testClearForm() {
        viewImpl.username.setText("user");
        viewImpl.password.setText("password");
        viewImpl.showErrorMessage("error");
        viewImpl.clearForm();
        assertEquals("", viewImpl.username.getText());
        assertEquals("", viewImpl.password.getText());
        assertFalse(viewImpl.errorLabel.isVisible());
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        LoginForm view = injector.getLoginForm();
        view.setPresenter(new LoginForm.Presenter() {
            @Override
            public void onSignInClicked() {
                signInCounter.incr();
            }
            @Override
            public void onNewUserClicked() {
                createNewCounter.incr();
            }
        });
        assertTrue(view instanceof LoginFormImpl);
        viewImpl = (LoginFormImpl) view;
        signInCounter.reset();
        createNewCounter.reset();
    }

    @Override
    protected void gwtTearDown() throws Exception {
        viewImpl = null;
        signInCounter.reset();
        createNewCounter.reset();
        super.gwtTearDown();
    }

}
