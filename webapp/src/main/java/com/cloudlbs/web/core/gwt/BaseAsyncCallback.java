package com.cloudlbs.web.core.gwt;

import com.cloudlbs.web.core.gwt.ui.wrapper.Wrapper;
import com.cloudlbs.web.i18n.msg.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;

/**
 * A standard implementation of GWT's {@link AsyncCallback} that handles some
 * common failure cases.
 * 
 * @author danmascenik
 * 
 * @param <T>
 */
public abstract class BaseAsyncCallback<T> implements AsyncCallback<T> {

    private Wrapper wrapper;
    private Messages messages;

    public BaseAsyncCallback(Wrapper wrapper, Messages messages) {
        this.wrapper = wrapper;
        this.messages = messages;
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
     */
    @Override
    public void onFailure(Throwable caught) {
        try {
            throw caught;
        } catch (StatusCodeException e) {
            if (e.getStatusCode() == 0) {
                /*
                 * Status code 0 indicates that the request could not go
                 * through. The network could be down, the client may have lost
                 * connectivity, etc.
                 */
                wrapper.alert(messages.noNetworkConnectivity());
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
    }

    private void showEmbarrassingMessage() {
        wrapper.alert(messages.unknownError());
    }
}
