package com.cloudlbs.web.pub.client.view;

import org.junit.Test;

import com.cloudlbs.web.core.gwt.Counter;
import com.cloudlbs.web.pub.client.PublicGWTTestCase;

public class GwtTestNewUserForm extends PublicGWTTestCase {

    NewUserFormImpl viewImpl;
    final Counter submitCounter = new Counter();
    final Counter cancelCounter = new Counter();

    /**
     * Tests that the view is connected to the presenter
     */
    @Test
    public void testSubmitClick() {
        viewImpl.username.setText("asdfasdf");
        viewImpl.email.setText("email@email.com");
        viewImpl.password.setText("asdf1234");
        viewImpl.passwordConf.setText("asdf1234");
        assertEquals(0, submitCounter.get());
        viewImpl.onCreateUserClicked(null);
        assertEquals(1, submitCounter.get());
    }

    /**
     * Tests that the view is connected to the presenter
     */
    @Test
    public void testCancelClick() {
        assertEquals(0, cancelCounter.get());
        viewImpl.onCancelClicked(null);
        assertEquals(1, cancelCounter.get());
    }

    @Test
    public void testSpinnerAndErrorMsgsInitiallyHidden() {
        assertFalse(viewImpl.workingSpinner.isVisible());
        assertFalse(viewImpl.userNameErrorLabel.isVisible());
        assertFalse(viewImpl.emailErrorLabel.isVisible());
        assertFalse(viewImpl.passwordErrorLabel.isVisible());
        assertFalse(viewImpl.passwordConfErrorLabel.isVisible());
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

    @Test
    public void testAllInputsValid() {
        assertFalse(viewImpl.createUser.isEnabled());
        assertFalse(viewImpl.userNameErrorLabel.isVisible());
        assertFalse(viewImpl.emailErrorLabel.isVisible());
        assertFalse(viewImpl.passwordErrorLabel.isVisible());
        assertFalse(viewImpl.passwordConfErrorLabel.isVisible());

        viewImpl.username.setText("asdfasdf");
        viewImpl.email.setText("email@email.com");
        viewImpl.password.setText("asdf1234");
        viewImpl.passwordConf.setText("asdf1234");

        // Trigger validation
        viewImpl.allValid(true);
        viewImpl.resetSubmitButton();

        assertTrue(viewImpl.createUser.isEnabled());
        assertFalse(viewImpl.userNameErrorLabel.isVisible());
        assertFalse(viewImpl.emailErrorLabel.isVisible());
        assertFalse(viewImpl.passwordErrorLabel.isVisible());
        assertFalse(viewImpl.passwordConfErrorLabel.isVisible());
    }

    /**
     * Submit button should be disabled when isWorking
     */
    @Test
    public void testSubmitButtonDisabledWhenWorking() {
        viewImpl.createUser.setEnabled(true);
        viewImpl.setWorking(true);
        assertFalse(viewImpl.createUser.isEnabled());
        viewImpl.setWorking(false);
        assertTrue(viewImpl.createUser.isEnabled());
    }
    @Test
    public void testClearForm() {
        viewImpl.username.setText("user");
        viewImpl.password.setText("password");
        viewImpl.email.setText("abc");
        viewImpl.passwordConf.setText("asdf");
        viewImpl.userNameErrorLabel.setVisible(true);
        viewImpl.emailErrorLabel.setVisible(true);
        viewImpl.passwordErrorLabel.setVisible(true);
        viewImpl.passwordConfErrorLabel.setVisible(true);
        viewImpl.clearForm();
        assertEquals("", viewImpl.username.getText());
        assertEquals("", viewImpl.email.getText());
        assertEquals("", viewImpl.password.getText());
        assertEquals("", viewImpl.passwordConf.getText());
        assertFalse(viewImpl.userNameErrorLabel.isVisible());
        assertFalse(viewImpl.emailErrorLabel.isVisible());
        assertFalse(viewImpl.passwordErrorLabel.isVisible());
        assertFalse(viewImpl.passwordConfErrorLabel.isVisible());
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        NewUserForm view = injector.getNewUserForm();
        view.setPresenter(new NewUserForm.Presenter() {
            @Override
            public void onSubmitClicked() {
                submitCounter.incr();
            }
            @Override
            public void onCancelClicked() {
                cancelCounter.incr();
            }
        });
        assertTrue(view instanceof NewUserFormImpl);
        viewImpl = (NewUserFormImpl) view;
        submitCounter.reset();
        cancelCounter.reset();
    }

    @Override
    protected void gwtTearDown() throws Exception {
        viewImpl = null;
        submitCounter.reset();
        cancelCounter.reset();
        super.gwtTearDown();
    }

}
