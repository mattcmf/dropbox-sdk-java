package com.dropbox.core;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;

import com.dropbox.core.stone.StoneSerializer;
import com.dropbox.core.http.HttpRequestor;
import com.dropbox.core.v2.callbacks.DbxGlobalErrorCallback;
import com.dropbox.core.v2.callbacks.DbxRouteErrorCallback;

/**
 * For internal use only.
 */
public final class DbxWrappedException extends Exception {
    private static final long serialVersionUID = 0L;

    private final Object errValue;  // Really an ErrT instance, but Throwable does not allow generic subclasses.
    private final String requestId;
    private final LocalizedText userMessage;

    public DbxWrappedException(Object errValue, String requestId, LocalizedText userMessage) {
        this.errValue = errValue;
        this.requestId = requestId;
        this.userMessage = userMessage;
    }

    public Object getErrorValue() {
        return errValue;
    }

    public String getRequestId() {
        return requestId;
    }

    public LocalizedText getUserMessage() {
        return userMessage;
    }

    public static <T> DbxWrappedException fromResponse(StoneSerializer<T> errSerializer, HttpRequestor.Response response)
        throws IOException, JsonParseException {
        String requestId = DbxRequestUtil.getRequestId(response);

        ApiErrorResponse<T> apiResponse = new ApiErrorResponse.Serializer<T>(errSerializer)
            .deserialize(response.getBody());

        T routeError = apiResponse.getError();

        DbxRouteErrorCallback<T> callback = DbxGlobalErrorCallback.getRouteErrorCallback(routeError.getClass());
        if (callback != null) {
            callback.setRouteError(routeError);
            callback.run();
        }

        return new DbxWrappedException(routeError, requestId, apiResponse.getUserMessage());
    }
}
