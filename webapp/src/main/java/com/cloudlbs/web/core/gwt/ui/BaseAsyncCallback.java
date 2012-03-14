package com.cloudlbs.web.core.gwt.ui;

import com.cloudlbs.web.i18n.msg.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;

/**
 * A standard implementation of GWT's {@link AsyncCallback} that handles some
 * common failure cases. A {@link View} must be provided in order to show error
 * messages and a "working" indicator.
 * 
 * @author danmascenik
 * 
 * @param <T>
 */
public abstract class BaseAsyncCallback<T> implements AsyncCallback<T> {

    private View view;
    private Messages messages;

    /**
     * Convenience constructor that immediately toggles the view's "working"
     * indicator on.
     * 
     * @param view
     * @param messages
     */
    public BaseAsyncCallback(View view, Messages messages) {
        this(view, messages, true);
    }

    /**
     * Allows immediate toggling of the "working" indicator to be deferred for
     * later.
     * 
     * @param view
     * @param messages
     */
    public BaseAsyncCallback(View view, Messages messages, boolean setWorkingImmediately) {
        this.view = view;
        this.messages = messages;
        if (setWorkingImmediately) {
            view.setWorking(true);
        }
    }

    /**
     * Activate/deactivate the "working" indicator manually.
     * 
     * @param isWorking
     */
    public void setWorkingIndicatorActive(boolean isWorking) {
        view.setWorking(isWorking);
    }

    /**
     * Called when the async call completed successfully, after other view state
     * management like turning off the "working" indicator completes.
     * 
     * @param result
     */
    public abstract void success(T result);

    /**
     * Called when the async call failed, after other view state management like
     * turning off the "working" indicator completes. The default implementation
     * does nothing.
     * 
     * @param result
     */
    public void failure(Throwable caught) {
        // does nothing
    }

    /**
     * Captures any kind of {@link StatusCodeException} and handles it in one of
     * two ways:
     * <ul>
     * <li>If a connection was not even possible, present a meaningful message
     * telling the user to check their connection.</li>
     * <li>If the connection was made, but an unchecked exception occurred,
     * present a generic error message asking the user to try again later.</li>
     * </ul>
     * Finishes by dispatching to {@link #failure(Throwable)}
     */
    @Override
    public final void onFailure(Throwable caught) {
        view.setWorking(false);
        try {
            throw caught;
        } catch (StatusCodeException e) {
            if (e.getStatusCode() == 0) {
                /*
                 * Status code 0 indicates that the request could not go
                 * through. The network could be down, the client may have lost
                 * connectivity, etc.
                 */
                view.alert(messages.noNetworkConnectivity());
            } else {
                /*
                 * This is anything other than a 200 status code from the
                 * server; server-side exception, a 404, etc.
                 */
                showEmbarrassingMessage();
            }
        } catch (Throwable e) {
            showEmbarrassingMessage();
        }
        failure(caught);
    }

    /**
     * Turns off the "working" indicator and dispatches to
     * {@link #success(Object)}
     */
    @Override
    public final void onSuccess(T result) {
        view.setWorking(false);
        success(result);
    }

    private void showEmbarrassingMessage() {
        view.alert(messages.unknownError());
    }
}
